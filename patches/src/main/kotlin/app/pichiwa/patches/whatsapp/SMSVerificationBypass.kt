package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.methodCall
import app.morphe.patcher.patch.bytecodePatch
import app.pichiwa.patches.shared.Constants.WHATSAPP
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference

@Suppress("unused")
val antiBan = bytecodePatch(
    name = "SMS Verification Bypass",
    description = "Bypasses SMS verification bans by spoofing signatures, installers, and hiding GMS. REQUIRED: You must manually install microG-RE for Play Integrity to pass.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // --- 1. Spoof Installer ---
        Fingerprint(
            filters = listOf(methodCall(name = "getInstallerPackageName"))
        ).let { match ->
            val invokeIdx = match.instructionMatches[0].index
            val impl = match.originalMethod.implementation ?: return@let
            val moveResult = impl.instructions.elementAtOrNull(invokeIdx + 1) as? OneRegisterInstruction ?: return@let
            val reg = moveResult.registerA
            match.method.addInstructions(invokeIdx + 2, """
                const-string v$reg, "com.android.vending"
            """)
        }

        Fingerprint(
            filters = listOf(methodCall(name = "getInitiatingPackageName"))
        ).let { match ->
            val invokeIdx = match.instructionMatches[0].index
            val impl = match.originalMethod.implementation ?: return@let
            val moveResult = impl.instructions.elementAtOrNull(invokeIdx + 1) as? OneRegisterInstruction ?: return@let
            val reg = moveResult.registerA
            match.method.addInstructions(invokeIdx + 2, """
                const-string v$reg, "com.android.vending"
            """)
        }

        // --- 2. Spoof Signature ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                var usesSignatures = false
                
                impl.instructions.forEach { instruction ->
                    if (instruction is ReferenceInstruction) {
                        val ref = instruction.reference
                        if (ref.toString() == "Landroid/content/pm/PackageInfo;->signatures:[Landroid/content/pm/Signature;") {
                            usesSignatures = true
                        }
                    }
                }
                
                if (usesSignatures) {
                    val instructionMatches = impl.instructions.mapIndexedNotNull { index, instruction ->
                        val ref = (instruction as? ReferenceInstruction)?.reference
                        if (ref?.toString()?.contains("equals") == true) index else null
                    }
                    
                    if (instructionMatches.isNotEmpty()) {
                        val mutableMethod = mutableClassDefBy(def).methods.firstOrNull { it.name == method.name && it.returnType == method.returnType }
                        instructionMatches.reversed().forEach { invokeIdx ->
                            val moveResult = impl.instructions.elementAtOrNull(invokeIdx + 1) as? OneRegisterInstruction ?: return@forEach
                            val reg = moveResult.registerA
                            mutableMethod?.addInstructions(invokeIdx + 2, """
                                const/4 v$reg, 0x1
                            """)
                        }
                    }
                }
            }
        }

        // GMS block removed – handled globally by ForceGmsSuccess.patch

        // --- 5. Force GoogleApiAvailability success (global) ---
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
                    val mutableMethod = mutableClassDefBy(def).methods.firstOrNull { it.name == method.name && it.returnType == method.returnType }
                    mutableMethod?.addInstructions(vendingInstructionIndex + 1, """
                        const-string v$vendingRegister, "app.revanced.android.vending"
                    """)
                }
            }
        }
    }
}
