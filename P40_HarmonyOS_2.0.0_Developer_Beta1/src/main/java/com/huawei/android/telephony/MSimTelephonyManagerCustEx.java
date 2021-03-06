package com.huawei.android.telephony;

import android.content.Context;
import android.provider.Settings;
import com.huawei.android.util.NoExtAPIException;

public class MSimTelephonyManagerCustEx {
    private static final int PREFERRED_NETWORK_MODE = 9;

    public static String getPesn(int subscription) {
        throw new NoExtAPIException("method not supported.");
    }

    public static int getUserDefaultSubscription(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "switch_dual_card_slots", 0);
    }

    public static int getNetworkmode(Context context, int subscription) {
        return Settings.Global.getInt(context.getContentResolver(), "preferred_network_mode", PREFERRED_NETWORK_MODE);
    }

    public static void setNetworkmode(Context context, int subScription, int networkMode) {
        Settings.Global.putInt(context.getContentResolver(), "preferred_network_mode", networkMode);
    }
}
