package com.android.internal.telephony.gsm;

import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import com.android.internal.telephony.fullnetwork.HwFullNetworkManagerUtils;
import com.android.internal.telephony.uicc.IAdnRecordCacheInner;
import com.android.internal.telephony.uicc.IIccFileHandlerInner;
import com.huawei.android.os.AsyncResultEx;
import com.huawei.android.os.SystemPropertiesEx;
import com.huawei.android.telephony.RlogEx;
import com.huawei.hwparttelephonyopt.BuildConfig;
import com.huawei.internal.telephony.GsmAlphabetEx;
import com.huawei.internal.telephony.gsm.SimTlvEx;
import com.huawei.internal.telephony.uicc.AdnRecordExt;
import com.huawei.internal.telephony.uicc.IccRecordsEx;
import com.huawei.internal.telephony.uicc.IccUtilsEx;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class HwUsimPhoneBookManagerEmailAnr extends Handler implements IHwUsimPhoneBookManagerEx {
    private static final int ADN_RECORD_LENGTH_DEFAULT = 20;
    private static final int ANR_ADDITIONAL_NUMBER_END_ID = 12;
    private static final int ANR_ADDITIONAL_NUMBER_START_ID = 3;
    private static final int ANR_ADN_RECORD_IDENTIFIER_ID = 16;
    private static final int ANR_ADN_SFI_ID = 15;
    private static final int ANR_BCD_NUMBER_LENGTH = 1;
    private static final int ANR_CAPABILITY_ID = 13;
    private static final int ANR_DESCRIPTION_ID = 0;
    private static final int ANR_EXTENSION_ID = 14;
    private static final int ANR_RECORD_LENGTH = 15;
    private static final int ANR_TON_NPI_ID = 2;
    private static final byte BYTE_MASK = -1;
    private static final int DATA_DESCRIPTION_ID_IN_EFEXT1 = 2;
    private static final int DATA_SIZE_IN_EFEXT1 = 13;
    private static final boolean DBG = true;
    private static final int EVENT_ANR_LOAD_DONE = 5;
    private static final int EVENT_EF_ANR_RECORD_SIZE_DONE = 7;
    private static final int EVENT_EF_EMAIL_RECORD_SIZE_DONE = 6;
    private static final int EVENT_EF_EXT1_RECORD_SIZE_DONE = 13;
    private static final int EVENT_EF_IAP_RECORD_SIZE_DONE = 10;
    private static final int EVENT_EMAIL_LOAD_DONE = 4;
    private static final int EVENT_EXT1_LOAD_DONE = 12;
    private static final int EVENT_GET_SIZE_DONE = 101;
    private static final int EVENT_IAP_LOAD_DONE = 3;
    private static final int EVENT_PBR_LOAD_DONE = 1;
    private static final int EVENT_UPDATE_ANR_RECORD_DONE = 9;
    private static final int EVENT_UPDATE_EMAIL_RECORD_DONE = 8;
    private static final int EVENT_UPDATE_EXT1_RECORD_DONE = 14;
    private static final int EVENT_UPDATE_IAP_RECORD_DONE = 11;
    private static final int EVENT_USIM_ADN_LOAD_DONE = 2;
    private static final int EXT1_RECORD_LENGTH_MAX_DEFAULT = 10;
    private static final int EXT_DESCRIPTION_ID_IN_EFEXT1 = 0;
    private static final int EXT_TAG_IN_EFEXT1 = 2;
    private static final int FREE_TAG_IN_EFEXT1 = 0;
    private static final boolean HW_DBG = SystemPropertiesEx.getBoolean("ro.debuggable", false);
    private static final int LENGTH_DESCRIPTION_ID_IN_EFEXT1 = 1;
    private static final String LOG_TAG = "HwUsimPhoneBookManagerEmailAnr";
    private static final int MAX_NUMBER_SIZE_BYTES = 11;
    private static final int RECORDS_SIZE_ARRAY_VALID_LENGTH = 3;
    private static final int RECORDS_TOTAL_NUMBER_ARRAY_INDEX = 2;
    private static final int USIM_EFAAS_TAG = 199;
    private static final int USIM_EFADN_TAG = 192;
    private static final int USIM_EFANR_TAG = 196;
    private static final int USIM_EFCCP1_TAG = 203;
    private static final int USIM_EFEMAIL_TAG = 202;
    private static final int USIM_EFEXT1_TAG = 194;
    private static final int USIM_EFGRP_TAG = 198;
    private static final int USIM_EFGSD_TAG = 200;
    private static final int USIM_EFIAP_TAG = 193;
    private static final int USIM_EFPBC_TAG = 197;
    private static final int USIM_EFSNE_TAG = 195;
    private static final int USIM_EFUID_TAG = 201;
    private static final HashSet<Integer> USIM_EF_TAG_SET = new HashSet<Integer>() {
        /* class com.android.internal.telephony.gsm.HwUsimPhoneBookManagerEmailAnr.AnonymousClass2 */

        {
            add(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_EFEMAIL_TAG));
            add(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_EFADN_TAG));
            add(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_EFEXT1_TAG));
            add(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_EFANR_TAG));
            add(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_EFPBC_TAG));
            add(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_EFGRP_TAG));
            add(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_EFAAS_TAG));
            add(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_EFGSD_TAG));
            add(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_EFUID_TAG));
            add(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_EFCCP1_TAG));
            add(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_EFIAP_TAG));
            add(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_EFSNE_TAG));
        }
    };
    private static final int USIM_TYPE1_TAG = 168;
    private static final int USIM_TYPE2_TAG = 169;
    private static final int USIM_TYPE3_TAG = 170;
    private static final HashSet<Integer> USIM_TYPE_TAG_SET = new HashSet<Integer>() {
        /* class com.android.internal.telephony.gsm.HwUsimPhoneBookManagerEmailAnr.AnonymousClass1 */

        {
            add(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_TYPE1_TAG));
            add(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_TYPE2_TAG));
            add(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_TYPE3_TAG));
        }
    };
    private IAdnRecordCacheInner mAdnCache;
    private ArrayList<Integer> mAdnLengthList;
    private Map<Integer, ArrayList<byte[]>> mAnrFileRecord;
    private Map<Integer, ArrayList<Integer>> mAnrFlags;
    private ArrayList<Integer>[] mAnrFlagsRecord;
    private boolean mAnrPresentInIap;
    private int mAnrTagNumberInIap;
    private Map<Integer, ArrayList<byte[]>> mEmailFileRecord;
    private Map<Integer, ArrayList<Integer>> mEmailFlags;
    private ArrayList<Integer>[] mEmailFlagsRecord;
    private boolean mEmailPresentInIap;
    private int mEmailTagNumberInIap;
    private Map<Integer, ArrayList<byte[]>> mExt1FileRecord;
    private Map<Integer, ArrayList<Integer>> mExt1Flags;
    private ArrayList<Integer>[] mExt1FlagsRecord;
    private Map<Integer, ArrayList<byte[]>> mIapFileRecord;
    private boolean mIapPresent;
    private IIccFileHandlerInner mIccFileHandlerInner;
    private Boolean mIsPbrPresent;
    private final Object mLock;
    private PbrFile mPbrFile;
    private ArrayList<AdnRecordExt> mPhoneBookRecords;
    private int mPhoneId;
    private int[] mRecordSize;
    private boolean mRefreshCache;
    private boolean mSuccess;
    private int[] temRecordSize;

    public HwUsimPhoneBookManagerEmailAnr(IIccFileHandlerInner iIccFileHandlerInner) {
        this.mLock = new Object();
        this.mEmailPresentInIap = false;
        this.mEmailTagNumberInIap = 0;
        this.mAnrPresentInIap = false;
        this.mAnrTagNumberInIap = 0;
        this.mIapPresent = false;
        this.mAdnLengthList = null;
        this.mSuccess = false;
        this.mRefreshCache = false;
        this.mRecordSize = new int[3];
        this.temRecordSize = new int[3];
        this.mPhoneId = -1;
        this.mIccFileHandlerInner = iIccFileHandlerInner;
        this.mPhoneBookRecords = new ArrayList<>();
        this.mPbrFile = null;
        this.mIsPbrPresent = true;
        this.mPhoneId = this.mIccFileHandlerInner.getUiccProfileEx().getPhoneIdHw();
        HwFullNetworkManagerUtils.getInstance().setSimContactLoaded(this.mPhoneId, false);
    }

    public HwUsimPhoneBookManagerEmailAnr(IUsimPhoneBookManagerInner iUsimPhoneBookManagerInner, IIccFileHandlerInner iIccFileHandlerInner, IAdnRecordCacheInner iAdnRecordCacheInner) {
        this.mLock = new Object();
        this.mEmailPresentInIap = false;
        this.mEmailTagNumberInIap = 0;
        this.mAnrPresentInIap = false;
        this.mAnrTagNumberInIap = 0;
        this.mIapPresent = false;
        this.mAdnLengthList = null;
        this.mSuccess = false;
        this.mRefreshCache = false;
        this.mRecordSize = new int[3];
        this.temRecordSize = new int[3];
        this.mPhoneId = -1;
        this.mIccFileHandlerInner = iIccFileHandlerInner;
        this.mPhoneBookRecords = new ArrayList<>();
        this.mAdnLengthList = new ArrayList<>();
        this.mIapFileRecord = new HashMap();
        this.mEmailFileRecord = new HashMap();
        this.mAnrFileRecord = new HashMap();
        this.mPbrFile = null;
        this.mAnrFlags = new HashMap();
        this.mEmailFlags = new HashMap();
        if (IccRecordsEx.getAdnLongNumberSupport()) {
            initExt1FileRecordAndFlags();
        }
        this.mIsPbrPresent = true;
        this.mAdnCache = iAdnRecordCacheInner;
        this.mPhoneId = this.mIccFileHandlerInner.getUiccProfileEx().getPhoneIdHw();
        HwFullNetworkManagerUtils.getInstance().setSimContactLoaded(this.mPhoneId, false);
    }

    public void reset() {
        PbrFile pbrFile;
        if (!(this.mAnrFlagsRecord == null || this.mEmailFlagsRecord == null || (pbrFile = this.mPbrFile) == null)) {
            int pbsFileSize = pbrFile.mFileIds.size();
            for (int i = 0; i < pbsFileSize; i++) {
                this.mAnrFlagsRecord[i].clear();
                this.mEmailFlagsRecord[i].clear();
            }
        }
        if (IccRecordsEx.getAdnLongNumberSupport()) {
            resetExt1Variables();
        }
        this.mAnrFlags.clear();
        this.mEmailFlags.clear();
        this.mPhoneBookRecords.clear();
        this.mIapFileRecord.clear();
        this.mEmailFileRecord.clear();
        this.mAnrFileRecord.clear();
        this.mPbrFile = null;
        this.mAdnLengthList.clear();
        this.mIsPbrPresent = true;
        this.mRefreshCache = false;
        HwFullNetworkManagerUtils.getInstance().setSimContactLoaded(this.mPhoneId, false);
    }

    public ArrayList<AdnRecordExt> loadEfFilesFromUsimHw() {
        synchronized (this.mLock) {
            if (!this.mPhoneBookRecords.isEmpty()) {
                if (this.mRefreshCache) {
                    this.mRefreshCache = false;
                    refreshCache();
                }
                return this.mPhoneBookRecords;
            } else if (!this.mIsPbrPresent.booleanValue()) {
                return null;
            } else {
                if (this.mPbrFile == null) {
                    readPbrFileAndWait();
                }
                if (this.mPbrFile == null) {
                    return null;
                }
                int numRecs = this.mPbrFile.mFileIds.size();
                if (this.mAnrFlagsRecord == null && this.mEmailFlagsRecord == null) {
                    this.mAnrFlagsRecord = new ArrayList[numRecs];
                    this.mEmailFlagsRecord = new ArrayList[numRecs];
                    for (int i = 0; i < numRecs; i++) {
                        this.mAnrFlagsRecord[i] = new ArrayList<>();
                        this.mEmailFlagsRecord[i] = new ArrayList<>();
                    }
                }
                if (this.mAdnLengthList != null && this.mAdnLengthList.size() == 0) {
                    for (int i2 = 0; i2 < numRecs; i2++) {
                        this.mAdnLengthList.add(0);
                    }
                }
                for (int i3 = 0; i3 < numRecs; i3++) {
                    readAdnFileAndWait(i3);
                    readEmailFileAndWait(i3);
                    readAnrFileAndWait(i3);
                }
                if (IccRecordsEx.getAdnLongNumberSupport()) {
                    loadExt1FilesFromUsim(numRecs);
                }
                HwFullNetworkManagerUtils.getInstance().setSimContactLoaded(this.mPhoneId, true);
                return this.mPhoneBookRecords;
            }
        }
    }

    private void refreshCache() {
        if (this.mPbrFile != null) {
            this.mPhoneBookRecords.clear();
            int numRecs = this.mPbrFile.mFileIds.size();
            for (int i = 0; i < numRecs; i++) {
                readAdnFileAndWait(i);
            }
        }
    }

    public void invalidateCache() {
        this.mRefreshCache = true;
    }

    private void readPbrFileAndWait() {
        this.mIccFileHandlerInner.loadEFLinearFixedAll(20272, obtainMessage(1));
        for (boolean isWait = true; isWait; isWait = false) {
            try {
                this.mLock.wait();
            } catch (InterruptedException e) {
                loge("Interrupted Exception in readAdnFileAndWait");
                return;
            }
        }
    }

    private void readEmailFileAndWait(int recNum) {
        PbrFile pbrFile = this.mPbrFile;
        if (pbrFile == null) {
            loge("mPbrFile is NULL, exiting from readEmailFileAndWait");
            return;
        }
        Map<Integer, Integer> fileIds = pbrFile.mFileIds.get(Integer.valueOf(recNum));
        if (fileIds != null && fileIds.containsKey(Integer.valueOf((int) USIM_EFEMAIL_TAG))) {
            if (this.mEmailPresentInIap) {
                if (fileIds.containsKey(Integer.valueOf((int) USIM_EFIAP_TAG))) {
                    readIapFileAndWait(fileIds.get(Integer.valueOf((int) USIM_EFIAP_TAG)).intValue(), recNum);
                } else {
                    log("fileIds don't contain USIM_EFIAP_TAG");
                }
                if (!hasRecordIn(this.mIapFileRecord, recNum)) {
                    loge("Error: IAP file is empty");
                    return;
                }
                this.mIccFileHandlerInner.loadEFLinearFixedAllExcludeEmpty(fileIds.get(Integer.valueOf((int) USIM_EFEMAIL_TAG)).intValue(), obtainMessage(4, Integer.valueOf(recNum)));
                log("readEmailFileAndWait email efid is : " + fileIds.get(Integer.valueOf((int) USIM_EFEMAIL_TAG)));
                for (boolean isWait = true; isWait; isWait = false) {
                    try {
                        this.mLock.wait();
                    } catch (InterruptedException e) {
                        loge("Interrupted Exception in readEmailFileAndWait");
                    }
                }
            } else {
                Iterator<Integer> it = this.mPbrFile.mEmailFileIds.get(Integer.valueOf(recNum)).iterator();
                while (it.hasNext()) {
                    int efid = it.next().intValue();
                    this.mIccFileHandlerInner.loadEFLinearFixedAllExcludeEmpty(efid, obtainMessage(4, Integer.valueOf(recNum)));
                    log("readEmailFileAndWait email efid is : " + efid + " recNum:" + recNum);
                    try {
                        this.mLock.wait();
                    } catch (InterruptedException e2) {
                        loge("Interrupted Exception in readEmailFileAndWait");
                    }
                }
            }
            ArrayList<byte[]> emailFileArray = this.mEmailFileRecord.get(Integer.valueOf(recNum));
            if (emailFileArray != null) {
                int emailDileArraySize = emailFileArray.size();
                for (int m = 0; m < emailDileArraySize; m++) {
                    this.mEmailFlagsRecord[recNum].add(0);
                }
            }
            this.mEmailFlags.put(Integer.valueOf(recNum), this.mEmailFlagsRecord[recNum]);
            updatePhoneAdnRecordWithEmail(recNum);
        }
    }

    private void readAnrFileAndWait(int recNum) {
        Map<Integer, Integer> fileIds = initFileIds(recNum);
        if (!(fileIds == null || fileIds.isEmpty()) && fileIds.containsKey(Integer.valueOf((int) USIM_EFANR_TAG))) {
            if (this.mAnrPresentInIap) {
                if (fileIds.containsKey(Integer.valueOf((int) USIM_EFIAP_TAG))) {
                    readIapFileAndWait(fileIds.get(Integer.valueOf((int) USIM_EFIAP_TAG)).intValue(), recNum);
                } else {
                    log("fileIds don't contain USIM_EFIAP_TAG");
                }
                if (!hasRecordIn(this.mIapFileRecord, recNum)) {
                    loge("Error: IAP file is empty");
                    return;
                }
                this.mIccFileHandlerInner.loadEFLinearFixedAllExcludeEmpty(fileIds.get(Integer.valueOf((int) USIM_EFANR_TAG)).intValue(), obtainMessage(5, Integer.valueOf(recNum)));
                log("readAnrFileAndWait anr efid is : " + fileIds.get(Integer.valueOf((int) USIM_EFANR_TAG)));
                for (boolean isWait = true; isWait; isWait = false) {
                    try {
                        this.mLock.wait();
                    } catch (InterruptedException e) {
                        loge("Interrupted Exception in readEmailFileAndWait");
                    }
                }
            } else {
                Iterator<Integer> it = this.mPbrFile.mAnrFileIds.get(Integer.valueOf(recNum)).iterator();
                while (it.hasNext()) {
                    int efid = it.next().intValue();
                    this.mIccFileHandlerInner.loadEFLinearFixedAllExcludeEmpty(efid, obtainMessage(5, Integer.valueOf(recNum)));
                    log("readAnrFileAndWait anr efid is : " + efid + " recNum:" + recNum);
                    for (boolean isWait2 = true; isWait2; isWait2 = false) {
                        try {
                            this.mLock.wait();
                        } catch (InterruptedException e2) {
                            loge("Interrupted Exception in readEmailFileAndWait");
                        }
                    }
                }
            }
            ArrayList<byte[]> anrFileArray = this.mAnrFileRecord.get(Integer.valueOf(recNum));
            if (anrFileArray != null) {
                int anrFileArraySize = anrFileArray.size();
                for (int m = 0; m < anrFileArraySize; m++) {
                    this.mAnrFlagsRecord[recNum].add(0);
                }
            }
            this.mAnrFlags.put(Integer.valueOf(recNum), this.mAnrFlagsRecord[recNum]);
            updatePhoneAdnRecordWithAnr(recNum);
        }
    }

    private Map<Integer, Integer> initFileIds(int recNum) {
        PbrFile pbrFile = this.mPbrFile;
        if (pbrFile != null) {
            return pbrFile.mFileIds.get(Integer.valueOf(recNum));
        }
        loge("mPbrFile is NULL, exiting from readAnrFileAndWait");
        return null;
    }

    private void readIapFileAndWait(int efid, int recNum) {
        log("pbrIndex is " + recNum + ",iap efid is : " + efid);
        this.mIccFileHandlerInner.loadEFLinearFixedAllExcludeEmpty(efid, obtainMessage(3, Integer.valueOf(recNum)));
        for (boolean isWait = true; isWait; isWait = false) {
            try {
                this.mLock.wait();
            } catch (InterruptedException e) {
                loge("Interrupted Exception in readIapFileAndWait");
                return;
            }
        }
    }

    public boolean updateEmailFile(int adnRecNum, String oldEmail, String newEmail, int efidIndex) {
        int pbrIndex = getPbrIndexBy(adnRecNum - 1);
        int efid = getEfidByTag(pbrIndex, USIM_EFEMAIL_TAG, efidIndex);
        if (oldEmail == null) {
            oldEmail = BuildConfig.FLAVOR;
        }
        if (newEmail == null) {
            newEmail = BuildConfig.FLAVOR;
        }
        String emails = oldEmail + "," + newEmail;
        this.mSuccess = false;
        log("updateEmailFile  efid" + efid + " adnRecNum: " + adnRecNum);
        if (efid == -1) {
            return this.mSuccess;
        }
        if (!this.mEmailPresentInIap || !TextUtils.isEmpty(oldEmail) || TextUtils.isEmpty(newEmail)) {
            this.mSuccess = true;
        } else if (getEmptyEmailNumByPbrindex(pbrIndex) == 0) {
            log("updateEmailFile getEmptyEmailNumByPbrindex=0, pbrIndex is " + pbrIndex);
            this.mSuccess = false;
            return this.mSuccess;
        } else {
            this.mSuccess = updateIapFile(adnRecNum, oldEmail, newEmail, USIM_EFEMAIL_TAG);
        }
        if (this.mSuccess) {
            synchronized (this.mLock) {
                this.mIccFileHandlerInner.getEFLinearRecordSize(efid, obtainMessage(6, adnRecNum, efid, emails));
                for (boolean isWait = true; isWait; isWait = false) {
                    try {
                        this.mLock.wait();
                    } catch (InterruptedException e) {
                        loge("interrupted while trying to update by search");
                    }
                }
            }
        }
        if (this.mEmailPresentInIap && this.mSuccess && !TextUtils.isEmpty(oldEmail) && TextUtils.isEmpty(newEmail)) {
            this.mSuccess = updateIapFile(adnRecNum, oldEmail, newEmail, USIM_EFEMAIL_TAG);
        }
        return this.mSuccess;
    }

    public boolean updateAnrFile(int adnRecNum, String oldAnr, String newAnr, int efidIndex) {
        int pbrIndex = getPbrIndexBy(adnRecNum - 1);
        int efid = getEfidByTag(pbrIndex, USIM_EFANR_TAG, efidIndex);
        if (oldAnr == null) {
            oldAnr = BuildConfig.FLAVOR;
        }
        if (newAnr == null) {
            newAnr = BuildConfig.FLAVOR;
        }
        String anrs = oldAnr + "," + newAnr;
        this.mSuccess = false;
        log("updateAnrFile  efid" + efid + ", adnRecNum: " + adnRecNum);
        if (efid == -1) {
            return this.mSuccess;
        }
        if (!this.mAnrPresentInIap || !TextUtils.isEmpty(oldAnr) || TextUtils.isEmpty(newAnr)) {
            this.mSuccess = true;
        } else if (getEmptyAnrNumByPbrindex(pbrIndex) == 0) {
            log("updateAnrFile getEmptyAnrNumByPbrindex=0, pbrIndex is " + pbrIndex);
            this.mSuccess = false;
            return this.mSuccess;
        } else {
            this.mSuccess = updateIapFile(adnRecNum, oldAnr, newAnr, USIM_EFANR_TAG);
        }
        synchronized (this.mLock) {
            this.mIccFileHandlerInner.getEFLinearRecordSize(efid, obtainMessage(7, adnRecNum, efid, anrs));
            for (boolean isWait = true; isWait; isWait = false) {
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    loge("interrupted while trying to update by search");
                }
            }
        }
        if (this.mAnrPresentInIap && this.mSuccess && !TextUtils.isEmpty(oldAnr) && TextUtils.isEmpty(newAnr)) {
            this.mSuccess = updateIapFile(adnRecNum, oldAnr, newAnr, USIM_EFANR_TAG);
        }
        return this.mSuccess;
    }

    private boolean updateIapFile(int adnRecNum, String oldValue, String newValue, int tag) {
        int efid = getEfidByTag(getPbrIndexBy(adnRecNum - 1), USIM_EFIAP_TAG, 0);
        this.mSuccess = false;
        int recordNumber = -1;
        if (efid == -1) {
            return this.mSuccess;
        }
        if (tag == USIM_EFANR_TAG) {
            recordNumber = getAnrRecNumber(adnRecNum - 1, this.mPhoneBookRecords.size(), oldValue);
        } else if (tag == USIM_EFEMAIL_TAG) {
            recordNumber = getEmailRecNumber(adnRecNum - 1, this.mPhoneBookRecords.size(), oldValue);
        }
        if (TextUtils.isEmpty(newValue)) {
            recordNumber = -1;
        }
        log("updateIapFile  efid=" + efid + ", recordNumber= " + recordNumber + ", adnRecNum=" + adnRecNum);
        synchronized (this.mLock) {
            this.mIccFileHandlerInner.getEFLinearRecordSize(efid, obtainMessage(10, adnRecNum, recordNumber, Integer.valueOf(tag)));
            for (boolean isWait = true; isWait; isWait = false) {
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    loge("interrupted while trying to update by search");
                }
            }
        }
        return this.mSuccess;
    }

    private int getEfidByTag(int recNum, int tag, int efidIndex) {
        PbrFile pbrFile = this.mPbrFile;
        if (pbrFile == null || pbrFile.mFileIds == null) {
            loge("mPbrFile is NULL, exiting from getEfidByTag");
            return -1;
        }
        Map<Integer, Integer> fileIds = this.mPbrFile.mFileIds.get(Integer.valueOf(recNum));
        if (fileIds == null || !fileIds.containsKey(Integer.valueOf(tag))) {
            return -1;
        }
        if (this.mEmailPresentInIap || USIM_EFEMAIL_TAG != tag) {
            if (this.mAnrPresentInIap || USIM_EFANR_TAG != tag) {
                return fileIds.get(Integer.valueOf(tag)).intValue();
            }
            if (!hasRecordIn(this.mPbrFile.mAnrFileIds.get(Integer.valueOf(recNum)), efidIndex)) {
                return -1;
            }
            return this.mPbrFile.mAnrFileIds.get(Integer.valueOf(recNum)).get(efidIndex).intValue();
        } else if (!hasRecordIn(this.mPbrFile.mEmailFileIds.get(Integer.valueOf(recNum)), efidIndex)) {
            return -1;
        } else {
            return this.mPbrFile.mEmailFileIds.get(Integer.valueOf(recNum)).get(efidIndex).intValue();
        }
    }

    public int getPbrIndexBy(int adnIndex) {
        int len = this.mAdnLengthList.size();
        int size = 0;
        for (int i = 0; i < len; i++) {
            size += this.mAdnLengthList.get(i).intValue();
            if (adnIndex < size) {
                return i;
            }
        }
        return -1;
    }

    public int getPbrIndexByEfid(int efid) {
        PbrFile pbrFile = this.mPbrFile;
        if (pbrFile == null || pbrFile.mFileIds == null) {
            return 0;
        }
        int pbrFileIdSize = this.mPbrFile.mFileIds.size();
        for (int i = 0; i < pbrFileIdSize; i++) {
            Map<Integer, Integer> val = this.mPbrFile.mFileIds.get(Integer.valueOf(i));
            if (val != null && val.containsValue(Integer.valueOf(efid))) {
                return i;
            }
        }
        return 0;
    }

    public int getInitIndexByPbr(int pbrIndex) {
        return getInitIndexBy(pbrIndex);
    }

    private int getInitIndexBy(int pbrIndex) {
        int index = 0;
        while (pbrIndex > 0) {
            index += this.mAdnLengthList.get(pbrIndex - 1).intValue();
            pbrIndex--;
        }
        return index;
    }

    private boolean hasRecordIn(ArrayList<Integer> record, int pbrIndex) {
        if (record == null || record.isEmpty() || record.size() <= pbrIndex) {
            return false;
        }
        return true;
    }

    private boolean hasRecordIn(Map<Integer, ArrayList<byte[]>> record, int pbrIndex) {
        if (record == null || record.isEmpty()) {
            return false;
        }
        try {
            if (record.get(Integer.valueOf(pbrIndex)) == null) {
                return false;
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            loge("record is empty in pbrIndex" + pbrIndex);
            return false;
        }
    }

    private void updatePhoneAdnRecordWithEmail(int pbrIndex) {
        if (!hasRecordIn(this.mEmailFileRecord, pbrIndex)) {
            loge("Error: Email file is empty");
        } else if (!hasRecordIn(this.mAdnLengthList, pbrIndex)) {
            loge("Error: mAdnLengthList is invalid");
        } else {
            int numAdnRecs = this.mAdnLengthList.get(pbrIndex).intValue();
            if (!this.mEmailPresentInIap || !hasRecordIn(this.mIapFileRecord, pbrIndex)) {
                int len = this.mAdnLengthList.get(pbrIndex).intValue();
                if (!this.mEmailPresentInIap) {
                    parseType1EmailFile(len, pbrIndex);
                    return;
                }
                return;
            }
            for (int i = 0; i < numAdnRecs; i++) {
                int recNum = getRecNumber(i, pbrIndex, this.mEmailTagNumberInIap);
                if (recNum > 0) {
                    String[] emails = {readEmailRecord(recNum - 1, pbrIndex, 0)};
                    int adnRecIndex = getInitIndexBy(pbrIndex) + i;
                    AdnRecordExt rec = this.mPhoneBookRecords.get(adnRecIndex);
                    if (rec != null && !TextUtils.isEmpty(emails[0])) {
                        rec.setEmails(emails);
                        this.mPhoneBookRecords.set(adnRecIndex, rec);
                        this.mEmailFlags.get(Integer.valueOf(pbrIndex)).set(recNum - 1, 1);
                    }
                }
            }
            int emailRecsSize = this.mEmailFileRecord.get(Integer.valueOf(pbrIndex)).size();
            for (int index = 0; index < emailRecsSize; index++) {
                if (1 != this.mEmailFlags.get(Integer.valueOf(pbrIndex)).get(index).intValue() && !BuildConfig.FLAVOR.equals(readEmailRecord(index, pbrIndex, 0))) {
                    byte[] emailRec = this.mEmailFileRecord.get(Integer.valueOf(pbrIndex)).get(index);
                    for (int i2 = 0; i2 < emailRec.length; i2++) {
                        emailRec[i2] = BYTE_MASK;
                    }
                }
            }
            log("updatePhoneAdnRecordWithEmail: no need to parse type1 EMAIL file");
        }
    }

    private void updatePhoneAdnRecordWithAnr(int pbrIndex) {
        int recNum;
        if (!hasRecordIn(this.mAnrFileRecord, pbrIndex)) {
            loge("Error: Anr file is empty");
        } else if (!hasRecordIn(this.mAdnLengthList, pbrIndex)) {
            loge("Error: mAdnLengthList is invalid");
        } else {
            int numAdnRecs = this.mAdnLengthList.get(pbrIndex).intValue();
            if (this.mAnrPresentInIap && hasRecordIn(this.mIapFileRecord, pbrIndex)) {
                int i = 0;
                while (i < numAdnRecs && (recNum = getRecNumber(i, pbrIndex, this.mAnrTagNumberInIap)) != -1) {
                    if (recNum > 0) {
                        String[] anrs = {readAnrRecord(recNum - 1, pbrIndex, 0)};
                        int adnRecIndex = getInitIndexBy(pbrIndex) + i;
                        AdnRecordExt rec = this.mPhoneBookRecords.get(adnRecIndex);
                        if (rec != null && !TextUtils.isEmpty(anrs[0])) {
                            rec.setAdditionalNumbers(anrs);
                            this.mPhoneBookRecords.set(adnRecIndex, rec);
                            this.mAnrFlags.get(Integer.valueOf(pbrIndex)).set(recNum - 1, 1);
                        }
                    }
                    i++;
                }
                int anrRecsSize = this.mAnrFileRecord.get(Integer.valueOf(pbrIndex)).size();
                for (int index = 0; index < anrRecsSize; index++) {
                    if (1 != this.mAnrFlags.get(Integer.valueOf(pbrIndex)).get(index).intValue() && !BuildConfig.FLAVOR.equals(readAnrRecord(index, pbrIndex, 0))) {
                        byte[] anrRec = this.mAnrFileRecord.get(Integer.valueOf(pbrIndex)).get(index);
                        for (int i2 = 0; i2 < anrRec.length; i2++) {
                            anrRec[i2] = BYTE_MASK;
                        }
                    }
                }
                log("updatePhoneAdnRecordWithAnr: no need to parse type1 ANR file");
            } else if (!this.mAnrPresentInIap) {
                parseType1AnrFile(numAdnRecs, pbrIndex);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void parseType1EmailFile(int numRecs, int pbrIndex) {
        AdnRecordExt rec;
        int numEmailFiles = this.mPbrFile.mEmailFileIds.get(Integer.valueOf(pbrIndex)).size();
        ArrayList<String> emailList = new ArrayList<>();
        int adnInitIndex = getInitIndexBy(pbrIndex);
        if (hasRecordIn(this.mEmailFileRecord, pbrIndex)) {
            log("parseType1EmailFile: pbrIndex is: " + pbrIndex + ", numRecs is: " + numRecs);
            for (int i = 0; i < numRecs; i++) {
                int count = 0;
                emailList.clear();
                for (int j = 0; j < numEmailFiles; j++) {
                    String email = readEmailRecord(i, pbrIndex, j * numRecs);
                    emailList.add(email);
                    if (!TextUtils.isEmpty(email)) {
                        count++;
                        this.mEmailFlags.get(Integer.valueOf(pbrIndex)).set((j * numRecs) + i, 1);
                    }
                }
                if (!(count == 0 || (rec = this.mPhoneBookRecords.get(i + adnInitIndex)) == null)) {
                    String[] emails = new String[emailList.size()];
                    System.arraycopy(emailList.toArray(), 0, emails, 0, emailList.size());
                    rec.setEmails(emails);
                    this.mPhoneBookRecords.set(i + adnInitIndex, rec);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void parseType1AnrFile(int numRecs, int pbrIndex) {
        AdnRecordExt rec;
        int numAnrFiles = this.mPbrFile.mAnrFileIds.get(Integer.valueOf(pbrIndex)).size();
        ArrayList<String> anrList = new ArrayList<>();
        int adnInitIndex = getInitIndexBy(pbrIndex);
        if (hasRecordIn(this.mAnrFileRecord, pbrIndex)) {
            log("parseType1AnrFile: pbrIndex is: " + pbrIndex + ", numRecs is: " + numRecs + ", numAnrFiles " + numAnrFiles);
            for (int i = 0; i < numRecs; i++) {
                int count = 0;
                anrList.clear();
                for (int j = 0; j < numAnrFiles; j++) {
                    String anr = readAnrRecord(i, pbrIndex, j * numRecs);
                    anrList.add(anr);
                    if (!TextUtils.isEmpty(anr)) {
                        count++;
                        this.mAnrFlags.get(Integer.valueOf(pbrIndex)).set((j * numRecs) + i, 1);
                    }
                }
                if (!(count == 0 || (rec = this.mPhoneBookRecords.get(i + adnInitIndex)) == null)) {
                    String[] anrs = new String[anrList.size()];
                    System.arraycopy(anrList.toArray(), 0, anrs, 0, anrList.size());
                    rec.setAdditionalNumbers(anrs);
                    this.mPhoneBookRecords.set(i + adnInitIndex, rec);
                }
            }
        }
    }

    private String readEmailRecord(int recNum, int pbrIndex, int offSet) {
        if (!hasRecordIn(this.mEmailFileRecord, pbrIndex)) {
            return null;
        }
        try {
            byte[] emailRec = this.mEmailFileRecord.get(Integer.valueOf(pbrIndex)).get(recNum + offSet);
            return IccUtilsEx.adnStringFieldToString(emailRec, 0, emailRec.length - 2);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private String readAnrRecord(int recNum, int pbrIndex, int offSet) {
        if (!hasRecordIn(this.mAnrFileRecord, pbrIndex)) {
            return null;
        }
        try {
            byte[] anrRec = this.mAnrFileRecord.get(Integer.valueOf(pbrIndex)).get(recNum + offSet);
            int numberLength = anrRec[1] & BYTE_MASK;
            if (numberLength > 11) {
                return BuildConfig.FLAVOR;
            }
            return PhoneNumberUtils.calledPartyBCDToString(anrRec, 2, numberLength);
        } catch (IndexOutOfBoundsException e) {
            loge("Error: Improper ICC card: No anr record for ADN, continuing");
            return null;
        }
    }

    private void readAdnFileAndWait(int recNum) {
        PbrFile pbrFile = this.mPbrFile;
        if (pbrFile == null) {
            loge("mPbrFile is NULL, exiting from readAdnFileAndWait");
            return;
        }
        Map<Integer, Integer> fileIds = pbrFile.mFileIds.get(Integer.valueOf(recNum));
        if (!(fileIds == null || fileIds.isEmpty())) {
            int extEf = 0;
            if (fileIds.containsKey(Integer.valueOf((int) USIM_EFEXT1_TAG))) {
                extEf = fileIds.get(Integer.valueOf((int) USIM_EFEXT1_TAG)).intValue();
            }
            log("readAdnFileAndWait adn efid is : " + fileIds.get(Integer.valueOf((int) USIM_EFADN_TAG)));
            if (fileIds.containsKey(Integer.valueOf((int) USIM_EFADN_TAG))) {
                this.mAdnCache.requestLoadAllAdnHw(fileIds.get(Integer.valueOf((int) USIM_EFADN_TAG)).intValue(), extEf, obtainMessage(2, Integer.valueOf(recNum)));
                for (boolean isWait = true; isWait; isWait = false) {
                    try {
                        this.mLock.wait();
                    } catch (InterruptedException e) {
                        loge("Interrupted Exception in readAdnFileAndWait");
                        return;
                    }
                }
            }
        }
    }

    private int getEmailRecNumber(int adnRecIndex, int numRecs, String oldEmail) {
        int i;
        int pbrIndex = getPbrIndexBy(adnRecIndex);
        int recordIndex = adnRecIndex - getInitIndexBy(pbrIndex);
        log("getEmailRecNumber adnRecIndex is: " + adnRecIndex + ", recordIndex is :" + recordIndex);
        if (!hasRecordIn(this.mEmailFileRecord, pbrIndex)) {
            log("getEmailRecNumber recordNumber is: -1");
            return -1;
        } else if (!this.mEmailPresentInIap || !hasRecordIn(this.mIapFileRecord, pbrIndex)) {
            return recordIndex + 1;
        } else {
            byte[] record = null;
            try {
                record = this.mIapFileRecord.get(Integer.valueOf(pbrIndex)).get(recordIndex);
            } catch (IndexOutOfBoundsException e) {
                loge("IndexOutOfBoundsException in getEmailRecNumber");
            }
            if (record == null || (i = this.mEmailTagNumberInIap) >= record.length || record[i] == -1 || (record[i] & BYTE_MASK) <= 0 || (record[i] & BYTE_MASK) > this.mEmailFileRecord.get(Integer.valueOf(pbrIndex)).size()) {
                int recsSize = this.mEmailFileRecord.get(Integer.valueOf(pbrIndex)).size();
                log("getEmailRecNumber recsSize is: " + recsSize);
                if (TextUtils.isEmpty(oldEmail)) {
                    for (int i2 = 0; i2 < recsSize; i2++) {
                        if (TextUtils.isEmpty(readEmailRecord(i2, pbrIndex, 0))) {
                            log("getEmailRecNumber: Got empty record.Email record num is :" + (i2 + 1));
                            return i2 + 1;
                        }
                    }
                }
                log("getEmailRecNumber: no email record index found");
                return -1;
            }
            int recordNumber = record[this.mEmailTagNumberInIap] & BYTE_MASK;
            log(" getEmailRecNumber: record is " + IccUtilsEx.bytesToHexString(record) + ", the email recordNumber is :" + recordNumber);
            return recordNumber;
        }
    }

    private int getAnrRecNumber(int adnRecIndex, int numRecs, String oldAnr) {
        int i;
        int pbrIndex = getPbrIndexBy(adnRecIndex);
        int recordIndex = adnRecIndex - getInitIndexBy(pbrIndex);
        if (!hasRecordIn(this.mAnrFileRecord, pbrIndex)) {
            return -1;
        }
        if (!this.mAnrPresentInIap || !hasRecordIn(this.mIapFileRecord, pbrIndex)) {
            return recordIndex + 1;
        }
        byte[] record = null;
        try {
            record = this.mIapFileRecord.get(Integer.valueOf(pbrIndex)).get(recordIndex);
        } catch (IndexOutOfBoundsException e) {
            loge("IndexOutOfBoundsException in getAnrRecNumber");
        }
        if (record == null || (i = this.mAnrTagNumberInIap) >= record.length || record[i] == -1 || (record[i] & BYTE_MASK) <= 0 || (record[i] & BYTE_MASK) > this.mAnrFileRecord.get(Integer.valueOf(pbrIndex)).size()) {
            int recsSize = this.mAnrFileRecord.get(Integer.valueOf(pbrIndex)).size();
            log("getAnrRecNumber: anr record size is :" + recsSize);
            if (TextUtils.isEmpty(oldAnr)) {
                for (int i2 = 0; i2 < recsSize; i2++) {
                    if (TextUtils.isEmpty(readAnrRecord(i2, pbrIndex, 0))) {
                        log("getAnrRecNumber: Empty anr record. Anr record num is :" + (i2 + 1));
                        return i2 + 1;
                    }
                }
            }
            log("getAnrRecNumber: no anr record index found");
            return -1;
        }
        int recordNumber = record[this.mAnrTagNumberInIap] & BYTE_MASK;
        log("getAnrRecNumber: recnum from iap is :" + recordNumber);
        return recordNumber;
    }

    private byte[] buildEmailData(int length, int adnRecIndex, String email) {
        byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            data[i] = BYTE_MASK;
        }
        if (TextUtils.isEmpty(email)) {
            log("[buildEmailData] Empty email record");
            return data;
        }
        byte[] byteEmail = GsmAlphabetEx.stringToGsm8BitPacked(email);
        if (byteEmail.length > data.length) {
            System.arraycopy(byteEmail, 0, data, 0, data.length);
        } else {
            System.arraycopy(byteEmail, 0, data, 0, byteEmail.length);
        }
        int recordIndex = adnRecIndex - getInitIndexBy(getPbrIndexBy(adnRecIndex));
        if (this.mEmailPresentInIap) {
            data[length - 1] = (byte) (recordIndex + 1);
        }
        return data;
    }

    private byte[] buildAnrData(int length, int adnRecIndex, String anr) {
        byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            data[i] = BYTE_MASK;
        }
        if (length < 15) {
            log("The length is invalid " + length);
            return data;
        } else if (TextUtils.isEmpty(anr)) {
            log("[buildAnrData] Empty anr record");
            return data;
        } else {
            data[0] = 0;
            byte[] byteAnr = PhoneNumberUtils.numberToCalledPartyBCD(anr);
            if (byteAnr == null) {
                return new byte[0];
            }
            if (byteAnr.length > 11) {
                System.arraycopy(byteAnr, 0, data, 2, 11);
                data[1] = (byte) 11;
            } else {
                System.arraycopy(byteAnr, 0, data, 2, byteAnr.length);
                data[1] = (byte) byteAnr.length;
            }
            data[13] = BYTE_MASK;
            data[14] = BYTE_MASK;
            if (length == 17) {
                data[16] = (byte) ((adnRecIndex - getInitIndexBy(getPbrIndexBy(adnRecIndex))) + 1);
            }
            return data;
        }
    }

    private void createPbrFile(ArrayList<byte[]> records) {
        if (records == null) {
            this.mPbrFile = null;
            this.mIsPbrPresent = false;
            return;
        }
        this.mPbrFile = new PbrFile(records);
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        int i = msg.what;
        if (i != EVENT_GET_SIZE_DONE) {
            switch (i) {
                case 1:
                    handlePbrLoadDone(msg);
                    return;
                case 2:
                    handleUsimAdnLoadDone(msg);
                    return;
                case 3:
                    handleIapLoadDone(msg);
                    return;
                case 4:
                    handleEmailLoadDone(msg);
                    return;
                case 5:
                    handeAnrLoadDone(msg);
                    return;
                case 6:
                    handleEfEmailRecordSizeDone(msg);
                    return;
                case 7:
                    handleEfAnrRecordSizeDone(msg);
                    return;
                case 8:
                    handleUpdateEmailRecordDone(msg);
                    return;
                case 9:
                    handleUpdateAnrRecordDone(msg);
                    return;
                case 10:
                    handleEfIapRecordSizeDone(msg);
                    return;
                case 11:
                    handleUpdateIapRecordDone(msg);
                    return;
                case HwGsmMmiCode.MATCH_GROUP_SIC /* 12 */:
                    handleExt1LoadDone(msg);
                    return;
                case 13:
                    handleEfExt1RecordSizeDone(msg);
                    return;
                case 14:
                    handleUpdateExt1RecordDone(msg);
                    return;
                default:
                    return;
            }
        } else {
            handleGetSizeDone(msg);
        }
    }

    private void handlePbrLoadDone(Message msg) {
        log("Loading PBR done");
        AsyncResultEx asyncResultEx = AsyncResultEx.from(msg.obj);
        if (asyncResultEx != null && asyncResultEx.getException() == null) {
            createPbrFile((ArrayList) asyncResultEx.getResult());
        }
        synchronized (this.mLock) {
            this.mLock.notify();
        }
    }

    private void handleUsimAdnLoadDone(Message msg) {
        log("handleUsimAdnLoadDone");
        AsyncResultEx asyncResultEx = AsyncResultEx.from(msg.obj);
        if (asyncResultEx == null || asyncResultEx.getException() != null) {
            log("can't load USIM ADN records");
        } else {
            int pbrIndex = ((Integer) asyncResultEx.getUserObj()).intValue();
            ArrayList<AdnRecordExt> result = AdnRecordExt.convertAdnRecordToExt(asyncResultEx.getResult());
            if (result != null) {
                this.mPhoneBookRecords.addAll(result);
                while (pbrIndex >= this.mAdnLengthList.size()) {
                    log("add empty item,pbrIndex=" + pbrIndex + " mAdnLengthList.size=" + this.mAdnLengthList.size());
                    this.mAdnLengthList.add(0);
                }
                this.mAdnLengthList.set(pbrIndex, Integer.valueOf(result.size()));
            }
        }
        synchronized (this.mLock) {
            this.mLock.notify();
        }
    }

    private void handleIapLoadDone(Message msg) {
        log("handleIapLoadDone");
        AsyncResultEx asyncResultEx = AsyncResultEx.from(msg.obj);
        if (asyncResultEx != null && asyncResultEx.getException() == null) {
            this.mIapFileRecord.put(Integer.valueOf(((Integer) asyncResultEx.getUserObj()).intValue()), (ArrayList) asyncResultEx.getResult());
        }
        synchronized (this.mLock) {
            this.mLock.notify();
        }
    }

    private void handleEmailLoadDone(Message msg) {
        log("handleEmailLoadDone");
        AsyncResultEx asyncResultEx = AsyncResultEx.from(msg.obj);
        if (!(asyncResultEx == null || asyncResultEx.getException() != null || this.mPbrFile == null)) {
            int pbrIndex = ((Integer) asyncResultEx.getUserObj()).intValue();
            ArrayList<byte[]> tmpList = this.mEmailFileRecord.get(Integer.valueOf(pbrIndex));
            if (tmpList == null) {
                this.mEmailFileRecord.put(Integer.valueOf(pbrIndex), (ArrayList) asyncResultEx.getResult());
            } else {
                tmpList.addAll((ArrayList) asyncResultEx.getResult());
                this.mEmailFileRecord.put(Integer.valueOf(pbrIndex), tmpList);
            }
            log("handlemessage EVENT_EMAIL_LOAD_DONE size is: " + this.mEmailFileRecord.get(Integer.valueOf(pbrIndex)).size());
        }
        synchronized (this.mLock) {
            this.mLock.notify();
        }
    }

    private void handeAnrLoadDone(Message msg) {
        log("handeAnrLoadDone");
        AsyncResultEx asyncResultEx = AsyncResultEx.from(msg.obj);
        if (!(asyncResultEx == null || asyncResultEx.getException() != null || this.mPbrFile == null)) {
            int pbrIndex = ((Integer) asyncResultEx.getUserObj()).intValue();
            ArrayList<byte[]> tmp = this.mAnrFileRecord.get(Integer.valueOf(pbrIndex));
            if (tmp == null) {
                this.mAnrFileRecord.put(Integer.valueOf(pbrIndex), (ArrayList) asyncResultEx.getResult());
            } else {
                tmp.addAll((ArrayList) asyncResultEx.getResult());
                this.mAnrFileRecord.put(Integer.valueOf(pbrIndex), tmp);
            }
            log("handlemessage EVENT_ANR_LOAD_DONE size is: " + this.mAnrFileRecord.get(Integer.valueOf(pbrIndex)).size());
        }
        synchronized (this.mLock) {
            this.mLock.notify();
        }
    }

    private void failAndReleaseLock() {
        this.mSuccess = false;
        synchronized (this.mLock) {
            this.mLock.notify();
        }
    }

    private void handleEfEmailRecordSizeDone(Message msg) {
        String newEmail;
        String oldEmail;
        int actualRecNumber;
        log("handleEfEmailRecordSizeDone");
        AsyncResultEx asyncResultEx = AsyncResultEx.from(msg.obj);
        if (asyncResultEx == null || asyncResultEx.getException() != null) {
            failAndReleaseLock();
            return;
        }
        int adnRecIndex = msg.arg1 - 1;
        int efid = msg.arg2;
        String[] email = ((String) asyncResultEx.getUserObj()).split(",");
        if (email.length == 1) {
            oldEmail = email[0];
            newEmail = BuildConfig.FLAVOR;
        } else if (email.length > 1) {
            oldEmail = email[0];
            newEmail = email[1];
        } else {
            oldEmail = null;
            newEmail = null;
        }
        int[] recordSize = (int[]) asyncResultEx.getResult();
        int recordNumber = getEmailRecNumber(adnRecIndex, this.mPhoneBookRecords.size(), oldEmail);
        if (recordSize.length == 3 && recordNumber <= recordSize[2]) {
            if (recordNumber > 0) {
                byte[] data = buildEmailData(recordSize[0], adnRecIndex, newEmail);
                if (!this.mEmailPresentInIap) {
                    int efidIndex = this.mPbrFile.mEmailFileIds.get(Integer.valueOf(getPbrIndexBy(adnRecIndex))).indexOf(Integer.valueOf(efid));
                    if (efidIndex == -1) {
                        log("wrong efid index:" + efid);
                        return;
                    }
                    int actualRecNumber2 = recordNumber + (this.mAdnLengthList.get(getPbrIndexBy(adnRecIndex)).intValue() * efidIndex);
                    log("EMAIL index:" + efidIndex + " efid:" + efid + " actual RecNumber:" + actualRecNumber2);
                    actualRecNumber = actualRecNumber2;
                } else {
                    actualRecNumber = recordNumber;
                }
                this.mIccFileHandlerInner.updateEFLinearFixed(efid, recordNumber, data, (String) null, obtainMessage(8, actualRecNumber, adnRecIndex, data));
                return;
            }
        }
        failAndReleaseLock();
    }

    private void handleEfAnrRecordSizeDone(Message msg) {
        String newAnr;
        String oldAnr;
        int actualRecNumber;
        log("handleEfAnrRecordSizeDone");
        AsyncResultEx asyncResultEx = AsyncResultEx.from(msg.obj);
        if (asyncResultEx == null || asyncResultEx.getException() != null) {
            failAndReleaseLock();
            return;
        }
        int adnRecIndex = msg.arg1 - 1;
        int efid = msg.arg2;
        String[] anr = ((String) asyncResultEx.getUserObj()).split(",");
        if (anr.length == 1) {
            oldAnr = anr[0];
            newAnr = BuildConfig.FLAVOR;
        } else if (anr.length > 1) {
            oldAnr = anr[0];
            newAnr = anr[1];
        } else {
            oldAnr = null;
            newAnr = null;
        }
        int[] recordSize = (int[]) asyncResultEx.getResult();
        int recordNumber = getAnrRecNumber(adnRecIndex, this.mPhoneBookRecords.size(), oldAnr);
        if (recordSize.length == 3 && recordNumber <= recordSize[2]) {
            if (recordNumber > 0) {
                byte[] data = buildAnrData(recordSize[0], adnRecIndex, newAnr);
                if (data.length == 0) {
                    failAndReleaseLock();
                    return;
                }
                if (!this.mAnrPresentInIap) {
                    int efidIndex = this.mPbrFile.mAnrFileIds.get(Integer.valueOf(getPbrIndexBy(adnRecIndex))).indexOf(Integer.valueOf(efid));
                    if (efidIndex == -1) {
                        log("wrong efid index:" + efid);
                        return;
                    }
                    int actualRecNumber2 = recordNumber + (this.mAdnLengthList.get(getPbrIndexBy(adnRecIndex)).intValue() * efidIndex);
                    log("ANR index:" + efidIndex + " efid:" + efid + " actual RecNumber:" + actualRecNumber2);
                    actualRecNumber = actualRecNumber2;
                } else {
                    actualRecNumber = recordNumber;
                }
                this.mIccFileHandlerInner.updateEFLinearFixed(efid, recordNumber, data, (String) null, obtainMessage(9, actualRecNumber, adnRecIndex, data));
                return;
            }
        }
        failAndReleaseLock();
    }

    private void handleUpdateEmailRecordDone(Message msg) {
        log("handleUpdateEmailRecordDone");
        AsyncResultEx asyncResultEx = AsyncResultEx.from(msg.obj);
        if (asyncResultEx == null) {
            failAndReleaseLock();
            return;
        }
        byte[] data = (byte[]) asyncResultEx.getUserObj();
        int recordNumber = msg.arg1;
        int pbrIndex = getPbrIndexBy(msg.arg2);
        log("EVENT_UPDATE_EMAIL_RECORD_DONE");
        this.mSuccess = true;
        if (hasRecordIn(this.mEmailFileRecord, pbrIndex)) {
            this.mEmailFileRecord.get(Integer.valueOf(pbrIndex)).set(recordNumber - 1, data);
            int i = 0;
            while (true) {
                if (i >= data.length) {
                    break;
                }
                log("EVENT_UPDATE_EMAIL_RECORD_DONE data = " + ((int) data[i]) + ",i is " + i);
                if (data[i] != -1) {
                    log("EVENT_UPDATE_EMAIL_RECORD_DONE data !=0xff");
                    this.mEmailFlags.get(Integer.valueOf(pbrIndex)).set(recordNumber - 1, 1);
                    break;
                }
                this.mEmailFlags.get(Integer.valueOf(pbrIndex)).set(recordNumber - 1, 0);
                i++;
            }
        } else {
            log("Email record is empty");
        }
        synchronized (this.mLock) {
            this.mLock.notify();
        }
    }

    private void handleUpdateAnrRecordDone(Message msg) {
        log("handleUpdateAnrRecordDone");
        AsyncResultEx asyncResultEx = AsyncResultEx.from(msg.obj);
        if (asyncResultEx == null) {
            failAndReleaseLock();
            return;
        }
        byte[] data = (byte[]) asyncResultEx.getUserObj();
        int recordNumber = msg.arg1;
        int pbrIndex = getPbrIndexBy(msg.arg2);
        log("EVENT_UPDATE_ANR_RECORD_DONE");
        this.mSuccess = true;
        if (hasRecordIn(this.mAnrFileRecord, pbrIndex)) {
            this.mAnrFileRecord.get(Integer.valueOf(pbrIndex)).set(recordNumber - 1, data);
            int i = 0;
            while (true) {
                if (i >= data.length) {
                    break;
                } else if (data[i] != -1) {
                    this.mAnrFlags.get(Integer.valueOf(pbrIndex)).set(recordNumber - 1, 1);
                    break;
                } else {
                    this.mAnrFlags.get(Integer.valueOf(pbrIndex)).set(recordNumber - 1, 0);
                    i++;
                }
            }
        } else {
            log("Anr record is empty");
        }
        synchronized (this.mLock) {
            this.mLock.notify();
        }
    }

    private void handleEfIapRecordSizeDone(Message msg) {
        log("handleEfIapRecordSizeDone");
        AsyncResultEx asyncResultEx = AsyncResultEx.from(msg.obj);
        if (asyncResultEx == null || asyncResultEx.getException() != null) {
            failAndReleaseLock();
            return;
        }
        int recordNumber = msg.arg2;
        int adnRecIndex = msg.arg1 - 1;
        int pbrIndex = getPbrIndexBy(adnRecIndex);
        int efid = getEfidByTag(pbrIndex, USIM_EFIAP_TAG, 0);
        int[] recordSize = (int[]) asyncResultEx.getResult();
        int recordIndex = adnRecIndex - getInitIndexBy(pbrIndex);
        log("handleIAP_RECORD_SIZE_DONE adnRecIndex is: " + adnRecIndex + ", recordNumber is: " + recordNumber + ", recordIndex is: " + recordIndex);
        if (isIapRecordParamInvalid(recordSize, recordIndex, recordNumber)) {
            failAndReleaseLock();
        } else if (hasRecordIn(this.mIapFileRecord, pbrIndex)) {
            int tag = ((Integer) asyncResultEx.getUserObj()).intValue();
            byte[] data = this.mIapFileRecord.get(Integer.valueOf(pbrIndex)).get(recordIndex);
            byte[] recordData = new byte[data.length];
            System.arraycopy(data, 0, recordData, 0, recordData.length);
            if (tag == USIM_EFANR_TAG) {
                recordData[this.mAnrTagNumberInIap] = (byte) recordNumber;
            } else if (tag == USIM_EFEMAIL_TAG) {
                recordData[this.mEmailTagNumberInIap] = (byte) recordNumber;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(" IAP  efid= ");
            sb.append(efid);
            sb.append(", update IAP index= ");
            sb.append(recordIndex);
            sb.append(" with value= ");
            sb.append(HW_DBG ? IccUtilsEx.bytesToHexString(recordData) : "***");
            log(sb.toString());
            this.mIccFileHandlerInner.updateEFLinearFixed(efid, recordIndex + 1, recordData, (String) null, obtainMessage(11, adnRecIndex, recordNumber, recordData));
        }
    }

    private void handleUpdateIapRecordDone(Message msg) {
        log("handleUpdateIapRecordDone");
        AsyncResultEx asyncResultEx = AsyncResultEx.from(msg.obj);
        if (asyncResultEx == null) {
            failAndReleaseLock();
            return;
        }
        byte[] data = (byte[]) asyncResultEx.getUserObj();
        int adnRecIndex = msg.arg1;
        int pbrIndex = getPbrIndexBy(adnRecIndex);
        int recordIndex = adnRecIndex - getInitIndexBy(pbrIndex);
        log("handleUpdateIapRecordDone recordIndex is: " + recordIndex + ", adnRecIndex is: " + adnRecIndex);
        this.mSuccess = true;
        if (hasRecordIn(this.mIapFileRecord, pbrIndex)) {
            this.mIapFileRecord.get(Integer.valueOf(pbrIndex)).set(recordIndex, data);
            log("Iap record is added");
        } else {
            log("Iap record is empty");
        }
        synchronized (this.mLock) {
            this.mLock.notify();
        }
    }

    private void handleGetSizeDone(Message msg) {
        log("handleGetSizeDone");
        AsyncResultEx asyncResultEx = AsyncResultEx.from(msg.obj);
        synchronized (this.mLock) {
            if (asyncResultEx != null) {
                if (asyncResultEx.getException() == null) {
                    this.mRecordSize = (int[]) asyncResultEx.getResult();
                    log("GET_RECORD_SIZE Size " + this.mRecordSize[0] + " total " + this.mRecordSize[1] + " #record " + this.mRecordSize[2]);
                }
            }
            this.mLock.notify();
        }
    }

    private void handleExt1LoadDone(Message msg) {
        log("handleExt1LoadDone");
        AsyncResultEx asyncResultEx = AsyncResultEx.from(msg.obj);
        int pbrIndex = msg.arg1;
        int efid = msg.arg2;
        if (asyncResultEx != null && asyncResultEx.getException() == null) {
            ArrayList<byte[]> tmp = this.mExt1FileRecord.get(Integer.valueOf(pbrIndex));
            if (tmp == null) {
                this.mExt1FileRecord.put(Integer.valueOf(pbrIndex), (ArrayList) asyncResultEx.getResult());
            } else {
                tmp.addAll((ArrayList) asyncResultEx.getResult());
                this.mExt1FileRecord.put(Integer.valueOf(pbrIndex), tmp);
            }
            log("handleExt1LoadDone size is: " + this.mExt1FileRecord.get(Integer.valueOf(pbrIndex)).size());
            ArrayList<byte[]> ext1FileArray = this.mExt1FileRecord.get(Integer.valueOf(pbrIndex));
            if (ext1FileArray != null) {
                if (this.mExt1FlagsRecord == null) {
                    this.mExt1FlagsRecord = new ArrayList[(pbrIndex + 1)];
                    this.mExt1FlagsRecord[pbrIndex] = new ArrayList<>();
                }
                if (pbrIndex < this.mExt1FlagsRecord.length) {
                    int ext1FileArraySize = ext1FileArray.size();
                    for (int m = 0; m < ext1FileArraySize; m++) {
                        this.mExt1FlagsRecord[pbrIndex].add(0);
                    }
                }
            }
            this.mExt1Flags.put(Integer.valueOf(pbrIndex), this.mExt1FlagsRecord[pbrIndex]);
            if (efid == 28474) {
                updateExt1RecordFlagsForSim(pbrIndex);
            } else {
                updateExt1RecordFlags(pbrIndex);
            }
        }
        synchronized (this.mLock) {
            this.mLock.notify();
        }
    }

    private void handleEfExt1RecordSizeDone(Message msg) {
        String newExt1;
        log("handleEfExt1RecordSizeDone");
        AsyncResultEx asyncResultEx = AsyncResultEx.from(msg.obj);
        if (asyncResultEx == null || asyncResultEx.getException() != null) {
            failAndReleaseLock();
            return;
        }
        AdnRecordExt newAdnRecord = (AdnRecordExt) asyncResultEx.getUserObj();
        String mNumber = newAdnRecord.getNumber();
        int adnRecIndex = msg.arg1 - 1;
        int efid = msg.arg2;
        int[] recordSize = (int[]) asyncResultEx.getResult();
        int recordNumber = newAdnRecord.getExtRecord();
        if (recordSize.length != 3 || recordNumber > recordSize[2] || recordNumber <= 0) {
            this.mSuccess = false;
            synchronized (this.mLock) {
                this.mLock.notify();
            }
            return;
        }
        if (mNumber.length() > ADN_RECORD_LENGTH_DEFAULT) {
            newExt1 = mNumber.substring(ADN_RECORD_LENGTH_DEFAULT);
        } else {
            newExt1 = BuildConfig.FLAVOR;
            newAdnRecord.setExtRecord(255);
        }
        byte[] data = buildExt1Data(recordSize[0], adnRecIndex, newExt1);
        this.mIccFileHandlerInner.updateEFLinearFixed(efid, recordNumber, data, (String) null, obtainMessage(14, recordNumber, adnRecIndex, data));
    }

    private void handleUpdateExt1RecordDone(Message msg) {
        log("handleUpdateExt1RecordDone");
        AsyncResultEx asyncResultEx = AsyncResultEx.from(msg.obj);
        if (asyncResultEx == null || asyncResultEx.getException() != null) {
            failAndReleaseLock();
            return;
        }
        byte[] data = (byte[]) asyncResultEx.getUserObj();
        int recordNumber = msg.arg1;
        int pbrIndex = getPbrIndexBy(msg.arg2);
        this.mSuccess = true;
        if (hasRecordIn(this.mExt1FileRecord, pbrIndex)) {
            this.mExt1FileRecord.get(Integer.valueOf(pbrIndex)).set(recordNumber - 1, data);
            int i = 0;
            while (true) {
                if (i < data.length) {
                    if (data[i] != -1 && data[i] != 0) {
                        log("EVENT_UPDATE_EXT1_RECORD_DONE data !=0xff and 0x00");
                        this.mExt1Flags.get(Integer.valueOf(pbrIndex)).set(recordNumber - 1, 1);
                        break;
                    }
                    this.mExt1Flags.get(Integer.valueOf(pbrIndex)).set(recordNumber - 1, 0);
                    i++;
                } else {
                    break;
                }
            }
        } else {
            log("Ext1 record is empty");
        }
        synchronized (this.mLock) {
            this.mLock.notify();
        }
    }

    private boolean isIapRecordParamInvalid(int[] recordSize, int recordIndex, int recordNumber) {
        return 3 != recordSize.length || recordIndex + 1 > recordSize[2] || recordIndex < 0 || recordNumber == 0;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void log(String msg) {
        RlogEx.i(LOG_TAG, "[" + this.mPhoneId + "]" + msg);
    }

    private void loge(String msg) {
        RlogEx.i(LOG_TAG, "[" + this.mPhoneId + "]" + msg);
    }

    public int getAnrCount() {
        int count = 0;
        if (this.mAnrPresentInIap && hasRecordIn(this.mIapFileRecord, 0)) {
            try {
                byte[] record = this.mIapFileRecord.get(0).get(0);
                if (record != null && this.mAnrTagNumberInIap >= record.length) {
                    log("getAnrCount mAnrTagNumberInIap: " + this.mAnrTagNumberInIap + " len:" + record.length);
                    return 0;
                }
            } catch (IndexOutOfBoundsException e) {
                loge("Error: getAnrCount ICC card: No IAP record for ADN, continuing");
                return 0;
            }
        }
        int pbrIndex = this.mAnrFlags.size();
        for (int j = 0; j < pbrIndex; j++) {
            count += this.mAnrFlags.get(Integer.valueOf(j)).size();
        }
        log("getAnrCount count is: " + count);
        return count;
    }

    public int getEmailCount() {
        int count = 0;
        if (this.mEmailPresentInIap && hasRecordIn(this.mIapFileRecord, 0)) {
            try {
                byte[] record = this.mIapFileRecord.get(0).get(0);
                if (record != null && this.mEmailTagNumberInIap >= record.length) {
                    log("getEmailCount mEmailTagNumberInIap: " + this.mEmailTagNumberInIap + " len:" + record.length);
                    return 0;
                }
            } catch (IndexOutOfBoundsException e) {
                loge("Error: getEmailCount ICC card: No IAP record for ADN, continuing");
                return 0;
            }
        }
        int pbrIndex = this.mEmailFlags.size();
        for (int j = 0; j < pbrIndex; j++) {
            count += this.mEmailFlags.get(Integer.valueOf(j)).size();
        }
        log("getEmailCount count is: " + count);
        return count;
    }

    public int getSpareAnrCount() {
        int count = 0;
        int pbrIndex = this.mAnrFlags.size();
        for (int j = 0; j < pbrIndex; j++) {
            int anrFlagSize = 0;
            if (this.mAnrFlags.get(Integer.valueOf(j)) != null) {
                anrFlagSize = this.mAnrFlags.get(Integer.valueOf(j)).size();
            }
            for (int i = 0; i < anrFlagSize; i++) {
                if (this.mAnrFlags.get(Integer.valueOf(j)).get(i).intValue() == 0) {
                    count++;
                }
            }
        }
        log("getSpareAnrCount count is" + count);
        return count;
    }

    public int getSpareEmailCount() {
        int count = 0;
        int pbrIndex = this.mEmailFlags.size();
        for (int j = 0; j < pbrIndex; j++) {
            int emailFlagSize = 0;
            if (this.mEmailFlags.get(Integer.valueOf(j)) != null) {
                emailFlagSize = this.mEmailFlags.get(Integer.valueOf(j)).size();
            }
            for (int i = 0; i < emailFlagSize; i++) {
                if (this.mEmailFlags.get(Integer.valueOf(j)).get(i).intValue() == 0) {
                    count++;
                }
            }
        }
        log("getSpareEmailCount count is: " + count);
        return count;
    }

    public int getUsimAdnCount() {
        ArrayList<AdnRecordExt> arrayList = this.mPhoneBookRecords;
        if (arrayList == null || arrayList.isEmpty()) {
            return 0;
        }
        log("getUsimAdnCount count is" + this.mPhoneBookRecords.size());
        return this.mPhoneBookRecords.size();
    }

    public int getEmptyEmailNumByPbrindex(int pbrindex) {
        int count = 0;
        if (!this.mEmailPresentInIap) {
            return 1;
        }
        if (this.mEmailFlags.containsKey(Integer.valueOf(pbrindex))) {
            int size = this.mEmailFlags.get(Integer.valueOf(pbrindex)).size();
            for (int i = 0; i < size; i++) {
                if (this.mEmailFlags.get(Integer.valueOf(pbrindex)).get(i).intValue() == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getEmptyAnrNumByPbrindex(int pbrindex) {
        int count = 0;
        if (!this.mAnrPresentInIap) {
            return 1;
        }
        if (this.mAnrFlags.containsKey(Integer.valueOf(pbrindex))) {
            int size = this.mAnrFlags.get(Integer.valueOf(pbrindex)).size();
            for (int i = 0; i < size; i++) {
                if (this.mAnrFlags.get(Integer.valueOf(pbrindex)).get(i).intValue() == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getEmailFilesCountEachAdn() {
        PbrFile pbrFile = this.mPbrFile;
        if (pbrFile == null) {
            loge("mPbrFile is NULL, exiting from getEmailFilesCountEachAdn");
            return 0;
        }
        Map<Integer, Integer> fileIds = pbrFile.mFileIds.get(0);
        if (fileIds == null || !fileIds.containsKey(Integer.valueOf((int) USIM_EFEMAIL_TAG))) {
            return 0;
        }
        if (!this.mEmailPresentInIap) {
            return this.mPbrFile.mEmailFileIds.get(0).size();
        }
        return 1;
    }

    public int getAnrFilesCountEachAdn() {
        PbrFile pbrFile = this.mPbrFile;
        if (pbrFile == null) {
            loge("mPbrFile is NULL, exiting from getAnrFilesCountEachAdn");
            return 0;
        }
        Map<Integer, Integer> fileIds = pbrFile.mFileIds.get(0);
        if (fileIds == null || !fileIds.containsKey(Integer.valueOf((int) USIM_EFANR_TAG))) {
            return 0;
        }
        if (!this.mAnrPresentInIap) {
            return this.mPbrFile.mAnrFileIds.get(0).size();
        }
        return 1;
    }

    public int getAdnRecordsFreeSize() {
        int freeRecs = 0;
        log("getAdnRecordsFreeSize(): enter.");
        int totalRecs = getUsimAdnCount();
        if (totalRecs != 0) {
            for (int i = 0; i < totalRecs; i++) {
                if (this.mPhoneBookRecords.get(i).isEmpty()) {
                    freeRecs++;
                }
            }
        } else {
            log("getAdnRecordsFreeSize(): error. ");
        }
        log("getAdnRecordsFreeSize(): freeRecs = " + freeRecs);
        return freeRecs;
    }

    public void setIccFileHandlerHw(IIccFileHandlerInner iccFileHandler) {
        this.mIccFileHandlerInner = iccFileHandler;
    }

    public int[] getAdnRecordsSizeFromEFHw() {
        synchronized (this.mLock) {
            if (!this.mIsPbrPresent.booleanValue()) {
                return null;
            }
            if (this.mPbrFile == null) {
                readPbrFileAndWait();
            }
            if (this.mPbrFile == null) {
                return null;
            }
            int numRecs = this.mPbrFile.mFileIds.size();
            this.temRecordSize[0] = 0;
            this.temRecordSize[1] = 0;
            this.temRecordSize[2] = 0;
            for (int i = 0; i < numRecs; i++) {
                this.mRecordSize[0] = 0;
                this.mRecordSize[1] = 0;
                this.mRecordSize[2] = 0;
                getAdnRecordsSizeAndWait(i);
                log("getAdnRecordsSizeFromEFHw: recordSize[2]=" + this.mRecordSize[2]);
                if (this.mRecordSize[0] != 0) {
                    this.temRecordSize[0] = this.mRecordSize[0];
                }
                if (this.mRecordSize[1] != 0) {
                    this.temRecordSize[1] = this.mRecordSize[1];
                }
                this.temRecordSize[2] = this.mRecordSize[2] + this.temRecordSize[2];
            }
            log("getAdnRecordsSizeFromEFHw: temRecordSize[2]=" + this.temRecordSize[2]);
            return this.temRecordSize;
        }
    }

    private void getAdnRecordsSizeAndWait(int recNum) {
        Map<Integer, Integer> fileIds;
        PbrFile pbrFile = this.mPbrFile;
        if (!(pbrFile == null || (fileIds = pbrFile.mFileIds.get(Integer.valueOf(recNum))) == null || fileIds.isEmpty())) {
            int efid = fileIds.get(Integer.valueOf((int) USIM_EFADN_TAG)).intValue();
            log("getAdnRecordsSize: efid=" + efid);
            this.mIccFileHandlerInner.getEFLinearRecordSize(efid, obtainMessage(EVENT_GET_SIZE_DONE));
            for (boolean isWait = true; isWait; isWait = false) {
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    loge("Interrupted Exception in getAdnRecordsSizeAndWait");
                    return;
                }
            }
        }
    }

    public int getPbrFileSizeHw() {
        int size = 0;
        PbrFile pbrFile = this.mPbrFile;
        if (!(pbrFile == null || pbrFile.mFileIds == null)) {
            size = this.mPbrFile.mFileIds.size();
        }
        log("getPbrFileSize:" + size);
        return size;
    }

    public int getEFidInPBRHw(int recNum, int tag) {
        Map<Integer, Integer> fileIds;
        int efid = 0;
        PbrFile pbrFile = this.mPbrFile;
        if (pbrFile == null || (fileIds = pbrFile.mFileIds.get(Integer.valueOf(recNum))) == null) {
            return 0;
        }
        if (fileIds.containsKey(Integer.valueOf(tag))) {
            efid = fileIds.get(Integer.valueOf(tag)).intValue();
        }
        log("getEFidInPBR, efid = " + efid + ", recNum = " + recNum + ", tag = " + tag);
        return efid;
    }

    private void initExt1FileRecordAndFlags() {
        this.mExt1FileRecord = new HashMap();
        this.mExt1Flags = new HashMap();
    }

    private void resetExt1Variables() {
        if (this.mExt1FlagsRecord != null && this.mPbrFile != null) {
            int i = 0;
            while (true) {
                ArrayList<Integer>[] arrayListArr = this.mExt1FlagsRecord;
                if (i >= arrayListArr.length) {
                    break;
                }
                arrayListArr[i].clear();
                i++;
            }
        } else {
            ArrayList<Integer>[] arrayListArr2 = this.mExt1FlagsRecord;
            if (arrayListArr2 != null && this.mPbrFile == null) {
                arrayListArr2[0].clear();
            }
        }
        this.mExt1Flags.clear();
        this.mExt1FileRecord.clear();
    }

    private void loadExt1FilesFromUsim(int numRecs) {
        this.mExt1FlagsRecord = new ArrayList[numRecs];
        for (int i = 0; i < numRecs; i++) {
            this.mExt1FlagsRecord[i] = new ArrayList<>();
        }
        for (int i2 = 0; i2 < numRecs; i2++) {
            readExt1FileAndWait(i2);
        }
    }

    private void readExt1FileAndWait(int recNum) {
        PbrFile pbrFile = this.mPbrFile;
        if (pbrFile == null) {
            loge("mPbrFile is NULL, exiting from readExt1FileAndWait");
            return;
        }
        Map<Integer, Integer> fileIds = pbrFile.mFileIds.get(Integer.valueOf(recNum));
        if (fileIds == null || fileIds.isEmpty()) {
            loge("fileIds is NULL, exiting from readExt1FileAndWait");
        } else if (fileIds.containsKey(Integer.valueOf((int) USIM_EFEXT1_TAG))) {
            this.mIccFileHandlerInner.loadEFLinearFixedAllExcludeEmpty(fileIds.get(Integer.valueOf((int) USIM_EFEXT1_TAG)).intValue(), obtainMessage(12, recNum, fileIds.get(Integer.valueOf((int) USIM_EFEXT1_TAG)).intValue()));
            log("readExt1FileAndWait EXT1 efid is : " + fileIds.get(Integer.valueOf((int) USIM_EFEXT1_TAG)));
            for (boolean isWait = true; isWait; isWait = false) {
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    loge("Interrupted Exception in readAdnFileAndWait");
                    return;
                }
            }
        }
    }

    private void updateExt1RecordFlags(int pbrIndex) {
        if (hasRecordIn(this.mExt1FileRecord, pbrIndex) && hasRecordIn(this.mAdnLengthList, pbrIndex)) {
            int numAdnRecs = this.mAdnLengthList.get(pbrIndex).intValue();
            for (int i = 0; i < numAdnRecs; i++) {
                AdnRecordExt rec = this.mPhoneBookRecords.get(getInitIndexBy(pbrIndex) + i);
                if (rec != null && rec.getExtRecord() != 255 && rec.getExtRecord() > 0 && rec.getExtRecord() <= this.mExt1Flags.get(Integer.valueOf(pbrIndex)).size()) {
                    this.mExt1Flags.get(Integer.valueOf(pbrIndex)).set(rec.getExtRecord() - 1, 1);
                }
            }
            int extRecsSize = this.mExt1FileRecord.get(Integer.valueOf(pbrIndex)).size();
            for (int index = 0; index < extRecsSize; index++) {
                if (1 != this.mExt1Flags.get(Integer.valueOf(pbrIndex)).get(index).intValue()) {
                    byte[] extRec = this.mExt1FileRecord.get(Integer.valueOf(pbrIndex)).get(index);
                    String extRecord = readExt1Record(pbrIndex, index, 0);
                    if (extRec != null && extRec.length > 0 && extRec[0] == 2 && BuildConfig.FLAVOR.equals(extRecord)) {
                        for (int i2 = 0; i2 < extRec.length; i2++) {
                            extRec[i2] = BYTE_MASK;
                        }
                    }
                }
            }
            log("updateExt1RecordFlags done");
        }
    }

    public void readExt1FileForSim(int efid) {
        if (efid == 28474) {
            this.mIccFileHandlerInner.loadEFLinearFixedAll(28490, obtainMessage(12, 0, efid));
            log("readExt1FileForSim Ext1 efid is : 28490");
        }
    }

    private void updateExt1RecordFlagsForSim(int recNum) {
        this.mPhoneBookRecords = this.mAdnCache.getAdnFilesForSim();
        ArrayList<AdnRecordExt> arrayList = this.mPhoneBookRecords;
        if (arrayList != null) {
            int numAdnRecs = arrayList.size();
            if (this.mAdnLengthList.size() == 0) {
                this.mAdnLengthList.add(0);
            }
            this.mAdnLengthList.set(recNum, Integer.valueOf(numAdnRecs));
            for (int i = 0; i < numAdnRecs; i++) {
                AdnRecordExt rec = this.mPhoneBookRecords.get(i);
                if (rec != null && rec.getExtRecord() != 255 && rec.getExtRecord() > 0 && rec.getExtRecord() <= this.mExt1Flags.get(Integer.valueOf(recNum)).size()) {
                    this.mExt1Flags.get(Integer.valueOf(recNum)).set(rec.getExtRecord() - 1, 1);
                }
            }
            int extRecsSize = this.mExt1FileRecord.get(Integer.valueOf(recNum)).size();
            for (int index = 0; index < extRecsSize; index++) {
                if (1 != this.mExt1Flags.get(Integer.valueOf(recNum)).get(index).intValue()) {
                    byte[] extRec = this.mExt1FileRecord.get(Integer.valueOf(recNum)).get(index);
                    String extRecord = readExt1Record(recNum, index, 0);
                    if (extRec != null && extRec.length > 0 && extRec[0] == 2 && BuildConfig.FLAVOR.equals(extRecord)) {
                        for (int i2 = 0; i2 < extRec.length; i2++) {
                            extRec[i2] = BYTE_MASK;
                        }
                    }
                }
            }
            log("updateExt1RecordFlags done");
        }
    }

    public boolean updateExt1File(int adnRecNum, AdnRecordExt oldAdnRecord, AdnRecordExt newAdnRecord, int tagOrEfid) {
        int efid;
        if (oldAdnRecord == null || newAdnRecord == null) {
            log("updateExt1File para error!");
            return false;
        }
        int pbrIndex = getPbrIndexBy(adnRecNum - 1);
        String oldNumber = oldAdnRecord.getNumber();
        String newNumber = newAdnRecord.getNumber();
        this.mSuccess = false;
        if (!IccRecordsEx.getAdnLongNumberSupport()) {
            this.mSuccess = true;
            return this.mSuccess;
        }
        log("updateExt1File adnRecNum: " + adnRecNum);
        if (oldNumber == null || newNumber == null || parsePlusLength(oldNumber) > ADN_RECORD_LENGTH_DEFAULT || parsePlusLength(newNumber) > ADN_RECORD_LENGTH_DEFAULT) {
            if (tagOrEfid == USIM_EFEXT1_TAG) {
                PbrFile pbrFile = this.mPbrFile;
                if (pbrFile == null || pbrFile.mFileIds == null) {
                    loge("mPbrFile is NULL, exiting from updateExt1File");
                    return this.mSuccess;
                }
                Map<Integer, Integer> fileIds = this.mPbrFile.mFileIds.get(Integer.valueOf(pbrIndex));
                if (fileIds == null) {
                    return this.mSuccess;
                }
                if (!fileIds.containsKey(Integer.valueOf(tagOrEfid))) {
                    return this.mSuccess;
                }
                efid = fileIds.get(Integer.valueOf(tagOrEfid)).intValue();
            } else if (tagOrEfid != 28490) {
                return this.mSuccess;
            } else {
                efid = tagOrEfid;
            }
            if (oldAdnRecord.getExtRecord() == 255 && !TextUtils.isEmpty(newNumber)) {
                int recNum = getExt1RecNumber(adnRecNum);
                if (recNum == -1) {
                    return this.mSuccess;
                }
                newAdnRecord.setExtRecord(recNum);
                log("Index Number in Ext is " + recNum);
            }
            synchronized (this.mLock) {
                this.mIccFileHandlerInner.getEFLinearRecordSize(efid, obtainMessage(13, adnRecNum, efid, newAdnRecord));
                for (boolean isWait = true; isWait; isWait = false) {
                    try {
                        this.mLock.wait();
                    } catch (InterruptedException e) {
                        loge("interrupted while trying to update by search");
                    }
                }
            }
            return this.mSuccess;
        }
        this.mSuccess = true;
        return this.mSuccess;
    }

    private String readExt1Record(int pbrIndex, int recNum, int offset) {
        int numberLength;
        if (!hasRecordIn(this.mExt1FileRecord, pbrIndex)) {
            return null;
        }
        try {
            byte[] extRec = this.mExt1FileRecord.get(Integer.valueOf(pbrIndex)).get(recNum + offset);
            if (extRec != null && extRec.length == 13 && (extRec[0] & BYTE_MASK) != 0 && (numberLength = extRec[1] & BYTE_MASK) <= 10) {
                return PhoneNumberUtils.calledPartyBCDFragmentToString(extRec, 2, numberLength);
            }
            return BuildConfig.FLAVOR;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private byte[] buildExt1Data(int length, int adnRecIndex, String ext) {
        byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            data[i] = BYTE_MASK;
        }
        data[0] = 0;
        if (TextUtils.isEmpty(ext) || length != 13) {
            log("[buildExtData] Empty ext1 record");
            return data;
        }
        byte[] byteExt = PhoneNumberUtils.numberToCalledPartyBCD(ext);
        if (byteExt == null) {
            return data;
        }
        data[0] = 2;
        if (byteExt.length > 11) {
            System.arraycopy(byteExt, 1, data, 2, 10);
            data[1] = 10;
        } else {
            System.arraycopy(byteExt, 1, data, 2, byteExt.length - 1);
            data[1] = (byte) (byteExt.length - 1);
        }
        return data;
    }

    private int getExt1RecNumber(int adnRecIndex) {
        int pbrIndex = getPbrIndexBy(adnRecIndex - 1);
        log("getExt1RecNumber adnRecIndex is: " + adnRecIndex);
        if (!hasRecordIn(this.mExt1FileRecord, pbrIndex)) {
            return -1;
        }
        int extRecordNumber = this.mPhoneBookRecords.get(adnRecIndex - 1).getExtRecord();
        if (extRecordNumber != 255 && extRecordNumber > 0 && extRecordNumber <= this.mExt1FileRecord.get(Integer.valueOf(pbrIndex)).size()) {
            return extRecordNumber;
        }
        int recordSize = this.mExt1FileRecord.get(Integer.valueOf(pbrIndex)).size();
        log("ext record Size: " + recordSize);
        for (int i = 0; i < recordSize; i++) {
            if (TextUtils.isEmpty(readExt1Record(pbrIndex, i, 0))) {
                return i + 1;
            }
        }
        return -1;
    }

    public int getExt1Count() {
        Map<Integer, ArrayList<Integer>> map = this.mExt1Flags;
        if (map == null) {
            return 0;
        }
        int count = 0;
        int pbrIndex = map.size();
        for (int j = 0; j < pbrIndex; j++) {
            count += this.mExt1Flags.get(Integer.valueOf(j)).size();
        }
        log("getExt1Count count is: " + count);
        return count;
    }

    public int getSpareExt1Count() {
        Map<Integer, ArrayList<Integer>> map = this.mExt1Flags;
        if (map == null) {
            return 0;
        }
        int count = 0;
        int pbrIndex = map.size();
        for (int j = 0; j < pbrIndex; j++) {
            int extFlagsSize = this.mExt1Flags.get(Integer.valueOf(j)).size();
            for (int i = 0; i < extFlagsSize; i++) {
                if (this.mExt1Flags.get(Integer.valueOf(j)).get(i).intValue() == 0) {
                    count++;
                }
            }
        }
        log("getSpareExt1Count count is: " + count);
        return count;
    }

    private int getRecNumber(int i, int pbrIndex, int tagNumber) {
        try {
            byte[] record = this.mIapFileRecord.get(Integer.valueOf(pbrIndex)).get(i);
            try {
                return record[tagNumber];
            } catch (IndexOutOfBoundsException e) {
                loge("updatePhoneAdnRecordWithAnr: IndexOutOfBoundsException mAnrTagNumberInIap: " + this.mAnrTagNumberInIap + " tagNumber: " + tagNumber + " len:" + record.length);
                return -1;
            }
        } catch (IndexOutOfBoundsException e2) {
            loge("Error: Improper ICC card: No IAP record for ADN, continuing");
            return -1;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void syncDataFromTagInParseEf(int parentTag, int tag, int tagNumberWithinParentTag) {
        if (!this.mEmailPresentInIap && parentTag == USIM_TYPE2_TAG && this.mIapPresent && tag == USIM_EFEMAIL_TAG) {
            this.mEmailPresentInIap = true;
            this.mEmailTagNumberInIap = tagNumberWithinParentTag;
            log("parseEf: EmailPresentInIap tag = " + this.mEmailTagNumberInIap);
        }
        if (!this.mAnrPresentInIap && parentTag == USIM_TYPE2_TAG && this.mIapPresent && tag == USIM_EFANR_TAG) {
            this.mAnrPresentInIap = true;
            this.mAnrTagNumberInIap = tagNumberWithinParentTag;
            log("parseEf: AnrPresentInIap tag = " + this.mAnrTagNumberInIap);
        }
    }

    private int parsePlusLength(String number) {
        int parsedLength = number.length();
        if (number.indexOf(43) != -1) {
            return parsedLength - 1;
        }
        return parsedLength;
    }

    /* access modifiers changed from: private */
    public class PbrFile {
        boolean isInvalidAnrType = false;
        boolean isInvalidEmailType = false;
        boolean isNoAnrExist = false;
        boolean isNoEmailExist = false;
        HashMap<Integer, ArrayList<Integer>> mAnrFileIds = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> mEmailFileIds = new HashMap<>();
        HashMap<Integer, Map<Integer, Integer>> mFileIds = new HashMap<>();

        PbrFile(ArrayList<byte[]> records) {
            int recNum = 0;
            if (records != null) {
                int listSize = records.size();
                for (int i = 0; i < listSize; i++) {
                    byte[] record = records.get(i);
                    HwUsimPhoneBookManagerEmailAnr.this.log("before making TLVs, data is " + IccUtilsEx.bytesToHexString(record));
                    if (record != null && !IccUtilsEx.bytesToHexString(record).startsWith("ffff")) {
                        SimTlvEx recTlv = new SimTlvEx(record, 0, record.length);
                        if (!recTlv.isValidObject()) {
                            HwUsimPhoneBookManagerEmailAnr.this.log("null == recTlv || !recTlv.isValidObject() is true");
                        } else {
                            parseTag(recTlv, recNum);
                            if (this.mFileIds.get(Integer.valueOf(recNum)) != null) {
                                recNum++;
                            }
                        }
                    }
                }
            }
        }

        private void parseTag(SimTlvEx tlv, int recNum) {
            HwUsimPhoneBookManagerEmailAnr.this.log("parseTag: recNum=xxxxxx");
            HwUsimPhoneBookManagerEmailAnr.this.mIapPresent = false;
            Map<Integer, Integer> val = new HashMap<>();
            ArrayList<Integer> anrList = new ArrayList<>();
            ArrayList<Integer> emailList = new ArrayList<>();
            do {
                int tag = tlv.getTag();
                if (HwUsimPhoneBookManagerEmailAnr.USIM_TYPE_TAG_SET.contains(Integer.valueOf(tag))) {
                    byte[] data = tlv.getData();
                    if (data != null && data.length != 0) {
                        parseEf(new SimTlvEx(data, 0, data.length), val, tag, anrList, emailList);
                    } else if (tag == HwUsimPhoneBookManagerEmailAnr.USIM_TYPE1_TAG) {
                        HwUsimPhoneBookManagerEmailAnr.this.log("parseTag: invalid A8 data, ignore the whole record");
                        return;
                    }
                }
            } while (tlv.nextObject());
            int size = anrList.size();
            Object obj = "***";
            if (size != 0) {
                this.mAnrFileIds.put(Integer.valueOf(recNum), anrList);
                HwUsimPhoneBookManagerEmailAnr hwUsimPhoneBookManagerEmailAnr = HwUsimPhoneBookManagerEmailAnr.this;
                StringBuilder sb = new StringBuilder();
                sb.append("parseTag: recNum=xxxxxx ANR file list:");
                sb.append(HwUsimPhoneBookManagerEmailAnr.HW_DBG ? anrList : obj);
                hwUsimPhoneBookManagerEmailAnr.log(sb.toString());
            }
            if (emailList.size() != 0) {
                HwUsimPhoneBookManagerEmailAnr hwUsimPhoneBookManagerEmailAnr2 = HwUsimPhoneBookManagerEmailAnr.this;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("parseTag: recNum=xxxxxx EMAIL file list:");
                if (HwUsimPhoneBookManagerEmailAnr.HW_DBG) {
                    obj = emailList;
                }
                sb2.append(obj);
                hwUsimPhoneBookManagerEmailAnr2.log(sb2.toString());
                this.mEmailFileIds.put(Integer.valueOf(recNum), emailList);
            }
            this.mFileIds.put(Integer.valueOf(recNum), val);
            if (val.size() != 0) {
                if (!val.containsKey(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_EFEMAIL_TAG))) {
                    this.isNoEmailExist = true;
                }
                if (!val.containsKey(Integer.valueOf((int) HwUsimPhoneBookManagerEmailAnr.USIM_EFANR_TAG))) {
                    this.isNoAnrExist = true;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void parseEf(SimTlvEx tlv, Map<Integer, Integer> val, int parentTag, ArrayList<Integer> anrList, ArrayList<Integer> emailList) {
            int tagNumberWithinParentTag = 0;
            do {
                int tag = tlv.getTag();
                if (parentTag == HwUsimPhoneBookManagerEmailAnr.USIM_TYPE1_TAG && tag == HwUsimPhoneBookManagerEmailAnr.USIM_EFIAP_TAG) {
                    HwUsimPhoneBookManagerEmailAnr.this.mIapPresent = true;
                }
                if (parentTag != HwUsimPhoneBookManagerEmailAnr.USIM_TYPE2_TAG || HwUsimPhoneBookManagerEmailAnr.this.mIapPresent) {
                    HwUsimPhoneBookManagerEmailAnr.this.syncDataFromTagInParseEf(parentTag, tag, tagNumberWithinParentTag);
                    if (HwUsimPhoneBookManagerEmailAnr.USIM_EF_TAG_SET.contains(Integer.valueOf(tag))) {
                        parseOneEfTag(tlv, val, tag, parentTag, anrList, emailList);
                    }
                    tagNumberWithinParentTag++;
                }
            } while (tlv.nextObject());
        }

        private void parseOneEfTag(SimTlvEx tlv, Map<Integer, Integer> val, int tag, int parentTag, ArrayList<Integer> anrList, ArrayList<Integer> emailList) {
            byte[] data = tlv.getData();
            if (data != null && data.length >= 2) {
                int efid = ((data[0] & HwUsimPhoneBookManagerEmailAnr.BYTE_MASK) << 8) | (data[1] & HwUsimPhoneBookManagerEmailAnr.BYTE_MASK);
                if (val.containsKey(Integer.valueOf(tag))) {
                    HwUsimPhoneBookManagerEmailAnr hwUsimPhoneBookManagerEmailAnr = HwUsimPhoneBookManagerEmailAnr.this;
                    hwUsimPhoneBookManagerEmailAnr.log("already have (" + tag + "," + efid + ") parent tag:" + parentTag);
                } else if (!shouldIgnoreEmail(tag, parentTag) && !shouldIgnoreAnr(tag, parentTag)) {
                    val.put(Integer.valueOf(tag), Integer.valueOf(efid));
                    if (parentTag == HwUsimPhoneBookManagerEmailAnr.USIM_TYPE1_TAG) {
                        if (tag == HwUsimPhoneBookManagerEmailAnr.USIM_EFANR_TAG) {
                            anrList.add(Integer.valueOf(efid));
                        } else if (tag == HwUsimPhoneBookManagerEmailAnr.USIM_EFEMAIL_TAG) {
                            emailList.add(Integer.valueOf(efid));
                        }
                    }
                    HwUsimPhoneBookManagerEmailAnr hwUsimPhoneBookManagerEmailAnr2 = HwUsimPhoneBookManagerEmailAnr.this;
                    hwUsimPhoneBookManagerEmailAnr2.log("parseEf.put(" + tag + "," + efid + ") parent tag:" + parentTag);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public boolean shouldIgnoreEmail(int tag, int parentTag) {
            if (tag == HwUsimPhoneBookManagerEmailAnr.USIM_EFEMAIL_TAG && (this.isInvalidEmailType || (parentTag != HwUsimPhoneBookManagerEmailAnr.USIM_TYPE1_TAG && parentTag != HwUsimPhoneBookManagerEmailAnr.USIM_TYPE2_TAG))) {
                HwUsimPhoneBookManagerEmailAnr.this.log("parseEf: invalid Email type!");
                this.isInvalidEmailType = true;
                return true;
            } else if (tag != HwUsimPhoneBookManagerEmailAnr.USIM_EFEMAIL_TAG || !this.isNoEmailExist) {
                return false;
            } else {
                HwUsimPhoneBookManagerEmailAnr.this.log("parseEf: isNoEmailExist");
                return true;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean shouldIgnoreAnr(int tag, int parentTag) {
            if (tag == HwUsimPhoneBookManagerEmailAnr.USIM_EFANR_TAG && (this.isInvalidAnrType || (parentTag != HwUsimPhoneBookManagerEmailAnr.USIM_TYPE1_TAG && parentTag != HwUsimPhoneBookManagerEmailAnr.USIM_TYPE2_TAG))) {
                HwUsimPhoneBookManagerEmailAnr.this.log("parseEf: invalid Anr type!");
                this.isInvalidAnrType = true;
                return true;
            } else if (tag != HwUsimPhoneBookManagerEmailAnr.USIM_EFANR_TAG || !this.isNoAnrExist) {
                return false;
            } else {
                HwUsimPhoneBookManagerEmailAnr.this.log("parseEf: isNoAnrExist");
                return true;
            }
        }
    }
}
