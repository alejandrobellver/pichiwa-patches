package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.pichiwa.patches.shared.Constants.WHATSAPP
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference

@Suppress("unused")
val microgSupport = bytecodePatch(
    name = "MicroG Support",
    description = "Redirects Play Integrity to microG-RE and prompts auto-install if missing.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // 1. Auto-install hook in AppShell.onCreate
        val appShellDef = classDefByOrNull("Lcom/whatsapp/AppShell;")
        if (appShellDef != null) {
            val onCreate = mutableClassDefBy(appShellDef).methods.firstOrNull { it.name == "onCreate" }
            onCreate?.addInstructions(0, """
                invoke-virtual {p0}, Landroid/content/Context;->getPackageManager()Landroid/content/pm/PackageManager;
                move-result-object v0
                const-string v1, "app.revanced.android.gms"
                const/4 v2, 0x0
                :try_start_0
                invoke-virtual {v0, v1, v2}, Landroid/content/pm/PackageManager;->getPackageInfo(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
                goto :goto_installed
                :try_end_0
                .catch Landroid/content/pm/PackageManager${'$'}NameNotFoundException; {:try_start_0 .. :try_end_0} :catch_not_found
            
                :catch_not_found
                new-instance v0, Landroid/content/Intent;
                const-string v1, "android.intent.action.VIEW"
                invoke-direct {v0, v1}, Landroid/content/Intent;-><init>(Ljava/lang/String;)V
                const-string v1, "https://github.com/morpheapp/microg-RE/releases/latest/download/microg.apk"
                invoke-static {v1}, Landroid/net/Uri;->parse(Ljava/lang/String;)Landroid/net/Uri;
                move-result-object v1
                invoke-virtual {v0, v1}, Landroid/content/Intent;->setData(Landroid/net/Uri;)Landroid/content/Intent;
                const/high16 v1, 0x10000000
                invoke-virtual {v0, v1}, Landroid/content/Intent;->addFlags(I)Landroid/content/Intent;
                invoke-virtual {p0, v0}, Landroid/content/Context;->startActivity(Landroid/content/Intent;)V
                :goto_installed
            """)
        }

        // 2. Redirect BIND_EXPRESS_INTEGRITY_SERVICE intent
        // Find the method that creates the intent and sets the package to "com.android.vending"
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                var hasIntegrityAction = false
                var vendingInstructionIndex = -1
                var vendingRegister = -1

                impl.instructions.forEachIndexed { index, instruction ->
                    if (instruction is ReferenceInstruction) {
                        val ref = instruction.reference
                        if (ref is StringReference) {
                            if (ref.string == "com.google.android.play.core.expressintegrityservice.BIND_EXPRESS_INTEGRITY_SERVICE") {
                                hasIntegrityAction = true
                            }
                            if (hasIntegrityAction && ref.string == "com.android.vending") {
                                vendingInstructionIndex = index
                                if (instruction is OneRegisterInstruction) {
                                    vendingRegister = instruction.registerA
                                }
                            }
                        }
                    }
                }

                if (hasIntegrityAction && vendingInstructionIndex != -1 && vendingRegister != -1) {
                    // Inject an instruction right AFTER the "com.android.vending" const-string
                    // to override the register with "app.revanced.android.vending"
                    val mutableMethod = mutableClassDefBy(def).methods.firstOrNull { it.name == method.name && it.returnType == method.returnType }
                    mutableMethod?.addInstructions(vendingInstructionIndex + 1, """
                        const-string v$vendingRegister, "app.revanced.android.vending"
                    """)
                }
            }
        }
    }
}
