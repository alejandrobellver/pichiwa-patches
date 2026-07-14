package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.methodCall
import app.morphe.patcher.patch.bytecodePatch
import app.pichiwa.patches.shared.Constants.WHATSAPP
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction

@Suppress("unused")
val spoofSignature = bytecodePatch(
    name = "Spoof Signature",
    description = "Forces WhatsApp signature checks to pass by patching the equals method comparison.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
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
    }
}
