package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

@Suppress("unused")
val loginFix = bytecodePatch(
    name = "Login Fix",
    description = "Bypasses verification bans. REQUIRED: Install microG-RE for Play Integrity.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // ponytail: each fingerprint wrapped in runCatching — strings may not
        // exist as const-string in all versions. Silently skip if not found.

        // ── Signature spoofing (return true) ──

        // Play Store SHA-256 hash comparison (Hze + Hza)
        runCatching {
            Fingerprint(
                filters = listOf(string("8P1sW0EPJcslw7UzRsiXL64w-O50Ed-RBICtay1g24M"))
            ).let { match ->
                if (match.method.returnType == "Z")
                    match.method.addInstructions(0, "const/4 v0, 0x1\n                return v0")
            }
        }

        // Play Store SHA-256 hash comparison API 33+ (M30 + M2z)
        runCatching {
            Fingerprint(
                filters = listOf(string("-5INOBvuGyCT8n3I8T2ZTaYp3JGIfQUps1yaLcT0psI"))
            ).let { match ->
                if (match.method.returnType == "Z")
                    match.method.addInstructions(0, "const/4 v0, 0x1\n                return v0")
            }
        }

        // Play Store dev-keys SHA-256 hash (debug builds)
        runCatching {
            Fingerprint(
                filters = listOf(string("GXWy8XF3vIml3_MfnmSmyuKBpT3B0dWbHRR_4cgq-gA"))
            ).let { match ->
                if (match.method.returnType == "Z")
                    match.method.addInstructions(0, "const/4 v0, 0x1\n                return v0")
            }
        }

        // GoogleSignatureVerifier (I7w)
        runCatching {
            Fingerprint(
                filters = listOf(
                    string("Package has more than one signature."),
                    string("GoogleSignatureVerifier")
                )
            ).let { match ->
                if (match.method.returnType == "Z")
                    match.method.addInstructions(0, "const/4 v0, 0x1\n                return v0")
            }
        }

        // Wear OS signature verifier (I8c — "ClockWork" certs)
        runCatching {
            Fingerprint(
                filters = listOf(string("ClockWork"))
            ).let { match ->
                if (match.method.returnType == "Z")
                    match.method.addInstructions(0, "const/4 v0, 0x1\n                return v0")
            }
        }

        // ── Emulator / root detection bypass (return false / return-void) ──

        // 00L.A0C — emulator detection ("sdk_gphone64_arm64" unique to A0C)
        runCatching {
            Fingerprint(
                filters = listOf(string("sdk_gphone64_arm64"))
            ).let { match ->
                if (match.method.returnType == "Z")
                    match.method.addInstructions(0, "const/4 v0, 0x0\n                return v0")
            }
        }

        // 00L.A0F or JA8.A0P — root detection ("test-keys" + Superuser.apk + su paths)
        // catpcha: both methods share these strings; Fingerprint returns the first match.
        runCatching {
            Fingerprint(
                filters = listOf(string("/system/xbin/su"))
            ).let { match ->
                if (match.method.returnType == "Z")
                    match.method.addInstructions(0, "const/4 v0, 0x0\n                return v0")
            }
        }

        // JA8.A0P — comprehensive root detection ("/su/bin/su" unique to JA8)
        runCatching {
            Fingerprint(
                filters = listOf(string("/su/bin/su"))
            ).let { match ->
                if (match.method.returnType == "Z")
                    match.method.addInstructions(0, "const/4 v0, 0x0\n                return v0")
            }
        }

        // L6w.1.C5w — device info / root detection (void method, return-void)
        runCatching {
            Fingerprint(
                returnType = "V",
                filters = listOf(string("ExtraDeviceInfoCollector.populateData"))
            ).let { match ->
                match.method.addInstructions(0, "return-void")
            }
        }

        // Ib4.1.BpW — Genymotion superuser + QEMU debug lib detection (void, return-void)
        runCatching {
            Fingerprint(
                returnType = "V",
                filters = listOf(string("/dev/com.genymotion.superuser.daemon"))
            ).let { match ->
                match.method.addInstructions(0, "return-void")
            }
        }

        // ponytail: SHA-1 hashes (38a0f7d5..., 8b0debf9...) do NOT exist as
        // const-string in this APK version — cannot fingerprint.
        // ponytail: getInstallerPackageName is a method ref, not a const-string
        // — string() filter cannot find it. Use WExtension.spoofPackageInfo
        // for runtime installer spoofing.
    }
}
