package util

import app.morphe.patcher.patch.Patch
import app.morphe.patcher.patch.loadPatchesFromJar
import com.google.gson.GsonBuilder
import java.io.File
import java.net.URLClassLoader
import java.util.jar.Manifest

fun main() {
    val patchFiles = setOf(
        File("build/libs/").listFiles { file ->
            val fileName = file.name
            !fileName.contains("javadoc") &&
                    !fileName.contains("sources") &&
                    fileName.endsWith(".mpp")
        }!!.first()
    )
    val loadedPatches = loadPatchesFromJar(patchFiles)
    val patchClassLoader = URLClassLoader(patchFiles.map { it.toURI().toURL() }.toTypedArray())
    val manifest = patchClassLoader.getResources("META-INF/MANIFEST.MF")

    while (manifest.hasMoreElements()) {
        Manifest(manifest.nextElement().openStream())
            .mainAttributes
            .getValue("Version")
            ?.let {
                generatePatchList(it, loadedPatches)
            }
    }
}

@Suppress("DEPRECATION")
private fun generatePatchList(version: String, patches: Set<Patch<*>>) {
    val listJson = File("../patches.json")

    val patchesList = patches.sortedBy { it.name }.map { patch ->
        JsonPatch(
            patch.name!!,
            patch.description,
            patch.use,
            patch.dependencies.map { it.javaClass.simpleName },
            patch.compatiblePackages?.map { (packageName, versions) -> JsonPatch.CompatiblePackage(packageName, versions) },
            patch.options.values.map { option ->
                JsonPatch.Option(
                    option.key,
                    option.title,
                    option.description,
                    option.required,
                    option.type.toString(),
                    option.default,
                    option.values,
                )
            },
        )
    }

    val gson = GsonBuilder()
        .serializeNulls()
        .disableHtmlEscaping()
        .setPrettyPrinting()
        .create()

    val output = gson.toJson(
        mapOf(
            "version" to version,
            "patches" to patchesList,
        )
    )

    listJson.writeText(output)
}

@Suppress("unused")
private data class JsonPatch(
    val name: String? = null,
    val description: String? = null,
    val use: Boolean = true,
    val dependencies: List<String>,
    val compatiblePackages: List<CompatiblePackage>? = null,
    val options: List<Option>,
) {
    data class CompatiblePackage(
        val name: String,
        val versions: Set<String>?
    )

    data class Option(
        val key: String,
        val title: String?,
        val description: String?,
        val required: Boolean,
        val type: String,
        val default: Any?,
        val values: Map<String, Any?>?,
    )
}
