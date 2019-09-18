package com.android.org.bouncycastle.math.ec;

import com.android.org.bouncycastle.math.raw.Mod;
import com.android.org.bouncycastle.math.raw.Nat;
import com.android.org.bouncycastle.util.Arrays;
import com.android.org.bouncycastle.util.BigIntegers;
import java.math.BigInteger;
import java.util.Random;

public abstract class ECFieldElement implements ECConstants {

    public static class F2m extends ECFieldElement {
        public static final int GNB = 1;
        public static final int PPB = 3;
        public static final int TPB = 2;
        private int[] ks;
        private int m;
        private int representation;
        private LongArray x;

        public F2m(int m2, int k1, int k2, int k3, BigInteger x2) {
            if (x2 == null || x2.signum() < 0 || x2.bitLength() > m2) {
                throw new IllegalArgumentException("x value invalid in F2m field element");
            }
            if (k2 == 0 && k3 == 0) {
                this.representation = 2;
                this.ks = new int[]{k1};
            } else if (k2 >= k3) {
                throw new IllegalArgumentException("k2 must be smaller than k3");
            } else if (k2 > 0) {
                this.representation = 3;
                this.ks = new int[]{k1, k2, k3};
            } else {
                throw new IllegalArgumentException("k2 must be larger than 0");
            }
            this.m = m2;
            this.x = new LongArray(x2);
        }

        public F2m(int m2, int k, BigInteger x2) {
            this(m2, k, 0, 0, x2);
        }

        private F2m(int m2, int[] ks2, LongArray x2) {
            this.m = m2;
            this.representation = ks2.length == 1 ? 2 : 3;
            this.ks = ks2;
            this.x = x2;
        }

        public int bitLength() {
            return this.x.degree();
        }

        public boolean isOne() {
            return this.x.isOne();
        }

        public boolean isZero() {
            return this.x.isZero();
        }

        public boolean testBitZero() {
            return this.x.testBitZero();
        }

        public BigInteger toBigInteger() {
            return this.x.toBigInteger();
        }

        public String getFieldName() {
            return "F2m";
        }

        public int getFieldSize() {
            return this.m;
        }

        public static void checkFieldElements(ECFieldElement a, ECFieldElement b) {
            if (!(a instanceof F2m) || !(b instanceof F2m)) {
                throw new IllegalArgumentException("Field elements are not both instances of ECFieldElement.F2m");
            }
            F2m aF2m = (F2m) a;
            F2m bF2m = (F2m) b;
            if (aF2m.representation != bF2m.representation) {
                throw new IllegalArgumentException("One of the F2m field elements has incorrect representation");
            } else if (aF2m.m != bF2m.m || !Arrays.areEqual(aF2m.ks, bF2m.ks)) {
                throw new IllegalArgumentException("Field elements are not elements of the same field F2m");
            }
        }

        public ECFieldElement add(ECFieldElement b) {
            LongArray iarrClone = (LongArray) this.x.clone();
            iarrClone.addShiftedByWords(((F2m) b).x, 0);
            return new F2m(this.m, this.ks, iarrClone);
        }

        public ECFieldElement addOne() {
            return new F2m(this.m, this.ks, this.x.addOne());
        }

        public ECFieldElement subtract(ECFieldElement b) {
            return add(b);
        }

        public ECFieldElement multiply(ECFieldElement b) {
            return new F2m(this.m, this.ks, this.x.modMultiply(((F2m) b).x, this.m, this.ks));
        }

        public ECFieldElement multiplyMinusProduct(ECFieldElement b, ECFieldElement x2, ECFieldElement y) {
            return multiplyPlusProduct(b, x2, y);
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v4, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v3, resolved type: com.android.org.bouncycastle.math.ec.LongArray} */
        /* JADX WARNING: Multi-variable type inference failed */
        public ECFieldElement multiplyPlusProduct(ECFieldElement b, ECFieldElement x2, ECFieldElement y) {
            LongArray ax = this.x;
            LongArray bx = ((F2m) b).x;
            LongArray xx = ((F2m) x2).x;
            LongArray yx = ((F2m) y).x;
            LongArray ab = ax.multiply(bx, this.m, this.ks);
            LongArray xy = xx.multiply(yx, this.m, this.ks);
            if (ab == ax || ab == bx) {
                ab = ab.clone();
            }
            ab.addShiftedByWords(xy, 0);
            ab.reduce(this.m, this.ks);
            return new F2m(this.m, this.ks, ab);
        }

