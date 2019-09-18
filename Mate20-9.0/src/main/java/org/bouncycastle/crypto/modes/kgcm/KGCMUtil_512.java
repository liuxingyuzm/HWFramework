package org.bouncycastle.crypto.modes.kgcm;

import org.bouncycastle.math.raw.Interleave;

public class KGCMUtil_512 {
    public static final int SIZE = 8;

    public static void add(long[] jArr, long[] jArr2, long[] jArr3) {
        jArr3[0] = jArr[0] ^ jArr2[0];
        jArr3[1] = jArr[1] ^ jArr2[1];
        jArr3[2] = jArr[2] ^ jArr2[2];
        jArr3[3] = jArr[3] ^ jArr2[3];
        jArr3[4] = jArr[4] ^ jArr2[4];
        jArr3[5] = jArr[5] ^ jArr2[5];
        jArr3[6] = jArr[6] ^ jArr2[6];
        jArr3[7] = jArr2[7] ^ jArr[7];
    }

    public static void copy(long[] jArr, long[] jArr2) {
        jArr2[0] = jArr[0];
        jArr2[1] = jArr[1];
        jArr2[2] = jArr[2];
        jArr2[3] = jArr[3];
        jArr2[4] = jArr[4];
        jArr2[5] = jArr[5];
        jArr2[6] = jArr[6];
        jArr2[7] = jArr[7];
    }

    public static boolean equal(long[] jArr, long[] jArr2) {
        return ((jArr2[7] ^ jArr[7]) | ((((((((jArr[0] ^ jArr2[0]) | 0) | (jArr[1] ^ jArr2[1])) | (jArr[2] ^ jArr2[2])) | (jArr[3] ^ jArr2[3])) | (jArr[4] ^ jArr2[4])) | (jArr[5] ^ jArr2[5])) | (jArr[6] ^ jArr2[6]))) == 0;
    }

    public static void multiply(long[] jArr, long[] jArr2, long[] jArr3) {
        int i = 0;
        long j = jArr2[0];
        long j2 = jArr2[1];
        long j3 = jArr2[2];
        long j4 = jArr2[3];
        long j5 = jArr2[4];
        long j6 = 0;
        int i2 = 0;
        long j7 = j;
        long j8 = j2;
        long j9 = j3;
        long j10 = j4;
        long j11 = j5;
        long j12 = jArr2[5];
        long j13 = jArr2[6];
        long j14 = jArr2[7];
        long j15 = 0;
        long j16 = 0;
        long j17 = 0;
        long j18 = 0;
        long j19 = 0;
        long j20 = 0;
        long j21 = 0;
        long j22 = 0;
        while (i2 < 8) {
            long j23 = jArr[i2];
            long j24 = jArr[i2 + 1];
            long j25 = j15;
            long j26 = j8;
            long j27 = j14;
            j14 = j13;
            j13 = j12;
            j12 = j11;
            j11 = j10;
            j10 = j9;
            j9 = j26;
            for (int i3 = i; i3 < 64; i3++) {
                long j28 = -(j23 & 1);
                j23 >>>= 1;
                j6 ^= j7 & j28;
                long j29 = j18 ^ (j11 & j28);
                long j30 = -(j24 & 1);
                j24 >>>= 1;
                j16 = (j16 ^ (j9 & j28)) ^ (j7 & j30);
                j17 = (j17 ^ (j10 & j28)) ^ (j9 & j30);
                j19 = (j19 ^ (j12 & j28)) ^ (j11 & j30);
                j20 = (j20 ^ (j13 & j28)) ^ (j12 & j30);
                j21 = (j21 ^ (j14 & j28)) ^ (j13 & j30);
                long j31 = (j22 ^ (j27 & j28)) ^ (j14 & j30);
                j25 ^= j27 & j30;
                long j32 = j27 >> 63;
                j27 = (j27 << 1) | (j14 >>> 63);
                j14 = (j14 << 1) | (j13 >>> 63);
                j13 = (j13 << 1) | (j12 >>> 63);
                j12 = (j12 << 1) | (j11 >>> 63);
                j11 = (j11 << 1) | (j10 >>> 63);
                j10 = (j10 << 1) | (j9 >>> 63);
                j9 = (j9 << 1) | (j7 >>> 63);
                j7 = (j7 << 1) ^ (j32 & 293);
                j18 = j29 ^ (j10 & j30);
                j22 = j31;
            }
            long j33 = ((j7 ^ (j27 >>> 62)) ^ (j27 >>> 59)) ^ (j27 >>> 56);
            j7 = ((j27 ^ (j27 << 2)) ^ (j27 << 5)) ^ (j27 << 8);
            i2 += 2;
            j8 = j33;
            j15 = j25;
            i = 0;
        }
        jArr3[0] = j6 ^ ((((j15 << 2) ^ j15) ^ (j15 << 5)) ^ (j15 << 8));
        jArr3[1] = (((j15 >>> 62) ^ (j15 >>> 59)) ^ (j15 >>> 56)) ^ j16;
        jArr3[2] = j17;
        jArr3[3] = j18;
        jArr3[4] = j19;
        jArr3[5] = j20;
        jArr3[6] = j21;
        jArr3[7] = j22;
    }

