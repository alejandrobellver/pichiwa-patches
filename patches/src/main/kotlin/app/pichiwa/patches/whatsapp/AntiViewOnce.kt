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
        var viewOnceInterface: String? = null
        var setViewOnceStateMethod: String? = null

        Fingerprint(
            filters = listOf(string("GET_VIEW_ONCE_STATE_BY_MESSAGE_ROW_ID_SQL"))
        ).let { match ->
            val impl = match.originalMethod.implementation ?: return@let
            val instructions = impl.instructions.toList()
            
            val lastInvoke = instructions.last { it.opcode.name == "invoke-interface" }
            val methodRef = (lastInvoke as com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction).reference as com.android.tools.smali.dexlib2.iface.reference.MethodReference
            viewOnceInterface = methodRef.definingClass
            setViewOnceStateMethod = methodRef.name
            
            val regD = (lastInvoke as com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction).registerD
            val index = instructions.indexOf(lastInvoke)
            
            match.method.addInstructions(index, """
                const/4 v$regD, 0x0
            """)
        }

        Fingerprint(
            filters = listOf(string("UPDATE_VIEW_ONCE_SQL"))
        ).let { match ->
            match.method.addInstructions(0, """
                const/4 p1, 0x0
            """)
        }

        classDefForEach { def ->
            if (viewOnceInterface != null && def.interfaces.contains(viewOnceInterface)) {
                val method = def.methods.firstOrNull { it.name == setViewOnceStateMethod }
                if (method != null) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.returnType == method.returnType }
                    mutableMethod.addInstructions(0, """
                        const/4 p1, 0x0
                    """)
                }
            }
        }

        classDefForEach { def ->
            if (def.type == "Lcom/whatsapp/viewonce/ui/messaging/ViewOnceViewerActivity;") {
                def.methods.forEach { method ->
                    val impl = method.implementation ?: return@forEach
                    val instructions = impl.instructions.toList()
                    
                    for (i in 0 until instructions.size - 1) {
                        val instr = instructions[i]
                        if (instr.opcode.name == "invoke-virtual" && instr is com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction) {
                            if (instr.reference.toString() == "Landroid/app/Activity;->getWindow()Landroid/view/Window;") {
                                val nextInstr = instructions[i + 1]
                                if (nextInstr.opcode.name == "move-result-object" && nextInstr is com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction) {
                                    val reg = nextInstr.registerA
                                    val mutableMethod = mutableClassDefBy(def).methods.firstOrNull { it.name == method.name && it.returnType == method.returnType }
                                    mutableMethod?.addInstructions(i + 2, """
                                        const/4 v$reg, 0x0
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
}