        public ECFieldElement divide(ECFieldElement b) {
            return multiply(b.invert());
        }

        public ECFieldElement negate() {
            return this;
        }

        public ECFieldElement square() {
            return new F2m(this.m, this.ks, this.x.modSquare(this.m, this.ks));
        }

        public ECFieldElement squareMinusProduct(ECFieldElement x2, ECFieldElement y) {
            return squarePlusProduct(x2, y);
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v4, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: com.android.org.bouncycastle.math.ec.LongArray} */
        /* JADX WARNING: Multi-variable type inference failed */
        public ECFieldElement squarePlusProduct(ECFieldElement x2, ECFieldElement y) {
            LongArray ax = this.x;
            LongArray xx = ((F2m) x2).x;
            LongArray yx = ((F2m) y).x;
            LongArray aa = ax.square(this.m, this.ks);
            LongArray xy = xx.multiply(yx, this.m, this.ks);
            if (aa == ax) {
                aa = aa.clone();
            }
            aa.addShiftedByWords(xy, 0);
            aa.reduce(this.m, this.ks);
            return new F2m(this.m, this.ks, aa);
        }

        public ECFieldElement squarePow(int pow) {
            return pow < 1 ? this : new F2m(this.m, this.ks, this.x.modSquareN(pow, this.m, this.ks));
        }

        public ECFieldElement invert() {
            return new F2m(this.m, this.ks, this.x.modInverse(this.m, this.ks));
        }

        public ECFieldElement sqrt() {
            return (this.x.isZero() || this.x.isOne()) ? this : squarePow(this.m - 1);
        }

        public int getRepresentation() {
            return this.representation;
        }

        public int getM() {
            return this.m;
        }

        public int getK1() {
            return this.ks[0];
        }

        public int getK2() {
            if (this.ks.length >= 2) {
                return this.ks[1];
            }
            return 0;
        }

        public int getK3() {
            if (this.ks.length >= 3) {
                return this.ks[2];
            }
            return 0;
        }

        public boolean equals(Object anObject) {
            boolean z = true;
            if (anObject == this) {
                return true;
            }
            if (!(anObject instanceof F2m)) {
                return false;
            }
            F2m b = (F2m) anObject;
            if (this.m != b.m || this.representation != b.representation || !Arrays.areEqual(this.ks, b.ks) || !this.x.equals(b.x)) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return (this.x.hashCode() ^ this.m) ^ Arrays.hashCode(this.ks);
        }
    }

    public static class Fp extends ECFieldElement {
        BigInteger q;
        BigInteger r;
        BigInteger x;

        static BigInteger calculateResidue(BigInteger p) {
            int bitLength = p.bitLength();
            if (bitLength < 96 || p.shiftRight(bitLength - 64).longValue() != -1) {
                return null;
            }
            return ONE.shiftLeft(bitLength).subtract(p);
        }

        public Fp(BigInteger q2, BigInteger x2) {
            this(q2, calculateResidue(q2), x2);
        }

        Fp(BigInteger q2, BigInteger r2, BigInteger x2) {
            if (x2 == null || x2.signum() < 0 || x2.compareTo(q2) >= 0) {
                throw new IllegalArgumentException("x value invalid in Fp field element");
            }
            this.q = q2;
            this.r = r2;
            this.x = x2;
        }

        public BigInteger toBigInteger() {
            return this.x;
        }

        public String getFieldName() {
            return "Fp";
        }

        public int getFieldSize() {
            return this.q.bitLength();
        }

        public BigInteger getQ() {
            return this.q;
        }

