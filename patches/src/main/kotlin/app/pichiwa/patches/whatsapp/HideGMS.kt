package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.pichiwa.patches.shared.Constants.WHATSAPP

@Suppress("unused")
val hideGMS = bytecodePatch(
    name = "Hide GMS",
    description = "Pretends that Google Play Services is missing to force fallback registration flows.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            definingClass = "Lcom/google/android/gms/common/GooglePlayServicesUtil;",
            name = "A00",
            returnType = "I",
            parameters = listOf("Landroid/content/Context;", "I")
        ).let { match ->
            match.method.addInstructions(0, """
                const/4 v1, 0x1
                return v1
            """)
        }
    }
}
