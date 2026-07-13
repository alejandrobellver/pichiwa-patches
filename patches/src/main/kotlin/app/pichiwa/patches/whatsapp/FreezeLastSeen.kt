package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

@Suppress("unused")
val freezeLastSeen = bytecodePatch(
    name = "Freeze Last Seen",
    description = "Freeze the last seen time.",
    default = false
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("presencestatemanager/setAvailable/new-state: "))
        ).let { match ->
            match.method.addInstructions(0, """
                return-void
            """.trimIndent())
        }
    }
}
