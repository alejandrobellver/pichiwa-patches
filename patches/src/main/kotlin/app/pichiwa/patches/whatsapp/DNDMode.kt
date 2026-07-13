package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

@Suppress("unused")
val dndMode = bytecodePatch(
    name = "Ghost Mode",
    description = "Do not receive or send messages while active.",
    default = false
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("MessageHandler/start"))
        ).let { match ->
            match.classDef.methods.first { it.name == "A03" }.let { method ->
                method.addInstructions(0, """
                    return-void
                """.trimIndent())
            }
        }
    }
}
