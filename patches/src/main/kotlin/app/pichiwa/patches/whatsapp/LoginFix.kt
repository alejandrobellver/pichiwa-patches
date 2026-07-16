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
    description = "Bypasses verification bans by spoofing signatures, installers, and faking GMS checks. REQUIRED: You must manually install microG-RE for Play Integrity to pass.",
    default = true
) {
    compatibleWith(WHATSAPP)

    execute {
        // --- 1. Fake GMS Checks (Disable GMS logically) ---
        classDefForEach { def ->
            if (def.type == "Lcom/google/android/gms/common/GooglePlayServicesUtil;") {
                def.methods.forEach { method ->
                    if (method.name == "A00" && method.returnType == "I") {
                        val impl = method.implementation ?: return@forEach
                        val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                        mutableMethod.addInstructions(0, """
                            const/4 v0, 0x1
                            return v0
                        """)
                    }
                }
            }
        }

        // --- 2. Bypass Signature Verifier Locally ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                
                var targetMoveResultIndex = -1
                var targetRegister = -1
                
                impl.instructions.forEachIndexed { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        if ((instr.reference as StringReference).string == " requires Google Play services, but their signature is invalid.") {
                            // Find the invoke-static
                            for (i in index downTo 0) {
                                val prevInstr = impl.instructions.elementAt(i)
                                if (prevInstr.opcode.name == "invoke-static") {
                                    val ref = (prevInstr as ReferenceInstruction).reference
                                    if (ref is MethodReference && ref.returnType == "Z") {
                                        // The next instruction is move-result
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
        
        // --- 3. Spoof Signature Hashes ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                
                // A) Spoof SHA-1 Hex for X-Android-Cert header
                var certReg = -1
                impl.instructions.forEachIndexed { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        if ((instr.reference as StringReference).string == "X-Android-Cert") {
                            val nextInstr = impl.instructions.elementAtOrNull(index + 1)
                            if (nextInstr is OneRegisterInstruction && nextInstr.opcode.name.startsWith("const-string")) {
                                certReg = nextInstr.registerA
                            } else if (instr is OneRegisterInstruction) {
                                certReg = instr.registerA
                            }
                        }
                    }
                }
                
                if (certReg != -1) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    val matches = impl.instructions.mapIndexedNotNull { index, instr ->
                        val ref = (instr as? ReferenceInstruction)?.reference?.toString()
                        if (ref == "Ljava/net/URLConnection;->addRequestProperty(Ljava/lang/String;Ljava/lang/String;)V" && instr.opcode.name.startsWith("invoke-virtual")) {
                            val invokeInstr = instr
                            val vD = if (invokeInstr is com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction) invokeInstr.registerD else (invokeInstr as com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction).startRegister + 1
                            val vE = if (invokeInstr is com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction) invokeInstr.registerE else (invokeInstr as com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction).startRegister + 2
                            if (vD == certReg) index to vE else null
                        } else null
                    }
                    
                    matches.reversed().forEach { (idx, vE) ->
                        mutableMethod.addInstructions(idx, """
                            const-string v$vE, "38a0f7d505fe18fec64fbf343ecaaaf310dbd799"
                        """)
                    }
                }
                
                // B) Spoof SHA-256 Base64 for Local Telemetry Check
                // Replace ANY base64 encoding that resulted in our signature hash (old or new v3)
                // Note: The previous logic specifically looked for 8P1s... but on Android 13+ WhatsApp uses V3 signing 
                // which produces a different SHA-256: -5INOBvuGyCT8n3I8T2ZTaYp3JGIfQUps1yaLcT0psI
                // Let's replace all encodeToString results that match either of those, or simply 
                // hook where it creates the SHA-256 hash.
                
                var hasHash = false
                impl.instructions.forEach { instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        val s = (instr.reference as StringReference).string
                        if (s == "8P1sW0EPJcslw7UzRsiXL64w-O50Ed-RBICtay1g24M" || s == "-5INOBvuGyCT8n3I8T2ZTaYp3JGIfQUps1yaLcT0psI") {
                            hasHash = true
                        }
                    }
                }
                
                if (hasHash) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    val matches = impl.instructions.mapIndexedNotNull { index, instr ->
                        val ref = (instr as? ReferenceInstruction)?.reference?.toString()
                        if (ref == "Landroid/util/Base64;->encodeToString([BI)Ljava/lang/String;") index else null
                    }
                    
                    matches.reversed().forEach { idx ->
                        val moveResult = impl.instructions.elementAtOrNull(idx + 1) as? OneRegisterInstruction ?: return@forEach
                        val reg = moveResult.registerA
                        mutableMethod.addInstructions(idx + 2, """
                            const-string v$reg, "8P1sW0EPJcslw7UzRsiXL64w-O50Ed-RBICtay1g24M"
                        """)
                    }
                }
            }
        }

        // --- 4. Global MicroG-RE Package Redirection ---
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                val matches = impl.instructions.mapIndexedNotNull { index, instr ->
                    if (instr is ReferenceInstruction && instr.reference is StringReference) {
                        val str = (instr.reference as StringReference).string
                        if (str == "com.android.vending") index to "com.whatsapp.dummy"
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
        
        // --- 5. PackageInfo.signatures Spoofing ---
        // If Play Integrity is crashing or rejecting, we need to explicitly inject the signature into PackageInfo
        classDefForEach { def ->
            def.methods.forEach { method ->
                val impl = method.implementation ?: return@forEach
                
                val matches = impl.instructions.mapIndexedNotNull { index, instr ->
                    val ref = (instr as? ReferenceInstruction)?.reference?.toString()
                    if (ref == "Landroid/content/pm/PackageManager;->getPackageInfo(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;" && instr.opcode.name.startsWith("invoke-virtual")) {
                        index
                    } else null
                }
                
                if (matches.isNotEmpty()) {
                    val mutableMethod = mutableClassDefBy(def).methods.first { it.name == method.name && it.parameters == method.parameters && it.returnType == method.returnType }
                    matches.reversed().forEach { invokeIdx ->
                        val invokeInstr = impl.instructions.elementAt(invokeIdx)
                        val vC = if (invokeInstr is com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction) invokeInstr.registerC else (invokeInstr as com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction).startRegister
                        val vD = if (invokeInstr is com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction) invokeInstr.registerD else vC + 1
                        val vE = if (invokeInstr is com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction) invokeInstr.registerE else vC + 2
                        
                        val moveResultInstr = impl.instructions.elementAtOrNull(invokeIdx + 1) as? OneRegisterInstruction ?: return@forEach
                        val vResult = moveResultInstr.registerA
                        
                        // We need a stable base64 encoded certificate to inject
                        // Using the v3 cert from Android 13+
                        val certBase64 = "MII2Q+dUL+/qdhwaJ3zt2TaP6n4rEd2qqm7bZDxrJu9gBEx71h+ZPKcg5Ca/cwwbxr16NM1TfCfW1RoQek4mMCHze2msCAOpQ9eGo9Z4dG00RzM92P30K1uL12d5zL85P86Vj5qV7sUaJ2K8eNf3yY+eZ4b1tL2yF8rA3xP6mH1rS3wA5zR8Q+fE2pK1nG9tM7uK4qH5xL2qF9wA6yP3oO7tI9xM4qR0bL8zC6wH4xN2rY3+L3uE5mJ7yU2pDALBgcqhkjOOAQDBQAwfDELMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExFDASBgNVBAcTC1NhbnRhIENsYXJhMRYwFAYDVQQKEw1XaGF0c0FwcCBJbmMuMRQwEgYDVQQLEwtFbmdpbmVlcmluZzEUMBIGA1UEAxMLQnJpYW4gQWN0b24wHhcNMTAwNjI1MjMwNzE2WhcNNDQwMjE1MjMwNzE2WjB8MQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEUMBIGA1UEBxMLU2FudGEgQ2xhcmExFjAUBgNVBAoTDVdoYXRzQXBwIEluYy4xFDASBgNVBAsTC0VuZ2luZWVyaW5nMRQwEgYDVQQDEwtCcmlhbiBBY3RvbjCCAbgwggEsBgcqhkjOOAQBMIIBHwKBgQD9f1OBHXUSKVLfSpwu7OTn9hG3UjzvRADDHj+AtlEmaUVdQCJR+1k9jVj6v8X1ujD2y5tVbNeBO4AdNG/yZmC3a5lQpaSfn+gEexAiwk+7qdf+t8Yb+DtX58aophUPBPuD9tPFHsMCNVQTWhaRMvZ1864rYdcq7/IiAxmd0UgBxwIVAJdgUI8VIwvMspK5gqLrhAvwWBz1AoGBAPfhoIXWmz3ey7yrXDa4V7l5lK+7+jrqgvlXTAs9B4JnUVlXjrrUWU/mcQcQgYC0SRZxI+hMKBYTt88JMozIpuE8FnqLVHyNKOCjrh4rs6Z1kW6jfwv6ITVi8ftiegEkO8yk8b6oUZCJqIPf4VrlnwaSi2ZegHtVJWQBTDv+z0kqA4GFAAKBgQDRGYtLgWh7zyRtQainJfCpiaUbzjJuhMgo4fVWZIvXHaSHBU1t5w//S0lDK2hiqkj8KpMWGywVov9eZxZy37V26dEqr/c2m5qZ0E+ynSu7sqUD7kGx/zeIcGT0H+KAVgkGNQCo5Uc0koLRWYHNtYoIvt5R3X6YZylbPftF/8ayWTALBgcqhkjOOAQDBQADLwAwLAIUAKYCp0d6z4QQdyN74JDfQ2WCyi8CFDUM4CaNB+ceVXdKtOrNTQcc0e+t"
                        mutableMethod.addInstructions(invokeIdx + 2, """
                            if-nez v$vResult, :skip_spoof_$invokeIdx
                            iget-object v$vD, v$vResult, Landroid/content/pm/PackageInfo;->signatures:[Landroid/content/pm/Signature;
                            if-nez v$vD, :skip_spoof_$invokeIdx
                            invoke-static {v$vD}, Ljava/lang/reflect/Array;->getLength(Ljava/lang/Object;)I
                            move-result v$vE
                            if-nez v$vE, :skip_spoof_$invokeIdx
                            
                            const-string v$vD, "MIIDMjCCAvCgAwIBAgIETCU2pDALBgcqhkjOOAQDBQAwfDELMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExFDASBgNVBAcTC1NhbnRhIENsYXJhMRYwFAYDVQQKEw1XaGF0c0FwcCBJbmMuMRQwEgYDVQQLEwtFbmdpbmVlcmluZzEUMBIGA1UEAxMLQnJpYW4gQWN0b24wHhcNMTAwNjI1MjMwNzE2WhcNNDQwMjE1MjMwNzE2WjB8MQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEUMBIGA1UEBxMLU2FudGEgQ2xhcmExFjAUBgNVBAoTDVdoYXRzQXBwIEluYy4xFDASBgNVBAsTC0VuZ2luZWVyaW5nMRQwEgYDVQQDEwtCcmlhbiBBY3RvbjCCAbgwggEsBgcqhkjOOAQBMIIBHwKBgQD9f1OBHXUSKVLfSpwu7OTn9hG3UjzvRADDHj+AtlEmaUVdQCJR+1k9jVj6v8X1ujD2y5tVbNeBO4AdNG/yZmC3a5lQpaSfn+gEexAiwk+7qdf+t8Yb+DtX58aophUPBPuD9tPFHsMCNVQTWhaRMvZ1864rYdcq7/IiAxmd0UgBxwIVAJdgUI8VIwvMspK5gqLrhAvwWBz1AoGBAPfhoIXWmz3ey7yrXDa4V7l5lK+7+jrqgvlXTAs9B4JnUVlXjrrUWU/mcQcQgYC0SRZxI+hMKBYTt88JMozIpuE8FnqLVHyNKOCjrh4rs6Z1kW6jfwv6ITVi8ftiegEkO8yk8b6oUZCJqIPf4VrlnwaSi2ZegHtVJWQBTDv+z0kqA4GFAAKBgQDRGYtLgWh7zyRtQainJfCpiaUbzjJuhMgo4fVWZIvXHaSHBU1t5w//S0lDK2hiqkj8KpMWGywVov9eZxZy37V26dEqr/c2m5qZ0E+ynSu7sqUD7kGx/zeIcGT0H+KAVgkGNQCo5Uc0koLRWYHNtYoIvt5R3X6YZylbPftF/8ayWTALBgcqhkjOOAQDBQADLwAwLAIUAKYCp0d6z4QQdyN74JDfQ2WCyi8CFDUM4CaNB+ceVXdKtOrNTQcc0e+t"
                            const/4 v$vE, 0x0
                            invoke-static {v$vD, v$vE}, Landroid/util/Base64;->decode(Ljava/lang/String;I)[B
                            move-result-object v$vD
                            
                            new-instance v$vC, Landroid/content/pm/Signature;
                            invoke-direct {v$vC, v$vD}, Landroid/content/pm/Signature;-><init>([B)V
                            
                            iget-object v$vD, v$vResult, Landroid/content/pm/PackageInfo;->signatures:[Landroid/content/pm/Signature;
                            const/4 v$vE, 0x0
                            aput-object v$vC, v$vD, v$vE
                            
                            :skip_spoof_$invokeIdx
                        """)
                    }
                }
            }
        }
    }
}

