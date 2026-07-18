package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val hideForwardedTag = bytecodePatch(
    name = "Hide Forwarded",
    description = "Quita la etiqueta \"reenviado\" de los mensajes.",
    default = false
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("chatInfo/incrementUnseenImportantMessageCount "))
        ).let { match ->
            match.method.addInstructions(0, """
                const/4 v0, 0x0
                return v0
            """)
        }
    }
}
