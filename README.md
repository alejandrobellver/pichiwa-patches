# PichiWA Patches

Morphe patches for WhatsApp.

> Not functional yet - work in progress.

## ⚠️ Disclaimer

Using patches on WhatsApp may result in temporary or permanent suspension of your account.
Meta (WhatsApp) can detect client modifications. Use these patches at your own risk.

## 📲 Add to Morphe

[<kbd> <br> Add to Morphe <br> </kbd>](https://morphe.software/add-source?github=alejandrobellver/pichiwa-patches)

Or manually add this repository URL as a patch source in Morphe:  
`https://github.com/alejandrobellver/pichiwa-patches`

<!-- PATCHES_START EXPANDED -->
> **[v0.3.2-dev.1](https://github.com/alejandrobellver/pichiwa-patches/releases/tag/v0.3.2-dev.1)**&nbsp;&nbsp;•&nbsp;&nbsp;`dev`&nbsp;&nbsp;•&nbsp;&nbsp;19 patches total
<details open>
<summary>📦 com.whatsapp&nbsp;&nbsp;•&nbsp;&nbsp;19 patches</summary>
<br>

**🎯 Supported versions:**

| 2.26.27.4 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Anti Desaparecer](#anti-desaparecer) | Mantiene los mensajes temporales visibles. |  |
| [Anti Detector](#anti-detector) | Bypass detección de root, emulador y ROM personalizada. |  |
| [Anti Editar](#anti-editar) | Evita que otros editen mensajes enviados. |  |
| [Anti Expiracion](#anti-expiracion) | Evita la verificacion forzada de version y expiracion de WhatsApp. |  |
| [Anti Revoke](#anti-revoke) | Evita que otros borren sus mensajes o estados. |  |
| [Anti-View Once](#anti-view-once) | Ve medios efímeros sin límite y permite capturas. |  |
| [Copiar estados](#copiar-estados) | Permite copiar texto de estados de contactos. |  |
| [DND Mode](#dnd-mode) | No marca mensajes como leídos al abrir chats. |  |
| [Descargar estados](#descargar-estados) | Guarda estados de foto y video directamente. |  |
| [Freeze Last Seen](#freeze-last-seen) | Congela la hora de última conexión. |  |
| [HD Media](#hd-media) | Envía imágenes y video sin compresión. |  |
| [Ocultar escritura](#ocultar-escritura) | Escribe sin mostrar "escribiendo...". |  |
| [Ocultar lectura](#ocultar-lectura) | Lee mensajes sin enviar ticks azules. |  |
| [Ocultar reenviado](#ocultar-reenviado) | Quita la etiqueta "reenviado" de los mensajes. |  |
| [Quitar Comunidades](#quitar-comunidades) | Oculta la pestaña de comunidades. |  |
| [Quitar Novedades](#quitar-novedades) | Oculta la pestaña de novedades/estados. |  |
| [Settings Menu](#settings-menu) | Añade acceso a ajustes de PichiWA en el menú de Home. |  |
| [Sin límite de reenvío](#sin-l-mite-de-reenv-o) | Reenvía mensajes a contactos ilimitados. |  |
| [Spoof instalador](#spoof-instalador) | Finge instalación desde Google Play para evitar restricciones. |  |

</details>

<!-- PATCHES_END -->

## 🛠️ Development

```bash
# Build patches (.mpp) and extension (.mpe)
./gradlew :patches:build :extensions:extension:build

# Optional: generate patches.json
./gradlew :patches:generatePatchesList

# Output:
#   patches/build/libs/patches-*.mpp
#   extensions/extension/build/morphe/extensions/extension.mpe
```

### Build requirements

```
JAVA_HOME=.../jdk-21
ANDROID_HOME=.../Android/Sdk
GITHUB_ACTOR=<github-username>
GITHUB_TOKEN=<github-token>
```

### Branches

- `dev` — Active development
- `main` — Stable releases

## 📜 License

PichiWA Patches are licensed under [GNU General Public License v3.0](LICENSE).
