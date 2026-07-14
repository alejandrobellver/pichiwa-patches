package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

private const val EXT = "Lapp/pichiwa/extension/extension/WExtension;"

@Suppress("unused")
val antiViewOnce = bytecodePatch(
    name = "Anti View Once",
    description = "View ephemeral media without limits and allow screenshots.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("GET_VIEW_ONCE_STATE_BY_MESSAGE_ROW_ID_SQL"))
        ).let { match ->
            val impl = match.originalMethod.implementation ?: return@let
            val instructions = impl.instructions.toList()
            
            val lastInvoke = instructions.last { it.opcode.name == "invoke-interface" }
            val regD = (lastInvoke as com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction).registerD
            val index = instructions.indexOf(lastInvoke)
            
            match.method.addInstructions(index, """
                const/4 v$regD, 0x0
            """)
        }
    }
}
