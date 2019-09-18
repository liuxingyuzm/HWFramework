package com.android.internal.telephony.cdma;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.telephony.AbstractRILConstants;
import javax.microedition.khronos.opengles.GL10;

public class HwCustTelephonyPlusCode {
    protected static final HwCustMccIddNddSid[] MccIddNddSidMap_support;
    protected static final HwCustMccSidLtmOff[] MccSidLtmOffMap_support = {new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 1, -20, -10), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.RUNNING_SERVICES, 1, 11, 11), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 7, -20, -10), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.RUNNING_SERVICES, 7, 11, 11), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 13, -20, -10), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_HIDE_APP_DISAMBIG_NONE_FEATURED, 13, 16, 16), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 1111, -20, -10), new HwCustMccSidLtmOff(450, 1111, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 1112, -20, -10), new HwCustMccSidLtmOff(450, 1112, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 1113, -20, -10), new HwCustMccSidLtmOff(450, 1113, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 1700, -20, -10), new HwCustMccSidLtmOff(450, 1700, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, AbstractRILConstants.RIL_REQUEST_HW_SEND_NCFG_OPER_INFO, -20, -10), new HwCustMccSidLtmOff(450, AbstractRILConstants.RIL_REQUEST_HW_SEND_NCFG_OPER_INFO, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, AbstractRILConstants.RIL_REQUEST_HW_GET_SIM_MATCHED_FILE, -20, -10), new HwCustMccSidLtmOff(450, AbstractRILConstants.RIL_REQUEST_HW_GET_SIM_MATCHED_FILE, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, AbstractRILConstants.RIL_REQUEST_HW_GET_CARD_TRAY_INFO, -20, -10), new HwCustMccSidLtmOff(450, AbstractRILConstants.RIL_REQUEST_HW_GET_CARD_TRAY_INFO, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, AbstractRILConstants.RIL_REQUEST_SET_CSCON_ENABLED, -20, -10), new HwCustMccSidLtmOff(450, AbstractRILConstants.RIL_REQUEST_SET_CSCON_ENABLED, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, AbstractRILConstants.RIL_REQUEST_GET_LTE_ATTACH_INFO, -20, -10), new HwCustMccSidLtmOff(450, AbstractRILConstants.RIL_REQUEST_GET_LTE_ATTACH_INFO, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2187, -20, -10), new HwCustMccSidLtmOff(450, 2187, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2189, -20, -10), new HwCustMccSidLtmOff(450, 2189, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2191, -20, -10), new HwCustMccSidLtmOff(450, 2191, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2193, -20, -10), new HwCustMccSidLtmOff(450, 2193, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2195, -20, -10), new HwCustMccSidLtmOff(450, 2195, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2197, -20, -10), new HwCustMccSidLtmOff(450, 2197, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2199, -20, -10), new HwCustMccSidLtmOff(450, 2199, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2201, -20, -10), new HwCustMccSidLtmOff(450, 2201, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2203, -20, -10), new HwCustMccSidLtmOff(450, 2203, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2205, -20, -10), new HwCustMccSidLtmOff(450, 2205, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2207, -20, -10), new HwCustMccSidLtmOff(450, 2207, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2209, -20, -10), new HwCustMccSidLtmOff(450, 2209, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2211, -20, -10), new HwCustMccSidLtmOff(450, 2211, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2213, -20, -10), new HwCustMccSidLtmOff(450, 2213, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2215, -20, -10), new HwCustMccSidLtmOff(450, 2215, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2217, -20, -10), new HwCustMccSidLtmOff(450, 2217, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2219, -20, -10), new HwCustMccSidLtmOff(450, 2219, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2221, -20, -10), new HwCustMccSidLtmOff(450, 2221, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2223, -20, -10), new HwCustMccSidLtmOff(450, 2223, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2225, -20, -10), new HwCustMccSidLtmOff(450, 2225, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2227, -20, -10), new HwCustMccSidLtmOff(450, 2227, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2229, -20, -10), new HwCustMccSidLtmOff(450, 2229, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2231, -20, -10), new HwCustMccSidLtmOff(450, 2231, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2233, -20, -10), new HwCustMccSidLtmOff(450, 2233, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2235, -20, -10), new HwCustMccSidLtmOff(450, 2235, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2237, -20, -10), new HwCustMccSidLtmOff(450, 2237, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2239, -20, -10), new HwCustMccSidLtmOff(450, 2239, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2241, -20, -10), new HwCustMccSidLtmOff(450, 2241, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2243, -20, -10), new HwCustMccSidLtmOff(450, 2243, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2301, -20, -10), new HwCustMccSidLtmOff(450, 2301, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2303, -20, -10), new HwCustMccSidLtmOff(450, 2303, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2369, -20, -10), new HwCustMccSidLtmOff(450, 2369, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2370, -20, -10), new HwCustMccSidLtmOff(450, 2370, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, 2371, -20, -10), new HwCustMccSidLtmOff(450, 2371, 18, 18), new HwCustMccSidLtmOff(450, 2222, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.RUNNING_SERVICES, 2222, 11, 11), new HwCustMccSidLtmOff(440, 12461, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_DELETION_HELPER_REMOVE_CANCEL, 12461, 12, 12), new HwCustMccSidLtmOff(440, 12463, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_DELETION_HELPER_REMOVE_CANCEL, 12463, 12, 12), new HwCustMccSidLtmOff(440, 12464, 18, 18), new HwCustMccSidLtmOff(MetricsProto.MetricsEvent.ACTION_DELETION_HELPER_REMOVE_CANCEL, 12464, 12, 12)};

    static {
        HwCustMccIddNddSid hwCustMccIddNddSid = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.ACTION_BUGREPORT_DETAILS_DESCRIPTION_CHANGED, "1", 16384, 18431, "011", "1");
        HwCustMccIddNddSid hwCustMccIddNddSid2 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_AUTO, "1", 1, AbstractRILConstants.RIL_REQUEST_HW_SET_DEEP_NO_DISTURB_SWITCH, "011", "1");
        HwCustMccIddNddSid hwCustMccIddNddSid3 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_ADJUST_DARK_THEME, "1", GL10.GL_CW, 7679, "011", "1");
        HwCustMccIddNddSid hwCustMccIddNddSid4 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_ADJUST_TINT, "1", 0, 0, "011", "1");
        HwCustMccIddNddSid hwCustMccIddNddSid5 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.ACTION_TUNER_NIGHT_MODE_ADJUST_BRIGHTNESS, "1", 0, 0, "011", "1");
        HwCustMccIddNddSid hwCustMccIddNddSid6 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.ACTION_TUNER_DO_NOT_DISTURB_VOLUME_PANEL, "1", 0, 0, "011", "1");
        HwCustMccIddNddSid hwCustMccIddNddSid7 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.ACTION_TUNER_DO_NOT_DISTURB_VOLUME_SHORTCUT, "1", 0, 0, "011", "1");
        HwCustMccIddNddSid hwCustMccIddNddSid8 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.ACTION_APP_CRASH, "1", 0, 0, "011", "1");
        HwCustMccIddNddSid hwCustMccIddNddSid9 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.VR_MANAGE_LISTENERS, "52", 24576, 25075, "00", "01");
        HwCustMccIddNddSid hwCustMccIddNddSid10 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.VR_MANAGE_LISTENERS, "52", 25100, 25124, "00", "01");
        HwCustMccIddNddSid hwCustMccIddNddSid11 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.RUNNING_SERVICES, "91", 14464, 14847, "00", "0");
        HwCustMccIddNddSid hwCustMccIddNddSid12 = new HwCustMccIddNddSid(425, "972", GL10.GL_MODULATE, 8479, "00", "0");
        HwCustMccIddNddSid hwCustMccIddNddSid13 = new HwCustMccIddNddSid(428, "976", 15520, 15551, "002", "0");
        HwCustMccIddNddSid hwCustMccIddNddSid14 = new HwCustMccIddNddSid(440, "81", 12288, 13311, "010", "0");
        HwCustMccIddNddSid hwCustMccIddNddSid15 = new HwCustMccIddNddSid(450, "82", AbstractRILConstants.RIL_REQUEST_HW_GET_CURRENT_CALLS_EXT, 2303, "00700", "0");
        HwCustMccIddNddSid hwCustMccIddNddSid16 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.ACTION_HIDE_APP_DISAMBIG_APP_FEATURED, "84", 13312, 13439, "00", "0");
        HwCustMccIddNddSid hwCustMccIddNddSid17 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.ACTION_HIDE_APP_DISAMBIG_NONE_FEATURED, "852", 10640, 10655, "001", "");
        HwCustMccIddNddSid hwCustMccIddNddSid18 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.ACTION_APP_DISAMBIG_ALWAYS, "853", 11296, 11311, "00", "0");
        HwCustMccIddNddSid hwCustMccIddNddSid19 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.ACTION_DELETION_SELECTION_PHOTOS, "86", 13568, 14335, "00", "0");
        HwCustMccIddNddSid hwCustMccIddNddSid20 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.ACTION_DELETION_SELECTION_PHOTOS, "86", 25600, 26111, "00", "0");
        HwCustMccIddNddSid hwCustMccIddNddSid21 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.ACTION_DELETION_DOWNLOADS_COLLAPSED, "886", 13504, 13535, "005", "");
        HwCustMccIddNddSid hwCustMccIddNddSid22 = new HwCustMccIddNddSid(MetricsProto.MetricsEvent.ACTION_DELETION_HELPER_REMOVE_CANCEL, "880", 13472, 13503, "00", "0");
        HwCustMccIddNddSid hwCustMccIddNddSid23 = new HwCustMccIddNddSid(510, "62", 10496, 10623, "01033", "0");
        MccIddNddSidMap_support = new HwCustMccIddNddSid[]{hwCustMccIddNddSid, hwCustMccIddNddSid2, hwCustMccIddNddSid3, hwCustMccIddNddSid4, hwCustMccIddNddSid5, hwCustMccIddNddSid6, hwCustMccIddNddSid7, hwCustMccIddNddSid8, hwCustMccIddNddSid9, hwCustMccIddNddSid10, hwCustMccIddNddSid11, hwCustMccIddNddSid12, hwCustMccIddNddSid13, hwCustMccIddNddSid14, hwCustMccIddNddSid15, hwCustMccIddNddSid16, hwCustMccIddNddSid17, hwCustMccIddNddSid18, hwCustMccIddNddSid19, hwCustMccIddNddSid20, hwCustMccIddNddSid21, hwCustMccIddNddSid22, hwCustMccIddNddSid23};
    }
}
