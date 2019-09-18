package com.android.internal.telephony.cat;

/* compiled from: CommandParams */
class CloseChannelParams extends CommandParams {
    TextMessage alertMsg = null;
    int channel = 0;

    CloseChannelParams(CommandDetails cmdDet, TextMessage alertMsg2, int channel2) {
        super(cmdDet);
        this.alertMsg = alertMsg2;
        this.channel = channel2;
    }
}
