group = "app.pichiwa"

patches {
    about {
        name = "PichiWA Patches"
        description = "Parches Morphe para WhatsApp — privacidad, anti-view-once, y más"
        source = "git@github.com:PichiWHO/pichiwa-patches.git"
        author = "Pichi"
        contact = "https://github.com/PichiWHO/pichiwa-patches"
        website = "https://github.com/PichiWHO/pichiwa-patches"
        license = "GPLv3"
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

// Separate configuration so gson is available at runtime for the
// generatePatchesList task but never bundled into the APK.
val patchListGeneratorClasspath: Configuration by configurations.creating

dependencies {
    compileOnly(libs.gson)
    patchListGeneratorClasspath(libs.gson)
    implementation(libs.morphe.patches.library)
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"

        dependsOn(build)

        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("util.PatchListGeneratorKt")
    }

    // Used by gradle-semantic-release-plugin.
    publish {
        dependsOn("generatePatchesList")
    }
}