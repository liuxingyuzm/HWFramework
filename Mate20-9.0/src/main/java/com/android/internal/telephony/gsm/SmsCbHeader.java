package com.android.internal.telephony.gsm;

import android.telephony.SmsCbCmasInfo;
import android.telephony.SmsCbEtwsInfo;
import com.android.internal.midi.MidiConstants;
import huawei.cust.HwCustUtils;
import java.util.Arrays;

public class SmsCbHeader {
    private static final int DISASTER = 153;
    private static final int EARTHQUACK = 0;
    static final int FORMAT_ETWS_PRIMARY = 3;
    static final int FORMAT_GSM = 1;
    static final int FORMAT_UMTS = 2;
    private static final int MESSAGE_TYPE_CBS_MESSAGE = 1;
    private static final int OTHER_TYPE = -2;
    static final int PDU_HEADER_LENGTH = 6;
    private static final int PDU_LENGTH_ETWS = 56;
    private static final int PDU_LENGTH_GSM = 88;
    private static final int TSUNAMI = 1;
    private final SmsCbCmasInfo mCmasInfo;
    private final int mDataCodingScheme;
    private final SmsCbEtwsInfo mEtwsInfo;
    private final int mFormat;
    private final int mGeographicalScope;
    private HwCustSmsCbHeader mHwCustSmsCbHeader = ((HwCustSmsCbHeader) HwCustUtils.createObj(HwCustSmsCbHeader.class, new Object[0]));
    private final int mMessageIdentifier;
    private final int mNrOfPages;
    private final int mPageIndex;
    private final int mSerialNumber;

    public SmsCbHeader(byte[] pdu) throws IllegalArgumentException {
        byte[] warningSecurityInfo;
        byte[] bArr = pdu;
        if (bArr == null || bArr.length < 6) {
            throw new IllegalArgumentException("Illegal PDU");
        }
        if (bArr.length <= 88) {
            this.mGeographicalScope = (bArr[0] & MidiConstants.STATUS_PROGRAM_CHANGE) >>> 6;
            this.mSerialNumber = ((bArr[0] & MidiConstants.STATUS_RESET) << 8) | (bArr[1] & MidiConstants.STATUS_RESET);
            this.mMessageIdentifier = ((bArr[2] & MidiConstants.STATUS_RESET) << 8) | (bArr[3] & MidiConstants.STATUS_RESET);
            if (!isEtwsMessage() || bArr.length > 56) {
                this.mFormat = 1;
                this.mDataCodingScheme = bArr[4] & MidiConstants.STATUS_RESET;
                int pageIndex = (bArr[5] & 240) >>> 4;
                int nrOfPages = bArr[5] & 15;
                if (pageIndex == 0 || nrOfPages == 0 || pageIndex > nrOfPages) {
                    pageIndex = 1;
                    nrOfPages = 1;
                }
                this.mPageIndex = pageIndex;
                this.mNrOfPages = nrOfPages;
            } else {
                this.mFormat = 3;
                this.mDataCodingScheme = -1;
                this.mPageIndex = -1;
                this.mNrOfPages = -1;
                boolean emergencyUserAlert = (bArr[4] & 1) != 0;
                boolean activatePopup = (bArr[5] & MidiConstants.STATUS_NOTE_OFF) != 0;
                int warningType = (bArr[4] & MidiConstants.STATUS_ACTIVE_SENSING) >>> 1;
                if (this.mHwCustSmsCbHeader != null && this.mHwCustSmsCbHeader.isShowCbsSettingForSBM()) {
                    int type = this.mHwCustSmsCbHeader.getEtwsTypeForSBM(this.mMessageIdentifier);
                    if (type == 153 || type == 0 || type == 1) {
                        warningType = type;
                    }
                }
                if (bArr.length > 6) {
                    warningSecurityInfo = Arrays.copyOfRange(bArr, 6, bArr.length);
                } else {
                    warningSecurityInfo = null;
                }
                SmsCbEtwsInfo smsCbEtwsInfo = new SmsCbEtwsInfo(warningType, emergencyUserAlert, activatePopup, true, warningSecurityInfo);
                this.mEtwsInfo = smsCbEtwsInfo;
                this.mCmasInfo = null;
                return;
            }
        } else {
            this.mFormat = 2;
            if (bArr[0] == 1) {
                this.mMessageIdentifier = ((bArr[1] & MidiConstants.STATUS_RESET) << 8) | (bArr[2] & MidiConstants.STATUS_RESET);
                this.mGeographicalScope = (bArr[3] & MidiConstants.STATUS_PROGRAM_CHANGE) >>> 6;
                this.mSerialNumber = ((bArr[3] & MidiConstants.STATUS_RESET) << 8) | (bArr[4] & MidiConstants.STATUS_RESET);
                this.mDataCodingScheme = bArr[5] & MidiConstants.STATUS_RESET;
                this.mPageIndex = 1;
                this.mNrOfPages = 1;
            } else {
                throw new IllegalArgumentException("Unsupported message type " + messageType);
            }
        }
        if (isEtwsMessage() != 0) {
            SmsCbEtwsInfo smsCbEtwsInfo2 = new SmsCbEtwsInfo(getEtwsWarningType(), isEtwsEmergencyUserAlert(), isEtwsPopupAlert(), false, null);
            this.mEtwsInfo = smsCbEtwsInfo2;
            this.mCmasInfo = null;
        } else if (isCmasMessage()) {
            int messageClass = getCmasMessageClass();
            int severity = getCmasSeverity();
            int urgency = getCmasUrgency();
            int certainty = getCmasCertainty();
            this.mEtwsInfo = null;
            SmsCbCmasInfo smsCbCmasInfo = new SmsCbCmasInfo(messageClass, -1, -1, severity, urgency, certainty);
            this.mCmasInfo = smsCbCmasInfo;
        } else {
            this.mEtwsInfo = null;
            this.mCmasInfo = null;
        }
    }

