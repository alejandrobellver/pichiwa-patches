# PichiWA Patches

Morphe patches for WhatsApp.

## ⚠️ Disclaimer

Using patches on WhatsApp may result in temporary or permanent suspension of your account.
Meta (WhatsApp) can detect client modifications. Use these patches at your own risk.

## ❓ About

Privacy and quality-of-life patches for WhatsApp:

- **Hide Read Receipts** — Read messages without sending blue ticks
- **Hide Typing** — Type without showing "typing..."
- **Hide Recording** — Record voice notes discreetly
- **Anti-View Once** — View once media without limits, take screenshots
- **Download Status** — Save photo/video status updates
- **No Forward Limit** — Forward messages to unlimited contacts
- **HD Media** — Send images/videos without compression
- **Remove Updates Tab** — Hide the status/updates tab
- **Remove Communities Tab** — Hide the communities tab

## 🩹 Patches

| Patch | Description |
|-------|-------------|
| HideReadReceipts | Hides blue ticks when reading messages |
| HideTypingIndicator | Hides "typing..." indicator |
| AntiViewOnce | View once media without limits, allow screenshots |
| DownloadStatus | Download photo/video status updates |
| RemoveForwardLimit | Remove forward message limit |
| MediaQuality | Send HD images/videos without compression |
| RemoveUpdatesTab | Remove the Updates tab |
| RemoveCommunitiesTab | Remove the Communities tab |

### How to use

Add in Morphe: [Morphe add-source link](https://morphe.software/add-source?github=PichiWHO/pichiwa-patches)

Or manually add the repo URL as a patch source in Morphe.

## 🛠️ Development

```bash
# Build the .mpp bundle
./gradlew buildAndroid

# Output is at:
# patches/build/libs/patches-*.mpp
```

### Branches

- `dev` — Active development (automatic pre-releases)
- `main` — Stable releases

### Semantic commits

| Type | Release |
|------|---------|
| `feat:` | Minor |
| `fix:` | Patch |
| `chore:` | No release |

## 📜 License

PichiWA Patches are licensed under [GNU General Public License v3.0](LICENSE).
