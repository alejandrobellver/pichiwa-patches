package app.pichiwa.patches.shared

object SmaliHelper {
    fun getPrefBoolean(prefName: String, defaultValue: Boolean, invertResult: Boolean = false, returnReg: String = "v0"): String {
        val defaultVal = if (defaultValue) "0x1" else "0x0"
        return """
            invoke-static {}, LX/00I;->A00()Landroid/app/Application;
            move-result-object v30
            if-nez v30, :cond_default
            
            const-string v31, "pichiwa_prefs"
            const/4 v32, 0x0
            invoke-virtual/range {v30 .. v32}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;
            move-result-object v30
            if-nez v30, :cond_read
            
            :cond_default
            const/4 $returnReg, $defaultVal
            goto :cond_invert
            
            :cond_read
            const-string v31, "$prefName"
            const/4 v32, $defaultVal
            invoke-interface/range {v30 .. v32}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z
            move-result $returnReg
            
            :cond_invert
            ${if (invertResult) "xor-int/lit8 $returnReg, $returnReg, 0x1" else ""}
        """.trimIndent()
    }

    fun getPrefHideReadReceipts(returnReg: String = "v0"): String {
        return """
            invoke-static {}, LX/00I;->A00()Landroid/app/Application;
            move-result-object v30
            if-nez v30, :cond_default
            
            const-string v31, "pichiwa_prefs"
            const/4 v32, 0x0
            invoke-virtual/range {v30 .. v32}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;
            move-result-object v30
            if-nez v30, :cond_read
            
            :cond_default
            const/4 $returnReg, 0x1
            goto :cond_end
            
            :cond_read
            const-string v31, "hide_read_receipts"
            const/4 v32, 0x1
            invoke-interface/range {v30 .. v32}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z
            move-result v33
            
            const-string v31, "hide_delivered"
            const/4 v32, 0x0
            invoke-interface/range {v30 .. v32}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z
            move-result v30
            
            if-eqz v33, :cond_check2
            const/4 $returnReg, 0x0
            goto :cond_end
            
            :cond_check2
            if-eqz v30, :cond_ret_true
            const/4 $returnReg, 0x0
            goto :cond_end
            
            :cond_ret_true
            const/4 $returnReg, 0x1
            
            :cond_end
        """.trimIndent()
    }

    fun getPrefIntMax(prefName: String, defaultValue: Boolean, returnReg: String = "v0"): String {
        val defaultVal = if (defaultValue) "0x1" else "0x0"
        return """
            invoke-static {}, LX/00I;->A00()Landroid/app/Application;
            move-result-object v30
            if-nez v30, :cond_default
            
            const-string v31, "pichiwa_prefs"
            const/4 v32, 0x0
            invoke-virtual/range {v30 .. v32}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;
            move-result-object v30
            if-nez v30, :cond_read
            
            :cond_default
            const/4 $returnReg, $defaultVal
            goto :cond_check
            
            :cond_read
            const-string v31, "$prefName"
            const/4 v32, $defaultVal
            invoke-interface/range {v30 .. v32}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z
            move-result $returnReg
            
            :cond_check
            if-eqz $returnReg, :cond_max
            const/4 $returnReg, -0x1
            goto :cond_end
            :cond_max
            const $returnReg, 0x7fffffff
            :cond_end
        """.trimIndent()
    }
}
