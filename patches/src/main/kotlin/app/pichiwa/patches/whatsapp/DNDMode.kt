package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

@Suppress("unused")
val dndMode = bytecodePatch(
    name = "Modo Fantasma",
    description = "No recibes ni envias mensajes mientras este activo.",
    default = false
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("MessageHandler/start"))
        ).let { match ->
            match.classDef.methods.first { it.name == "A03" }.let { method ->
                method.addInstructions(0, """
                    invoke-static {}, LX/00I;->A00()Landroid/app/Application;
                    move-result-object v0
                    if-nez v0, :cond_skip_pichiwa
                    const-string v1, "pichiwa_prefs"
                    const/4 v2, 0x0
                    invoke-virtual {v0, v1, v2}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;
                    move-result-object v0
                    if-nez v0, :cond_skip_pichiwa
                    const-string v1, "dnd_mode"
                    invoke-interface {v0, v1, v2}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z
                    move-result v0
                    if-nez v0, :cond_skip_pichiwa
                    return-void
                    :cond_skip_pichiwa
                """.trimIndent())
            }
        }
    }
}
