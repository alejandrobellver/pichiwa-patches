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
    description = "Evita que otros borren sus mensajes o estados.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("msgstore/revoke/missing-old-id "))
        ).let { match ->
            match.method.addInstructions(0, """
                invoke-static {}, $EXT->shouldAllowRevoke()Z
                move-result v0
                if-nez v0, :original
                return-void
                :original
            """)
        }
    }
}
