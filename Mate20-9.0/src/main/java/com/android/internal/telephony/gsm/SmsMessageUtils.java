package com.android.internal.telephony.gsm;

import com.android.internal.telephony.gsm.SmsMessage;

public class SmsMessageUtils {
    public void setProtocolIdentifier(SmsMessage smsMessage, int value) {
        smsMessage.setProtocolIdentifierHw(value);
    }

    public void setDataCodingScheme(SmsMessage smsMessage, int value) {
        smsMessage.setDataCodingSchemeHw(value);
    }

    public void parseUserData(SmsMessage smsMessage, SmsMessage.PduParser p, boolean hasUserDataHeader) {
        smsMessage.parseUserDataHw(p, hasUserDataHeader);
    }
}
