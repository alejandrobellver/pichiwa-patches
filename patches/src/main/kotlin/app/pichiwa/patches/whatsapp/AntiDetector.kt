package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

@Suppress("unused")
val antiDetector = bytecodePatch(
    name = "Anti Detector",
    description = "Bypass root, emulator, and custom ROM detection.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // ponytail: check returnType before patching — non-boolean methods
        // used in registration flow will NPE if patched to return 0.

        runCatching {
            Fingerprint(
                filters = listOf(string("/system/bin/su"))
            ).let { match ->
                if (match.method.returnType == "Z") {
                    match.method.addInstructions(0, "const/4 v0, 0x0\nreturn v0")
                }
            }
        }

        runCatching {
            Fingerprint(
                filters = listOf(string("Android SDK built for x86"))
            ).let { match ->
                if (match.method.returnType == "Z") {
                    match.method.addInstructions(0, "const/4 v0, 0x0\nreturn v0")
                }
            }
        }

        runCatching {
            Fingerprint(
                filters = listOf(string("cyanogen"))
            ).let { match ->
                if (match.method.returnType == "Z") {
                    match.method.addInstructions(0, "const/4 v0, 0x0\nreturn v0")
                }
            }
        }
    }
}
