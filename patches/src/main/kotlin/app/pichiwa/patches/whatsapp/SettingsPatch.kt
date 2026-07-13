package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

@Suppress("unused")
val settingsPatch = bytecodePatch(
    name = "Settings Menu",
    description = "Add the Pichiwa menu in settings.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            definingClass = "Lcom/whatsapp/home/ui/HomeActivity;",
            name = "onCreateOptionsMenu",
            returnType = "Z",
            parameters = listOf("Landroid/view/Menu;")
        ).let { match ->
            match.method.addInstructions(0, """
                nop
            """)
        }
    }
}

