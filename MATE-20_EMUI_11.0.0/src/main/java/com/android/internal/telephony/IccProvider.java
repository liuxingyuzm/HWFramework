package com.android.internal.telephony;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import com.android.internal.telephony.IIccPhoneBook;
import com.android.internal.telephony.uicc.AdnRecord;
import com.android.internal.telephony.uicc.IccConstants;
import java.util.List;

public class IccProvider extends ContentProvider {
    @UnsupportedAppUsage
    private static final String[] ADDRESS_BOOK_COLUMN_NAMES = {"name", STR_NUMBER, STR_EMAILS, HbpcdLookup.ID};
    protected static final int ADN = 1;
    protected static final int ADN_ALL = 7;
    protected static final int ADN_SUB = 2;
    @UnsupportedAppUsage
    private static final boolean DBG = true;
    protected static final int FDN = 3;
    protected static final int FDN_SUB = 4;
    protected static final int SDN = 5;
    protected static final int SDN_SUB = 6;
    protected static final String STR_EMAILS = "emails";
    protected static final String STR_NUMBER = "number";
    protected static final String STR_PIN2 = "pin2";
    protected static final String STR_TAG = "tag";
    private static final String TAG = "IccProvider";
    private static final UriMatcher URL_MATCHER = new UriMatcher(-1);
    private SubscriptionManager mSubscriptionManager;

    static {
        URL_MATCHER.addURI("icc", "adn", 1);
        URL_MATCHER.addURI("icc", "adn/subId/#", 2);
        URL_MATCHER.addURI("icc", "fdn", 3);
        URL_MATCHER.addURI("icc", "fdn/subId/#", 4);
        URL_MATCHER.addURI("icc", "sdn", 5);
        URL_MATCHER.addURI("icc", "sdn/subId/#", 6);
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        this.mSubscriptionManager = SubscriptionManager.from(getContext());
        return true;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri url, String[] projection, String selection, String[] selectionArgs, String sort) {
        log("query");
        if (HwTelephonyFactory.getHwUiccManager().isHwSimPhonebookEnabled()) {
            return HwTelephonyFactory.getHwUiccManager().simContactsQuery(getContext(), url, projection, selection, selectionArgs, sort);
        }
        switch (URL_MATCHER.match(url)) {
            case 1:
                return loadFromEf(28474, SubscriptionManager.getDefaultSubscriptionId());
            case 2:
                return loadFromEf(28474, getRequestSubId(url));
            case 3:
                return loadFromEf(IccConstants.EF_FDN, SubscriptionManager.getDefaultSubscriptionId());
            case 4:
                return loadFromEf(IccConstants.EF_FDN, getRequestSubId(url));
            case 5:
                return loadFromEf(IccConstants.EF_SDN, SubscriptionManager.getDefaultSubscriptionId());
            case 6:
                return loadFromEf(IccConstants.EF_SDN, getRequestSubId(url));
            case 7:
                return loadAllSimContacts(28474);
            default:
                throw new IllegalArgumentException("Unknown URL " + url);
        }
    }

    private Cursor loadAllSimContacts(int efType) {
        Cursor[] result;
        List<SubscriptionInfo> subInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList(false);
        if (subInfoList == null || subInfoList.size() == 0) {
            result = new Cursor[0];
        } else {
            int subIdCount = subInfoList.size();
            result = new Cursor[subIdCount];
            for (int i = 0; i < subIdCount; i++) {
                int subId = subInfoList.get(i).getSubscriptionId();
                result[i] = loadFromEf(efType, subId);
                Rlog.i(TAG, "ADN Records loaded for Subscription ::" + subId);
            }
        }
        return new MergeCursor(result);
    }

