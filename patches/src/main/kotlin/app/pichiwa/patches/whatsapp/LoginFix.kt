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
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // ponytail: each fingerprint wrapped in runCatching — hash strings may not
        // exist as const-string in all versions. Silently skip if not found.

        // Spoof Play Store SHA-256 hash comparison (Hze + Hza)
        runCatching {
            Fingerprint(
                filters = listOf(string("8P1sW0EPJcslw7UzRsiXL64w-O50Ed-RBICtay1g24M"))
            ).let { match ->
                val rt = match.method.returnType
                if (rt == "Z") match.method.addInstructions(0, "const/4 v0, 0x1\n                return v0")
            }
        }

        // Spoof Play Store SHA-256 hash comparison API 33+ (M30 + M2z)
        runCatching {
            Fingerprint(
                filters = listOf(string("-5INOBvuGyCT8n3I8T2ZTaYp3JGIfQUps1yaLcT0psI"))
            ).let { match ->
                val rt = match.method.returnType
                if (rt == "Z") match.method.addInstructions(0, "const/4 v0, 0x1\n                return v0")
            }
        }

        // Spoof Play Store dev-keys SHA-256 hash (debug builds)
        runCatching {
            Fingerprint(
                filters = listOf(string("GXWy8XF3vIml3_MfnmSmyuKBpT3B0dWbHRR_4cgq-gA"))
            ).let { match ->
                val rt = match.method.returnType
                if (rt == "Z") match.method.addInstructions(0, "const/4 v0, 0x1\n                return v0")
            }
        }

        // Spoof GoogleSignatureVerifier (I7w)
        runCatching {
            Fingerprint(
                filters = listOf(
                    string("Package has more than one signature."),
                    string("GoogleSignatureVerifier")
                )
            ).let { match ->
                val rt = match.method.returnType
                if (rt == "Z") match.method.addInstructions(0, "const/4 v0, 0x1\n                return v0")
            }
        }

        // Spoof Wear OS signature verifier (I8c — "ClockWork" certs)
        runCatching {
            Fingerprint(
                filters = listOf(string("ClockWork"))
            ).let { match ->
                val rt = match.method.returnType
                if (rt == "Z") match.method.addInstructions(0, "const/4 v0, 0x1\n                return v0")
            }
        }

        // ponytail: SHA-1 hashes (38a0f7d5..., 8b0debf9...) do NOT exist as
        // const-string in this APK version — cannot fingerprint.
        // ponytail: getInstallerPackageName is a method ref, not a const-string
        // — string() filter cannot find it. Use WExtension.spoofPackageInfo
        // for runtime installer spoofing.
    }
}
