package com.android.internal.telephony.vsim.process;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import com.android.internal.telephony.HwVSimPhoneFactory;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.uicc.HwVSimIccCardProxy;
import com.android.internal.telephony.vsim.HwVSimConstants;
import com.android.internal.telephony.vsim.HwVSimController;
import com.android.internal.telephony.vsim.HwVSimEventReport;
import com.android.internal.telephony.vsim.HwVSimLog;
import com.android.internal.telephony.vsim.HwVSimModemAdapter;
import com.android.internal.telephony.vsim.HwVSimRequest;

public class HwVSimSwitchModeProcessor extends HwVSimProcessor {
    public static final String LOG_TAG = "HwVSimSwitchModeProcessor";
    protected Handler mHandler = this.mVSimController.getHandler();
    protected HwVSimController mVSimController;

    public HwVSimSwitchModeProcessor(HwVSimController controller, HwVSimModemAdapter modemAdapter, HwVSimRequest request) {
        super(modemAdapter, request);
        this.mVSimController = controller;
    }

    public void onEnter() {
        logd("onEnter");
        cmdSem_release();
        HwVSimRequest request = this.mRequest;
        if (request == null) {
            transitionToState(0);
            return;
        }
        request.createGotCardType(HwVSimModemAdapter.PHONE_COUNT);
        request.createCardTypes(HwVSimModemAdapter.PHONE_COUNT);
        this.mModemAdapter.getSimSlot(this, request, 2);
        this.mVSimController.setProcessAction(HwVSimController.ProcessAction.PROCESS_ACTION_SWITCHWORKMODE);
        this.mVSimController.setOnRadioAvaliable(this.mHandler, 83, null);
    }

    public void onExit() {
        logd("onExit");
        noticeApkToReEnable();
        this.mVSimController.updateUserPreferences();
        this.mVSimController.unSetOnRadioAvaliable(this.mHandler);
    }

    public Message obtainMessage(int what, Object obj) {
        return this.mVSimController.obtainMessage(what, obj);
    }

    public void transitionToState(int state) {
        this.mVSimController.transitionToState(state);
    }

    public void doProcessException(AsyncResult ar, HwVSimRequest request) {
        doSwitchModeProcessException(ar, request);
    }

    /* access modifiers changed from: protected */
    public void logd(String s) {
        HwVSimLog.VSimLogD(LOG_TAG, s);
    }

    public boolean processMessage(Message msg) {
        switch (msg.what) {
            case 54:
                onGetSimSlotDone(msg);
                return true;
            case HwVSimConstants.EVENT_SWITCH_COMMRIL_DONE:
                onSwitchCommrilDone(msg);
                return true;
            case HwVSimConstants.EVENT_QUERY_CARD_TYPE_DONE:
                onQueryCardTypeDone(msg);
                return true;
            default:
                return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onGetSimSlotDone(Message msg) {
        logd("onGetSimSlotDone");
        HwVSimEventReport.VSimEventInfoUtils.setCauseType(this.mVSimController.mEventInfo, 1);
        AsyncResult ar = (AsyncResult) msg.obj;
        if (isAsyncResultValid(ar)) {
            this.mModemAdapter.onGetSimSlotDone(this, ar);
            HwVSimRequest request = (HwVSimRequest) ar.userObj;
            if (request.getIsVSimOnM0()) {
                this.mModemAdapter.getAllCardTypes(this, this.mRequest);
            } else {
                notifyResult(request, false);
                transitionToState(0);
            }
        }
    }

    private void noticeApkToReEnable() {
        IccCard icccard = HwVSimPhoneFactory.getVSimPhone().getIccCard();
        if (icccard == null) {
            logd("icccard is null, return.");
            return;
        }
        IccCardConstants.State state = icccard.getState();
        if (IccCardConstants.State.ABSENT == state && (icccard instanceof HwVSimIccCardProxy)) {
            ((HwVSimIccCardProxy) icccard).broadcastIccStateChangedIntent(state);
        }
    }

    /* access modifiers changed from: protected */
    public void onQueryCardTypeDone(Message msg) {
        logd("onQueryCardTypeDone");
        HwVSimEventReport.VSimEventInfoUtils.setCauseType(this.mVSimController.mEventInfo, 1);
        AsyncResult ar = (AsyncResult) msg.obj;
        if (isAsyncResultValid(ar)) {
            this.mModemAdapter.onQueryCardTypeDone(this, ar);
            if (this.mRequest.isGotAllCardTypes()) {
                logd("onQueryCardTypeDone : isGotAllCardTypes");
                this.mModemAdapter.checkSwitchModeSimCondition(this, this.mRequest);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSwitchCommrilDone(Message msg) {
        logd("onSwitchCommrilDone");
        HwVSimEventReport.VSimEventInfoUtils.setCauseType(this.mVSimController.mEventInfo, 1);
        AsyncResult ar = (AsyncResult) msg.obj;
        if (isAsyncResultValid(ar)) {
            this.mModemAdapter.onSwitchCommrilDone(this, ar);
            transitionToState(12);
        }
    }

    /* access modifiers changed from: protected */
    public void cmdSem_release() {
        if (this.mVSimController != null) {
            this.mVSimController.cmdSem_release();
        }
    }
}
