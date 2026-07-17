package app.pichiwa.patches.whatsapp

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.pichiwa.patches.shared.Constants.WHATSAPP
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.iface.reference.StringReference

@Suppress("unused")
val loginFix = bytecodePatch(
    name = "Login Fix",
    description = "Bypasses verification bans by spoofing signatures, installers, and faking GMS checks. REQUIRED: You must manually install microG-RE for Play Integrity to pass.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // --- 1. Surgical Redirection for MicroG-RE (GMS) ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                
                val matches = impl.instructions.mapIndexedNotNull { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        val str = (instr.reference as StringReference).string
                        if (str == "com.google.android.gms") index else null
                    } else null
                }
                
                if (matches.isNotEmpty()) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    matches.reversed().forEach { idx ->
                        var isIntentSetPackage = false
                        for (i in idx + 1 until impl.instructions.count()) {
                            val nextInstr = impl.instructions.elementAt(i)
                            if (nextInstr is ReferenceInstruction && nextInstr.reference is MethodReference) {
                                val methodRef = nextInstr.reference as MethodReference
                                if (methodRef.name == "setPackage" && methodRef.definingClass == "Landroid/content/Intent;") {
                                    isIntentSetPackage = true
                                    break
                                }
                            }
                        }
                        
                        if (isIntentSetPackage) {
                            val instr = impl.instructions.elementAtOrNull(idx) as? OneRegisterInstruction ?: return@forEach
                            val reg = instr.registerA
                            mutableMethod.addInstructions(idx + 1, """
                                const-string v$reg, "app.revanced.android.gms"
                            """)
                        }
                    }
                }
            }
        }

        // --- 2. Bypass Signature Verifier Locally ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                
                var targetMoveResultIndex = -1
                var targetRegister = -1
                
                impl.instructions.forEachIndexed { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        if ((instr.reference as StringReference).string == " requires Google Play services, but their signature is invalid.") {
                            for (i in index downTo 0) {
                                val prevInstr = impl.instructions.elementAt(i)
                                if (prevInstr.opcode.name == "invoke-static") {
                                    val ref = (prevInstr as ReferenceInstruction).reference
                                    if (ref is MethodReference && ref.returnType == "Z") {
                                        val moveInstr = impl.instructions.elementAt(i + 1)
                                        if (moveInstr.opcode.name.startsWith("move-result") && moveInstr is OneRegisterInstruction) {
                                            targetMoveResultIndex = i + 1
                                            targetRegister = moveInstr.registerA
                                        }
                                        break
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (targetMoveResultIndex != -1 && targetRegister != -1) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    mutableMethod.addInstructions(targetMoveResultIndex + 1, """
                        const/4 v$targetRegister, 0x1
                    """)
                }
            }
        }
        
        // --- 3. Global MicroG-RE Vending Redirection ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                val matches = impl.instructions.mapIndexedNotNull { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        val str = (instr.reference as StringReference).string
                        if (str == "com.android.vending") index to "app.revanced.android.vending"
                        else null
                    } else null
                }
                
                if (matches.isNotEmpty()) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    matches.reversed().forEach { (idx, newPackage) ->
                        val instr = impl.instructions.elementAtOrNull(idx) as? OneRegisterInstruction ?: return@forEach
                        val reg = instr.registerA
                        mutableMethod.addInstructions(idx + 1, """
                            const-string v$reg, "$newPackage"
                        """)
                    }
                }
            }
        }
        
        // --- 4. Spoof Signature Hashes Directly ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                
                // A) Spoof SHA-1 Hex for X-Android-Cert header
                var certReg = -1
                impl.instructions.forEachIndexed { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        if ((instr.reference as StringReference).string == "X-Android-Cert") {
                            val nextInstr = impl.instructions.elementAtOrNull(index + 1)
                            if (nextInstr is OneRegisterInstruction && nextInstr.opcode.name.startsWith("const-string")) {
                                certReg = nextInstr.registerA
                            } else if (instr is OneRegisterInstruction) {
                                certReg = instr.registerA
                            }
                        }
                    }
                }
                
                if (certReg != -1) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    val matches = impl.instructions.mapIndexedNotNull { index, instr ->
                        val ref = (instr as? ReferenceInstruction)?.reference?.toString()
                        if (ref == "Ljava/net/URLConnection;->addRequestProperty(Ljava/lang/String;Ljava/lang/String;)V" && instr.opcode.name.startsWith("invoke-virtual")) {
                            val invokeInstr = instr
                            val vD = if (invokeInstr is com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction) invokeInstr.registerD else (invokeInstr as com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction).startRegister + 1
                            val vE = if (invokeInstr is com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction) invokeInstr.registerE else (invokeInstr as com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction).startRegister + 2
                            if (vD == certReg) index to vE else null
                        } else null
                    }
                    
                    matches.reversed().forEach { (idx, vE) ->
                        mutableMethod.addInstructions(idx, """
                            const-string v$vE, "38a0f7d505fe18fec64fbf343ecaaaf310dbd799"
                        """)
                    }
                }
                
                // B) Spoof local SHA-256 string comparisons
                val matchesStringComp = impl.instructions.mapIndexedNotNull { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        val str = (instr.reference as StringReference).string
                        if (str == "8P1sW0EPJcslw7UzRsiXL64w-O50Ed-RBICtay1g24M") {
                            var moveResultIdx = -1
                            var vZ = -1
                            for (i in index + 1 until impl.instructions.count()) {
                                val nextInstr = impl.instructions.elementAt(i)
                                if (nextInstr.opcode.name.startsWith("move-result")) {
                                    moveResultIdx = i
                                    vZ = (nextInstr as OneRegisterInstruction).registerA
                                    break
                                }
                            }
                            if (moveResultIdx != -1) moveResultIdx to vZ else null
                        } else null
                    } else null
                }
                
                if (matchesStringComp.isNotEmpty()) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    matchesStringComp.reversed().forEach { (moveResultIdx, vZ) ->
                        mutableMethod.addInstructions(moveResultIdx + 1, """
                            const/4 v$vZ, 0x1
                        """)
                    }
                }
            }
        }
    }
        
        // --- 5. Spoof Installer Package Name ---
        // Forces getInstallerPackageName to return "com.android.vending"
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                impl.instructions.forEachIndexed { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is MethodReference) {
                        val ref = instr.reference as MethodReference
                        if (ref.name == "getInstallerPackageName" && ref.definingClass == "Landroid/content/pm/PackageManager;") {
                            val nextInstr = impl.instructions.elementAtOrNull(index + 1)
                            if (nextInstr?.opcode?.name == "move-result-object") {
                                val reg = (nextInstr as OneRegisterInstruction).registerA
                                val mutableMethod = mutableClassDefBy(def).methods.first {
                                    it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType
                                }
                                mutableMethod.addInstructions(index + 2, """
                                    const-string v$reg, "com.android.vending"
                                """)
                            }
                        }
                    }
                }
            }
        }
        
        // --- 6. Additional Signature Hash Comparison Forcing ---
        // Also force comparisons for SDK>=33 hash values not covered in section 4
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                val knownHashes = setOf(
                    "38a0f7d505fe18fec64fbf343ecaaaf310dbd799",
                    "8b0debf9516af037c9be2f539584b97fe9781764",
                    "-5INOBvuGyCT8n3I8T2ZTaYp3JGIfQUps1yaLcT0psI",
                )
                val matches = impl.instructions.mapIndexedNotNull { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        val str = (instr.reference as StringReference).string
                        if (str in knownHashes) {
                            for (i in index + 1 until impl.instructions.count()) {
                                val nextInstr = impl.instructions.elementAt(i)
                                if (nextInstr.opcode.name.startsWith("move-result")) {
                                    val reg = (nextInstr as OneRegisterInstruction).registerA
                                    return@mapIndexedNotNull i to reg
                                }
                            }
                        }
                    }
                    null
                }
                if (matches.isNotEmpty()) {
                    val mutableMethod = mutableClassDefBy(def).methods.first {
                        it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType
                    }
                    matches.reversed().forEach { (moveResultIdx, vZ) ->
                        mutableMethod.addInstructions(moveResultIdx + 1, """
                            const/4 v$vZ, 0x1
                        """)
                    }
                }
            }
        }
    }
}
