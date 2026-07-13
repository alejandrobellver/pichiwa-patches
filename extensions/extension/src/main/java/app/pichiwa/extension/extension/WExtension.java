package app.pichiwa.extension.extension;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

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

    public static boolean canViewOnceBeViewed() {
        SharedPreferences p = getPrefs();
        return p == null || p.getBoolean("anti_view_once", true);
    }

    public static boolean shouldSendReadReceipt() {
        SharedPreferences p = getPrefs();
        // ponytail: hidelivered pref piggybacks on this same patch injection
        if (p == null) return true;
        boolean hideRead = p.getBoolean("hide_read_receipts", true);
        boolean hideDeliv = p.getBoolean("hide_delivered", false);
        return !hideRead && !hideDeliv;
    }

    public static boolean shouldSendDeliveryReceipt() {
        SharedPreferences p = getPrefs();
        if (p == null) return true;
        return !p.getBoolean("hide_delivered", false);
    }

    public static boolean isTypingAllowed() {
        SharedPreferences p = getPrefs();
        return p == null || !p.getBoolean("hide_typing", true);
    }

    public static boolean isRecordingAllowed() {
        SharedPreferences p = getPrefs();
        return p == null || !p.getBoolean("hide_recording", true);
    }

    public static int getForwardLimit() {
        SharedPreferences p = getPrefs();
        if (p != null && p.getBoolean("remove_forward_limit", true)) {
            return Integer.MAX_VALUE;
        }
        return -1;
    }

    public static boolean useHighQualityMedia() {
        SharedPreferences p = getPrefs();
        return p == null || p.getBoolean("hd_media", true);
    }

    public static boolean canDownloadStatus() {
        SharedPreferences p = getPrefs();
        return p == null || p.getBoolean("download_status", true);
    }

    public static boolean canForwardTagBeHidden() {
        SharedPreferences p = getPrefs();
        return p != null && p.getBoolean("hide_forwarded_tag", false);
    }

    public static boolean shouldAllowRevoke() {
        SharedPreferences p = getPrefs();
        return p == null || !p.getBoolean("anti_revoke", true);
    }

    public static boolean shouldPreventDisappearing() {
        SharedPreferences p = getPrefs();
        return p != null && p.getBoolean("anti_disappearing", false);
    }

    public static boolean shouldFreezeLastSeen() {
        SharedPreferences p = getPrefs();
        return p != null && p.getBoolean("freeze_last_seen", false);
    }

    public static boolean shouldEnableStatusCopy() {
        SharedPreferences p = getPrefs();
        return p != null && p.getBoolean("enable_copy_status", false);
    }

    public static boolean shouldEnableDndMode() {
        SharedPreferences p = getPrefs();
        return p != null && p.getBoolean("dnd_mode", false);
    }

    public static boolean shouldAllowEdit() {
        SharedPreferences p = getPrefs();
        return p == null || !p.getBoolean("anti_edit", true);
    }

    public static java.util.Date getFutureDate() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(2099, 11, 31);
        return cal.getTime();
    }
}
