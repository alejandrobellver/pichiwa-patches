## [0.4.0-dev.3](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.2...v0.4.0-dev.3) (2026-07-14)

### Bug Fixes

* **whatsapp:** fix incorrect returnType in SpoofSignature fingerprint for 0e8 ([d065c7b](https://github.com/alejandrobellver/pichiwa-patches/commit/d065c7b2dc16b36745033ed4c446e23ab9309c57))

## [0.4.0-dev.2](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.1...v0.4.0-dev.2) (2026-07-14)

### Bug Fixes

* **whatsapp:** fix kotlin compilation error in SpoofSignature patch ([428e7a3](https://github.com/alejandrobellver/pichiwa-patches/commit/428e7a31a989496be21a1c9bb73233cdb3c0f972))

### New Features

* **whatsapp:** add SpoofSignature patch to force signature checks to pass ([5c1c19f](https://github.com/alejandrobellver/pichiwa-patches/commit/5c1c19fc61fd67575592157e367a2d696484d5ae))

## [0.4.0-dev.1](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.21...v0.4.0-dev.1) (2026-07-14)

### Bug Fixes

* **patches:** update HideGMS to new Morphe API ([01ea4e1](https://github.com/alejandrobellver/pichiwa-patches/commit/01ea4e1ce6d9a898ea86bd03182f6b1dd7a99d7b))

### New Features

* **patches:** add HideGMS to bypass integrity ([f0e2ca8](https://github.com/alejandrobellver/pichiwa-patches/commit/f0e2ca8c1790f1e1b189651cb5bcdb61b4a1d10d))

## [0.3.2-dev.21](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.20...v0.3.2-dev.21) (2026-07-14)

### Bug Fixes

* **patches:** update SpoofInstaller to support Android 11+ (getInitiatingPackageName) ([f8fecc6](https://github.com/alejandrobellver/pichiwa-patches/commit/f8fecc6cce5c27b002d54b8d7330e1668b100095))

## [0.3.2-dev.20](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.19...v0.3.2-dev.20) (2026-07-13)

### Bug Fixes

* **patches:** rip out WExtension completely ([aefab87](https://github.com/alejandrobellver/pichiwa-patches/commit/aefab8771b8a0b2dcdb48890d265c99be3da7846))
* **patches:** rip out WExtension completely ([b8df012](https://github.com/alejandrobellver/pichiwa-patches/commit/b8df012ba0f7ff8743789d3ca50c054a27be130b))
* resolve merge conflict in patches.json ([8424d62](https://github.com/alejandrobellver/pichiwa-patches/commit/8424d62bc015227f4d279f9c142ea0fc0e111566))

## [0.3.2-dev.19](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.18...v0.3.2-dev.19) (2026-07-13)

### Bug Fixes

* **patches:** add @Keep annotations to extension classes to guarantee R8 does not strip them, preventing Dalvik NoClassDefFoundError ([97da667](https://github.com/alejandrobellver/pichiwa-patches/commit/97da66764ebde228d28daa538fa2cdb3051cf453))
* **patches:** revert @Keep to fix build and inject extension URL into patches-bundle.json to fix NoClassDefFoundError ([de5b3fc](https://github.com/alejandrobellver/pichiwa-patches/commit/de5b3fca025ebfc231497ba18fac38599e5d5b7f))

## [0.3.2-dev.18](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.17...v0.3.2-dev.18) (2026-07-13)

### Bug Fixes

* **patches:** revert to WExtension and prevent R8 from stripping it to fix Dalvik Verification errors without corrupting locals ([6936b02](https://github.com/alejandrobellver/pichiwa-patches/commit/6936b02b068f46ba7c069b42bea4b56dcf88be11))

## [0.3.2-dev.17](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.16...v0.3.2-dev.17) (2026-07-13)

### Bug Fixes

* **patches:** use low registers for Dalvik injection in SettingsPatch ([8592539](https://github.com/alejandrobellver/pichiwa-patches/commit/85925396a35e2126e351a53e4b59c49b20afeb3c))

## [0.3.2-dev.16](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.15...v0.3.2-dev.16) (2026-07-13)

### Bug Fixes

* **patches:** restore correct fingerprint for SettingsPatch ([958873d](https://github.com/alejandrobellver/pichiwa-patches/commit/958873d447d21d4cd64b245784269e7027e94d78))

## [0.3.2-dev.15](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.14...v0.3.2-dev.15) (2026-07-13)

### Bug Fixes

* **patches:** fix syntax error in AntiViewOnce, translate patches and UI to English, and update README ([929f298](https://github.com/alejandrobellver/pichiwa-patches/commit/929f298e291e0be86cedb0f66a47c708277561fe))

## [0.3.2-dev.14](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.13...v0.3.2-dev.14) (2026-07-13)

### Bug Fixes

* **patches:** eliminate all WExtension usages to bypass Dalvik AOT class verification NoClassDefFoundError ([5dc21a6](https://github.com/alejandrobellver/pichiwa-patches/commit/5dc21a61cf496985d7a042ead0135deb33f94b69))
* **patches:** remove static dependency on PichiwaMenuHook to avoid AOT verification failure ([64639f5](https://github.com/alejandrobellver/pichiwa-patches/commit/64639f587d40f6f70a2cdcacb99aecdb31a00671))
* **patches:** resolve kotlin compilation errors caused by incorrectly escaped double quotes ([0f8ab05](https://github.com/alejandrobellver/pichiwa-patches/commit/0f8ab053c95f0798dffbd19130e30085d7579cde))

## [0.3.2-dev.13](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.12...v0.3.2-dev.13) (2026-07-13)

### Bug Fixes

* resolve ClassNotFoundException during early startup ([27b4b86](https://github.com/alejandrobellver/pichiwa-patches/commit/27b4b869a13ef70c0a67a4e001097f7681f62798))

## [0.3.2-dev.12](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.3.2-dev.11...v0.3.2-dev.12) (2026-07-13)

### Bug Fixes

* **whatsapp:** fix missing move-exception in DNDMode and FreezeLastSeen smali ([f7d831a](https://github.com/alejandrobellver/pichiwa-patches/commit/f7d831a5632e00999af0504c820ccf3b6f4508c0))

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