        public ECFieldElement add(ECFieldElement b) {
            return new Fp(this.q, this.r, modAdd(this.x, b.toBigInteger()));
        }

        public ECFieldElement addOne() {
            BigInteger x2 = this.x.add(ECConstants.ONE);
            if (x2.compareTo(this.q) == 0) {
                x2 = ECConstants.ZERO;
            }
            return new Fp(this.q, this.r, x2);
        }

        public ECFieldElement subtract(ECFieldElement b) {
            return new Fp(this.q, this.r, modSubtract(this.x, b.toBigInteger()));
        }

        public ECFieldElement multiply(ECFieldElement b) {
            return new Fp(this.q, this.r, modMult(this.x, b.toBigInteger()));
        }

        public ECFieldElement multiplyMinusProduct(ECFieldElement b, ECFieldElement x2, ECFieldElement y) {
            BigInteger ax = this.x;
            BigInteger bx = b.toBigInteger();
            BigInteger xx = x2.toBigInteger();
            BigInteger yx = y.toBigInteger();
            return new Fp(this.q, this.r, modReduce(ax.multiply(bx).subtract(xx.multiply(yx))));
        }

        public ECFieldElement multiplyPlusProduct(ECFieldElement b, ECFieldElement x2, ECFieldElement y) {
            BigInteger ax = this.x;
            BigInteger bx = b.toBigInteger();
            BigInteger xx = x2.toBigInteger();
            BigInteger yx = y.toBigInteger();
            return new Fp(this.q, this.r, modReduce(ax.multiply(bx).add(xx.multiply(yx))));
        }

        public ECFieldElement divide(ECFieldElement b) {
            return new Fp(this.q, this.r, modMult(this.x, modInverse(b.toBigInteger())));
        }

        public ECFieldElement negate() {
            return this.x.signum() == 0 ? this : new Fp(this.q, this.r, this.q.subtract(this.x));
        }

        public ECFieldElement square() {
            return new Fp(this.q, this.r, modMult(this.x, this.x));
        }

        public ECFieldElement squareMinusProduct(ECFieldElement x2, ECFieldElement y) {
            BigInteger ax = this.x;
            BigInteger xx = x2.toBigInteger();
            BigInteger yx = y.toBigInteger();
            return new Fp(this.q, this.r, modReduce(ax.multiply(ax).subtract(xx.multiply(yx))));
        }

        public ECFieldElement squarePlusProduct(ECFieldElement x2, ECFieldElement y) {
            BigInteger ax = this.x;
            BigInteger xx = x2.toBigInteger();
            BigInteger yx = y.toBigInteger();
            return new Fp(this.q, this.r, modReduce(ax.multiply(ax).add(xx.multiply(yx))));
        }

        public ECFieldElement invert() {
            return new Fp(this.q, this.r, modInverse(this.x));
        }

        public ECFieldElement sqrt() {
            if (isZero() || isOne()) {
                return this;
            }
            if (!this.q.testBit(0)) {
                throw new RuntimeException("not done yet");
            } else if (this.q.testBit(1)) {
                return checkSqrt(new Fp(this.q, this.r, this.x.modPow(this.q.shiftRight(2).add(ECConstants.ONE), this.q)));
            } else if (this.q.testBit(2)) {
                BigInteger t1 = this.x.modPow(this.q.shiftRight(3), this.q);
                BigInteger t2 = modMult(t1, this.x);
                if (modMult(t2, t1).equals(ECConstants.ONE)) {
                    return checkSqrt(new Fp(this.q, this.r, t2));
                }
                return checkSqrt(new Fp(this.q, this.r, modMult(t2, ECConstants.TWO.modPow(this.q.shiftRight(2), this.q))));
            } else {
                BigInteger legendreExponent = this.q.shiftRight(1);
                if (!this.x.modPow(legendreExponent, this.q).equals(ECConstants.ONE)) {
                    return null;
                }
                BigInteger X = this.x;
                BigInteger fourX = modDouble(modDouble(X));
                BigInteger k = legendreExponent.add(ECConstants.ONE);
                BigInteger qMinusOne = this.q.subtract(ECConstants.ONE);
                Random rand = new Random();
                while (true) {
                    BigInteger P = new BigInteger(this.q.bitLength(), rand);
                    if (P.compareTo(this.q) < 0 && modReduce(P.multiply(P).subtract(fourX)).modPow(legendreExponent, this.q).equals(qMinusOne)) {
                        BigInteger[] result = lucasSequence(P, X, k);
                        BigInteger U = result[0];
                        BigInteger V = result[1];
                        if (modMult(V, V).equals(fourX)) {
                            return new Fp(this.q, this.r, modHalfAbs(V));
                        }
                        if (!U.equals(ECConstants.ONE) && !U.equals(qMinusOne)) {
                            return null;
                        }
                    }
                }
            }
        }

