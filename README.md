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
