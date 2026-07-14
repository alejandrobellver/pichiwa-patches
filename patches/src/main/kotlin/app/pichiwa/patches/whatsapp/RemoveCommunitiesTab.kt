package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.literal
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
            filters = listOf(literal(200), literal(300), literal(400), literal(900))
        ).let { match ->
            val impl = match.originalMethod.implementation ?: return@let
            val instructions = impl.instructions.toList()
            
            for (i in 0 until instructions.size - 3) {
                val instr1 = instructions[i]
                if (instr1.opcode.name == "const/16" && instr1 is com.android.tools.smali.dexlib2.iface.instruction.NarrowLiteralInstruction) {
                    if (instr1.narrowLiteral == 600) {
                        val instr2 = instructions[i + 1]
                        if (instr2.opcode.name == "invoke-static") {
                            val instr4 = instructions[i + 3]
                            if (instr4.opcode.name == "invoke-virtual") {
                                val listReg = (instr4 as com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction).registerC
                                val tempReg = if (listReg == 0) 1 else 0
                                match.method.addInstructions(i + 4, """
                                    const/16 v${tempReg}, 0x258
                                    invoke-static {v${tempReg}}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
                                    move-result-object v${tempReg}
                                    invoke-virtual {v${listReg}, v${tempReg}}, Ljava/util/ArrayList;->remove(Ljava/lang/Object;)Z
                                """)
                                break
                            }
                        }
                    }
                }
            }
        }
    }
}

