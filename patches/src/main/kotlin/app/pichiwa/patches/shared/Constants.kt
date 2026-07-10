package app.pichiwa.patches.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val WHATSAPP = Compatibility(
        name = "WhatsApp",
        packageName = "com.whatsapp",
        apkFileType = ApkFileType.APK,
        appIconColor = 0x25D366,
        targets = listOf(
            AppTarget(version = null, isExperimental = true)
        )
    )
}
