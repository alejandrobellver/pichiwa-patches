package app.pichiwa.patches.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.SupportedAbi

object Constants {
    val WHATSAPP = Compatibility(
        name = "WhatsApp",
        packageName = "com.whatsapp",
        apkFileType = ApkFileType.APK,
        appIconColor = 0x25D366,
        targets = listOf(
            AppTarget(
                version = "2.26.27.4",
                versionCodes = mapOf(SupportedAbi.ARM64_V8A to 262700402),
                isExperimental = false
            )
        )
    )
}
