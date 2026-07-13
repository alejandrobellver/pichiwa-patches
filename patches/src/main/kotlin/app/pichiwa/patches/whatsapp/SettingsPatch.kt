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
            filters = listOf(string("conversations/oncontextitemselected/unsupported/"))
        ).let { match ->
            match.method.addInstructions(0, """
                const-string v30, "app.pichiwa.extension.extension.PichiwaMenuHook"
                invoke-static/range {v30 .. v30}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;
                move-result-object v30
                
                const-string v31, "injectMenuItems"
                const/4 v32, 0x2
                new-array v32, v32, [Ljava/lang/Class;
                const/4 v33, 0x0
                const-class v34, Landroid/app/Activity;
                aput-object v34, v32, v33
                const/4 v33, 0x1
                const-class v34, Landroid/view/Menu;
                aput-object v34, v32, v33
                
                invoke-virtual/range {v30 .. v32}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
                move-result-object v30
                
                const/4 v31, 0x0
                const/4 v32, 0x2
                new-array v32, v32, [Ljava/lang/Object;
                const/4 v33, 0x0
                aput-object p0, v32, v33
                const/4 v33, 0x1
                aput-object p1, v32, v33
                
                invoke-virtual/range {v30 .. v32}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
            """)
        }
    }
}

