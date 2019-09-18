package com.android.server.location;

import android.app.PendingIntent;
import android.location.GeoFenceParams;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class HwGeoFencerBase {
    private static final String TAG = "HwGeoFencerBase";
    private HashMap<PendingIntent, GeoFenceParams> mGeoFences = new HashMap<>();

    /* access modifiers changed from: protected */
    public abstract boolean start(GeoFenceParams geoFenceParams);

    /* access modifiers changed from: protected */
    public abstract boolean stop(PendingIntent pendingIntent);

    public void add(double latitude, double longitude, float radius, long expiration, PendingIntent intent, String packageName) {
        GeoFenceParams geoFenceParams = new GeoFenceParams(latitude, longitude, radius, expiration, intent, packageName);
        add(geoFenceParams);
    }

    public void add(GeoFenceParams geoFence) {
        synchronized (this.mGeoFences) {
            this.mGeoFences.put(geoFence.mIntent, geoFence);
        }
        if (!start(geoFence)) {
            synchronized (this.mGeoFences) {
                this.mGeoFences.remove(geoFence.mIntent);
            }
        }
    }

    public void remove(PendingIntent intent) {
        remove(intent, false);
    }

    public void remove(PendingIntent intent, boolean localOnly) {
        GeoFenceParams geoFence;
        synchronized (this.mGeoFences) {
            geoFence = this.mGeoFences.remove(intent);
        }
        if (geoFence != null && !localOnly && !stop(intent)) {
            synchronized (this.mGeoFences) {
                this.mGeoFences.put(geoFence.mIntent, geoFence);
            }
        }
    }

    public int getNumbOfGeoFences() {
        return this.mGeoFences.size();
    }

    public Collection<GeoFenceParams> getAllGeoFences() {
        return this.mGeoFences.values();
    }

    public GeoFenceParams getGeoFence(PendingIntent intent) {
        return this.mGeoFences.get(intent);
    }

    public boolean hasCaller(int uid) {
        for (GeoFenceParams alert : this.mGeoFences.values()) {
            if (alert.mUid == uid) {
                return true;
            }
        }
        return false;
    }

    public void removeCaller(int uid) {
        ArrayList<PendingIntent> removedFences = null;
        for (GeoFenceParams alert : this.mGeoFences.values()) {
            if (alert.mUid == uid) {
                if (removedFences == null) {
                    removedFences = new ArrayList<>();
                }
                removedFences.add(alert.mIntent);
            }
        }
        if (removedFences != null) {
            for (int i = removedFences.size() - 1; i >= 0; i--) {
                this.mGeoFences.remove(removedFences.get(i));
            }
        }
    }

    public void transferService(HwGeoFencerBase geofencer) {
        for (GeoFenceParams alert : geofencer.mGeoFences.values()) {
            geofencer.stop(alert.mIntent);
            add(alert);
        }
    }

    public void dump(PrintWriter pw, String prefix) {
        String localPrefix = prefix;
        if (this.mGeoFences.size() > 0) {
            pw.println(localPrefix + "  GeoFences:");
            String localPrefix2 = localPrefix + "    ";
            for (Map.Entry<PendingIntent, GeoFenceParams> i : this.mGeoFences.entrySet()) {
                pw.println(localPrefix2 + i.getKey() + ":");
                i.getValue().dump(pw, localPrefix2);
            }
        }
    }
}
