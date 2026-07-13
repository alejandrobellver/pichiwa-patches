package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val dndMode = bytecodePatch(
    name = "DND Mode",
    description = "No marca mensajes como leídos al abrir chats.",
    default = false
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("MessageHandler/start"))
        ).let { match ->
            match.method.addInstructions(0, """
                .catch Ljava/lang/Throwable; {:try_start_dnd .. :try_end_dnd} :catch_dnd
                :try_start_dnd
                const-string v0, "app.pichiwa.extension.extension.WExtension"
                invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;
                move-result-object v0
                
                const-string v1, "shouldEnableDndMode"
                const/4 v2, 0x0
                new-array v2, v2, [Ljava/lang/Class;
                invoke-virtual {v0, v1, v2}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
                move-result-object v0
                
                const/4 v1, 0x0
                new-array v2, v1, [Ljava/lang/Object;
                invoke-virtual {v0, v1, v2}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
                move-result-object v0
                
                check-cast v0, Ljava/lang/Boolean;
                invoke-virtual {v0}, Ljava/lang/Boolean;->booleanValue()Z
                move-result v0
                if-nez v0, :original
                return-void
                :try_end_dnd
                .catchall {:try_start_dnd .. :try_end_dnd} :catch_dnd
                :catch_dnd
                :original
            """)
        }
    }
}
