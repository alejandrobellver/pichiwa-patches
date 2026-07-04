package app.template.patches.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_EXAMPLE = Compatibility(
        name = "XYZ app", // App name as it appears in the Android launcher.
        packageName = "com.example.app",
        apkFileType = ApkFileType.APK, // Preferred or recommended file type.
        appIconColor = 0xFF0045, // Icon color in Morphe Manager. Usually the same color as the icon background.
        targets = listOf(
            // "version = null" means the patch works with the latest app target
            // and is expected to work with all future app targets.
            //
            // It is highly recommended to always include the exact app version you developed your patches for
            // or the last version you have confirmed as 100% working.
            AppTarget(
                version = "2.0.0"
            ),
            AppTarget(
                version = "1.0.2"
            )
        )
    )


    val COMPATIBILITY_EXAMPLE_2 = Compatibility(
        name = "ABC app",
        packageName = "com.example.app",
        apkFileType = ApkFileType.APKM,
        appIconColor = 0x00FF45, // Icon color in Morphe Manager. Usually the same color as the icon background.
        targets = listOf(
            // 'any' version supported experimentally.
            AppTarget(
                version = null,
                isExperimental = true
            ),
            // App version confirmed 100% working.
            AppTarget(
                version = "1.0.2"
            )
        )
    )

}
