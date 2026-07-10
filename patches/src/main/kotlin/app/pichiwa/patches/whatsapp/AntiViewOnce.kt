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
    name = "Anti-View Once",
    description = "Ve medios efímeros sin límite y permite capturas.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        Fingerprint(
            filters = listOf(string("INSERT_VIEW_ONCE_SQL"))
        ).let { match ->
            val im = match.instructionMatches[0]
            val register = im.getInstruction<OneRegisterInstruction>().registerA

            match.method.addInstructions(im.index + 1, """
                invoke-static {}, $EXT->canViewOnceBeViewed()Z
                move-result v$register
                if-nez v$register, :original
                const/4 v$register, 0x0
                :original
            """)
        }
    }
}
