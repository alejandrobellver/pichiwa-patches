# PichiWA Patches

Morphe patches for WhatsApp.

# 🚨 EXPERIMENTAL: UNDER DEVELOPMENT 🚨
## This project is currently a Work In Progress and DOES NOT WORK.
### Please do not use it yet.

> Not functional yet - work in progress.

## ⚠️ Disclaimer

Using patches on WhatsApp may result in temporary or permanent suspension of your account.
Meta (WhatsApp) can detect client modifications. Use these patches at your own risk.

## 📲 Add to Morphe

[<kbd> <br> Add to Morphe <br> </kbd>](https://morphe.software/add-source?github=alejandrobellver/pichiwa-patches)

Or manually add this repository URL as a patch source in Morphe:  
`https://github.com/alejandrobellver/pichiwa-patches`

<!-- PATCHES_START EXPANDED -->
> **[v0.4.0-dev.54](https://github.com/alejandrobellver/pichiwa-patches/releases/tag/v0.4.0-dev.54)**&nbsp;&nbsp;•&nbsp;&nbsp;`dev`&nbsp;&nbsp;•&nbsp;&nbsp;18 patches total
<details open>
<summary>📦 com.whatsapp&nbsp;&nbsp;•&nbsp;&nbsp;18 patches</summary>
<br>

**🎯 Supported versions:**

| 2.26.27.4 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Anti Detector](#anti-detector) | Bypass root, emulator, and custom ROM detection. |  |
| [Anti Disappearing](#anti-disappearing) | Keep disappearing messages visible. |  |
| [Anti Edit](#anti-edit) | Prevent others from editing sent messages. |  |
| [Anti Expiration](#anti-expiration) | Bypass WhatsApp forced version and expiration verification. |  |
| [Anti Revoke](#anti-revoke) | Prevent others from deleting their messages or statuses. |  |
| [Anti View Once](#anti-view-once) | View ephemeral media without limits and allow screenshots. |  |
| [Copy Statuses](#copy-statuses) | Allow copying text from contact statuses. |  |
| [Freeze Last Seen](#freeze-last-seen) | Freeze the last seen time. |  |
| [Ghost Mode](#ghost-mode) | Do not receive or send messages while active. |  |
| [HD Media](#hd-media) | Send images and videos without compression. |  |
| [Hide Forwarded](#hide-forwarded) | Quita la etiqueta "reenviado" de los mensajes. |  |
| [Hide Read Receipts](#hide-read-receipts) | Read messages without sending blue ticks. |  |
| [Hide Typing](#hide-typing) | Escribe sin mostrar "escribiendo...". |  |
| [Login Fix](#login-fix) | Bypasses verification bans by spoofing signatures, installers, and faking GMS checks. REQUIRED: You must manually install microG-RE for Play Integrity to pass. |  |
| [No Forward Limit](#no-forward-limit) | Forward messages to unlimited contacts. |  |
| [Remove Communities](#remove-communities) | Hide the communities tab. |  |
| [Remove Updates](#remove-updates) | Hide the updates/statuses tab. |  |
| [Settings Menu](#settings-menu) | Add the Pichiwa menu in settings. |  |

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
