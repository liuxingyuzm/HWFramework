package com.android.server.wifi;

public interface IHwWifiSettingsStoreEx {
    void changeAirplaneModeRadios(boolean z, boolean z2);

    int getLocationMode();

    void resetAirplaneModeRadios();
}
