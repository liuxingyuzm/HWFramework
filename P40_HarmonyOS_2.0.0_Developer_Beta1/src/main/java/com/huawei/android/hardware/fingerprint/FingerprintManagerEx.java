package com.huawei.android.hardware.fingerprint;

import android.content.Context;
import android.hardware.fingerprint.Fingerprint;
import android.hardware.fingerprint.FingerprintManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class FingerprintManagerEx {
    private static final String TAG = "FingerprintManagerEx";
    private FingerprintManager mFingerprintManager;

    public interface RemovalCallback {
        void onRemovalError(FingerprintEx fingerprintEx, int i, CharSequence charSequence);

        void onRemovalSucceeded(FingerprintEx fingerprintEx);

        void onRemovalSucceeded(FingerprintEx fingerprintEx, int i);
    }

    public FingerprintManagerEx(Context context) {
        if (context != null) {
            this.mFingerprintManager = (FingerprintManager) context.getSystemService("fingerprint");
            return;
        }
        throw new NullPointerException("The params context cannot be null.");
    }

    public void remove(FingerprintEx fingerprintEx, int userId, final RemovalCallback removalCallback) {
        if (fingerprintEx != null) {
            FingerprintManager fingerprintManager = this.mFingerprintManager;
            if (fingerprintManager == null) {
                Log.e(TAG, "call remove() Error:the service Context.FINGERPRINT_SERVICE is not supported.");
            } else {
                fingerprintManager.remove(fingerprintEx.getFingerprint(), userId, new FingerprintManager.RemovalCallback() {
                    /* class com.huawei.android.hardware.fingerprint.FingerprintManagerEx.AnonymousClass1 */

                    public void onRemovalSucceeded(Fingerprint fingerprint, int remaining) {
                        RemovalCallback removalCallback = removalCallback;
                        if (removalCallback != null) {
                            removalCallback.onRemovalSucceeded(new FingerprintEx(fingerprint), remaining);
                        }
                    }

                    public void onRemovalSucceeded(Fingerprint fingerprint) {
                        RemovalCallback removalCallback = removalCallback;
                        if (removalCallback != null) {
                            removalCallback.onRemovalSucceeded(new FingerprintEx(fingerprint));
                        }
                    }

                    public void onRemovalError(Fingerprint fp, int errMsgId, CharSequence errString) {
                        RemovalCallback removalCallback = removalCallback;
                        if (removalCallback != null) {
                            removalCallback.onRemovalError(new FingerprintEx(fp), errMsgId, errString);
                        }
                    }
                });
            }
        } else {
            throw new NullPointerException("The params fingerprintEx cannot be null.");
        }
    }

    public void rename(int fpId, int userId, String newName) {
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        if (fingerprintManager == null) {
            Log.e(TAG, "call rename() Error:the service Context.FINGERPRINT_SERVICE is not supported.");
        } else {
            fingerprintManager.rename(fpId, userId, newName);
        }
    }

    public List<FingerprintEx> getEnrolledFingerprints(int userId) {
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        if (fingerprintManager == null) {
            Log.e(TAG, "getEnrolledFingerprints() Error:the service Context.FINGERPRINT_SERVICE is not supported.");
            return null;
        }
        List<Fingerprint> list = fingerprintManager.getEnrolledFingerprints(userId);
        List<FingerprintEx> result = new ArrayList<>();
        if (list == null || list.size() == 0) {
            return result;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            result.add(new FingerprintEx(list.get(i)));
        }
        return result;
    }

    public static List<Fingerprint> getEnrolledFingerprints(FingerprintManager fingerprintManager, int userId) {
        return fingerprintManager.getEnrolledFingerprints();
    }

    public long preEnroll() {
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        if (fingerprintManager != null) {
            return fingerprintManager.preEnroll();
        }
        Log.e(TAG, "call preEnroll() Error:the service Context.FINGERPRINT_SERVICE is not supported.");
        return 0;
    }
}
