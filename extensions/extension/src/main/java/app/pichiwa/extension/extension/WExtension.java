package app.pichiwa.extension.extension;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
public final class WExtension {

    private static Context appContext;
    private static SharedPreferences prefs;

    private WExtension() {}

    private static SharedPreferences getPrefs() {
        if (prefs == null) {
            try {
                if (appContext == null) {
                    Class<?> activityThread = Class.forName("android.app.ActivityThread");
                    Method method = activityThread.getMethod("currentApplication");
                    appContext = (Application) method.invoke(null);
                }
                prefs = appContext.getSharedPreferences("pichiwa_prefs", Context.MODE_PRIVATE);
            } catch (Exception e) {
                return null;
            }
        }
        return prefs;
    }

    public static String getCert() {
        if (Build.VERSION.SDK_INT >= 33) {
            return "8b0debf9516af037c9be2f539584b97fe9781764";
        }
        return "38a0f7d505fe18fec64fbf343ecaaaf310dbd799";
    }

    public static String getHash() {
        if (Build.VERSION.SDK_INT >= 33) {
            // Wait, what is the SHA-256 base64 for API 33+?
            // apksigner printed: 
            // SHA-256 digest: fb920d381bee1b2093f27dc8f13d994da629dc91887d0529b35c9a2dc4f4a6c2
            // We need to convert this to Base64 (URL-safe, no padding)
            return "NO_IDEA_YET";
        }
        return "8P1sW0EPJcslw7UzRsiXL64w-O50Ed-RBICtay1g24M";
    }
}

