package app.pichiwa.patches.whatsapp

import app.morphe.patcher.api.Patch
import app.morphe.patcher.api.PatchContext

class HideGMS : Patch() {
    override val name = "Hide GMS"
    override val description = "Pretends that Google Play Services is missing to force fallback registration flows."
    override val version = "1.0.0"

    override fun execute(context: PatchContext) {
        val targetClass = "com/google/android/gms/common/GooglePlayServicesUtil"
        val targetMethod = "A00(Landroid/content/Context;I)I"

        context.smali.getClass(targetClass)?.let { cls ->
            cls.getMethod(targetMethod)?.let { method ->
                method.clearInstructions()
                method.addInstruction("const/4 v0, 0x1")
                method.addInstruction("return v0")
                context.log("Injected HideGMS hook into GooglePlayServicesUtil.A00")
            } ?: context.log("Failed to find A00 in GooglePlayServicesUtil")
        } ?: context.log("Failed to find GooglePlayServicesUtil class")
    }
}
