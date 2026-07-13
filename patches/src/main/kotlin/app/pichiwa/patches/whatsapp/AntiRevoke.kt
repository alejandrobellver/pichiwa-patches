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
            match.method.addInstructions(0, """
                ${app.pichiwa.patches.shared.SmaliHelper.getPrefBoolean("anti_revoke", true, true, "v0")}
                if-nez v0, :original
                return-void
                :original
            """)
        }
    }
}
