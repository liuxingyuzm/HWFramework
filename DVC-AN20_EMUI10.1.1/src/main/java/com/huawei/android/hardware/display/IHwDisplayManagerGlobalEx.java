package com.huawei.android.hardware.display;

public interface IHwDisplayManagerGlobalEx {
    void checkVerificationResult(boolean z);

    void connectWifiDisplay(String str, HwWifiDisplayParameters hwWifiDisplayParameters);

    boolean sendWifiDisplayAction(String str);

    void startWifiDisplayScan(int i);
}
