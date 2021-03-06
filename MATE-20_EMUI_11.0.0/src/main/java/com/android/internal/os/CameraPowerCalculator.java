package com.android.internal.os;

import android.os.BatteryStats;

public class CameraPowerCalculator extends PowerCalculator {
    private final double mCameraPowerOnAvg;

    public CameraPowerCalculator(PowerProfile profile) {
        this.mCameraPowerOnAvg = profile.getAveragePower(PowerProfile.POWER_CAMERA);
    }

    @Override // com.android.internal.os.PowerCalculator
    public void calculateApp(BatterySipper app, BatteryStats.Uid u, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        BatteryStats.Timer timer = u.getCameraTurnedOnTimer();
        if (timer != null) {
            long totalTime = timer.getTotalTimeLocked(rawRealtimeUs, statsType) / 1000;
            app.cameraTimeMs = totalTime;
            app.cameraPowerMah = (((double) totalTime) * this.mCameraPowerOnAvg) / 3600000.0d;
            return;
        }
        app.cameraTimeMs = 0;
        app.cameraPowerMah = 0.0d;
    }
}
