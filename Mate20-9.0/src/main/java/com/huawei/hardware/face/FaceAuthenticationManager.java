package com.huawei.hardware.face;

import android.content.Context;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.security.keystore.AndroidKeyStoreProvider;
import android.util.Log;
import android.util.Slog;
import huawei.android.security.facerecognition.FaceRecognizeManagerImpl;
import java.security.Signature;
import java.util.HashMap;
import javax.crypto.Cipher;
import javax.crypto.Mac;

public class FaceAuthenticationManager {
    private static final int FACERECOGNITION_OFF = 0;
    private static final int FACERECOGNITION_ON = 1;
    public static final int FACE_ACQUIRED_GOOD = 0;
    public static final int FACE_ACQUIRED_INSUFFICIENT = 1;
    public static final int FACE_ACQUIRED_NOT_DETECTED = 12;
    public static final int FACE_ACQUIRED_POOR_GAZE = 11;
    public static final int FACE_ACQUIRED_TOO_BRIGHT = 2;
    public static final int FACE_ACQUIRED_TOO_CLOSE = 4;
    public static final int FACE_ACQUIRED_TOO_DARK = 3;
    public static final int FACE_ACQUIRED_TOO_FAR = 5;
    public static final int FACE_ACQUIRED_TOO_HIGH = 6;
    public static final int FACE_ACQUIRED_TOO_LEFT = 9;
    public static final int FACE_ACQUIRED_TOO_LOW = 7;
    public static final int FACE_ACQUIRED_TOO_MUCH_MOTION = 10;
    public static final int FACE_ACQUIRED_TOO_RIGHT = 8;
    public static final int FACE_ACQUIRED_VENDOR = 13;
    public static final int FACE_ACQUIRED_VENDOR_BASE = 1000;
    public static final int FACE_ERROR_BUSY = 13;
    public static final int FACE_ERROR_CANCELED = 5;
    public static final int FACE_ERROR_HW_NOT_PRESENT = 12;
    public static final int FACE_ERROR_HW_UNAVAILABLE = 1;
    public static final int FACE_ERROR_LOCKOUT = 7;
    public static final int FACE_ERROR_LOCKOUT_PERMANENT = 9;
    public static final int FACE_ERROR_NOT_ENROLLED = 11;
    public static final int FACE_ERROR_NO_SPACE = 4;
    public static final int FACE_ERROR_TIMEOUT = 3;
    public static final int FACE_ERROR_UNABLE_TO_PROCESS = 2;
    public static final int FACE_ERROR_UNABLE_TO_REMOVE = 6;
    public static final int FACE_ERROR_USER_CANCELED = 10;
    public static final int FACE_ERROR_VENDOR = 8;
    public static final int FACE_ERROR_VENDOR_BASE = 1000;
    private static final int MSG_ACQUIRED = 101;
    private static final int MSG_AUTHENTICATION_FAILED = 103;
    private static final int MSG_AUTHENTICATION_SUCCEEDED = 102;
    private static final int MSG_ENROLL_RESULT = 100;
    private static final int MSG_ERROR = 104;
    private static final int MSG_REMOVED = 105;
    private static final String TAG = "FaceManager";
    public static final String USE_FACE_AUTHENTICATION = "android.permission.USE_FACERECOGNITION";
    /* access modifiers changed from: private */
    public static final HashMap<Integer, Integer> mAcquiredCodeMap = new HashMap<Integer, Integer>() {
        {
            put(0, 0);
            put(1, 13);
            put(2, 13);
            put(3, 1);
            put(4, 1);
            put(5, 12);
            put(6, 5);
            put(7, 4);
            put(8, 9);
            put(9, 6);
            put(10, 8);
            put(11, 7);
            put(12, 13);
            put(13, 13);
            put(14, 13);
            put(15, 13);
            put(16, 13);
            put(17, 13);
            put(18, 13);
            put(19, 10);
            put(20, 13);
            put(21, 11);
            put(22, 11);
            put(23, 13);
            put(27, 13);
            put(28, 10);
            put(29, 13);
            put(30, 13);
            put(31, 13);
            put(32, 13);
        }
    };
    /* access modifiers changed from: private */
    public static final HashMap<Integer, Integer> mErrorCodeMap = new HashMap<Integer, Integer>() {
        {
            put(1, 8);
            put(2, 5);
            put(3, 8);
            put(4, 3);
            put(5, 2);
            put(6, 2);
            put(7, 4);
            put(8, 7);
            put(9, 8);
            put(10, 11);
            put(11, 2);
            put(13, 13);
        }
    };
    /* access modifiers changed from: private */
    public AuthenticationCallback mAuthenticationCallback;
    /* access modifiers changed from: private */
    public final Object mAuthenticationLock = new Object();
    private final Context mContext;
    /* access modifiers changed from: private */
    public CryptoObject mCryptoObject;
    /* access modifiers changed from: private */
    public FaceRecognizeManagerImpl mFaceManagerImpl;
    /* access modifiers changed from: private */
    public Handler mHandler;

