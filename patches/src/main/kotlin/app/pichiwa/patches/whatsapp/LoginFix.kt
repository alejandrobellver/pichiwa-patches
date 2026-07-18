package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference

@Suppress("unused")
val loginFix = bytecodePatch(
    name = "Login Fix",
    description = "Bypasses verification bans by spoofing signature hashes.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // Track which fingerprints matched for reporting
        var patchedCount = 0

        // ponytail: classDefForEach to catch DUPLICATE classes with same hash string
        // (Hze + Hza both have "8P1sW0EP...", M30 + M2z both have "-5INOBvu...")
        classDefForEach { def ->
            val mutableDef = mutableClassDefBy(def)
            mutableDef.methods.forEach { method ->
                if (method.name == "<clinit>" || method.name == "<init>") return@forEach
                if (method.returnType != "Z") return@forEach
                val impl = method.implementation ?: return@forEach
                val hasHash = impl.instructions.any { instr ->
                    instr is ReferenceInstruction && instr.reference is StringReference &&
                        (instr.reference as StringReference).string in HASHES
                }
                if (hasHash) {
                    method.addInstructions(0, """
                        const/4 v0, 0x1
                        return v0
                    """)
                    patchedCount++
                }
            }
        }

        // Patch GoogleSignatureVerifier — unique multi-string fingerprint
        runCatching {
            Fingerprint(
                filters = listOf(
                    string("Package has more than one signature."),
                    string("GoogleSignatureVerifier")
                )
            ).let { match ->
                if (match.method.returnType == "Z") {
                    match.method.addInstructions(0, """
                        const/4 v0, 0x1
                        return v0
                    """)
                    patchedCount++
                }
            }
        }

        // NOTE: Facebook AppManager gatekeeper (0e8.A00 with "Provider package signature
        // does not match") returns Bundle, not boolean. Skip for now.
    }
}

private val HASHES = setOf(
    "8P1sW0EPJcslw7UzRsiXL64w-O50Ed-RBICtay1g24M",
    "-5INOBvuGyCT8n3I8T2ZTaYp3JGIfQUps1yaLcT0psI",
    "GXWy8XF3vIml3_MfnmSmyuKBpT3B0dWbHRR_4cgq-gA",
    "8b0debf9516af037c9be2f539584b97fe9781764",
    "38a0f7d505fe18fec64fbf343ecaaaf310dbd799"
)
