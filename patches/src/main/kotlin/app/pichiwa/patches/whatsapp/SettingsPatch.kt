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
                const-string v0, "app.pichiwa.extension.extension.PichiwaMenuHook"
                invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;
                move-result-object v0
                
                const-string v1, "injectMenuItems"
                const/4 v2, 0x2
                new-array v2, v2, [Ljava/lang/Class;
                const/4 v3, 0x0
                const-class v4, Landroid/app/Activity;
                aput-object v4, v2, v3
                const/4 v3, 0x1
                const-class v4, Landroid/view/Menu;
                aput-object v4, v2, v3
                
                invoke-virtual {v0, v1, v2}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
                move-result-object v0
                
                const/4 v1, 0x0
                const/4 v2, 0x2
                new-array v2, v2, [Ljava/lang/Object;
                const/4 v3, 0x0
                aput-object p0, v2, v3
                const/4 v3, 0x1
                aput-object p1, v2, v3
                
                invoke-virtual {v0, v1, v2}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
            """)
        }
    }
}

