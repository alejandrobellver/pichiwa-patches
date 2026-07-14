package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

@Suppress("unused")
val removeCommunitiesTab = bytecodePatch(
    name = "Remove Communities",
    description = "Hide the communities tab.",
    default = false
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(returnType = "V", 
            filters = listOf(string("No HomeFragment mapping for community tab id: "))
        ).let { match ->
            match.method.addInstructions(0, """
                return-void
            """)
        }
    }
}

