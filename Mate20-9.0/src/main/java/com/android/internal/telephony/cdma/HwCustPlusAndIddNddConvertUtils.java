package com.android.internal.telephony.cdma;

import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.AbstractTelephonyProperties;
import java.util.ArrayList;

public class HwCustPlusAndIddNddConvertUtils {
    private static final boolean DBG_NUM = false;
    static final String LOG_TAG = "HwCustPlusAndIddNddConvertUtils";
    public static final String PLUS_PREFIX = "+";
    private static HwCustPlusCountryListTable plusCountryList = HwCustPlusCountryListTable.getInstance();

    private static HwCustMccIddNddSid getNetworkMccIddList() {
        HwCustMccIddNddSid mccIddNddSid = null;
        if (plusCountryList != null) {
            String findMcc = SystemProperties.get(AbstractTelephonyProperties.PROPERTY_CDMA_OPERATOR_MCC, "");
            if (findMcc == null || TextUtils.isEmpty(findMcc)) {
                Rlog.e(LOG_TAG, "plus: getNetworkMccIddList could not find mcc in ril.curMcc");
                return null;
            }
            HwCustPlusCountryListTable hwCustPlusCountryListTable = plusCountryList;
            mccIddNddSid = HwCustPlusCountryListTable.getItemFromCountryListByMcc(findMcc);
        }
        Rlog.d(LOG_TAG, "plus: getNetworkMccIddList mccIddNddSid = " + mccIddNddSid);
        return mccIddNddSid;
    }

    public static String getCurMccBySidLtmoff(String sSid) {
        String findMcc;
        String sMcc = SystemProperties.get(AbstractTelephonyProperties.PROPERTY_CDMA_OPERATOR_MCC_FROM_NW, "");
        String sLtmoff = SystemProperties.get(AbstractTelephonyProperties.PROPERTY_CDMA_TIME_LTMOFFSET, "");
        Rlog.d(LOG_TAG, "plus: getCurMccBySidLtmoff Mcc = " + sMcc + ",Sid = " + sSid + ",Ltmoff = " + sLtmoff);
        HwCustPlusCountryListTable hwCustPlusCountryListTable = plusCountryList;
        ArrayList<HwCustMccSidLtmOff> itemList = HwCustPlusCountryListTable.getItemsFromSidConflictTableBySid(sSid);
        if (itemList == null || itemList.size() == 0) {
            Rlog.d(LOG_TAG, "plus: no mcc_array found in ConflictTable, try to get mcc in Main Table");
            HwCustPlusCountryListTable hwCustPlusCountryListTable2 = plusCountryList;
            findMcc = HwCustPlusCountryListTable.getMccFromMainTableBySid(sSid);
        } else {
            Rlog.d(LOG_TAG, "plus: more than 2 mcc found in ConflictTable");
            findMcc = plusCountryList.getCcFromConflictTableByLTM(itemList, sLtmoff);
        }
        if (findMcc == null) {
            Rlog.e(LOG_TAG, "plus: could not find mcc by sid and ltmoff, use Network Mcc anyway");
            findMcc = sMcc;
        }
        Rlog.d(LOG_TAG, "plus: getCurMccBySidLtmoff, mcc = " + findMcc);
        return findMcc;
    }

    private static String removePlusAddIdd(String number, HwCustMccIddNddSid mccIddNddSid) {
        String formatNum;
        if (number == null || mccIddNddSid == null || !number.startsWith(PLUS_PREFIX)) {
            Rlog.e(LOG_TAG, "plus: removePlusAddIdd input param invalid");
            return number;
        }
        String formatNum2 = number;
        String sCC = mccIddNddSid.Cc;
        Rlog.d(LOG_TAG, "plus: number auto format correctly, mccIddNddSid = " + mccIddNddSid.toString());
        if (formatNum2.startsWith(sCC, 1)) {
            String formatNum3 = formatNum2.substring(1, formatNum2.length());
            formatNum = mccIddNddSid.Idd + formatNum3;
        } else {
            String formatNum4 = formatNum2.substring(1, formatNum2.length());
            formatNum = mccIddNddSid.Idd + formatNum4;
        }
        return formatNum;
    }

