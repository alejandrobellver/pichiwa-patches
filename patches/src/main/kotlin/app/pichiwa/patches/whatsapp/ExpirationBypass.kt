package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val expirationBypass = bytecodePatch(
    name = "Anti Expiracion",
    description = "Evita la verificacion forzada de version y expiracion de WhatsApp.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // ponytail: hooks software_forced_expiration to return 2099-12-31
        Fingerprint(
            returnType = "Ljava/util/Date;",
            filters = listOf(string("software_forced_expiration"))
        ).let { match ->
            match.method.addInstructions(0, """
                new-instance v0, Ljava/util/Date;
                const-wide v1, 0x3bb2328600L
                invoke-direct {v0, v1, v2}, Ljava/util/Date;-><init>(J)V
                return-object v0
            """)
        }
    }
}