        private ECFieldElement checkSqrt(ECFieldElement z) {
            if (z.square().equals(this)) {
                return z;
            }
            return null;
        }

        private BigInteger[] lucasSequence(BigInteger P, BigInteger Q, BigInteger k) {
            int n = k.bitLength();
            int s = k.getLowestSetBit();
            BigInteger Uh = ECConstants.ONE;
            BigInteger Vl = ECConstants.TWO;
            BigInteger Vh = P;
            BigInteger Ql = ECConstants.ONE;
            BigInteger Qh = ECConstants.ONE;
            for (int j = n - 1; j >= s + 1; j--) {
                Ql = modMult(Ql, Qh);
                if (k.testBit(j)) {
                    Qh = modMult(Ql, Q);
                    Uh = modMult(Uh, Vh);
                    Vl = modReduce(Vh.multiply(Vl).subtract(P.multiply(Ql)));
                    Vh = modReduce(Vh.multiply(Vh).subtract(Qh.shiftLeft(1)));
                } else {
                    Qh = Ql;
                    Uh = modReduce(Uh.multiply(Vl).subtract(Ql));
                    Vh = modReduce(Vh.multiply(Vl).subtract(P.multiply(Ql)));
                    Vl = modReduce(Vl.multiply(Vl).subtract(Ql.shiftLeft(1)));
                }
            }
            BigInteger Ql2 = modMult(Ql, Qh);
            BigInteger Qh2 = modMult(Ql2, Q);
            BigInteger Uh2 = modReduce(Uh.multiply(Vl).subtract(Ql2));
            BigInteger Vl2 = modReduce(Vh.multiply(Vl).subtract(P.multiply(Ql2)));
            BigInteger Ql3 = modMult(Ql2, Qh2);
            BigInteger Vl3 = Vl2;
            BigInteger Uh3 = Uh2;
            for (int j2 = 1; j2 <= s; j2++) {
                Uh3 = modMult(Uh3, Vl3);
                Vl3 = modReduce(Vl3.multiply(Vl3).subtract(Ql3.shiftLeft(1)));
                Ql3 = modMult(Ql3, Ql3);
            }
            return new BigInteger[]{Uh3, Vl3};
        }

        /* access modifiers changed from: protected */
        public BigInteger modAdd(BigInteger x1, BigInteger x2) {
            BigInteger x3 = x1.add(x2);
            if (x3.compareTo(this.q) >= 0) {
                return x3.subtract(this.q);
            }
            return x3;
        }

        /* access modifiers changed from: protected */
        public BigInteger modDouble(BigInteger x2) {
            BigInteger _2x = x2.shiftLeft(1);
            if (_2x.compareTo(this.q) >= 0) {
                return _2x.subtract(this.q);
            }
            return _2x;
        }

        /* access modifiers changed from: protected */
        public BigInteger modHalf(BigInteger x2) {
            if (x2.testBit(0)) {
                x2 = this.q.add(x2);
            }
            return x2.shiftRight(1);
        }

        /* access modifiers changed from: protected */
        public BigInteger modHalfAbs(BigInteger x2) {
            if (x2.testBit(0)) {
                x2 = this.q.subtract(x2);
            }
            return x2.shiftRight(1);
        }

