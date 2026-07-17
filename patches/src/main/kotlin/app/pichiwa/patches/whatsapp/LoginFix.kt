package app.pichiwa.patches.whatsapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.pichiwa.patches.shared.Constants.WHATSAPP

@Suppress("unused")
val loginFix = bytecodePatch(
    name = "Login Fix",
    description = "Bypasses verification bans by spoofing signature hashes and installer. REQUIRED: Install microG-RE for Play Integrity.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // Spoof SHA-1 cert comparison
        Fingerprint(
            filters = listOf(string("38a0f7d505fe18fec64fbf343ecaaaf310dbd799"))
        ).let { match ->
            val returnType = match.method.returnType
            if (returnType == "Z") {
                match.method.addInstructions(0, """
                    const/4 v0, 0x1
                    return v0
                """)
            } else if (returnType == "V") {
                match.method.addInstructions(0, """
                    return-void
                """)
            }
        }

        // Spoof SHA-256 hash comparison
        Fingerprint(
            filters = listOf(string("8P1sW0EPJcslw7UzRsiXL64w-O50Ed-RBICtay1g24M"))
        ).let { match ->
            val returnType = match.method.returnType
            if (returnType == "Z") {
                match.method.addInstructions(0, """
                    const/4 v0, 0x1
                    return v0
                """)
            } else if (returnType == "V") {
                match.method.addInstructions(0, """
                    return-void
                """)
            }
        }

        // Spoof SHA-1 cert comparison (SDK 33+)
        Fingerprint(
            filters = listOf(string("8b0debf9516af037c9be2f539584b97fe9781764"))
        ).let { match ->
            val returnType = match.method.returnType
            if (returnType == "Z") {
                match.method.addInstructions(0, """
                    const/4 v0, 0x1
                    return v0
                """)
            } else if (returnType == "V") {
                match.method.addInstructions(0, """
                    return-void
                """)
            }
        }

        // Spoof SHA-256 hash comparison (SDK 33+)
        Fingerprint(
            filters = listOf(string("-5INOBvuGyCT8n3I8T2ZTaYp3JGIfQUps1yaLcT0psI"))
        ).let { match ->
            val returnType = match.method.returnType
            if (returnType == "Z") {
                match.method.addInstructions(0, """
                    const/4 v0, 0x1
                    return v0
                """)
            } else if (returnType == "V") {
                match.method.addInstructions(0, """
                    return-void
                """)
            }
        }

        // Spoof installer package name check
        Fingerprint(
            filters = listOf(string("getInstallerPackageName"))
        ).let { match ->
            val returnType = match.method.returnType
            if (returnType == "Z") {
                match.method.addInstructions(0, """
                    const/4 v0, 0x1
                    return v0
                """)
            } else if (returnType == "V") {
                match.method.addInstructions(0, """
                    return-void
                """)
            }
        }
    }
}
