package app.pichiwa.patches.whatsapp

import app.morphe.patcher.patch.bytecodePatch
import app.pichiwa.patches.shared.Constants.WHATSAPP

@Suppress("unused")
val loginFix = bytecodePatch(
    name = "Login Fix",
    description = "Bypasses verification bans.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
    }
}