    @Override // android.content.ContentProvider
    public String getType(Uri url) {
        if (HwTelephonyFactory.getHwUiccManager().isHwSimPhonebookEnabled()) {
            return HwTelephonyFactory.getHwUiccManager().simContactsGetType(getContext(), url);
        }
        switch (URL_MATCHER.match(url)) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                return "vnd.android.cursor.dir/sim-contact";
            default:
                throw new IllegalArgumentException("Unknown URL " + url);
        }
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri url, ContentValues initialValues) {
        int subId;
        String pin2;
        int efType;
        if (HwTelephonyFactory.getHwUiccManager().isHwSimPhonebookEnabled()) {
            return HwTelephonyFactory.getHwUiccManager().simContactsInsert(getContext(), url, initialValues);
        }
        log("insert");
        int match = URL_MATCHER.match(url);
        if (match == 1) {
            pin2 = null;
            efType = 28474;
            subId = SubscriptionManager.getDefaultSubscriptionId();
        } else if (match == 2) {
            pin2 = null;
            efType = 28474;
            subId = getRequestSubId(url);
        } else if (match == 3) {
            int subId2 = SubscriptionManager.getDefaultSubscriptionId();
            pin2 = initialValues.getAsString(STR_PIN2);
            efType = 28475;
            subId = subId2;
        } else if (match == 4) {
            int subId3 = getRequestSubId(url);
            pin2 = initialValues.getAsString(STR_PIN2);
            efType = 28475;
            subId = subId3;
        } else {
            throw new UnsupportedOperationException("Cannot insert into URL: " + url);
        }
        if (!addIccRecordToEf(efType, initialValues.getAsString(STR_TAG), initialValues.getAsString(STR_NUMBER), null, pin2, subId)) {
            return null;
        }
        StringBuilder buf = new StringBuilder("content://icc/");
        if (match == 1) {
            buf.append("adn/");
        } else if (match == 2) {
            buf.append("adn/subId/");
        } else if (match == 3) {
            buf.append("fdn/");
        } else if (match == 4) {
            buf.append("fdn/subId/");
        }
        buf.append(0);
        Uri resultUri = Uri.parse(buf.toString());
        getContext().getContentResolver().notifyChange(url, null);
        return resultUri;
    }

    private String normalizeValue(String inVal) {
        int len = inVal.length();
        if (len == 0) {
            log("len of input String is 0");
            return inVal;
        } else if (inVal.charAt(0) == '\'' && inVal.charAt(len - 1) == '\'') {
            return inVal.substring(1, len - 1);
        } else {
            return inVal;
        }
    }

    @Override // android.content.ContentProvider
    public int delete(Uri url, String where, String[] whereArgs) {
        int subId;
        int efType;
        if (HwTelephonyFactory.getHwUiccManager().isHwSimPhonebookEnabled()) {
            return HwTelephonyFactory.getHwUiccManager().simContactsDelete(getContext(), url, where, whereArgs);
        }
        int match = URL_MATCHER.match(url);
        if (match == 1) {
            efType = 28474;
            subId = SubscriptionManager.getDefaultSubscriptionId();
        } else if (match == 2) {
            efType = 28474;
            subId = getRequestSubId(url);
        } else if (match == 3) {
            efType = 28475;
            subId = SubscriptionManager.getDefaultSubscriptionId();
        } else if (match == 4) {
            efType = 28475;
            subId = getRequestSubId(url);
        } else {
            throw new UnsupportedOperationException("Cannot insert into URL: " + url);
        }
        log("delete");
        String[] tokens = where.split(" AND ");
        int n = tokens.length;
        String tag = null;
        String number = null;
        String[] emails = null;
        String pin2 = null;
        while (true) {
            int n2 = n - 1;
            if (n2 < 0) {
                break;
            }
            String param = tokens[n2];
            log("parsing '" + param + "'");
            String[] pair = param.split("=", 2);
            if (pair.length != 2) {
                Rlog.e(TAG, "resolve: bad whereClause parameter: " + param);
                n = n2;
            } else {
                String key = pair[0].trim();
                String val = pair[1].trim();
                if (STR_TAG.equals(key)) {
                    tag = normalizeValue(val);
                } else if (STR_NUMBER.equals(key)) {
                    number = normalizeValue(val);
                } else if (STR_EMAILS.equals(key)) {
                    emails = null;
                } else if (STR_PIN2.equals(key)) {
                    pin2 = normalizeValue(val);
                }
                n = n2;
            }
        }
        if ((efType == 3 && TextUtils.isEmpty(pin2)) || !deleteIccRecordFromEf(efType, tag, number, emails, pin2, subId)) {
            return 0;
        }
        getContext().getContentResolver().notifyChange(url, null);
        return 1;
    }

    @Override // android.content.ContentProvider
    public int update(Uri url, ContentValues values, String where, String[] whereArgs) {
        int subId;
        int efType;
        if (HwTelephonyFactory.getHwUiccManager().isHwSimPhonebookEnabled()) {
            return HwTelephonyFactory.getHwUiccManager().simContactsUpdate(getContext(), url, values, where, whereArgs);
        }
        String pin2 = null;
        log("update");
        int match = URL_MATCHER.match(url);
        if (match == 1) {
            efType = 28474;
            subId = SubscriptionManager.getDefaultSubscriptionId();
        } else if (match == 2) {
            efType = 28474;
            subId = getRequestSubId(url);
        } else if (match == 3) {
            efType = IccConstants.EF_FDN;
            int subId2 = SubscriptionManager.getDefaultSubscriptionId();
            pin2 = values.getAsString(STR_PIN2);
            subId = subId2;
        } else if (match == 4) {
            efType = IccConstants.EF_FDN;
            int subId3 = getRequestSubId(url);
            pin2 = values.getAsString(STR_PIN2);
            subId = subId3;
        } else {
            throw new UnsupportedOperationException("Cannot insert into URL: " + url);
        }
        if (!updateIccRecordInEf(efType, values.getAsString(STR_TAG), values.getAsString(STR_NUMBER), values.getAsString("newTag"), values.getAsString("newNumber"), pin2, subId)) {
            return 0;
        }
        getContext().getContentResolver().notifyChange(url, null);
        return 1;
    }

    private MatrixCursor loadFromEf(int efType, int subId) {
        log("loadFromEf: efType=0x" + Integer.toHexString(efType).toUpperCase() + ", subscription=" + subId);
        List<AdnRecord> adnRecords = null;
        try {
            IIccPhoneBook iccIpb = IIccPhoneBook.Stub.asInterface(ServiceManager.getService("simphonebook"));
            if (iccIpb != null) {
                adnRecords = iccIpb.getAdnRecordsInEfForSubscriber(subId, efType);
            }
        } catch (RemoteException e) {
        } catch (SecurityException ex) {
            log(ex.toString());
        }
        if (adnRecords != null) {
            int N = adnRecords.size();
            MatrixCursor cursor = new MatrixCursor(ADDRESS_BOOK_COLUMN_NAMES, N);
            log("adnRecords.size=" + N);
            for (int i = 0; i < N; i++) {
                loadRecord(adnRecords.get(i), cursor, i);
            }
            return cursor;
        }
        Rlog.w(TAG, "Cannot load ADN records");
        return new MatrixCursor(ADDRESS_BOOK_COLUMN_NAMES);
    }

    private boolean addIccRecordToEf(int efType, String name, String number, String[] emails, String pin2, int subId) {
        log("addIccRecordToEf: efType=0x" + Integer.toHexString(efType).toUpperCase() + ", subscription=" + subId);
        boolean success = false;
        try {
            IIccPhoneBook iccIpb = IIccPhoneBook.Stub.asInterface(ServiceManager.getService("simphonebook"));
            if (iccIpb != null) {
                success = iccIpb.updateAdnRecordsInEfBySearchForSubscriber(subId, efType, PhoneConfigurationManager.SSSS, PhoneConfigurationManager.SSSS, name, number, pin2);
            }
        } catch (RemoteException e) {
        } catch (SecurityException ex) {
            log(ex.toString());
        }
        log("addIccRecordToEf: " + success);
        return success;
    }

    private boolean updateIccRecordInEf(int efType, String oldName, String oldNumber, String newName, String newNumber, String pin2, int subId) {
        log("updateIccRecordInEf: efType=0x" + Integer.toHexString(efType).toUpperCase() + ", subscription=" + subId);
        boolean success = false;
        try {
            IIccPhoneBook iccIpb = IIccPhoneBook.Stub.asInterface(ServiceManager.getService("simphonebook"));
            if (iccIpb != null) {
                success = iccIpb.updateAdnRecordsInEfBySearchForSubscriber(subId, efType, oldName, oldNumber, newName, newNumber, pin2);
            }
        } catch (RemoteException e) {
        } catch (SecurityException ex) {
            log(ex.toString());
        }
        log("updateIccRecordInEf: " + success);
        return success;
    }

    private boolean deleteIccRecordFromEf(int efType, String name, String number, String[] emails, String pin2, int subId) {
        log("deleteIccRecordFromEf: efType=0x" + Integer.toHexString(efType).toUpperCase() + ", subscription=" + subId);
        boolean success = false;
        try {
            IIccPhoneBook iccIpb = IIccPhoneBook.Stub.asInterface(ServiceManager.getService("simphonebook"));
            if (iccIpb != null) {
                success = iccIpb.updateAdnRecordsInEfBySearchForSubscriber(subId, efType, name, number, PhoneConfigurationManager.SSSS, PhoneConfigurationManager.SSSS, pin2);
            }
        } catch (RemoteException e) {
        } catch (SecurityException ex) {
            log(ex.toString());
        }
        log("deleteIccRecordFromEf: " + success);
        return success;
    }

    @UnsupportedAppUsage
    private void loadRecord(AdnRecord record, MatrixCursor cursor, int id) {
        if (!record.isEmpty()) {
            Object[] contact = new Object[4];
            String alphaTag = record.getAlphaTag();
            String number = record.getNumber();
            contact[0] = alphaTag;
            contact[1] = number;
            String[] emails = record.getEmails();
            if (emails != null) {
                StringBuilder emailString = new StringBuilder();
                for (String email : emails) {
                    log("Adding email:" + Rlog.pii(TAG, email));
                    emailString.append(email);
                    emailString.append(",");
                }
                contact[2] = emailString.toString();
            }
            contact[3] = Integer.valueOf(id);
            cursor.addRow(contact);
        }
    }

    @UnsupportedAppUsage
    private void log(String msg) {
        Rlog.d(TAG, "[IccProvider] " + msg);
    }

    private int getRequestSubId(Uri url) {
        log("getRequestSubId url: " + url);
        try {
            return Integer.parseInt(url.getLastPathSegment());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unknown URL " + url);
        }
    }
}
