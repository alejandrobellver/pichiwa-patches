package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val downloadStatus = bytecodePatch(
    name = "Descargar estados",
    description = "Guarda estados de foto y video directamente.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("msgstore/status_media/"))
        ).let { match ->
            match.method.addInstructions(0, """
                invoke-static {}, $EXT->canDownloadStatus()Z
                move-result v0
                if-nez v0, :original
                return-void
                :original
            """)
        }
    }
}
