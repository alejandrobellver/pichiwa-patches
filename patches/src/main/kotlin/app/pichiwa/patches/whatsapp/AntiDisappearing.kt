package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val antiDisappearing = bytecodePatch(
    name = "Anti Desaparecer",
    description = "Mantiene los mensajes temporales visibles.",
    default = false
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("expire_timestamp"))
        ).let { match ->
            match.method.addInstructions(0, """
                ${app.pichiwa.patches.shared.SmaliHelper.getPrefBoolean("anti_disappearing", false, false, "v0")}
                if-nez v0, :original
                return-void
                :original
            """)
        }
    }
}
