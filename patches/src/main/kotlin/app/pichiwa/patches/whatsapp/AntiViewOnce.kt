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

        // --- 3. Bypass FLAG_SECURE universal utility ---
        // Instead of targeting ViewOnceViewerActivity, we target the utility method itself (e.g. LX/2mi;->A00(Landroid/view/Window;)V)
        // This ensures FLAG_SECURE is never applied, even if called from other fragments or on window focus changes.
        classDefForEach { def ->
            def.methods.forEach { method ->
                // The utility method takes a single Window parameter
                if (method.parameters.size == 1 && method.parameters[0].type == "Landroid/view/Window;") {
                    val impl = method.implementation ?: return@forEach
                    
                    var hasSecureFlag = false
                    var hasFlagsCall = false
                    
                    for (instr in impl.instructions) {
                        if (instr is com.android.tools.smali.dexlib2.iface.instruction.NarrowLiteralInstruction) {
                            if (instr.narrowLiteral == 0x2000) hasSecureFlag = true
                        }
                        if (instr is com.android.tools.smali.dexlib2.iface.instruction.WideLiteralInstruction) {
                            if (instr.wideLiteral == 0x2000L) hasSecureFlag = true
                        }
                        if (instr is com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction) {
                            val refStr = instr.reference.toString()
                            if (refStr == "Landroid/view/Window;->setFlags(II)V" || refStr == "Landroid/view/Window;->addFlags(I)V") {
                                hasFlagsCall = true
                            }
                        }
                    }
                    
                    if (hasSecureFlag && hasFlagsCall) {
                        val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters }
                        mutableMethod.addInstructions(0, """
                            return-void
                        """)
                    }
                }
            }
        }
    }
}
