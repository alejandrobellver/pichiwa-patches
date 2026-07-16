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
    description = "Bypasses verification bans by spoofing signatures and faking GMS checks. REQUIRED: You must manually install microG-RE for Play Integrity to pass.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // --- 1. Force GMS signature check to pass ---
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
        
        // --- 2. Force local hash comparison to pass ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                
                impl.instructions.forEachIndexed { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        val str = (instr.reference as StringReference).string
                        if (str == "8P1sW0EPJcslw7UzRsiXL64w-O50Ed-RBICtay1g24M" ||
                            str == "-5INOBvuGyCT8n3I8T2ZTaYp3JGIfQUps1yaLcT0psI") {
                            for (i in index + 1 until minOf(index + 5, impl.instructions.count())) {
                                val next = impl.instructions.elementAt(i)
                                if (next.opcode.name == "move-result" && next is OneRegisterInstruction) {
                                    val reg = next.registerA
                                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                                    mutableMethod.addInstructions(i + 1, """
                                        const/4 v$reg, 0x1
                                    """)
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // --- 3. Redirect GMS service binding to MicroG-RE ---
        // WhatsApp calls Intent.setPackage("com.google.android.gms") to bind to GMS for attestation.
        // We redirect this to app.revanced.android.gms so MicroG-RE handles the request instead.
        // Only patches methods that also call Intent.setPackage (service binding), not class-level GMS refs.
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                
                val setPackageRef = "Landroid/content/Intent;->setPackage(Ljava/lang/String;)Landroid/content/Intent;"
                val hasSetPackage = impl.instructions.any { instr ->
                    instr is ReferenceInstruction && instr.reference.toString() == setPackageRef
                }
                if (!hasSetPackage) return@forEach
                
                val matches = impl.instructions.mapIndexedNotNull { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        val str = (instr.reference as StringReference).string
                        if (str == "com.google.android.gms" && instr is OneRegisterInstruction) {
                            // Only redirect if the next non-line instruction is setPackage
                            val nextMeaningful = (index + 1 until minOf(index + 5, impl.instructions.count()))
                                .map { impl.instructions.elementAt(it) }
                                .firstOrNull { it.opcode.name != "nop" }
                            val isSetPackage = nextMeaningful is ReferenceInstruction &&
                                nextMeaningful.reference.toString() == setPackageRef
                            if (isSetPackage) index to instr.registerA else null
                        } else null
                    } else null
                }
                
                if (matches.isNotEmpty()) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    matches.reversed().forEach { (idx, reg) ->
                        mutableMethod.addInstructions(idx + 1, """
                            const-string v$reg, "app.revanced.android.gms"
                        """)
                    }
                }
            }
        }
        
        // --- 4. Redirect com.android.vending to MicroG-RE companion ---
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
    }
}
