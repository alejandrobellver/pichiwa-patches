package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.methodCall
import app.morphe.patcher.patch.bytecodePatch
import app.pichiwa.patches.shared.Constants.WHATSAPP
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

@Suppress("unused")
val spoofInstaller = bytecodePatch(
    name = "Spoof Installer",
    description = "Fake installation from Google Play to avoid restrictions.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
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
    }
}
