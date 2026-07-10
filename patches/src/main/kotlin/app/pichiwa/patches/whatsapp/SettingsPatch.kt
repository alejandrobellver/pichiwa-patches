package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val MENU_HOOK = "Lapp/pichiwa/extension/extension/PichiwaMenuHook;"

@Suppress("unused")
val settingsPatch = bytecodePatch(
    name = "Settings Menu",
    description = "Añade acceso a ajustes de PichiWA en el menú de Home.",
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
                invoke-static {p0, p1}, $MENU_HOOK->injectMenuItems(Landroid/app/Activity;Landroid/view/Menu;)V
            """)
        }
    }
}
