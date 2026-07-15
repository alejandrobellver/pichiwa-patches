package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.methodCall
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
        // --- 1. Spoof Installer ---
        Fingerprint(
            filters = listOf(methodCall(name = "getInstallerPackageName"))
        ).let { match ->
            val invokeIdx = match.instructionMatches[0].index
            val impl = match.originalMethod.implementation ?: return@let
            val moveResult = impl.instructions.elementAtOrNull(invokeIdx + 1) as? OneRegisterInstruction ?: return@let
            val reg = moveResult.registerA
            match.method.addInstructions(invokeIdx + 2, """
                const-string v$reg, "com.android.vending"
            """)
        }

        Fingerprint(
            filters = listOf(methodCall(name = "getInitiatingPackageName"))
        ).let { match ->
            val invokeIdx = match.instructionMatches[0].index
            val impl = match.originalMethod.implementation ?: return@let
            val moveResult = impl.instructions.elementAtOrNull(invokeIdx + 1) as? OneRegisterInstruction ?: return@let
            val reg = moveResult.registerA
            match.method.addInstructions(invokeIdx + 2, """
                const-string v$reg, "com.android.vending"
            """)
        }

        // --- 2. Spoof Signature (Removed due to native crash) ---

        // GMS checks are handled natively by MicroG-RE. We only redirect Integrity.

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
                            if (ref.string == "com.google.android.play.core.expressintegrityservice.BIND_EXPRESS_INTEGRITY_SERVICE") {
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
