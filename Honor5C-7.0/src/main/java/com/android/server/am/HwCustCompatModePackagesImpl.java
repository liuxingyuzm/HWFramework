package com.android.server.am;

import android.os.SystemProperties;

public class HwCustCompatModePackagesImpl extends HwCustCompatModePackages {
    private static final boolean isRogSupported;
    private boolean mLowPowerDisplayMode;

    static {
        isRogSupported = SystemProperties.getBoolean("ro.config.ROG", false);
    }

    public HwCustCompatModePackagesImpl() {
        boolean z = false;
        if (SystemProperties.getInt("persist.sys.res.level", 0) > 0) {
            z = true;
        }
        this.mLowPowerDisplayMode = z;
    }

    public boolean isLowPowerDisplayMode() {
        if (isRogSupported && this.mLowPowerDisplayMode) {
            return this.mLowPowerDisplayMode;
        }
        return super.isLowPowerDisplayMode();
    }
}
