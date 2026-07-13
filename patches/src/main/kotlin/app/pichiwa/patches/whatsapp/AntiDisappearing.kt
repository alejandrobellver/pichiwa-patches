package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val antiDisappearing = bytecodePatch(
    name = "Anti Disappearing",
    description = "Keep disappearing messages visible.",
    default = false
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("expire_timestamp"))
        ).let { match ->
            match.method.addInstructions(0, """
                return-void
                :original
            """)
        }
    }
}
