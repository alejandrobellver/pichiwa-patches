package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.pichiwa.patches.shared.Constants.WHATSAPP

@Suppress("unused")
val forceGmsSuccess = bytecodePatch(
    name = "Force GMS Success",
    description = "Ensures every GooglePlayServicesUtil.A00 call returns SUCCESS (0) to avoid update prompts.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // Iterate over all classes and replace any method matching the exact signature.
        classDefForEach { def ->
            def.methods.forEach { method ->
                if (method.name == "A00" && method.parameters == listOf("Landroid/content/Context;", "I") && method.returnType == "I") {
                    // Overwrite the method to immediately return SUCCESS (0).
                    method.addInstructions(0, """
                        const/4 v1, 0x0
                        return v1
                    """)
                }
            }
        }
    }
}
