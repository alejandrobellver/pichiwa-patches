package app.pichiwa.patches.whatsapp

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.pichiwa.patches.shared.Constants.WHATSAPP
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.iface.reference.StringReference

@Suppress("unused")
val loginFix = bytecodePatch(
    name = "Login Fix",
    description = "Bypasses verification bans by spoofing signatures and faking GMS checks. REQUIRED: You must manually install microG-RE for Play Integrity to pass.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // --- 1. Force GMS signature check to pass ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                
                var targetMoveResultIndex = -1
                var targetRegister = -1
                
                impl.instructions.forEachIndexed { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        if ((instr.reference as StringReference).string == " requires Google Play services, but their signature is invalid.") {
                            for (i in index downTo 0) {
                                val prevInstr = impl.instructions.elementAt(i)
                                if (prevInstr.opcode.name == "invoke-static") {
                                    val ref = (prevInstr as ReferenceInstruction).reference
                                    if (ref is MethodReference && ref.returnType == "Z") {
                                        val moveInstr = impl.instructions.elementAt(i + 1)
                                        if (moveInstr.opcode.name.startsWith("move-result") && moveInstr is OneRegisterInstruction) {
                                            targetMoveResultIndex = i + 1
                                            targetRegister = moveInstr.registerA
                                        }
                                        break
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (targetMoveResultIndex != -1 && targetRegister != -1) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    mutableMethod.addInstructions(targetMoveResultIndex + 1, """
                        const/4 v$targetRegister, 0x1
                    """)
                }
            }
        }
        
        // --- 2. Force local hash comparison to pass ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                
                impl.instructions.forEachIndexed { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        val str = (instr.reference as StringReference).string
                        if (str == "8P1sW0EPJcslw7UzRsiXL64w-O50Ed-RBICtay1g24M" ||
                            str == "-5INOBvuGyCT8n3I8T2ZTaYp3JGIfQUps1yaLcT0psI") {
                            for (i in index + 1 until minOf(index + 5, impl.instructions.count())) {
                                val next = impl.instructions.elementAt(i)
                                if (next.opcode.name == "move-result" && next is OneRegisterInstruction) {
                                    val reg = next.registerA
                                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                                    mutableMethod.addInstructions(i + 1, """
                                        const/4 v$reg, 0x1
                                    """)
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // --- 3. Redirect GMS service binding to MicroG-RE ---
        // WhatsApp calls Intent.setPackage("com.google.android.gms") to bind to GMS for attestation.
        // We redirect this to app.revanced.android.gms so MicroG-RE handles the request instead.
        // Only patches methods that also call Intent.setPackage (service binding), not class-level GMS refs.
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                
                val setPackageRef = "Landroid/content/Intent;->setPackage(Ljava/lang/String;)Landroid/content/Intent;"
                val hasSetPackage = impl.instructions.any { instr ->
                    instr is ReferenceInstruction && instr.reference.toString() == setPackageRef
                }
                if (!hasSetPackage) return@forEach
                
                val matches = impl.instructions.mapIndexedNotNull { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        val str = (instr.reference as StringReference).string
                        if (str == "com.google.android.gms" && instr is OneRegisterInstruction) {
                            // Only redirect if the next non-line instruction is setPackage
                            val nextMeaningful = (index + 1 until minOf(index + 5, impl.instructions.count()))
                                .map { impl.instructions.elementAt(it) }
                                .firstOrNull { it.opcode.name != "nop" }
                            val isSetPackage = nextMeaningful is ReferenceInstruction &&
                                nextMeaningful.reference.toString() == setPackageRef
                            if (isSetPackage) index to instr.registerA else null
                        } else null
                    } else null
                }
                
                if (matches.isNotEmpty()) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    matches.reversed().forEach { (idx, reg) ->
                        mutableMethod.addInstructions(idx + 1, """
                            const-string v$reg, "app.revanced.android.gms"
                        """)
                    }
                }
            }
        }
        
        // --- 4. Redirect com.android.vending to MicroG-RE companion ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                val matches = impl.instructions.mapIndexedNotNull { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        val str = (instr.reference as StringReference).string
                        if (str == "com.android.vending") index to "app.revanced.android.vending"
                        else null
                    } else null
                }
                
                if (matches.isNotEmpty()) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    matches.reversed().forEach { (idx, newPackage) ->
                        val instr = impl.instructions.elementAtOrNull(idx) as? OneRegisterInstruction ?: return@forEach
                        val reg = instr.registerA
                        mutableMethod.addInstructions(idx + 1, """
                            const-string v$reg, "$newPackage"
                        """)
                    }
                }
            }
        }

        // --- 5. Inject fake-signature for MicroG-RE ---
        // This injects the original WhatsApp signature into AndroidManifest.xml
        // so MicroG-RE can send it to Google instead of the modified APK signature.
        xmlDefForEach("AndroidManifest.xml") { def ->
            def.application?.let { app ->
                if (app.metaData.none { it.name == "fake-signature" }) {
                    app.addMetaData("fake-signature", "30820332308202f0a00302010202044c2536a4300b06072a8648ce3804030500307c310b3009060355040613025553311330110603550408130a43616c69666f726e6961311430120603550407130b53616e746120436c61726131163014060355040a130d576861747341707020496e632e31143012060355040b130b456e67696e656572696e67311430120603550403130b427269616e204163746f6e301e170d3130303632353233303731365a170d3434303231353233303731365a307c310b3009060355040613025553311330110603550408130a43616c69666f726e6961311430120603550407130b53616e746120436c61726131163014060355040a130d576861747341707020496e632e31143012060355040b130b456e67696e656572696e67311430120603550403130b427269616e204163746f6e308201b83082012c06072a8648ce3804013082011f02818100fd7f53811d75122952df4a9c2eece4e7f611b7523cef4400c31e3f80b6512669455d402251fb593d8d58fabfc5f5ba30f6cb9b556cd7813b801d346ff26660b76b9950a5a49f9fe8047b1022c24fbba9d7feb7c61bf83b57e7c6a8a6150f04fb83f6d3c51ec3023554135a169132f675f3ae2b61d72aeff22203199dd14801c70215009760508f15230bccb292b982a2eb840bf0581cf502818100f7e1a085d69b3ddecbbcab5c36b857b97994afbbfa3aea82f9574c0b3d0782675159578ebad4594fe67107108180b449167123e84c281613b7cf09328cc8a6e13c167a8b547c8d28e0a3ae1e2bb3a675916ea37f0bfa213562f1fb627a01243bcca4f1bea8519089a883dfe15ae59f06928b665e807b552564014c3bfecf492a0381850002818100d1198b4b81687bcf246d41a8a725f0a989a51bce326e84c828e1f556648bd71da487054d6de70fff4b49432b6862aa48fc2a93161b2c15a2ff5e671672dfb576e9d12aaff7369b9a99d04fb29d2bbbb2a503ee41b1ff37887064f41fe2805609063500a8e547349282d15981cdb58a08bede51dd7e9867295b3dfb45ffc6b259300b06072a8648ce3804030500032f00302c021400a602a7477acf841077237be090df436582ca2f0214350ce0268d07e71e55774ab4eacd4d071cd1efad")
                }
            }
        }
    }
}
