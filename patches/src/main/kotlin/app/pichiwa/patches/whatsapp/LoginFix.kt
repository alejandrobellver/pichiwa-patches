package app.pichiwa.patches.whatsapp

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.pichiwa.patches.shared.Constants.WHATSAPP
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference

@Suppress("unused")
val loginFix = bytecodePatch(
    name = "Login Fix",
    description = "Bypasses verification bans by spoofing signatures, installers, and faking GMS checks. REQUIRED: You must manually install microG-RE for Play Integrity to pass.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // --- 1. Safely Spoof Installer and Initiating Package Names ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                val matches = impl.instructions.mapIndexedNotNull { index, instr ->
                    val ref = (instr as? ReferenceInstruction)?.reference?.toString()
                    if (ref == "Landroid/content/pm/PackageManager;->getInstallerPackageName(Ljava/lang/String;)Ljava/lang/String;" ||
                        ref == "Landroid/content/pm/InstallSourceInfo;->getInitiatingPackageName()Ljava/lang/String;"
                    ) index else null
                }
                
                if (matches.isNotEmpty()) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    matches.reversed().forEach { invokeIdx ->
                        val moveResult = impl.instructions.elementAtOrNull(invokeIdx + 1) as? OneRegisterInstruction ?: return@forEach
                        val reg = moveResult.registerA
                        mutableMethod.addInstructions(invokeIdx + 2, """
                            const-string v$reg, "com.android.vending"
                        """)
                    }
                }
            }
        }

        // --- 2. Force GoogleApiAvailability success (obfuscation-safe) ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                var isTarget = false
                impl.instructions.forEach { instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        if ((instr.reference as StringReference).string == " requires Google Play services, but their signature is invalid.") {
                            isTarget = true
                        }
                    }
                }
                if (isTarget) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    mutableMethod.addInstructions(0, """
                        const/4 v0, 0x0
                        return v0
                    """)
                }
            }
        }

        // --- 3. Redirect Play Integrity Binding ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                var hasIntegrityAction = false
                var vendingInstructionIndex = -1
                var vendingRegister = -1

                impl.instructions.forEachIndexed { index, instruction ->
                    if (instruction is ReferenceInstruction) {
                        val ref = instruction.reference
                        if (ref is StringReference) {
                            if (ref.string == "com.google.android.play.core.expressintegrityservice.BIND_EXPRESS_INTEGRITY_SERVICE" ||
                                ref.string == "com.google.android.play.core.integrityservice.BIND_INTEGRITY_SERVICE") {
                                hasIntegrityAction = true
                            }
                            if (hasIntegrityAction && ref.string == "com.android.vending") {
                                vendingInstructionIndex = index
                                if (instruction is OneRegisterInstruction) {
                                    vendingRegister = instruction.registerA
                                }
                            }
                        }
                    }
                }

                if (hasIntegrityAction && vendingInstructionIndex != -1 && vendingRegister != -1) {
                    val mutableMethod = mutableClassDefBy(def).methods.firstOrNull { it.name == method.name && it.returnType == method.returnType }
                    mutableMethod?.addInstructions(vendingInstructionIndex + 1, """
                        const-string v$vendingRegister, "app.revanced.android.vending"
                    """)
                }
            }
        }
    }
}
