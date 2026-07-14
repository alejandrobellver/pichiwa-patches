package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.methodCall
import app.morphe.patcher.patch.bytecodePatch
import app.pichiwa.patches.shared.Constants.WHATSAPP
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

@Suppress("unused")
val spoofSignature = bytecodePatch(
    name = "Spoof Signature",
    description = "Forces WhatsApp signature checks to pass by patching the equals method comparison.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            definingClass = "LX/0e9;",
            name = "A01",
            returnType = "LX/0eL;"
        ).let { match ->
            match.instructionMatches = match.originalMethod.implementation?.instructions?.mapIndexedNotNull { index, instruction ->
                if (instruction.toString().contains("equals")) {
                    app.morphe.patcher.InstructionMatch(index, instruction)
                } else null
            } ?: emptyList()
            
            match.instructionMatches.forEach { insnMatch ->
                val invokeIdx = insnMatch.index
                val impl = match.originalMethod.implementation ?: return@forEach
                val moveResult = impl.instructions.elementAtOrNull(invokeIdx + 1) as? OneRegisterInstruction ?: return@forEach
                val reg = moveResult.registerA
                match.method.addInstructions(invokeIdx + 2, """
                    const/4 v$reg, 0x1
                """)
            }
        }
        
        Fingerprint(
            definingClass = "LX/0e8;",
            name = "A00",
            returnType = "LX/Hij;"
        ).let { match ->
            match.instructionMatches = match.originalMethod.implementation?.instructions?.mapIndexedNotNull { index, instruction ->
                if (instruction.toString().contains("equals")) {
                    app.morphe.patcher.InstructionMatch(index, instruction)
                } else null
            } ?: emptyList()
            
            match.instructionMatches.forEach { insnMatch ->
                val invokeIdx = insnMatch.index
                val impl = match.originalMethod.implementation ?: return@forEach
                val moveResult = impl.instructions.elementAtOrNull(invokeIdx + 1) as? OneRegisterInstruction ?: return@forEach
                val reg = moveResult.registerA
                match.method.addInstructions(invokeIdx + 2, """
                    const/4 v$reg, 0x1
                """)
            }
        }
        
        Fingerprint(
            definingClass = "LX/0eC;",
            name = "A00",
            returnType = "LX/Hij;"
        ).let { match ->
            match.instructionMatches = match.originalMethod.implementation?.instructions?.mapIndexedNotNull { index, instruction ->
                if (instruction.toString().contains("equals")) {
                    app.morphe.patcher.InstructionMatch(index, instruction)
                } else null
            } ?: emptyList()
            
            match.instructionMatches.forEach { insnMatch ->
                val invokeIdx = insnMatch.index
                val impl = match.originalMethod.implementation ?: return@forEach
                val moveResult = impl.instructions.elementAtOrNull(invokeIdx + 1) as? OneRegisterInstruction ?: return@forEach
                val reg = moveResult.registerA
                match.method.addInstructions(invokeIdx + 2, """
                    const/4 v$reg, 0x1
                """)
            }
        }
    }
}