    public static void multiplyX(long[] jArr, long[] jArr2) {
        long j = jArr[0];
        long j2 = jArr[1];
        long j3 = jArr[2];
        long j4 = jArr[3];
        long j5 = jArr[4];
        long j6 = jArr[5];
        long j7 = jArr[6];
        long j8 = jArr[7];
        jArr2[0] = (j << 1) ^ ((j8 >> 63) & 293);
        jArr2[1] = (j2 << 1) | (j >>> 63);
        jArr2[2] = (j3 << 1) | (j2 >>> 63);
        jArr2[3] = (j4 << 1) | (j3 >>> 63);
        jArr2[4] = (j5 << 1) | (j4 >>> 63);
        jArr2[5] = (j6 << 1) | (j5 >>> 63);
        jArr2[6] = (j7 << 1) | (j6 >>> 63);
        jArr2[7] = (j8 << 1) | (j7 >>> 63);
    }

    public static void multiplyX8(long[] jArr, long[] jArr2) {
        long j = jArr[0];
        long j2 = jArr[1];
        long j3 = jArr[2];
        long j4 = jArr[3];
        long j5 = jArr[4];
        long j6 = jArr[5];
        long j7 = jArr[6];
        long j8 = jArr[7];
        long j9 = j8 >>> 56;
        jArr2[0] = ((((j << 8) ^ j9) ^ (j9 << 2)) ^ (j9 << 5)) ^ (j9 << 8);
        jArr2[1] = (j2 << 8) | (j >>> 56);
        jArr2[2] = (j3 << 8) | (j2 >>> 56);
        jArr2[3] = (j4 << 8) | (j3 >>> 56);
        jArr2[4] = (j5 << 8) | (j4 >>> 56);
        jArr2[5] = (j6 << 8) | (j5 >>> 56);
        jArr2[6] = (j7 << 8) | (j6 >>> 56);
        jArr2[7] = (j8 << 8) | (j7 >>> 56);
    }

    public static void one(long[] jArr) {
        jArr[0] = 1;
        jArr[1] = 0;
        jArr[2] = 0;
        jArr[3] = 0;
        jArr[4] = 0;
        jArr[5] = 0;
        jArr[6] = 0;
        jArr[7] = 0;
    }

    public static void square(long[] jArr, long[] jArr2) {
        int i = 16;
        long[] jArr3 = new long[16];
        for (int i2 = 0; i2 < 8; i2++) {
            Interleave.expand64To128(jArr[i2], jArr3, i2 << 1);
        }
        while (true) {
            i--;
            if (i >= 8) {
                long j = jArr3[i];
                int i3 = i - 8;
                jArr3[i3] = jArr3[i3] ^ ((((j << 2) ^ j) ^ (j << 5)) ^ (j << 8));
                int i4 = i3 + 1;
                jArr3[i4] = ((j >>> 56) ^ ((j >>> 62) ^ (j >>> 59))) ^ jArr3[i4];
            } else {
                copy(jArr3, jArr2);
                return;
            }
        }
    }

    public static void x(long[] jArr) {
        jArr[0] = 2;
        jArr[1] = 0;
        jArr[2] = 0;
        jArr[3] = 0;
        jArr[4] = 0;
        jArr[5] = 0;
        jArr[6] = 0;
        jArr[7] = 0;
    }

    public static void zero(long[] jArr) {
        jArr[0] = 0;
        jArr[1] = 0;
        jArr[2] = 0;
        jArr[3] = 0;
        jArr[4] = 0;
        jArr[5] = 0;
        jArr[6] = 0;
        jArr[7] = 0;
    }
}
