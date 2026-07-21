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
        // --- 1. Fix Registration NPE (X.DFX.A02 NullPointerException) ---
        classDefForEach { def ->
            if (def.type == "LX/DFX;") {
                def.methods.forEach { method ->
                    if (method.name == "A02" && method.parameters.size == 1 && method.parameters[0].type == "Ljava/util/Set;") {
                        val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                        mutableMethod.addInstructions(0, """
                            if-nez p0, :cond_skip_null_p0
                            invoke-static {}, Ljava/util/Collections;->emptySet()Ljava/util/Set;
                            move-result-object p0
                            :cond_skip_null_p0
                        """.trimIndent())
                    }
                }
            }
        }

        // --- 1.5 Fix getInstallerPackageName null crash (6xM.A00) ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                if (method.name == "A00" && method.parameters.size == 2 && method.parameters[0].type == "Landroid/content/Context;" && method.parameters[1].type == "Ljava/lang/String;" && method.returnType == "Ljava/lang/String;") {
                    var isPackageNameUtility = false
                    impl.instructions.forEach { instr ->
                        if (instr is ReferenceInstruction && instr.reference is MethodReference) {
                            if ((instr.reference as MethodReference).name == "startsWith") {
                                isPackageNameUtility = true
                            }
                        }
                    }
                    if (isPackageNameUtility) {
                        val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                        mutableMethod.addInstructions(0, """
                            if-nez p1, :cond_skip_null_p1
                            const-string p1, "unknown"
                            :cond_skip_null_p1
                        """.trimIndent())
                    }
                }
            }
        }

        // --- 2. Disable AntiDetector System (X.00L) ---
        classDefForEach { def ->
            var isAntiDetector = false
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                impl.instructions.forEach { instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        if ((instr.reference as StringReference).string == "/system/app/Superuser.apk") {
                            isAntiDetector = true
                        }
                    }
                }
            }

            if (isAntiDetector) {
                def.methods.forEach { method ->
                    if (method.returnType == "Z") {
                        val impl = method.implementation ?: return@forEach
                        val matches = impl.instructions.mapIndexedNotNull { index, instr ->
                            if (instr.opcode.name == "return") {
                                val returnReg = (instr as OneRegisterInstruction).registerA
                                index to returnReg
                            } else null
                        }
                        
                        if (matches.isNotEmpty()) {
                            val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                            matches.reversed().forEach { (idx, returnReg) ->
                                mutableMethod.addInstructions(idx, """
                                    const/16 v$returnReg, 0x0
                                """.trimIndent())
                            }
                        }
                    }
                }
            }
        }

        // --- 3. Play Store / GMS Signature Verification Bypass ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                if (method.name == "<clinit>") return@forEach
                
                // A) Bypass local SHA-256 string comparisons (Play Store / App signature)
                val matchesStringComp = impl.instructions.mapIndexedNotNull { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        val str = (instr.reference as StringReference).string
                        if (str == "8P1sW0EPJcslw7UzRsiXL64w-O50Ed-RBICtay1g24M" || str == "-5INOBvuGyCT8n3I8T2ZTaYp3JGIfQUps1yaLcT0psI") {
                            var moveResultIdx = -1
                            var vZ = -1
                            for (i in index + 1 until minOf(index + 5, impl.instructions.count())) {
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
                            const/16 v$vZ, 0x1
                        """.trimIndent())
                    }
                }

                // B) Bypass GMS signature check (Google Play Services)
                // Looks for GoogleSignatureVerifier which contains "Package has more than one signature."
                if (method.name == "A01" && method.returnType == "Z" && method.parameters.size == 2) {
                    var isGmsVerifier = false
                    impl.instructions.forEach { instr ->
                        if (instr is ReferenceInstruction && instr.reference is StringReference) {
                            if ((instr.reference as StringReference).string == "Package has more than one signature.") {
                                isGmsVerifier = true
                            }
                        }
                    }
                    if (isGmsVerifier) {
                        val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                        mutableMethod.addInstructions(0, """
                            const/16 v0, 0x1
                            return v0
                        """.trimIndent())
                    }
                }
            }
        }

        // --- 4. MicroG-RE Redirections ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                
                // A) Global Vending Redirection
                val matchesVending = impl.instructions.mapIndexedNotNull { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        val str = (instr.reference as StringReference).string
                        if (str == "com.android.vending") index to "app.revanced.android.vending"
                        else null
                    } else null
                }
                
                if (matchesVending.isNotEmpty()) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    matchesVending.reversed().forEach { (idx, newPackage) ->
                        val instr = impl.instructions.elementAtOrNull(idx) as? OneRegisterInstruction ?: return@forEach
                        val reg = instr.registerA
                        mutableMethod.addInstructions(idx + 1, """
                            const-string v$reg, "$newPackage"
                        """.trimIndent())
                    }
                }

                // B) Surgical GMS Redirection (only with Intent.setPackage)
                val matchesGms = impl.instructions.mapIndexedNotNull { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        val str = (instr.reference as StringReference).string
                        if (str == "com.google.android.gms") index else null
                    } else null
                }
                
                if (matchesGms.isNotEmpty()) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    matchesGms.reversed().forEach { idx ->
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
                            """.trimIndent())
                        }
                    }
                }
            }
        }

        // --- 5. X-Android-Cert / AppIdentity Spoofing ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                
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
                        """.trimIndent())
                    }
                }
            }
        }
    }
}
