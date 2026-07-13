package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val removeForwardLimit = bytecodePatch(
    name = "No Forward Limit",
    description = "Forward messages to unlimited contacts.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("UserActionsMessageForwarding/userActionForwardMessage"))
        ).let { match ->
            match.method.addInstructions(0, """
                invoke-static {}, $EXT->getForwardLimit()I
                move-result v0
                :original
            """)
        }
    }
}
