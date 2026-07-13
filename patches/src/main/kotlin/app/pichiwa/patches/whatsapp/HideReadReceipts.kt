package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val hideReadReceipts = bytecodePatch(
    name = "Ocultar lectura",
    description = "Lee mensajes sin enviar ticks azules.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            returnType = "V",
            parameters = emptyList(),
            filters = listOf(string("receipt"))
        ).let { match ->
            match.method.addInstructions(0, """
                ${app.pichiwa.patches.shared.SmaliHelper.getPrefHideReadReceipts("v0")}
                if-nez v0, :original
                return-void
                :original
            """)
        }
    }
}
