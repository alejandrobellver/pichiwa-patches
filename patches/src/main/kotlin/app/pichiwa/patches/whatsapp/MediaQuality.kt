package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val mediaQuality = bytecodePatch(
    name = "HD Media",
    description = "Envía imágenes y video sin compresión.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("ProcessVideoQuality(videoLimitMb="))
        ).let { match ->
            match.method.addInstructions(0, """
                ${app.pichiwa.patches.shared.SmaliHelper.getPrefBoolean(\"hd_media\", true, false, \"v0\")}
                if-nez v0, :original
                const/4 v0, 0x1
                return v0
                :original
            """)
        }
    }
}
