package com.android.server.am;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import com.android.server.wm.ConfigurationContainer;

public abstract class AbsActivityRecord extends ConfigurationContainer {
    /* access modifiers changed from: protected */
    public void initSplitMode(Intent intent) {
    }

    /* access modifiers changed from: protected */
    public boolean isSplitMode() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isForceRotationMode(String packageName, Intent intent) {
        return false;
    }

    /* access modifiers changed from: protected */
    public int overrideRealConfigChanged(ActivityInfo info) {
        return info.getRealConfigChanged();
    }

    /* access modifiers changed from: protected */
    public boolean isSplitBaseActivity() {
        return false;
    }
}
