package android.util;

public final class HiLogString {
    public static final String privateString = "<private>";

    public static String format(boolean isFmtStrPrivate, String format, Object... args) {
        boolean showPrivacy = HiLog.isDebuggable();
        if (showPrivacy || !isFmtStrPrivate) {
            return new HiLogFormatter().format(showPrivacy, format, args).toString();
        }
        return privateString;
    }
}
