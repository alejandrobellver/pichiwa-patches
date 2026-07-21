package app.pichiwa.patches.whatsapp

import app.morphe.patcher.patch.resourcePatch
import app.pichiwa.patches.shared.Constants.WHATSAPP
import org.w3c.dom.Element

@Suppress("unused")
val networkSecurityConfigPatch = resourcePatch(
    name = "Network Security Config",
    description = "Habilita la intercepción de red (MITM) agregando un archivo limpio de configuración.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // Modificar el AndroidManifest para apuntar a nuestro archivo XML personalizado
        document("AndroidManifest.xml").use { manifestDoc ->
            val appElement = manifestDoc.getElementsByTagName("application").item(0) as? Element
            appElement?.setAttribute("android:networkSecurityConfig", "@xml/custom_network_security_config")
        }
        
        // Crear nuestro archivo de configuración (Morphe lo escribirá en el APK)
        val xmlContent = "" +
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<network-security-config>\n" +
            "    <base-config cleartextTrafficPermitted=\"true\">\n" +
            "        <trust-anchors>\n" +
            "            <certificates src=\"system\" />\n" +
            "            <certificates overridePins=\"true\" src=\"user\" />\n" +
            "        </trust-anchors>\n" +
            "    </base-config>\n" +
            "</network-security-config>"
            
        get("res/xml/custom_network_security_config.xml", true).writeText(xmlContent)
    }
}
