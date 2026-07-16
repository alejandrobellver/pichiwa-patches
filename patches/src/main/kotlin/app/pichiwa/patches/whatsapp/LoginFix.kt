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
                        val invokeInstr = impl.instructions.elementAtOrNull(invokeIdx) ?: return@forEach
                        var vC = 0
                        var vD = 1
                        var vE = 2
                        if (invokeInstr is com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c) {
                            vC = invokeInstr.registerC
                            vD = invokeInstr.registerD
                            vE = invokeInstr.registerE
                        } else if (invokeInstr is com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rc) {
                            vC = invokeInstr.startRegister
                            vD = invokeInstr.startRegister + 1
                            vE = invokeInstr.startRegister + 2
                        } else {
                            return@forEach
                        }
                        
                        val moveResultInstr = impl.instructions.elementAtOrNull(invokeIdx + 1) as? OneRegisterInstruction ?: return@forEach
                        val vResult = moveResultInstr.registerA
                        
                        // Prevent register collision with vResult
                        if (vResult == vC || vResult == vD || vResult == vE) {
                            return@forEach
                        }
                        
                        mutableMethod.addInstructions(invokeIdx + 2, """
                            if-eqz v$vResult, :cond_skip_spoof_${"$"}{invokeIdx}
                            
                            const-string v$vC, "MIIDMjCCAvCgAwIBAgIETCU2pDALBgcqhkjOOAQDBQAwfDELMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExFDASBgNVBAcTC1NhbnRhIENsYXJhMRYwFAYDVQQKEw1XaGF0c0FwcCBJbmMuMRQwEgYDVQQLEwtFbmdpbmVlcmluZzEUMBIGA1UEAxMLQnJpYW4gQWN0b24wHhcNMTAwNjI1MjMwNzE2WhcNNDQwMjE1MjMwNzE2WjB8MQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEUMBIGA1UEBxMLU2FudGEgQ2xhcmExFjAUBgNVBAoTDVdoYXRzQXBwIEluYy4xFDASBgNVBAsTC0VuZ2luZWVyaW5nMRQwEgYDVQQDEwtCcmlhbiBBY3RvbjCCAbgwggEsBgcqhkjOOAQBMIIBHwKBgQD9f1OBHXUSKVLfSpwu7OTn9hG3UjzvRADDHj+AtlEmaUVdQCJR+1k9jVj6v8X1ujD2y5tVbNeBO4AdNG/yZmC3a5lQpaSfn+gEexAiwk+7qdf+t8Yb+DtX58aophUPBPuD9tPFHsMCNVQTWhaRMvZ1864rYdcq7/IiAxmd0UgBxwIVAJdgUI8VIwvMspK5gqLrhAvwWBz1AoGBAPfhoIXWmz3ey7yrXDa4V7l5lK+7+jrqgvlXTAs9B4JnUVlXjrrUWU/mcQcQgYC0SRZxI+hMKBYTt88JMozIpuE8FnqLVHyNKOCjrh4rs6Z1kW6jfwv6ITVi8ftiegEkO8yk8b6oUZCJqIPf4VrlnwaSi2ZegHtVJWQBTDv+z0kqA4GFAAKBgQDRGYtLgWh7zyRtQainJfCpiaUbzjJuhMgo4fVWZIvXHaSHBU1t5w//S0lDK2hiqkj8KpMWGywVov9eZxZy37V26dEqr/c2m5qZ0E+ynSu7sqUD7kGx/zeIcGT0H+KAVgkGNQCo5Uc0koLRWYHNtYoIvt5R3X6YZylbPftF/8ayWTALBgcqhkjOOAQDBQADLwAwLAIUAKYCp0d6z4QQdyN74JDfQ2WCyi8CFDUM4CaNB+ceVXdKtOrNTQcc0e+t"
                            
                            const/4 v$vD, 0x0
                            invoke-static {v$vC, v$vD}, Landroid/util/Base64;->decode(Ljava/lang/String;I)[B
                            move-result-object v$vC
                            
                            new-instance v$vD, Landroid/content/pm/Signature;
                            invoke-direct {v$vD, v$vC}, Landroid/content/pm/Signature;-><init>([B)V
                            
                            iget-object v$vC, v$vResult, Landroid/content/pm/PackageInfo;->signatures:[Landroid/content/pm/Signature;
                            if-eqz v$vC, :cond_skip_spoof_${"$"}{invokeIdx}
                            
                            array-length v$vE, v$vC
                            if-lez v$vE, :cond_skip_spoof_${"$"}{invokeIdx}
                            
                            const/4 v$vE, 0x0
                            aput-object v$vD, v$vC, v$vE
                            
                            :cond_skip_spoof_${"$"}{invokeIdx}
                        """.trimIndent())
                    }
                }
            }
        }
    }
}