    public static abstract class AuthenticationCallback {
        public void onAuthenticationError(int errorCode, CharSequence errString) {
        }

        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult result) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationAcquired(int acquireInfo) {
        }
    }

    public static class AuthenticationResult {
        private CryptoObject mCryptoObject;
        private Face mFace;
        private int mUserId;

        public AuthenticationResult(CryptoObject crypto, Face face, int userId) {
            this.mCryptoObject = crypto;
            this.mFace = face;
            this.mUserId = userId;
        }

        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }

        public Face getFace() {
            return this.mFace;
        }

        public int getUserId() {
            return this.mUserId;
        }
    }

    public static final class CryptoObject {
        private final Object mCrypto;

        public CryptoObject(Signature signature) {
            this.mCrypto = signature;
        }

        public CryptoObject(Cipher cipher) {
            this.mCrypto = cipher;
        }

        public CryptoObject(Mac mac) {
            this.mCrypto = mac;
        }

        public Signature getSignature() {
            if (this.mCrypto instanceof Signature) {
                return (Signature) this.mCrypto;
            }
            return null;
        }

        public Cipher getCipher() {
            if (this.mCrypto instanceof Cipher) {
                return (Cipher) this.mCrypto;
            }
            return null;
        }

        public Mac getMac() {
            if (this.mCrypto instanceof Mac) {
                return (Mac) this.mCrypto;
            }
            return null;
        }

        public long getOpId() {
            if (this.mCrypto != null) {
                return AndroidKeyStoreProvider.getKeyStoreOperationHandle(this.mCrypto);
            }
            return 0;
        }
    }

    public static abstract class EnrollmentCallback {
        public void onEnrollmentError(int errMsgId, CharSequence errString) {
        }

        public void onEnrollmentHelp(int helpMsgId, CharSequence helpString) {
        }

        public void onEnrollmentProgress(int remaining, long vendorMsg) {
        }
    }

    public static final class FaceRecognitionAbility {
        public int faceMode;
        public boolean isFaceRecognitionSupport;
        public int reserve;
        public int secureLevel;
    }

    public static abstract class LockoutResetCallback {
        public void onLockoutReset() {
        }
    }

    private class MyHandler extends Handler {
        private MyHandler(Context context) {
            super(context.getMainLooper());
        }

        private MyHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    sendAcquiredResult(((Long) msg.obj).longValue(), msg.arg1, msg.arg2);
                    return;
                case FaceAuthenticationManager.MSG_AUTHENTICATION_SUCCEEDED /*102*/:
                    sendAuthenticatedSucceeded((Face) msg.obj, msg.arg1);
                    return;
                case FaceAuthenticationManager.MSG_AUTHENTICATION_FAILED /*103*/:
                    sendAuthenticatedFailed();
                    return;
                case FaceAuthenticationManager.MSG_ERROR /*104*/:
                    sendErrorResult(((Long) msg.obj).longValue(), msg.arg1, msg.arg2);
                    return;
                default:
                    return;
            }
        }

        private void sendErrorResult(long deviceId, int errMsgId, int vendorCode) {
            int clientErrMsgId = errMsgId == 8 ? vendorCode + 1000 : errMsgId;
            synchronized (FaceAuthenticationManager.this.mAuthenticationLock) {
                if (FaceAuthenticationManager.this.mAuthenticationCallback != null) {
                    FaceAuthenticationManager.this.mAuthenticationCallback.onAuthenticationError(clientErrMsgId, FaceAuthenticationManager.this.getErrorString(errMsgId, vendorCode));
                    AuthenticationCallback unused = FaceAuthenticationManager.this.mAuthenticationCallback = null;
                }
            }
        }

        private void sendAuthenticatedSucceeded(Face face, int userId) {
            synchronized (FaceAuthenticationManager.this.mAuthenticationLock) {
                if (FaceAuthenticationManager.this.mAuthenticationCallback != null) {
                    FaceAuthenticationManager.this.mAuthenticationCallback.onAuthenticationSucceeded(new AuthenticationResult(FaceAuthenticationManager.this.mCryptoObject, face, userId));
                    AuthenticationCallback unused = FaceAuthenticationManager.this.mAuthenticationCallback = null;
                }
            }
        }

        private void sendAuthenticatedFailed() {
            synchronized (FaceAuthenticationManager.this.mAuthenticationLock) {
                if (FaceAuthenticationManager.this.mAuthenticationCallback != null) {
                    FaceAuthenticationManager.this.mAuthenticationCallback.onAuthenticationFailed();
                    AuthenticationCallback unused = FaceAuthenticationManager.this.mAuthenticationCallback = null;
                }
            }
        }

        private void sendAcquiredResult(long deviceId, int acquireInfo, int vendorCode) {
            String msg = FaceAuthenticationManager.this.getAcquiredString(acquireInfo, vendorCode);
            if (msg != null) {
                int clientInfo = acquireInfo == 13 ? vendorCode + 1000 : acquireInfo;
                synchronized (FaceAuthenticationManager.this.mAuthenticationLock) {
                    if (FaceAuthenticationManager.this.mAuthenticationCallback != null) {
                        FaceAuthenticationManager.this.mAuthenticationCallback.onAuthenticationAcquired(acquireInfo);
                        FaceAuthenticationManager.this.mAuthenticationCallback.onAuthenticationHelp(clientInfo, msg);
                    }
                }
            }
        }
    }

    private class OnAuthenticationCancelListener implements CancellationSignal.OnCancelListener {
        private CryptoObject mCrypto;

        OnAuthenticationCancelListener(CryptoObject crypto) {
            this.mCrypto = crypto;
        }

        public void onCancel() {
            FaceAuthenticationManager.this.cancelAuthentication(this.mCrypto);
        }
    }

    public static abstract class RemovalCallback {
        public void onRemovalError(Face face, int errMsgId, CharSequence errString) {
        }

        public void onRemovalSucceeded(Face face) {
        }
    }

    public FaceAuthenticationManager(Context context) {
        this.mContext = context;
        this.mHandler = new MyHandler(context);
        this.mAuthenticationCallback = null;
        this.mFaceManagerImpl = new FaceRecognizeManagerImpl(context, new FaceRecognizeManagerImpl.FaceRecognizeCallback() {
            public void onCallbackEvent(int reqId, int type, int code, int errorCode) {
                if (type == 2) {
                    synchronized (FaceAuthenticationManager.this.mAuthenticationLock) {
                        if (FaceAuthenticationManager.this.mAuthenticationCallback != null) {
                            if (1 == code) {
                                if (errorCode == 0) {
                                    FaceAuthenticationManager.this.mHandler.obtainMessage(FaceAuthenticationManager.MSG_AUTHENTICATION_SUCCEEDED, UserHandle.myUserId(), 0, null).sendToTarget();
                                } else if (3 == errorCode) {
                                    FaceAuthenticationManager.this.mHandler.obtainMessage(FaceAuthenticationManager.MSG_AUTHENTICATION_FAILED).sendToTarget();
                                } else {
                                    int vendorCode = errorCode;
                                    int error = 8;
                                    Integer result = (Integer) FaceAuthenticationManager.mErrorCodeMap.get(Integer.valueOf(errorCode));
                                    if (result != null) {
                                        error = result.intValue();
                                    }
                                    FaceAuthenticationManager.this.mHandler.obtainMessage(FaceAuthenticationManager.MSG_ERROR, error, vendorCode, 0L).sendToTarget();
                                }
                            } else if (3 == code) {
                                int vendorCode2 = errorCode;
                                int acquireInfo = 13;
                                Integer result2 = (Integer) FaceAuthenticationManager.mAcquiredCodeMap.get(Integer.valueOf(errorCode));
                                if (result2 != null) {
                                    acquireInfo = result2.intValue();
                                }
                                FaceAuthenticationManager.this.mHandler.obtainMessage(101, acquireInfo, vendorCode2, 0L).sendToTarget();
                            }
                        }
                    }
                    if (1 == code && FaceAuthenticationManager.this.mFaceManagerImpl.release() != 0) {
                        Log.w(FaceAuthenticationManager.TAG, "Authentication release failed.");
                    }
                }
            }
        });
    }

    public void authenticate(CryptoObject crypto, CancellationSignal cancel, int flags, AuthenticationCallback callback, Handler handler) {
        authenticate(crypto, cancel, flags, callback, handler, UserHandle.myUserId());
    }

    private void useHandler(Handler handler) {
        if (handler != null) {
            this.mHandler = new MyHandler(handler.getLooper());
        } else if (this.mHandler.getLooper() != this.mContext.getMainLooper()) {
            this.mHandler = new MyHandler(this.mContext.getMainLooper());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0045, code lost:
        if (r5.mFaceManagerImpl.init() == 0) goto L_0x0057;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0047, code lost:
        android.util.Log.w(TAG, "Authentication initialization failed.");
        r9.onAuthenticationError(1, getErrorString(1, 0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0056, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0057, code lost:
        r5.mCryptoObject = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0059, code lost:
        if (r6 == null) goto L_0x0060;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005b, code lost:
        r1 = r6.getOpId();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0060, code lost:
        r1 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0062, code lost:
        r5.mFaceManagerImpl.authenticate(r1, r8, null);
     */
    public void authenticate(CryptoObject crypto, CancellationSignal cancel, int flags, AuthenticationCallback callback, Handler handler, int userId) {
        if (callback != null) {
            if (cancel != null) {
                if (cancel.isCanceled()) {
                    Log.w(TAG, "authentication already canceled");
                    return;
                }
                cancel.setOnCancelListener(new OnAuthenticationCancelListener(crypto));
            }
            if (this.mFaceManagerImpl != null) {
                useHandler(handler);
                synchronized (this.mAuthenticationLock) {
                    if (this.mAuthenticationCallback != null) {
                        Log.w(TAG, "Authentication is in running, do not accept dup request.");
                        callback.onAuthenticationError(1, getErrorString(1, 0));
                        return;
                    }
                    this.mAuthenticationCallback = callback;
                }
            }
            return;
        }
        throw new IllegalArgumentException("Must supply an authentication callback");
    }

    public boolean hasEnrolledFace() {
        boolean z = false;
        if (this.mFaceManagerImpl == null) {
            return false;
        }
        if (this.mFaceManagerImpl.getEnrolledFaceIDs().length > 0) {
            z = true;
        }
        return z;
    }

    public int getEnrolledFaceID() {
        if (this.mFaceManagerImpl == null) {
            return 0;
        }
        int[] faceIds = this.mFaceManagerImpl.getEnrolledFaceIDs();
        if (faceIds.length == 0) {
            return 0;
        }
        return faceIds[0];
    }

    public boolean isHardwareDetected() {
        if (this.mFaceManagerImpl == null || (this.mFaceManagerImpl.getHardwareSupportType() & 1) == 0) {
            return false;
        }
        return true;
    }

    public FaceRecognitionAbility getFaceRecognitionAbility() {
        FaceRecognizeManagerImpl.FaceRecognitionAbility ability = this.mFaceManagerImpl.getFaceRecognitionAbility();
        if (ability == null) {
            return null;
        }
        FaceRecognitionAbility faceAbility = new FaceRecognitionAbility();
        faceAbility.isFaceRecognitionSupport = ability.isFaceRecognitionSupport;
        faceAbility.faceMode = ability.faceMode;
        faceAbility.secureLevel = ability.secureLevel;
        faceAbility.reserve = ability.reserve;
        return faceAbility;
    }

    /* access modifiers changed from: private */
    public void cancelAuthentication(CryptoObject cryptoObject) {
        if (this.mFaceManagerImpl != null) {
            this.mFaceManagerImpl.cancelAuthenticate(this.mCryptoObject != null ? this.mCryptoObject.getOpId() : 0);
        }
    }

    /* access modifiers changed from: private */
    public String getErrorString(int errMsg, int vendorCode) {
        switch (errMsg) {
            case 1:
                return "face_error_hw_not_available";
            case 2:
                return "face_error_unable_to_process";
            case 3:
                return "face_error_timeout";
            case 4:
                return "face_error_no_space";
            case 5:
                return "face_error_canceled";
            case 7:
                return "face_error_lockout";
            case 8:
                return "face_error_vendor: code " + String.valueOf(vendorCode);
            case 9:
                return "face_error_lockout_permanent";
            case 11:
                return "face_error_not_enrolled";
            case 12:
                return "face_error_hw_not_present";
            default:
                Slog.w(TAG, "Invalid error message: " + errMsg + ", " + vendorCode);
                return null;
        }
    }

    /* access modifiers changed from: private */
    public String getAcquiredString(int acquireInfo, int vendorCode) {
        switch (acquireInfo) {
            case 0:
                return null;
            case 1:
                return "face_acquired_insufficient";
            case 2:
                return "face_acquired_too_bright";
            case 3:
                return "face_acquired_too_dark";
            case 4:
                return "face_acquired_too_close";
            case 5:
                return "face_acquired_too_far";
            case 6:
                return "face_acquired_too_high";
            case 7:
                return "face_acquired_too_low";
            case 8:
                return "face_acquired_too_right";
            case 9:
                return "face_acquired_too_left";
            case 10:
                return "face_acquired_too_much_motion";
            case 11:
                return "face_acquired_poor_gaze";
            case 12:
                return "face_acquired_not_detected";
            case 13:
                return "face_acquired_vendor: code " + String.valueOf(vendorCode);
            default:
                Slog.w(TAG, "Invalid acquired message: " + acquireInfo + ", " + vendorCode);
                return null;
        }
    }
}
