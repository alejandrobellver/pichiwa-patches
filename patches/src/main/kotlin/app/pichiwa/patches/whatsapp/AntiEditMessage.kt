package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val antiEditMessage = bytecodePatch(
    name = "Anti Editar",
    description = "Evita que otros editen mensajes enviados.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("MessageEditInfoStore/insertEditInfo/missing information in the FMessage"))
        ).let { match ->
            match.method.addInstructions(0, """
                ${app.pichiwa.patches.shared.SmaliHelper.getPrefBoolean("anti_edit", true, true, "v0")}
                if-nez v0, :original
                return-void
                :original
            """)
        }
    }
}
