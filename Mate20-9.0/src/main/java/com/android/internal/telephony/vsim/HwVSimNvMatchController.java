package com.android.internal.telephony.vsim;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;

public class HwVSimNvMatchController extends Handler {
    private static final int DELAY_TIME = 2000;
    private static final int EVENT_RESTART_RILD_RETRY = 1;
    private static final String LOG_TAG = "VSimNvMatchController";
    private static final int MAX_RETRY_COUNT = 150;
    private static HwVSimNvMatchController sInstance = null;
    private static final Object sLock = new Object();
    private boolean mNeedRestartRildForNvMatch = false;
    private int mRetryCountForRestartRild = 0;
    private HwVSimController mVSimController;

    private HwVSimNvMatchController(HwVSimController vsimController) {
        this.mVSimController = vsimController;
    }

    public static HwVSimNvMatchController create(HwVSimController vsimController) {
        HwVSimNvMatchController hwVSimNvMatchController;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new HwVSimNvMatchController(vsimController);
                hwVSimNvMatchController = sInstance;
            } else {
                throw new RuntimeException("HwVSimNvMatchController already created");
            }
        }
        return hwVSimNvMatchController;
    }

    public static HwVSimNvMatchController getInstance() {
        HwVSimNvMatchController hwVSimNvMatchController;
        synchronized (sLock) {
            if (sInstance != null) {
                hwVSimNvMatchController = sInstance;
            } else {
                throw new RuntimeException("HwVSimNvMatchController not yet created");
            }
        }
        return hwVSimNvMatchController;
    }

    public void handleMessage(Message msg) {
        int i = msg.what;
        if (i != 1) {
            switch (i) {
                case HwVSimConstants.EVENT_JUDGE_RESTART_RILD_NV_MATCH:
                    onJudgeRestartRildNvMatch(msg);
                    return;
                case HwVSimConstants.EVENT_JUDGE_RESTART_RILD_NV_MATCH_TIMEOUT:
                    removeMessageAndStopListen();
                    return;
                default:
                    return;
            }
        } else {
            logd("EVENT_RESTART_RILD_RETRY, count = " + this.mRetryCountForRestartRild);
            restartRildIfIdle();
        }
    }

    public void restartRildIfIdle() {
        if (!getIfNeedRestartRildForNvMatch()) {
            logd("restartRildIfIdle: no need to restart rild for nv match, return!");
            return;
        }
        if (this.mVSimController.canProcessRestartRild()) {
            logd("restartRildIfIdle -> transitionToState(STATE_RESTART_RILD)");
            this.mVSimController.getHandler().sendEmptyMessage(84);
        } else {
            handleRestartRildDelay();
        }
    }

    private void handleRestartRildDelay() {
        this.mRetryCountForRestartRild++;
        removeMessages(1);
        if (this.mRetryCountForRestartRild > MAX_RETRY_COUNT) {
            logd("handleRestartRildDelay has retry 150 times, no try again");
            this.mRetryCountForRestartRild = 0;
            return;
        }
        sendMessageDelayed(obtainMessage(1), HwVSimConstants.GET_MODEM_SUPPORT_VERSION_INTERVAL);
    }

    private void onJudgeRestartRildNvMatch(Message msg) {
        logd("onJudgeRestartRildNvMatch");
        AsyncResult ar = (AsyncResult) msg.obj;
        if (ar != null && ar.exception == null && (ar.result instanceof int[])) {
            int response = ((int[]) ar.result)[0];
            logd("onJudgeRestartRildNvMatch, response = " + response);
            removeMessageAndStopListen();
            if (1 == response) {
                storeIfNeedRestartRildForNvMatch(true);
                restartRildIfIdle();
                return;
            }
            storeIfNeedRestartRildForNvMatch(false);
        }
    }

    /* access modifiers changed from: package-private */
    public void startNvMatchUnsolListener() {
        logd("startNvMatchUnsolListener");
        if (HwVSimUtilsInner.isPlatformNeedWaitNvMatchUnsol() && HwVSimUtils.isVSimEnabled()) {
            if (this.mVSimController.canStartNvMatchListener()) {
                this.mVSimController.setOnRestartRildNvMatch(0, this, 81, null);
                this.mVSimController.setOnRestartRildNvMatch(1, this, 81, null);
                sendEmptyMessageDelayed(82, HwVSimConstants.WAIT_FOR_NV_CFG_MATCH_TIMEOUT);
                this.mRetryCountForRestartRild = 0;
            } else {
                logd("startNvMatchUnsolListener, current state is :" + this.mVSimController.getCurrentState() + ", ignore.");
            }
        }
    }

    private void removeMessageAndStopListen() {
        removeMessages(81);
        removeMessages(82);
        this.mVSimController.unSetOnRestartRildNvMatch(0, this);
        this.mVSimController.unSetOnRestartRildNvMatch(1, this);
    }

    public void storeIfNeedRestartRildForNvMatch(boolean isNeed) {
        logd("storeIfNeedRestartRildForNvMatch, from " + this.mNeedRestartRildForNvMatch + " to " + isNeed);
        this.mNeedRestartRildForNvMatch = isNeed;
    }

    public boolean getIfNeedRestartRildForNvMatch() {
        logd("getIfNeedRestartRildForNvMatch: " + this.mNeedRestartRildForNvMatch);
        return this.mNeedRestartRildForNvMatch;
    }

    private void logd(String s) {
        HwVSimLog.VSimLogD(LOG_TAG, s);
    }
}
