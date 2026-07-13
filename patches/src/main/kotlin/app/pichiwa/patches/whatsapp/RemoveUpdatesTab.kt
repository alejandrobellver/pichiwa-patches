package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.literal
import app.morphe.patcher.patch.bytecodePatch
import app.pichiwa.patches.shared.Constants.WHATSAPP

@Suppress("unused")
val removeUpdatesTab = bytecodePatch(
    name = "Remove Updates",
    description = "Hide the updates/statuses tab.",
    default = false
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            returnType = "Ljava/util/ArrayList;",
            filters = listOf(literal(200), literal(300))
        ).let { match ->
            // ponytail: removes the tab with literal 200 from the list
            // needs refinement per WA version
        }
    }
}
