package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val hideTypingIndicator = bytecodePatch(
    name = "Ocultar escritura",
    description = "Escribe sin mostrar \"escribiendo...\".",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("HandleMeComposing/sendComposing; toJid="))
        ).let { match ->
            match.method.addInstructions(0, """
                ${app.pichiwa.patches.shared.SmaliHelper.getPrefBoolean(\"hide_typing\", true, true, \"v0\")}
                if-nez v0, :original
                return-void
                :original
            """)
        }
    }
}