    public static String replacePlusCodeWithIddNdd(String number) {
        if (number == null || number.length() == 0 || !number.startsWith(PLUS_PREFIX)) {
            Rlog.w(LOG_TAG, "plus: replacePlusCodeWithIddNdd invalid number, no need to replacePlusCode");
            return number;
        }
        HwCustMccIddNddSid mccIddNddSid = getNetworkMccIddList();
        if (mccIddNddSid != null) {
            return removePlusAddIdd(number, mccIddNddSid);
        }
        Rlog.e(LOG_TAG, "plus: replacePlusCodeWithIddNdd find no operator that match the MCC");
        return number;
    }

    public static String replaceIddNddWithPlus(String number, int toa) {
        if (number == null || number.length() == 0) {
            Rlog.e(LOG_TAG, "plus: replaceIddNddWithPlus please check the param ");
            return number;
        }
        HwCustMccIddNddSid mccIddNddSid = getNetworkMccIddList();
        if (mccIddNddSid == null) {
            Rlog.e(LOG_TAG, "plus: replaceIddNddWithPlus find no operator that match the MCC ");
            return number;
        }
        Rlog.d(LOG_TAG, "plus: replaceIddNddWithPlus mccIddNddSid =" + mccIddNddSid);
        int ccIndex = 0;
        if (number.startsWith(PLUS_PREFIX)) {
            ccIndex = 1;
        }
        if (number.startsWith(mccIddNddSid.Idd, ccIndex)) {
            ccIndex += mccIddNddSid.Idd.length();
            toa = 145;
        }
        return PhoneNumberUtils.stringFromStringAndTOA(number.substring(ccIndex, number.length()), toa);
    }

    private static HwCustMccIddNddSid getMccIddListForSms() {
        String mcc = SystemProperties.get(AbstractTelephonyProperties.PROPERTY_ICC_CDMA_OPERATOR_MCC, "");
        Rlog.d(LOG_TAG, "plus: getMccIddListForSms Mcc = " + mcc);
        if (plusCountryList == null) {
            return null;
        }
        Rlog.d(LOG_TAG, "plus: [canFormatPlusCodeForSms] Mcc = " + mcc);
        if (mcc == null || mcc.length() == 0) {
            return null;
        }
        HwCustPlusCountryListTable hwCustPlusCountryListTable = plusCountryList;
        HwCustMccIddNddSid mccIddNddSid = HwCustPlusCountryListTable.getItemFromCountryListByMcc(mcc);
        Rlog.d(LOG_TAG, "plus: getMccIddListForSms getItemFromCountryListByMcc mccIddNddSid = " + mccIddNddSid);
        return mccIddNddSid;
    }

    public static String replacePlusCodeWithIddNddForSms(String number) {
        if (number == null || number.length() == 0 || !number.startsWith(PLUS_PREFIX)) {
            Rlog.e(LOG_TAG, "plus: replacePlusCodeWithIddNddForSms faild ,invalid param");
            return number;
        }
        HwCustMccIddNddSid mccIddNddSid = getMccIddListForSms();
        if (mccIddNddSid != null) {
            return removePlusAddIdd(number, mccIddNddSid);
        }
        Rlog.e(LOG_TAG, "plus: replacePlusCodeWithIddNddForSms faild ,mccIddNddSid is null");
        return number;
    }

    public static String replaceIddNddWithPlusForSms(String number) {
        if (number == null || number.length() == 0) {
            Rlog.d(LOG_TAG, "plus: [replaceIddNddWithPlusForSms] please check the param ");
            return number;
        }
        String formatNumber = number;
        if (!number.startsWith(PLUS_PREFIX)) {
            HwCustMccIddNddSid mccIddNddSid = getMccIddListForSms();
            if (mccIddNddSid == null) {
                Rlog.d(LOG_TAG, "plus: [replaceIddNddWithPlusForSms] find no operator that match the MCC ");
                return formatNumber;
            }
            String Idd = mccIddNddSid.Idd;
            Rlog.d(LOG_TAG, "plus: [replaceIddNddWithPlusForSms] find match the cc, Idd = " + Idd);
            if (number.startsWith(Idd) && number.length() > Idd.length()) {
                String number2 = number.substring(Idd.length(), number.length());
                formatNumber = PLUS_PREFIX + number2;
            }
        }
        return formatNumber;
    }
}
