package app.pichiwa.extension.extension;

@SuppressWarnings("unused")
public final class WExtension {

    private WExtension() {}

    public static boolean canViewOnceBeViewed() {
        return true;
    }

    public static boolean shouldSendReadReceipt() {
        return false;
    }

    public static boolean isTypingAllowed() {
        return false;
    }

    public static boolean isRecordingAllowed() {
        return false;
    }

    public static int getForwardLimit() {
        return Integer.MAX_VALUE;
    }

    public static boolean useHighQualityMedia() {
        return true;
    }

    public static boolean canDownloadStatus() {
        return true;
    }
}
