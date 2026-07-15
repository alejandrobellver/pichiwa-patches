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

        // --- 3. Hide GMS ---
        Fingerprint(
            definingClass = "Lcom/google/android/gms/common/GooglePlayServicesUtil;",
            name = "A00",
            returnType = "I",
            parameters = listOf("Landroid/content/Context;", "I")
        ).let { match ->
            match.method.addInstructions(0, """
                const/4 v1, 0x1
                return v1
            """)
        }

        // --- 4. MicroG Support (Redirect BIND_EXPRESS_INTEGRITY_SERVICE intent) ---
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
