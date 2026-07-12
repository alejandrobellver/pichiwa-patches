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

## 🩹 Patches (19)

| Patch | Description | Default |
|-------|-------------|---------|
| AntiDetector | Bypass root/emulator/custom ROM detection | ✅ |
| AntiDisappearing | Keep disappearing messages visible | ❌ |
| AntiEditMessage | Prevent others from editing sent messages | ✅ |
| AntiRevoke | Prevent others from deleting messages/statuses | ✅ |
| AntiViewOnce | View once media without limits, allow screenshots | ✅ |
| DNDMode | Don't mark messages as read when opening chats | ❌ |
| DownloadStatus | Download photo/video status updates | ✅ |
| EnableCopyStatus | Copy text from contact statuses | ❌ |
| ExpirationBypass | Bypass forced verification and expiration | ✅ |
| FreezeLastSeen | Freeze last seen timestamp | ❌ |
| HideForwardedTag | Remove "forwarded" label from messages | ❌ |
| HideReadReceipts | Hide blue ticks when reading messages | ✅ |
| HideTypingIndicator | Hide "typing..." indicator | ✅ |
| MediaQuality | Send HD images/videos without compression | ✅ |
| RemoveCommunitiesTab | Remove the Communities tab | ❌ |
| RemoveForwardLimit | Remove forward message limit | ✅ |
| RemoveUpdatesTab | Remove the Updates tab | ❌ |
| SettingsMenu | PichiWA settings in Home overflow menu | ✅ |
| SpoofInstaller | Fake Google Play as installer source | ✅ |

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
