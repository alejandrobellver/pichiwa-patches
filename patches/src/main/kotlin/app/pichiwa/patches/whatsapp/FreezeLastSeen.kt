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
            filters = listOf(string("presencestatemanager/setAvailable/new-state"))
        ).let { match ->
            match.method.addInstructions(0, """
                invoke-static {}, $EXT->shouldFreezeLastSeen()Z
                move-result v0
                if-nez v0, :original
                return-void
                :original
            """)
        }
    }
}
