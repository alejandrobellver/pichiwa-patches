## [0.3.2-dev.11](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.10...v0.3.2-dev.11) (2026-07-13)

### Bug Fixes

* **whatsapp:** use reflection to avoid class verification errors in DND and FreezeLastSeen ([b7b8873](https://github.com/alejandrobellver/pichiwa-patches/commit/b7b88738116ddca344f09839eb586a40bff333ac))

## [0.3.2-dev.10](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.9...v0.3.2-dev.10) (2026-07-13)

### Bug Fixes

* **whatsapp:** fix incorrect fingerprint for DNDMode ([c02531d](https://github.com/alejandrobellver/pichiwa-patches/commit/c02531d528c38a76803ef2e17bc28d36728ea85e))

## [0.3.2-dev.9](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.8...v0.3.2-dev.9) (2026-07-13)

### Bug Fixes

* **whatsapp:** wrap WExtension calls in try-catch for DND and FreezeLastSeen to avoid startup crash ([a651530](https://github.com/alejandrobellver/pichiwa-patches/commit/a6515303188788342bb903edbff94256008efd95))

## [0.3.2-dev.8](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.7...v0.3.2-dev.8) (2026-07-13)

### Bug Fixes

* **whatsapp:** correct hex timestamp for 2099-12-31 in ExpirationBypass ([6c8bd7b](https://github.com/alejandrobellver/pichiwa-patches/commit/6c8bd7b6950a1540b61b560de53d49e8c1555e4d))

## [0.3.2-dev.7](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.6...v0.3.2-dev.7) (2026-07-13)

### Bug Fixes

* **whatsapp:** fix ExpirationBypass crash on root mount mode by replacing WExtension with pure smali ([9377a61](https://github.com/alejandrobellver/pichiwa-patches/commit/9377a61680f74b6129635e85b37719e64fc47420))

## [0.3.2-dev.6](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.5...v0.3.2-dev.6) (2026-07-13)

### Bug Fixes

* **whatsapp:** fix VerifyError in HideForwardedTag patch ([29d1553](https://github.com/alejandrobellver/pichiwa-patches/commit/29d155360bf87e4d4076009b3cd6bf15a38fa48b))

## [0.3.2-dev.5](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.4...v0.3.2-dev.5) (2026-07-13)

### 🐛 Bug Fixes

* **whatsapp:** update remaining fingerprints for v2.26.27.4 ([a0b0324](https://github.com/alejandrobellver/pichiwa-patches/commit/a0b0324bf0f3e89d41d474a446e0b0a41e62ba30))

## [0.3.2-dev.4](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.3...v0.3.2-dev.4) (2026-07-13)

### 🐛 Bug Fixes

* **whatsapp:** update fingerprints for EnableCopyStatus and RemoveForwardLimit for v2.26.27.4 ([ded0a82](https://github.com/alejandrobellver/pichiwa-patches/commit/ded0a826adeb9664faf2f9e1e065ac47838ddab4))

## [0.3.2-dev.3](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.2...v0.3.2-dev.3) (2026-07-13)

### 🐛 Bug Fixes

* **whatsapp:** update fingerprints for AntiEditMessage and AntiRevoke for v2.26.27.4 ([fb9472c](https://github.com/alejandrobellver/pichiwa-patches/commit/fb9472c547187e34682ee61215ef3c57c06e4aa1))

## [0.3.2-dev.2](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.1...v0.3.2-dev.2) (2026-07-13)

### 🐛 Bug Fixes

* correct path to extension.mpe for release upload ([dbb3891](https://github.com/alejandrobellver/pichiwa-patches/commit/dbb38912f96cb2f4135354418def3a4d9b6adcef))

## [0.3.2-dev.1](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.1...v0.3.2-dev.1) (2026-07-13)

### 🐛 Bug Fixes

* correct compatiblePackages schema in patches.json and readme generator ([92b3b08](https://github.com/alejandrobellver/pichiwa-patches/commit/92b3b085a0ba255b63678d51fff9aac78be9b143))
* rename patches-list.json to patches.json and add to release assets ([2997c63](https://github.com/alejandrobellver/pichiwa-patches/commit/2997c63c94599e79c0a305212f2216ee9549eec5))
* restore auto-generator markers in README.md ([70b1420](https://github.com/alejandrobellver/pichiwa-patches/commit/70b14209ecbeee8af250ab7a22f971e6da499f80))
* restore GitHub Actions workflows to automate releases ([6d253c2](https://github.com/alejandrobellver/pichiwa-patches/commit/6d253c297aa62205b0b6a5d05953a0b0db1e08b5))
* update github username to alejandrobellver ([795b4c2](https://github.com/alejandrobellver/pichiwa-patches/commit/795b4c2057f8545542dc40a518f458fa0c420e28))
* upload extension.mpe to github release and add to patches-bundle.json ([b6a2d1f](https://github.com/alejandrobellver/pichiwa-patches/commit/b6a2d1f633150c41ec5dbe8cf795734a653d1e2f))
