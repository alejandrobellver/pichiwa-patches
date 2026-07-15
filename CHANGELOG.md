## [0.4.0-dev.41](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.40...v0.4.0-dev.41) (2026-07-15)

### Bug Fixes

* **whatsapp:** check signatures array length to prevent ArrayIndexOutOfBoundsException crash ([22c7d30](https://github.com/alejandrobellver/pichiwa-patches/commit/22c7d3036a153c1a4f0221dea86ceefbb07e4aaf))
* **whatsapp:** spoof Java PackageInfo.signatures and restore MicroG-RE Play Integrity hook ([7f5403d](https://github.com/alejandrobellver/pichiwa-patches/commit/7f5403d956e2c97f988fba5101bcee094f34587a))
* **whatsapp:** test if disabling java signature spoofing fixes VerifyError ([f2ddcc0](https://github.com/alejandrobellver/pichiwa-patches/commit/f2ddcc0f34f82645782abf9b004eebdde8f2d807))

## [0.4.0-dev.41](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.40...v0.4.0-dev.41) (2026-07-15)

### Bug Fixes

* **whatsapp:** spoof Java PackageInfo.signatures and restore MicroG-RE Play Integrity hook ([7f5403d](https://github.com/alejandrobellver/pichiwa-patches/commit/7f5403d956e2c97f988fba5101bcee094f34587a))
* **whatsapp:** test if disabling java signature spoofing fixes VerifyError ([f2ddcc0](https://github.com/alejandrobellver/pichiwa-patches/commit/f2ddcc0f34f82645782abf9b004eebdde8f2d807))

## [0.4.0-dev.40](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.39...v0.4.0-dev.40) (2026-07-15)

### Bug Fixes

* **whatsapp:** disable Play Integrity entirely by removing installer spoofing and redirecting vending to dummy package to force fallback ([47da55a](https://github.com/alejandrobellver/pichiwa-patches/commit/47da55ac5ff9c8b2affdecfd70e5064875a8c8b0))

## [0.4.0-dev.39](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.38...v0.4.0-dev.39) (2026-07-15)

### Bug Fixes

* **whatsapp:** remove global GMS spoofing to fix FCM and SMS Retriever crashes, keep only Vending spoofing for Play Integrity ([fea1861](https://github.com/alejandrobellver/pichiwa-patches/commit/fea186153b1a5a0b4c643e3f5cc8c488574bf6cd))

## [0.4.0-dev.38](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.37...v0.4.0-dev.38) (2026-07-15)

### Bug Fixes

* **whatsapp:** update MicroG-RE signature payload with official WhatsApp certificate ([874f3ae](https://github.com/alejandrobellver/pichiwa-patches/commit/874f3ae396a33e03fa0aed87c9a8e21e3fe8e374))

## [0.4.0-dev.37](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.36...v0.4.0-dev.37) (2026-07-15)

### Bug Fixes

* **whatsapp:** inject original signature into manifest to enable MicroG-RE spoofing ([8da55b3](https://github.com/alejandrobellver/pichiwa-patches/commit/8da55b3d42d066e10d3f60ffcf8849109adbebe6))

## [0.4.0-dev.36](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.35...v0.4.0-dev.36) (2026-07-15)

### Bug Fixes

* **whatsapp:** inject microg packages into queries via extension manifest to bypass package visibility restrictions ([144888f](https://github.com/alejandrobellver/pichiwa-patches/commit/144888fc917a2f6fdf2cb200455ac8550ade8e12))

## [0.4.0-dev.35](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.34...v0.4.0-dev.35) (2026-07-15)

### Bug Fixes

* **whatsapp:** globally redirect gms and vending to MicroG-RE to fix Play Integrity / SafetyNet fallback ([b977564](https://github.com/alejandrobellver/pichiwa-patches/commit/b9775643bb1a0fd697da562ee6546cdc0e2e7258))

## [0.4.0-dev.34](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.33...v0.4.0-dev.34) (2026-07-15)

### Bug Fixes

* **whatsapp:** add missing MethodReference import in LoginFix ([0a03f26](https://github.com/alejandrobellver/pichiwa-patches/commit/0a03f26f8d75f61f5a86470b88ee52254d0892b5))
* **whatsapp:** bypass Play Services signature locally instead of breaking API availability ([2938a46](https://github.com/alejandrobellver/pichiwa-patches/commit/2938a4611f15b83b80208ad7efd7908fb1596e00))

## [0.4.0-dev.33](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.32...v0.4.0-dev.33) (2026-07-15)

### Bug Fixes

* **whatsapp:** intercept both Standard and Express Integrity intents ([581ab1a](https://github.com/alejandrobellver/pichiwa-patches/commit/581ab1ae5815c0a92836f988e24089298eb04549))

## [0.4.0-dev.32](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.31...v0.4.0-dev.32) (2026-07-15)

### Bug Fixes

* **whatsapp:** target exact signature method instead of all Context methods ([917a228](https://github.com/alejandrobellver/pichiwa-patches/commit/917a228eccb39d938883363332cd3a2d9d7ea4fa))

## [0.4.0-dev.31](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.30...v0.4.0-dev.31) (2026-07-15)

### Bug Fixes

* **whatsapp:** revert global GMS replace and dynamically patch GooglePlayServicesUtil ([712f8d5](https://github.com/alejandrobellver/pichiwa-patches/commit/712f8d5e0ed176edec0d068c6577a2ff62040a24))

## [0.4.0-dev.30](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.29...v0.4.0-dev.30) (2026-07-15)

### New Features

* **whatsapp:** globally redirect GMS strings to microG-RE ([06a0b2d](https://github.com/alejandrobellver/pichiwa-patches/commit/06a0b2d4b6f211c88dacc78c45d21b512cbc8bbb))

## [0.4.0-dev.29](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.28...v0.4.0-dev.29) (2026-07-15)

### New Features

* **whatsapp:** globally redirect GMS and Vending to microG-RE ([a49883b](https://github.com/alejandrobellver/pichiwa-patches/commit/a49883b225f5b666ee0a1f5b832983856e487070))

## [0.4.0-dev.28](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.27...v0.4.0-dev.28) (2026-07-15)

### Bug Fixes

* **whatsapp:** remove dangerous signature spoofing that caused SIGABRT in RegisterPhone ([a95549d](https://github.com/alejandrobellver/pichiwa-patches/commit/a95549de32e0b5a8ef3f09d7bd03e918bdc967ec))

## [0.4.0-dev.27](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.26...v0.4.0-dev.27) (2026-07-15)

### Bug Fixes

* remove express integrity bypass which caused native crash ([f93ed89](https://github.com/alejandrobellver/pichiwa-patches/commit/f93ed8949fd669de733bcc9904495668009353ca))

## [0.4.0-dev.26](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.25...v0.4.0-dev.26) (2026-07-15)

### Bug Fixes

* remove global GMS spoofing as it may cause native aborts ([4c5eacb](https://github.com/alejandrobellver/pichiwa-patches/commit/4c5eacb3c37d12f0c0effaaf688d43bd59fe3c10))

## [0.4.0-dev.25](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.24...v0.4.0-dev.25) (2026-07-15)

### Bug Fixes

* remove signature spoofing which caused JNI native crash in msys ([e478957](https://github.com/alejandrobellver/pichiwa-patches/commit/e478957bf8998f2dae51192da96fc9105c68aba4))

## [0.4.0-dev.24](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.23...v0.4.0-dev.24) (2026-07-15)

### New Features

* rename patch to Login Bypass and delete aggressive ForceGmsSuccess patch ([2385dc4](https://github.com/alejandrobellver/pichiwa-patches/commit/2385dc4ed464b7e556b5421eb0915e1dc34ba290))

## [0.4.0-dev.23](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.22...v0.4.0-dev.23) (2026-07-15)

### Bug Fixes

* actually remove communities and updates tabs from the ArrayList to prevent ViewPager NPE ([bd5e6d7](https://github.com/alejandrobellver/pichiwa-patches/commit/bd5e6d772217594b032adbd83b568873fcbdc540))
* add missing import for literal in RemoveCommunitiesTab ([6e6f567](https://github.com/alejandrobellver/pichiwa-patches/commit/6e6f56721d8829320aa8e8f4bbcb377b0a8ca9f0))
* anchor Remove Communities on 200 instead of 600 due to smali goto block ([7eed174](https://github.com/alejandrobellver/pichiwa-patches/commit/7eed174702776e8af2c0816b3af518c9b30d03e9))
* AntiViewOnce now hooks GET_VIEW_ONCE_STATE_BY_MESSAGE_ROW_ID_SQL to bypass expiration checks instead of causing insertion exceptions ([881d1e0](https://github.com/alejandrobellver/pichiwa-patches/commit/881d1e0be6f1ca213cbd4a09978de0740d7ba0f6))
* bypass FLAG_SECURE for screenshots in view once media and intercept UPDATE_VIEW_ONCE_SQL ([190cf1d](https://github.com/alejandrobellver/pichiwa-patches/commit/190cf1d209fde47719d67cade42533633430ca1e))
* bypass ViewOnce screenshot block using universal FLAG_SECURE hook ([1f9b111](https://github.com/alejandrobellver/pichiwa-patches/commit/1f9b1116b6ceb94a376978dae463ff8cffa3cbe3))
* dynamically hook LX/5k7 interface and intercept CM2 to force view once state to unread in memory ([4ae581f](https://github.com/alejandrobellver/pichiwa-patches/commit/4ae581f7f516e8cbb769c1b96ae130b4c30c5c6d))
* force GooglePlayServicesUtil to return SUCCESS to prevent update prompts ([bda7876](https://github.com/alejandrobellver/pichiwa-patches/commit/bda7876b5c887d7236525f910c7f621f5a8a4e7c))
* highly robust instruction injection before return-object in A07 ([ce6ef24](https://github.com/alejandrobellver/pichiwa-patches/commit/ce6ef24e341ba4e142d8dd760438390678551374))
* make fingerprint strictly match tab population method to prevent PatchException and apply patch correctly ([35b071d](https://github.com/alejandrobellver/pichiwa-patches/commit/35b071db869eac7193ede25caf742ada72c95f0e))
* **patches:** add @Keep annotations to extension classes to guarantee R8 does not strip them, preventing Dalvik NoClassDefFoundError ([71b883d](https://github.com/alejandrobellver/pichiwa-patches/commit/71b883d45a2a9cb03e66433086a12f5e324f8e89))
* **patches:** eliminate all WExtension usages to bypass Dalvik AOT class verification NoClassDefFoundError ([98f5e22](https://github.com/alejandrobellver/pichiwa-patches/commit/98f5e2255c8c99590b33e2871d97e0f7e104c208))
* **patches:** fix syntax error in AntiViewOnce, translate patches and UI to English, and update README ([3da4dc3](https://github.com/alejandrobellver/pichiwa-patches/commit/3da4dc36b76b793eb068b15a567389cc7638492e))
* **patches:** remove static dependency on PichiwaMenuHook to avoid AOT verification failure ([d54356a](https://github.com/alejandrobellver/pichiwa-patches/commit/d54356a3f68b3d3e565bd23890f17e76de39c715))
* **patches:** resolve kotlin compilation errors caused by incorrectly escaped double quotes ([71a47b0](https://github.com/alejandrobellver/pichiwa-patches/commit/71a47b06d9a03f147746479bee278b526ce6d022))
* **patches:** restore correct fingerprint for SettingsPatch ([049f559](https://github.com/alejandrobellver/pichiwa-patches/commit/049f559786a1cd33925181822cec63ab67108092))
* **patches:** revert @Keep to fix build and inject extension URL into patches-bundle.json to fix NoClassDefFoundError ([862348e](https://github.com/alejandrobellver/pichiwa-patches/commit/862348ed6c2789f54eebd0799fd7c8fdf1890813))
* **patches:** revert to WExtension and prevent R8 from stripping it to fix Dalvik Verification errors without corrupting locals ([024fcbe](https://github.com/alejandrobellver/pichiwa-patches/commit/024fcbeab35769acea502859edfb1b805e47da8e))
* **patches:** rip out WExtension completely ([85a4779](https://github.com/alejandrobellver/pichiwa-patches/commit/85a4779931208e2cca7a01641fd6c7296f650d95))
* **patches:** rip out WExtension completely ([cb0b709](https://github.com/alejandrobellver/pichiwa-patches/commit/cb0b70905b18d8104c1422fc752a34de724fb78f))
* **patches:** update HideGMS to new Morphe API ([1e44f63](https://github.com/alejandrobellver/pichiwa-patches/commit/1e44f6381caee944bfd11f21705b227746185516))
* **patches:** update SpoofInstaller to support Android 11+ (getInitiatingPackageName) ([fc9b4f7](https://github.com/alejandrobellver/pichiwa-patches/commit/fc9b4f7da0a6120777107bb93d73994fd8d662be))
* **patches:** use low registers for Dalvik injection in SettingsPatch ([83acfd4](https://github.com/alejandrobellver/pichiwa-patches/commit/83acfd4160a3b7d81e5a8147649c5f0f7aec4354))
* PatchException in RemoveCommunitiesTab by returning correct type ([be20a39](https://github.com/alejandrobellver/pichiwa-patches/commit/be20a396efef1953562a58c93c25d25a266ba4bd))
* remove HideGMS from SMSVerificationBypass as it causes crash with MicroG ([902eef1](https://github.com/alejandrobellver/pichiwa-patches/commit/902eef188fd3eacabcc5fdaacba8dac913a76e23))
* resolve compilation errors in Kotlin patches ([1339669](https://github.com/alejandrobellver/pichiwa-patches/commit/133966904c641c57623f29af1d1148f9d7915267))
* resolve merge conflict in patches.json ([da66053](https://github.com/alejandrobellver/pichiwa-patches/commit/da660537147ea96fd86bb0d729fcdf586674e60a))
* revert RemoveUpdatesTab to use the reliable const/16 anchor strategy ([68ec6aa](https://github.com/alejandrobellver/pichiwa-patches/commit/68ec6aa3e9f2e24918e9c186041f964cd7b181ed))
* robust instruction injection for Remove Communities and Remove Updates ([b7856fc](https://github.com/alejandrobellver/pichiwa-patches/commit/b7856fc6ee708f732e152e0302f2b5bff8dd3a54))
* VerifyError in AntiRevoke and NoSuchMethodError in SpoofSignature ([af6ba57](https://github.com/alejandrobellver/pichiwa-patches/commit/af6ba573aeec9b216a1722406d36440c602bdb7b))
* **whatsapp:** correct hex timestamp for 2099-12-31 in ExpirationBypass ([b6ff113](https://github.com/alejandrobellver/pichiwa-patches/commit/b6ff113690da7e6ee08bae3962dea2b5f7d6d1d5))
* **whatsapp:** fix ExpirationBypass crash on root mount mode by replacing WExtension with pure smali ([c3fff80](https://github.com/alejandrobellver/pichiwa-patches/commit/c3fff806e13295653daf0504e19bc7f60ae3423d))
* **whatsapp:** fix incorrect fingerprint for DNDMode ([9b7e925](https://github.com/alejandrobellver/pichiwa-patches/commit/9b7e92521261e6da3eac12324d185d165108f90e))
* **whatsapp:** fix incorrect returnType in SpoofSignature fingerprint for 0e8 ([1aa798d](https://github.com/alejandrobellver/pichiwa-patches/commit/1aa798df4bfac0fbce388a5557e51c5d160f9b50))
* **whatsapp:** fix kotlin compilation error in SpoofSignature patch ([c6e90cb](https://github.com/alejandrobellver/pichiwa-patches/commit/c6e90cb49cf4ca84ac2d1f9e39ecef149b93e7cb))
* **whatsapp:** fix missing move-exception in DNDMode and FreezeLastSeen smali ([d2df1cf](https://github.com/alejandrobellver/pichiwa-patches/commit/d2df1cfeb3d8e071901495ca3b0deaddf931aecf))
* **whatsapp:** fix VerifyError in HideForwardedTag patch ([e9aa043](https://github.com/alejandrobellver/pichiwa-patches/commit/e9aa043b5d8da8cd8e7deee45f37232268bb03c4))
* **whatsapp:** iterate backwards to prevent index shift in SpoofSignature ([301b50a](https://github.com/alejandrobellver/pichiwa-patches/commit/301b50a28fa62e8c92c47350f446b7bb6c275d81))
* **whatsapp:** properly check if instruction is ReferenceInstruction in SpoofSignature ([f469e1e](https://github.com/alejandrobellver/pichiwa-patches/commit/f469e1e5a2ad731fc01aaa3c192895be7d979da0))
* **whatsapp:** use correct register type in HideGMS to avoid VerifyError ([7f9ce99](https://github.com/alejandrobellver/pichiwa-patches/commit/7f9ce99676c196b3b98ac3d9295565f99fbeb888))
* **whatsapp:** use reflection to avoid class verification errors in DND and FreezeLastSeen ([fe6f633](https://github.com/alejandrobellver/pichiwa-patches/commit/fe6f63389f268c562b353958cca0e8bbb132edde))
* **whatsapp:** wrap WExtension calls in try-catch for DND and FreezeLastSeen to avoid startup crash ([779b918](https://github.com/alejandrobellver/pichiwa-patches/commit/779b9181832bba80fba2c9067e93100982962483))

### New Features

* add global ForceGmsSuccess patch and clean SMSVerificationBypass ([97aca27](https://github.com/alejandrobellver/pichiwa-patches/commit/97aca270a020ab1db385fe2a5dfed88e45742261))
* add global ForceGmsSuccess patch and clean SMSVerificationBypass ([ccbc7d5](https://github.com/alejandrobellver/pichiwa-patches/commit/ccbc7d5bceb4f41b37ed24b04602a7396de31a51))
* add MicroG-RE support and auto-install ([7d16162](https://github.com/alejandrobellver/pichiwa-patches/commit/7d1616280a02232b6f7f52c8352eb128f16bbc13))
* merge SMS verification bypass patches into SMSVerificationBypass ([b454643](https://github.com/alejandrobellver/pichiwa-patches/commit/b45464355c77eeb033c1855ae7efeb7ab391a50f))
* merge spoof, microG, and anti‑ban patches into SMSVerificationBypass ([d444ee0](https://github.com/alejandrobellver/pichiwa-patches/commit/d444ee07b18f8986b9f8f15fa28fd089069b3a9b))
* merge spoof, microG, and anti‑ban patches into SMSVerificationBypass ([b7d5ffa](https://github.com/alejandrobellver/pichiwa-patches/commit/b7d5ffaf1dca11133e8afa1971645ed2e68e6f09))
* **patches:** add HideGMS to bypass integrity ([6d7a019](https://github.com/alejandrobellver/pichiwa-patches/commit/6d7a01935984a17269b5840dd1c813c9d0e38728))
* **whatsapp:** add SpoofSignature patch to force signature checks to pass ([6530638](https://github.com/alejandrobellver/pichiwa-patches/commit/65306387fe89b420be7b66d456a9824744f6f73e))

## [0.4.0-dev.22](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.21...v0.4.0-dev.22) (2026-07-15)

### Bug Fixes

* force GooglePlayServicesUtil to return SUCCESS to prevent update prompts ([793fc8c](https://github.com/alejandrobellver/pichiwa-patches/commit/793fc8c424638d8adee585756bc43bdd481af167))

## [0.4.0-dev.21](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.20...v0.4.0-dev.21) (2026-07-15)

### Bug Fixes

* remove HideGMS from SMSVerificationBypass as it causes crash with MicroG ([64beb79](https://github.com/alejandrobellver/pichiwa-patches/commit/64beb795da19c09e72fbe25e5e7d7566805cb119))

## [0.4.0-dev.20](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.19...v0.4.0-dev.20) (2026-07-15)

### New Features

* merge SMS verification bypass patches into SMSVerificationBypass ([5dea517](https://github.com/alejandrobellver/pichiwa-patches/commit/5dea51727f747b7eff462ae0330c61de83ec3d32))

## [0.4.0-dev.19](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.18...v0.4.0-dev.19) (2026-07-15)

### Bug Fixes

* bypass ViewOnce screenshot block using universal FLAG_SECURE hook ([ef7c35d](https://github.com/alejandrobellver/pichiwa-patches/commit/ef7c35d5cb5fea453580a1165c48f838602855d2))

## [0.4.0-dev.18](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.17...v0.4.0-dev.18) (2026-07-14)

### Bug Fixes

* dynamically hook LX/5k7 interface and intercept CM2 to force view once state to unread in memory ([9accbd4](https://github.com/alejandrobellver/pichiwa-patches/commit/9accbd4054687aa945d63d2d948fb53259d91032))

## [0.4.0-dev.17](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.16...v0.4.0-dev.17) (2026-07-14)

### Bug Fixes

* bypass FLAG_SECURE for screenshots in view once media and intercept UPDATE_VIEW_ONCE_SQL ([e0f8aee](https://github.com/alejandrobellver/pichiwa-patches/commit/e0f8aeed0737ff17dffdb89574698e1574cedc08))

## [0.4.0-dev.16](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.15...v0.4.0-dev.16) (2026-07-14)

### Bug Fixes

* AntiViewOnce now hooks GET_VIEW_ONCE_STATE_BY_MESSAGE_ROW_ID_SQL to bypass expiration checks instead of causing insertion exceptions ([8a05df9](https://github.com/alejandrobellver/pichiwa-patches/commit/8a05df92d2b22a5c91918010113c0d7c6aa512a1))

## [0.4.0-dev.15](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.14...v0.4.0-dev.15) (2026-07-14)

### Bug Fixes

* revert RemoveUpdatesTab to use the reliable const/16 anchor strategy ([71cd529](https://github.com/alejandrobellver/pichiwa-patches/commit/71cd52911e946732f90b1f84107a3f026836f73e))

## [0.4.0-dev.14](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.13...v0.4.0-dev.14) (2026-07-14)

### Bug Fixes

* anchor Remove Communities on 200 instead of 600 due to smali goto block ([1847000](https://github.com/alejandrobellver/pichiwa-patches/commit/1847000ee8634cb65c948d755495aeeca63f4ca4))

## [0.4.0-dev.13](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.12...v0.4.0-dev.13) (2026-07-14)

### Bug Fixes

* highly robust instruction injection before return-object in A07 ([55c8512](https://github.com/alejandrobellver/pichiwa-patches/commit/55c85122b6bb04aea14b0f7c9b77df75ff11abc9))

## [0.4.0-dev.12](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.11...v0.4.0-dev.12) (2026-07-14)

### Bug Fixes

* robust instruction injection for Remove Communities and Remove Updates ([67f5b57](https://github.com/alejandrobellver/pichiwa-patches/commit/67f5b57126d8cb8ad6fa07e37894acfa023fe286))

## [0.4.0-dev.11](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.10...v0.4.0-dev.11) (2026-07-14)

### Bug Fixes

* make fingerprint strictly match tab population method to prevent PatchException and apply patch correctly ([18cf53f](https://github.com/alejandrobellver/pichiwa-patches/commit/18cf53fe4f9dcb5411f5c84fa1548d09b0ca2704))

## [0.4.0-dev.10](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.9...v0.4.0-dev.10) (2026-07-14)

### Bug Fixes

* actually remove communities and updates tabs from the ArrayList to prevent ViewPager NPE ([40f95a3](https://github.com/alejandrobellver/pichiwa-patches/commit/40f95a3f02bd83d732e8ec46eae7beccbba4bd7c))
* add missing import for literal in RemoveCommunitiesTab ([94542a8](https://github.com/alejandrobellver/pichiwa-patches/commit/94542a8369118c61032cdadb1f493171c28bae6e))

## [0.4.0-dev.9](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.8...v0.4.0-dev.9) (2026-07-14)

### Bug Fixes

* PatchException in RemoveCommunitiesTab by returning correct type ([65f6536](https://github.com/alejandrobellver/pichiwa-patches/commit/65f6536204887cd837d0f54cc48ea4305392fd67))

## [0.4.0-dev.8](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.7...v0.4.0-dev.8) (2026-07-14)

### Bug Fixes

* VerifyError in AntiRevoke and NoSuchMethodError in SpoofSignature ([7c810ba](https://github.com/alejandrobellver/pichiwa-patches/commit/7c810ba6e0cae0f54bda3b80178d6e51f049c166))

## [0.4.0-dev.7](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.6...v0.4.0-dev.7) (2026-07-14)

### New Features

* add MicroG-RE support and auto-install ([91b1e6d](https://github.com/alejandrobellver/pichiwa-patches/commit/91b1e6da3029a9e5f3f533b753fed92883a3837a))

## [0.4.0-dev.6](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.5...v0.4.0-dev.6) (2026-07-14)

### Bug Fixes

* **whatsapp:** iterate backwards to prevent index shift in SpoofSignature ([29fa56f](https://github.com/alejandrobellver/pichiwa-patches/commit/29fa56f81adbe0c664eb8ff3c5852410ed8fcf31))

## [0.4.0-dev.5](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.4...v0.4.0-dev.5) (2026-07-14)

### Bug Fixes

* **whatsapp:** properly check if instruction is ReferenceInstruction in SpoofSignature ([dda93f1](https://github.com/alejandrobellver/pichiwa-patches/commit/dda93f18e61e537e79111c45bed3d8d8d68c1d27))

## [0.4.0-dev.4](https://github.com/alejandrobellver/pichiwa-patches/compare/v0.4.0-dev.3...v0.4.0-dev.4) (2026-07-14)

### Bug Fixes

* **whatsapp:** use correct register type in HideGMS to avoid VerifyError ([dac0667](https://github.com/alejandrobellver/pichiwa-patches/commit/dac066734bbb0e1ff0de051277b472c206910a7b))

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
