package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

@Suppress("unused")
val removeCommunitiesTab = bytecodePatch(
    name = "Remove Communities",
    description = "Hide the communities tab.",
    default = false
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            returnType = "Ljava/util/ArrayList;",
            filters = listOf(literal(200), literal(300))
        ).let { match ->
            val impl = match.originalMethod.implementation ?: return@let
            val returnIndices = impl.instructions.mapIndexedNotNull { index, instr ->
                if (instr.opcode.name == "return-object") index else null
            }
            returnIndices.reversed().forEach { retIdx ->
                val retInstr = impl.instructions.elementAt(retIdx) as com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
                val listReg = retInstr.registerA
                val tempReg = if (listReg == 0) 1 else 0
                match.method.addInstructions(retIdx, """
                    const/16 v${tempReg}, 0xc8
                    invoke-static {v${tempReg}}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
                    move-result-object v${tempReg}
                    invoke-virtual {v${listReg}, v${tempReg}}, Ljava/util/ArrayList;->remove(Ljava/lang/Object;)Z
                """)
            }
        }
    }
}

