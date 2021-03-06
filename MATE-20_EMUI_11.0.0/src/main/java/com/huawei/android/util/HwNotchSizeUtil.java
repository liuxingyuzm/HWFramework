package com.huawei.android.util;

import android.app.ActivityThread;
import android.app.Application;
import android.aps.IApsManager;
import android.common.HwFrameworkFactory;
import android.graphics.Point;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.view.IWindowManager;
import android.view.WindowManagerGlobal;
import com.huawei.android.smcs.SmartTrimProcessEvent;

public class HwNotchSizeUtil {
    private static final String HW_NOTCH_SIZE = SystemProperties.get("ro.config.hw_notch_size", "");
    private static int[] NOTCH_PARAMS;
    private static String TAG = "HwNotchSizeUtil";
    private static int sDefaultWidth = 0;

    static {
        try {
            if (hasNotchInScreen()) {
                String[] params = HW_NOTCH_SIZE.split(SmartTrimProcessEvent.ST_EVENT_STRING_TOKEN);
                int length = params.length;
                if (length < 4) {
                    Log.e(TAG, "hw_notch_size conifg error");
                    NOTCH_PARAMS = null;
                    return;
                }
                NOTCH_PARAMS = new int[length];
                for (int i = 0; i < length; i++) {
                    NOTCH_PARAMS[i] = Integer.parseInt(params[i]);
                }
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "hw_notch_size conifg NumberFormatException");
            NOTCH_PARAMS = null;
        } catch (Exception e2) {
            Log.e(TAG, "hw_notch_size conifg Exception");
            NOTCH_PARAMS = null;
        }
    }

    public static boolean hasNotchInScreen() {
        return !TextUtils.isEmpty(HW_NOTCH_SIZE);
    }

    public static int[] getNotchSize() {
        int[] iArr = NOTCH_PARAMS;
        return iArr == null ? new int[]{0, 0} : new int[]{calculateSize(iArr[0]), calculateSize(NOTCH_PARAMS[1])};
    }

    public static int getNotchOffset() {
        int[] iArr = NOTCH_PARAMS;
        if (iArr == null) {
            return 0;
        }
        return calculateSize(iArr[2]);
    }

    public static int getNotchCorner() {
        int[] iArr = NOTCH_PARAMS;
        if (iArr == null) {
            return 0;
        }
        return calculateSize(iArr[3]);
    }

    private static int calculateSize(int size) {
        int defaultWidth = getDefaultWidth();
        int rogWidth = SystemProperties.getInt("persist.sys.rog.width", 0);
        int rogHeight = SystemProperties.getInt("persist.sys.rog.height", 0);
        if (defaultWidth == 0 || rogWidth == 0 || rogHeight == 0) {
            int resolutionSize = getResolutionSize(size);
            String str = TAG;
            Log.w(str, "calculateSize error mRogWidth = " + rogWidth + "mRogWHeigth = " + rogHeight + ", mDefaultWidth = " + defaultWidth + ",size = " + size + ",resolutionSize =" + resolutionSize);
            return resolutionSize;
        }
        int min = rogWidth < rogHeight ? rogWidth : rogHeight;
        if (defaultWidth != min) {
            size = (int) (((((float) (size * min)) * 1.0f) / ((float) defaultWidth)) + 0.5f);
        }
        int resolutionSize2 = getResolutionSize(size);
        String str2 = TAG;
        Log.d(str2, "mRogWidth = " + rogWidth + "mRogWHeigth = " + rogHeight + ", mDefaultWidth = " + defaultWidth + ",size = " + size + ",resolutionSize =" + resolutionSize2);
        return resolutionSize2;
    }

    private static int getResolutionSize(int size) {
        Application application = ActivityThread.currentApplication();
        if (application == null) {
            return size;
        }
        String packagename = application.getPackageName();
        float resolutionRatio = -1.0f;
        try {
            IApsManager Apsmanager = HwFrameworkFactory.getApsManager();
            if (Apsmanager != null) {
                resolutionRatio = Apsmanager.getResolution(packagename);
            }
            if (0.0f >= resolutionRatio || resolutionRatio >= 1.0f) {
                return size;
            }
            int size2 = (int) ((((float) size) * resolutionRatio) + 0.5f);
            String str = TAG;
            Log.d(str, "getResolutionSize resolutionRatio " + resolutionRatio);
            return size2;
        } catch (Exception e) {
            Log.e(TAG, "getResolutionSize catch Exception");
            return size;
        }
    }

    private static synchronized int getDefaultWidth() {
        int i;
        IWindowManager iwm;
        synchronized (HwNotchSizeUtil.class) {
            if (sDefaultWidth == 0 && (iwm = WindowManagerGlobal.getWindowManagerService()) != null) {
                Point point = new Point();
                try {
                    iwm.getInitialDisplaySize(0, point);
                    sDefaultWidth = point.x < point.y ? point.x : point.y;
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException while calculate device size", e);
                    sDefaultWidth = 0;
                }
            }
            i = sDefaultWidth;
        }
        return i;
    }
}
