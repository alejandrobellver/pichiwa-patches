package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

@Suppress("unused")
val antiDetector = bytecodePatch(
    name = "Anti Detector",
    description = "Bypass detección de root, emulador y ROM personalizada.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // Root detection — methods containing "/system/bin/su"
        // ponytail: hooks first match only; matchAll if multiple root checks
        Fingerprint(
            filters = listOf(string("/system/bin/su"))
        ).let { match ->
            match.method.addInstructions(0, """
                const/4 v0, 0x0
                return v0
            """)
        }

        // Emulator detection — contains "Android SDK built for x86"
        Fingerprint(
            filters = listOf(string("Android SDK built for x86"))
        ).let { match ->
            match.method.addInstructions(0, """
                const/4 v0, 0x0
                return v0
            """)
        }

        // Custom ROM detection — contains "cyanogen"
        Fingerprint(
            filters = listOf(string("cyanogen"))
        ).let { match ->
            match.method.addInstructions(0, """
                const/4 v0, 0x0
                return v0
            """)
        }
    }
}
