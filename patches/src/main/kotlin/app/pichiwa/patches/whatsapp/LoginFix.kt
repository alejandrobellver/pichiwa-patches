package app.pichiwa.patches.whatsapp

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.pichiwa.patches.shared.Constants.WHATSAPP
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference

@Suppress("unused")
val loginFix = bytecodePatch(
    name = "Login Fix",
    description = "Bypasses verification bans by redirecting all GMS communication to microG-RE. REQUIRED: You must manually install microG-RE for Play Integrity to pass.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // --- 1. Safely Spoof Installer and Initiating Package Names ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                val matches = impl.instructions.mapIndexedNotNull { index, instr ->
                    val ref = (instr as? ReferenceInstruction)?.reference?.toString()
                    // Match getInstallerPackageName or getInitiatingPackageName
                    if (ref == "Landroid/content/pm/PackageManager;->getInstallerPackageName(Ljava/lang/String;)Ljava/lang/String;" ||
                        ref == "Landroid/content/pm/InstallSourceInfo;->getInitiatingPackageName()Ljava/lang/String;"
                    ) index else null
                }
                
                if (matches.isNotEmpty()) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    // Process in reverse to maintain correct indices
                    matches.reversed().forEach { invokeIdx ->
                        val moveResult = impl.instructions.elementAtOrNull(invokeIdx + 1) as? OneRegisterInstruction ?: return@forEach
                        val reg = moveResult.registerA
                        mutableMethod.addInstructions(invokeIdx + 2, """
                            const-string v$reg, "com.android.vending"
                        """)
                    }
                }
            }
        }

        // --- 2. Force GoogleApiAvailability success (global) ---
        // Overwrite methods that check Google Play Services availability to always succeed.
        classDefForEach { def ->
            def.methods.forEach { method ->
                // isGooglePlayServicesAvailable(Context) -> SUCCESS (0)
                if (method.name == "isGooglePlayServicesAvailable" && method.parameters == listOf("Landroid/content/Context;") && method.returnType == "I") {
                    val mutableMethod = mutableClassDefBy(def).methods.firstOrNull { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    mutableMethod?.addInstructions(0, """
                        const/4 v0, 0x0
                        return v0
                    """)
                }
                // makeGooglePlayServicesAvailable(Activity) -> SUCCESS (0)
                if (method.name == "makeGooglePlayServicesAvailable" && method.parameters == listOf("Landroid/app/Activity;") && method.returnType == "I") {
                    val mutableMethod = mutableClassDefBy(def).methods.firstOrNull { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    mutableMethod?.addInstructions(0, """
                        const/4 v0, 0x0
                        return v0
                    """)
                }
                // getErrorResolutionPendingIntent(Context, int) -> null (no intent)
                if (method.name == "getErrorResolutionPendingIntent" && method.parameters == listOf("Landroid/content/Context;", "I") && method.returnType == "Landroid/app/PendingIntent;") {
                    val mutableMethod = mutableClassDefBy(def).methods.firstOrNull { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    mutableMethod?.addInstructions(0, """
                        const/4 v0, 0x0
                        return-object v0
                    """)
                }
            }
        }

        // --- 3. Global String Replacement for MicroG-RE ---
        // Redirects all Play Services and Play Store communications to microG-RE.
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                
                val replacements = mutableListOf<Pair<Int, String>>()
                
                impl.instructions.forEachIndexed { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        val str = (instr.reference as StringReference).string
                        if (str == "com.google.android.gms") {
                            if (instr is OneRegisterInstruction) {
                                replacements.add(index to "app.revanced.android.gms")
                            }
                        } else if (str == "com.android.vending") {
                            if (instr is OneRegisterInstruction) {
                                replacements.add(index to "app.revanced.android.vending")
                            }
                        }
                    }
                }
                
                if (replacements.isNotEmpty()) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    replacements.reversed().forEach { (index, newStr) ->
                        val instr = impl.instructions.elementAt(index) as OneRegisterInstruction
                        val reg = instr.registerA
                        mutableMethod.addInstructions(index + 1, """
                            const-string v$reg, "$newStr"
                        """)
                    }
                }
            }
        }
    }
}
