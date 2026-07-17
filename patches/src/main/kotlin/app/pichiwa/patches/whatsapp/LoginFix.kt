package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

@Suppress("unused")
val loginFix = bytecodePatch(
    name = "Login Fix",
    description = "Bypasses verification bans by spoofing signature hashes. REQUIRED: Install microG-RE for Play Integrity.",
    default = false
) {
    compatibleWith(WHATSAPP)

    execute {
        // ponytail: each fingerprint wrapped in runCatching — hash strings may not
        // exist as const-string in all versions. Silently skip if not found.

        runCatching {
            Fingerprint(
                filters = listOf(string("38a0f7d505fe18fec64fbf343ecaaaf310dbd799"))
            ).let { match ->
                val returnType = match.method.returnType
                when (returnType) {
                    "Z" -> match.method.addInstructions(0, "const/4 v0, 0x1\n                return v0")
                    "V" -> match.method.addInstructions(0, "return-void")
                }
            }
        }

        runCatching {
            Fingerprint(
                filters = listOf(string("8P1sW0EPJcslw7UzRsiXL64w-O50Ed-RBICtay1g24M"))
            ).let { match ->
                val returnType = match.method.returnType
                when (returnType) {
                    "Z" -> match.method.addInstructions(0, "const/4 v0, 0x1\n                return v0")
                    "V" -> match.method.addInstructions(0, "return-void")
                }
            }
        }

        runCatching {
            Fingerprint(
                filters = listOf(string("8b0debf9516af037c9be2f539584b97fe9781764"))
            ).let { match ->
                val returnType = match.method.returnType
                when (returnType) {
                    "Z" -> match.method.addInstructions(0, "const/4 v0, 0x1\n                return v0")
                    "V" -> match.method.addInstructions(0, "return-void")
                }
            }
        }

        runCatching {
            Fingerprint(
                filters = listOf(string("-5INOBvuGyCT8n3I8T2ZTaYp3JGIfQUps1yaLcT0psI"))
            ).let { match ->
                val returnType = match.method.returnType
                when (returnType) {
                    "Z" -> match.method.addInstructions(0, "const/4 v0, 0x1\n                return v0")
                    "V" -> match.method.addInstructions(0, "return-void")
                }
            }
        }
    }
}