        /* access modifiers changed from: protected */
        public BigInteger modInverse(BigInteger x2) {
            int bits = getFieldSize();
            int len = (bits + 31) >> 5;
            int[] p = Nat.fromBigInteger(bits, this.q);
            int[] n = Nat.fromBigInteger(bits, x2);
            int[] z = Nat.create(len);
            Mod.invert(p, n, z);
            return Nat.toBigInteger(len, z);
        }

        /* access modifiers changed from: protected */
        public BigInteger modMult(BigInteger x1, BigInteger x2) {
            return modReduce(x1.multiply(x2));
        }

        /* access modifiers changed from: protected */
        public BigInteger modReduce(BigInteger x2) {
            if (this.r == null) {
                return x2.mod(this.q);
            }
            boolean negative = x2.signum() < 0;
            if (negative) {
                x2 = x2.abs();
            }
            int qLen = this.q.bitLength();
            boolean rIsOne = this.r.equals(ECConstants.ONE);
            while (x2.bitLength() > qLen + 1) {
                BigInteger u = x2.shiftRight(qLen);
                BigInteger v = x2.subtract(u.shiftLeft(qLen));
                if (!rIsOne) {
                    u = u.multiply(this.r);
                }
                x2 = u.add(v);
            }
            while (x2.compareTo(this.q) >= 0) {
                x2 = x2.subtract(this.q);
            }
            if (!negative || x2.signum() == 0) {
                return x2;
            }
            return this.q.subtract(x2);
        }

        /* access modifiers changed from: protected */
        public BigInteger modSubtract(BigInteger x1, BigInteger x2) {
            BigInteger x3 = x1.subtract(x2);
            if (x3.signum() < 0) {
                return x3.add(this.q);
            }
            return x3;
        }

        public boolean equals(Object other) {
            boolean z = true;
            if (other == this) {
                return true;
            }
            if (!(other instanceof Fp)) {
                return false;
            }
            Fp o = (Fp) other;
            if (!this.q.equals(o.q) || !this.x.equals(o.x)) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return this.q.hashCode() ^ this.x.hashCode();
        }
    }

    public abstract ECFieldElement add(ECFieldElement eCFieldElement);

    public abstract ECFieldElement addOne();

    public abstract ECFieldElement divide(ECFieldElement eCFieldElement);

    public abstract String getFieldName();

    public abstract int getFieldSize();

    public abstract ECFieldElement invert();

    public abstract ECFieldElement multiply(ECFieldElement eCFieldElement);

    public abstract ECFieldElement negate();

    public abstract ECFieldElement sqrt();

    public abstract ECFieldElement square();

    public abstract ECFieldElement subtract(ECFieldElement eCFieldElement);

    public abstract BigInteger toBigInteger();

    public int bitLength() {
        return toBigInteger().bitLength();
    }

    public boolean isOne() {
        return bitLength() == 1;
    }

    public boolean isZero() {
        return toBigInteger().signum() == 0;
    }

    public ECFieldElement multiplyMinusProduct(ECFieldElement b, ECFieldElement x, ECFieldElement y) {
        return multiply(b).subtract(x.multiply(y));
    }

    public ECFieldElement multiplyPlusProduct(ECFieldElement b, ECFieldElement x, ECFieldElement y) {
        return multiply(b).add(x.multiply(y));
    }

    public ECFieldElement squareMinusProduct(ECFieldElement x, ECFieldElement y) {
        return square().subtract(x.multiply(y));
    }

    public ECFieldElement squarePlusProduct(ECFieldElement x, ECFieldElement y) {
        return square().add(x.multiply(y));
    }

    public ECFieldElement squarePow(int pow) {
        ECFieldElement r = this;
        for (int i = 0; i < pow; i++) {
            r = r.square();
        }
        return r;
    }

    public boolean testBitZero() {
        return toBigInteger().testBit(0);
    }

    public String toString() {
        return toBigInteger().toString(16);
    }

    public byte[] getEncoded() {
        return BigIntegers.asUnsignedByteArray((getFieldSize() + 7) / 8, toBigInteger());
    }
}
