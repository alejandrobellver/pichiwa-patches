package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val dndMode = bytecodePatch(
    name = "DND Mode",
    description = "No marca mensajes como leídos al abrir chats.",
    default = false
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("presencestatemanager/setUnavailable "))
        ).let { match ->
            match.method.addInstructions(0, """
                .catch Ljava/lang/Throwable; {:try_start_dnd .. :try_end_dnd} :catch_dnd
                :try_start_dnd
                invoke-static {}, $EXT->shouldEnableDndMode()Z
                move-result v0
                if-nez v0, :original
                return-void
                :try_end_dnd
                .catchall {:try_start_dnd .. :try_end_dnd} :catch_dnd
                :catch_dnd
                :original
            """)
        }
    }
}
