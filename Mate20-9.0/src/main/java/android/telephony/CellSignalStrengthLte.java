package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

public final class CellSignalStrengthLte extends CellSignalStrength implements Parcelable {
    public static final Parcelable.Creator<CellSignalStrengthLte> CREATOR = new Parcelable.Creator<CellSignalStrengthLte>() {
        public CellSignalStrengthLte createFromParcel(Parcel in) {
            return new CellSignalStrengthLte(in);
        }

        public CellSignalStrengthLte[] newArray(int size) {
            return new CellSignalStrengthLte[size];
        }
    };
    private static final boolean DBG = false;
    private static final String LOG_TAG = "CellSignalStrengthLte";
    private int mCqi;
    private int mRsrp;
    private int mRsrq;
    private int mRssnr;
    private int mSignalStrength;
    private int mTimingAdvance;

    public CellSignalStrengthLte() {
        setDefaultValues();
    }

    public CellSignalStrengthLte(int signalStrength, int rsrp, int rsrq, int rssnr, int cqi, int timingAdvance) {
        this.mSignalStrength = signalStrength;
        this.mRsrp = rsrp;
        this.mRsrq = rsrq;
        this.mRssnr = rssnr;
        this.mCqi = cqi;
        this.mTimingAdvance = timingAdvance;
    }

    public CellSignalStrengthLte(CellSignalStrengthLte s) {
        copyFrom(s);
    }

    /* access modifiers changed from: protected */
    public void copyFrom(CellSignalStrengthLte s) {
        this.mSignalStrength = s.mSignalStrength;
        this.mRsrp = s.mRsrp;
        this.mRsrq = s.mRsrq;
        this.mRssnr = s.mRssnr;
        this.mCqi = s.mCqi;
        this.mTimingAdvance = s.mTimingAdvance;
    }

    public CellSignalStrengthLte copy() {
        return new CellSignalStrengthLte(this);
    }

    public void setDefaultValues() {
        this.mSignalStrength = Integer.MAX_VALUE;
        this.mRsrp = Integer.MAX_VALUE;
        this.mRsrq = Integer.MAX_VALUE;
        this.mRssnr = Integer.MAX_VALUE;
        this.mCqi = Integer.MAX_VALUE;
        this.mTimingAdvance = Integer.MAX_VALUE;
    }

    public int getLevel() {
        int levelRsrp;
        int levelRssnr;
        if (this.mRsrp == Integer.MAX_VALUE) {
            levelRsrp = 0;
        } else if (this.mRsrp >= -95) {
            levelRsrp = 4;
        } else if (this.mRsrp >= -105) {
            levelRsrp = 3;
        } else if (this.mRsrp >= -115) {
            levelRsrp = 2;
        } else {
            levelRsrp = 1;
        }
        if (this.mRssnr == Integer.MAX_VALUE) {
            levelRssnr = 0;
        } else if (this.mRssnr >= 45) {
            levelRssnr = 4;
        } else if (this.mRssnr >= 10) {
            levelRssnr = 3;
        } else if (this.mRssnr >= -30) {
            levelRssnr = 2;
        } else {
            levelRssnr = 1;
        }
        if (this.mRsrp == Integer.MAX_VALUE) {
            return levelRssnr;
        }
        if (this.mRssnr == Integer.MAX_VALUE) {
            return levelRsrp;
        }
        return levelRssnr < levelRsrp ? levelRssnr : levelRsrp;
    }

    public int getRsrq() {
        return this.mRsrq;
    }

    public int getRssnr() {
        return this.mRssnr;
    }

    public int getRsrp() {
        return this.mRsrp;
    }

    public int getCqi() {
        return this.mCqi;
    }

    public int getDbm() {
        return this.mRsrp;
    }

    public int getAsuLevel() {
        int lteDbm = getDbm();
        if (lteDbm == Integer.MAX_VALUE) {
            return 99;
        }
        if (lteDbm <= -140) {
            return 0;
        }
        if (lteDbm >= -43) {
            return 97;
        }
        return lteDbm + 140;
    }

    public int getTimingAdvance() {
        return this.mTimingAdvance;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mSignalStrength), Integer.valueOf(this.mRsrp), Integer.valueOf(this.mRsrq), Integer.valueOf(this.mRssnr), Integer.valueOf(this.mCqi), Integer.valueOf(this.mTimingAdvance)});
    }

    public boolean equals(Object o) {
        boolean z = false;
        try {
            CellSignalStrengthLte s = (CellSignalStrengthLte) o;
            if (o == null) {
                return false;
            }
            if (this.mSignalStrength == s.mSignalStrength && this.mRsrp == s.mRsrp && this.mRsrq == s.mRsrq && this.mRssnr == s.mRssnr && this.mCqi == s.mCqi && this.mTimingAdvance == s.mTimingAdvance) {
                z = true;
            }
            return z;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        return "CellSignalStrengthLte: ss=" + this.mSignalStrength + " rsrp=" + this.mRsrp + " rsrq=" + this.mRsrq + " rssnr=" + this.mRssnr + " cqi=" + this.mCqi + " ta=" + this.mTimingAdvance;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSignalStrength);
        int i = 1;
        dest.writeInt(this.mRsrp * (this.mRsrp != Integer.MAX_VALUE ? -1 : 1));
        int i2 = this.mRsrq;
        if (this.mRsrq != Integer.MAX_VALUE) {
            i = -1;
        }
        dest.writeInt(i2 * i);
        dest.writeInt(this.mRssnr);
        dest.writeInt(this.mCqi);
        dest.writeInt(this.mTimingAdvance);
    }

    private CellSignalStrengthLte(Parcel in) {
        this.mSignalStrength = in.readInt();
        this.mRsrp = in.readInt();
        if (this.mRsrp != Integer.MAX_VALUE) {
            this.mRsrp *= -1;
        }
        this.mRsrq = in.readInt();
        if (this.mRsrq != Integer.MAX_VALUE) {
            this.mRsrq *= -1;
        }
        this.mRssnr = in.readInt();
        this.mCqi = in.readInt();
        this.mTimingAdvance = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
