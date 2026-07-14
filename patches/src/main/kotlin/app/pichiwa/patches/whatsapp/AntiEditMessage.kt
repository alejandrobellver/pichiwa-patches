package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val antiEditMessage = bytecodePatch(
    name = "Anti Edit",
    description = "Prevent others from editing sent messages.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(returnType = "V", 
            filters = listOf(string("MessageEditInfoStore/insertEditInfo/missing information in the FMessage"))
        ).let { match ->
            match.method.addInstructions(0, """
                return-void
                :original
            """)
        }
    }
}

