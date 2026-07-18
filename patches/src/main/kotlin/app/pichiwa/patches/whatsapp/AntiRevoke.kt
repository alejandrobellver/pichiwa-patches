package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val antiRevoke = bytecodePatch(
    name = "Anti Revoke",
    description = "Prevent others from deleting their messages or statuses.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("msgstore/revoke/missing-old-id "))
        ).let { match ->
            val returnType = match.method.returnType
            val returnInst = if (returnType == "V") "return-void" else "const/4 v0, 0x0\n                return v0"
            match.method.addInstructions(0, """
                $returnInst
                :original
            """)
        }
    }
}
