package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val freezeLastSeen = bytecodePatch(
    name = "Freeze Last Seen",
    description = "Congela la hora de última conexión.",
    default = false
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("presencestatemanager/setAvailable/new-state: "))
        ).let { match ->
            match.method.addInstructions(0, """
                .catch Ljava/lang/Throwable; {:try_start_freeze .. :try_end_freeze} :catch_freeze
                :try_start_freeze
                invoke-static {}, $EXT->shouldFreezeLastSeen()Z
                move-result v0
                if-nez v0, :original
                return-void
                :try_end_freeze
                .catchall {:try_start_freeze .. :try_end_freeze} :catch_freeze
                :catch_freeze
                :original
            """)
        }
    }
}
