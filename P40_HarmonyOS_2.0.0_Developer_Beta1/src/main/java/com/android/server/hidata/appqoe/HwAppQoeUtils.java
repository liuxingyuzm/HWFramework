package com.android.server.hidata.appqoe;

import android.os.SystemProperties;
import android.util.wifi.HwHiLog;
import com.android.server.hidata.arbitration.HwArbitrationDefs;

public class HwAppQoeUtils {
    public static final int APP_ACTION_INTERNET_TEACH_MASK = 2;
    public static final int APP_ACTION_TRUE = 2;
    public static final int APP_DEFAULT_SCENCE_ID_DIVIDEND = 1000;
    public static final int APP_REGION_DEFAULT = 0;
    public static final int APP_REGION_OVERSEA = 1;
    public static final int APP_SCENCE_TYPE_AUDIO_VIDEO = 3;
    public static final int APP_SCENCE_TYPE_DEFAULT = 0;
    public static final int APP_SCENCE_TYPE_HUAWEI_APP = 4;
    public static final int APP_SCENCE_TYPE_IAWARE_APP = 5;
    public static final int APP_SCENCE_TYPE_IGNORE = 255;
    public static final int APP_SCENCE_TYPE_PAY = 1;
    public static final int APP_SCENCE_TYPE_TAXI = 2;
    public static final int APP_TYPE_APK = 1000;
    public static final int APP_TYPE_GAME = 2000;
    public static final int APP_TYPE_GENERAL_GAME = 3000;
    public static final int APP_TYPE_STREAMING = 4000;
    public static final String EMCOM_PARA_READY_ACTION = "huawei.intent.action.ACTION_EMCOM_PARA_READY";
    public static final String EMCOM_PARA_READY_REC = "EXTRA_EMCOM_PARA_READY_REC";
    public static final String EMCOM_PARA_UPGRADE_PERMISSION = "huawei.permission.RECEIVE_EMCOM_PARA_UPGRADE";
    public static final boolean GAME_ASSISIT_ENABLE = "1".equalsIgnoreCase(SystemProperties.get("ro.config.gameassist", ""));
    public static final int GAME_SCENCE_IN_WAR = 200002;
    public static final int GAME_SCENCE_NOT_IN_WAR = 200001;
    public static final int GAME_SPECIALINFO_SOURCES_DEFAULT = 0;
    public static final int GAME_SPECIALINFO_SOURCES_FI = 2;
    public static final int GAME_SPECIALINFO_SOURCES_SDK = 1;
    public static final String INVALID_STRING_VALUE = "None";
    public static final int INVALID_VALUE = -1;
    public static final boolean IS_TABLET = "tablet".equals(SystemProperties.get("ro.build.characteristics", ""));
    public static final int MASKBIT_PARA_FILE_TYPE_NONCELL = 4;
    public static final int MPLINK_ENTERED_FROM_CELL_MONITOR = 1;
    public static final String MPLINK_STATE_BROADCAST_PERMISSION = "com.huawei.hidata.permission.MPLINK_START_CHECK";
    public static final String MPLINK_STATE_CHANGE = "com.android.server.hidata.arbitration.HwArbitrationStateMachine";
    public static final String MPLINK_STATE_KEY_NETWORK_TYPE = "MPLinkSuccessNetworkKey";
    public static final String MPLINK_STATE_KEY_UID = "MPLinkSuccessUIDKey";
    public static final int MSG_APP_STATE_APP_REPORT_BAD = 112;
    public static final int MSG_APP_STATE_APP_STOP_MPLINK = 111;
    public static final int MSG_APP_STATE_BACKGROUND = 104;
    public static final int MSG_APP_STATE_BAD = 107;
    public static final int MSG_APP_STATE_END = 101;
    public static final int MSG_APP_STATE_FOREGROUND = 103;
    public static final int MSG_APP_STATE_GOOD = 106;
    public static final int MSG_APP_STATE_MONITOR = 105;
    public static final int MSG_APP_STATE_NO_RX = 110;
    public static final int MSG_APP_STATE_START = 100;
    public static final int MSG_APP_STATE_UNKNOW = 108;
    public static final int MSG_APP_STATE_UPDATE = 102;
    public static final int MSG_APP_STATE_WEAK = 109;
    public static final int MSG_CELL_STATE_CONNECTED = 7;
    public static final int MSG_CELL_STATE_DISCONNECT = 8;
    public static final int MSG_INTERNAL_GAME_CALL_BACK_RE_REG = 202;
    public static final int MSG_INTERNAL_MODULE_INIT = 200;
    public static final int MSG_INTERNAL_NETWORK_STATE_CHANGE = 201;
    public static final int MSG_NETWORK_STATE_CELL = 801;
    public static final int MSG_NETWORK_STATE_UKNOWN = 802;
    public static final int MSG_NETWORK_STATE_WIFI = 800;
    public static final int MSG_REGISTER_HICOM = 207;
    public static final int MSG_WIFI_STATE_CONNECTED = 3;
    public static final int MSG_WIFI_STATE_DISCONNECT = 4;
    public static final int PARA_FILE_TYPE_NONCELL = 16;
    public static final int PARA_PATH_TYPE_COTA = 1;
    public static final int SCENCE_HICALL = 101101;
    public static final int SCENE_AUDIO = 100105;
    public static final int SCENE_DOUYIN = 100501;
    public static final int SCENE_KUAISHOU = 100701;
    public static final int SCENE_TIKTOK = 100901;
    public static final int SCENE_VIDEO = 100106;
    public static final String TAG = (HwArbitrationDefs.BASE_TAG + HwAppQoeUtils.class.getSimpleName());

    public static void logE(String tag, boolean isFmtStrPrivate, String info, Object... args) {
        HwHiLog.e(tag, isFmtStrPrivate, info, args);
    }

    public static void logD(String tag, boolean isFmtStrPrivate, String info, Object... args) {
        HwHiLog.d(tag, isFmtStrPrivate, info, args);
    }
}