    /* access modifiers changed from: package-private */
    public int getGeographicalScope() {
        return this.mGeographicalScope;
    }

    /* access modifiers changed from: package-private */
    public int getSerialNumber() {
        return this.mSerialNumber;
    }

    /* access modifiers changed from: package-private */
    public int getServiceCategory() {
        return this.mMessageIdentifier;
    }

    /* access modifiers changed from: package-private */
    public int getDataCodingScheme() {
        return this.mDataCodingScheme;
    }

    /* access modifiers changed from: package-private */
    public int getPageIndex() {
        return this.mPageIndex;
    }

    /* access modifiers changed from: package-private */
    public int getNumberOfPages() {
        return this.mNrOfPages;
    }

    /* access modifiers changed from: package-private */
    public SmsCbEtwsInfo getEtwsInfo() {
        return this.mEtwsInfo;
    }

    /* access modifiers changed from: package-private */
    public SmsCbCmasInfo getCmasInfo() {
        return this.mCmasInfo;
    }

    /* access modifiers changed from: package-private */
    public boolean isEmergencyMessage() {
        return this.mMessageIdentifier >= 4352 && this.mMessageIdentifier <= 6399;
    }

    private boolean isEtwsMessage() {
        boolean z = true;
        if (this.mHwCustSmsCbHeader != null && this.mHwCustSmsCbHeader.isShowCbsSettingForSBM() && this.mHwCustSmsCbHeader.isEtwsMessageForSBM(this.mMessageIdentifier)) {
            return true;
        }
        if ((this.mMessageIdentifier & SmsCbConstants.MESSAGE_ID_ETWS_TYPE_MASK) != 4352) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public boolean isEtwsPrimaryNotification() {
        return this.mFormat == 3;
    }

    /* access modifiers changed from: package-private */
    public boolean isUmtsFormat() {
        return this.mFormat == 2;
    }

    private boolean isCmasMessage() {
        return this.mMessageIdentifier >= 4370 && this.mMessageIdentifier <= 4399;
    }

    private boolean isEtwsPopupAlert() {
        return (this.mSerialNumber & 4096) != 0;
    }

    private boolean isEtwsEmergencyUserAlert() {
        return (this.mSerialNumber & SmsCbConstants.SERIAL_NUMBER_ETWS_EMERGENCY_USER_ALERT) != 0;
    }

    private int getEtwsWarningType() {
        if (this.mHwCustSmsCbHeader != null && this.mHwCustSmsCbHeader.isShowCbsSettingForSBM()) {
            int type = this.mHwCustSmsCbHeader.getEtwsTypeForSBM(this.mMessageIdentifier);
            if (type != -2) {
                return type;
            }
        }
        return this.mMessageIdentifier - 4352;
    }

    private int getCmasMessageClass() {
        switch (this.mMessageIdentifier) {
            case 4370:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_PRESIDENTIAL_LEVEL_LANGUAGE:
                return 0;
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_OBSERVED:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_LIKELY:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_OBSERVED_LANGUAGE:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_LIKELY_LANGUAGE:
                return 1;
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_OBSERVED:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_LIKELY:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_OBSERVED:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_LIKELY:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_OBSERVED:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_LIKELY:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_OBSERVED_LANGUAGE:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_LIKELY_LANGUAGE:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_OBSERVED_LANGUAGE:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_LIKELY_LANGUAGE:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_OBSERVED_LANGUAGE:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_LIKELY_LANGUAGE:
                return 2;
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_CHILD_ABDUCTION_EMERGENCY:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_CHILD_ABDUCTION_EMERGENCY_LANGUAGE:
                return 3;
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_REQUIRED_MONTHLY_TEST:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_REQUIRED_MONTHLY_TEST_LANGUAGE:
                return 4;
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXERCISE:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXERCISE_LANGUAGE:
                return 5;
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_OPERATOR_DEFINED_USE:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_OPERATOR_DEFINED_USE_LANGUAGE:
                return 6;
            default:
                return -1;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x000b, code lost:
        return 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x000d, code lost:
        return 0;
     */
    private int getCmasSeverity() {
        int i = this.mMessageIdentifier;
        switch (i) {
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_OBSERVED:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_LIKELY:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_OBSERVED:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_LIKELY:
                break;
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_OBSERVED:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_LIKELY:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_OBSERVED:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_LIKELY:
                break;
            default:
                switch (i) {
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_OBSERVED_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_LIKELY_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_OBSERVED_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_LIKELY_LANGUAGE:
                        break;
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_OBSERVED_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_LIKELY_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_OBSERVED_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_LIKELY_LANGUAGE:
                        break;
                    default:
                        return -1;
                }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x000b, code lost:
        return 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x000d, code lost:
        return 0;
     */
    private int getCmasUrgency() {
        int i = this.mMessageIdentifier;
        switch (i) {
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_OBSERVED:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_LIKELY:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_OBSERVED:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_LIKELY:
                break;
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_OBSERVED:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_LIKELY:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_OBSERVED:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_LIKELY:
                break;
            default:
                switch (i) {
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_OBSERVED_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_LIKELY_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_OBSERVED_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_LIKELY_LANGUAGE:
                        break;
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_OBSERVED_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_LIKELY_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_OBSERVED_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_LIKELY_LANGUAGE:
                        break;
                    default:
                        return -1;
                }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x000b, code lost:
        return 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x000d, code lost:
        return 0;
     */
    private int getCmasCertainty() {
        int i = this.mMessageIdentifier;
        switch (i) {
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_OBSERVED:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_OBSERVED:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_OBSERVED:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_OBSERVED:
                break;
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_LIKELY:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_LIKELY:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_LIKELY:
            case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_LIKELY:
                break;
            default:
                switch (i) {
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_OBSERVED_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_OBSERVED_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_OBSERVED_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_OBSERVED_LANGUAGE:
                        break;
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_IMMEDIATE_LIKELY_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_EXTREME_EXPECTED_LIKELY_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_IMMEDIATE_LIKELY_LANGUAGE:
                    case SmsCbConstants.MESSAGE_ID_CMAS_ALERT_SEVERE_EXPECTED_LIKELY_LANGUAGE:
                        break;
                    default:
                        return -1;
                }
        }
    }

    public String toString() {
        return "SmsCbHeader{GS=" + this.mGeographicalScope + ", serialNumber=0x" + Integer.toHexString(this.mSerialNumber) + ", messageIdentifier=0x" + Integer.toHexString(this.mMessageIdentifier) + ", format=" + this.mFormat + ", DCS=0x" + Integer.toHexString(this.mDataCodingScheme) + ", page " + this.mPageIndex + " of " + this.mNrOfPages + '}';
    }
}
