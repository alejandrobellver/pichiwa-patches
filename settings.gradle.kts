rootProject.name = "pichiwa-patches"

buildCache {
    local {
        isEnabled = !System.getenv().containsKey("CI")
    }
}

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        
        maven { url = uri("https://jitpack.io") }
    }
}

plugins {
    id("app.morphe.patches") version "1.3.2"
}

settings {
    extensions {
        defaultNamespace = "app.pichiwa.extension"
        proguardFiles(rootProject.projectDir.resolve("extensions/proguard-rules.pro").toString())
    }
}
