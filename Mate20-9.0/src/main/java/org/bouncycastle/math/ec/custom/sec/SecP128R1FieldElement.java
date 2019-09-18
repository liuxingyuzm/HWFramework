package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat128;
import org.bouncycastle.util.Arrays;

public class SecP128R1FieldElement extends ECFieldElement.AbstractFp {
    public static final BigInteger Q = SecP128R1Curve.q;
    protected int[] x;

    public SecP128R1FieldElement() {
        this.x = Nat128.create();
    }

    public SecP128R1FieldElement(BigInteger bigInteger) {
        if (bigInteger == null || bigInteger.signum() < 0 || bigInteger.compareTo(Q) >= 0) {
            throw new IllegalArgumentException("x value invalid for SecP128R1FieldElement");
        }
        this.x = SecP128R1Field.fromBigInteger(bigInteger);
    }

    protected SecP128R1FieldElement(int[] iArr) {
        this.x = iArr;
    }

    public ECFieldElement add(ECFieldElement eCFieldElement) {
        int[] create = Nat128.create();
        SecP128R1Field.add(this.x, ((SecP128R1FieldElement) eCFieldElement).x, create);
        return new SecP128R1FieldElement(create);
    }

    public ECFieldElement addOne() {
        int[] create = Nat128.create();
        SecP128R1Field.addOne(this.x, create);
        return new SecP128R1FieldElement(create);
    }

    public ECFieldElement divide(ECFieldElement eCFieldElement) {
        int[] create = Nat128.create();
        Mod.invert(SecP128R1Field.P, ((SecP128R1FieldElement) eCFieldElement).x, create);
        SecP128R1Field.multiply(create, this.x, create);
        return new SecP128R1FieldElement(create);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SecP128R1FieldElement)) {
            return false;
        }
        return Nat128.eq(this.x, ((SecP128R1FieldElement) obj).x);
    }

    public String getFieldName() {
        return "SecP128R1Field";
    }

    public int getFieldSize() {
        return Q.bitLength();
    }

    public int hashCode() {
        return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 4);
    }

    public ECFieldElement invert() {
        int[] create = Nat128.create();
        Mod.invert(SecP128R1Field.P, this.x, create);
        return new SecP128R1FieldElement(create);
    }

    public boolean isOne() {
        return Nat128.isOne(this.x);
    }

    public boolean isZero() {
        return Nat128.isZero(this.x);
    }

    public ECFieldElement multiply(ECFieldElement eCFieldElement) {
        int[] create = Nat128.create();
        SecP128R1Field.multiply(this.x, ((SecP128R1FieldElement) eCFieldElement).x, create);
        return new SecP128R1FieldElement(create);
    }

    public ECFieldElement negate() {
        int[] create = Nat128.create();
        SecP128R1Field.negate(this.x, create);
        return new SecP128R1FieldElement(create);
    }

    public ECFieldElement sqrt() {
        int[] iArr = this.x;
        if (Nat128.isZero(iArr) || Nat128.isOne(iArr)) {
            return this;
        }
        int[] create = Nat128.create();
        SecP128R1Field.square(iArr, create);
        SecP128R1Field.multiply(create, iArr, create);
        int[] create2 = Nat128.create();
        SecP128R1Field.squareN(create, 2, create2);
        SecP128R1Field.multiply(create2, create, create2);
        int[] create3 = Nat128.create();
        SecP128R1Field.squareN(create2, 4, create3);
        SecP128R1Field.multiply(create3, create2, create3);
        SecP128R1Field.squareN(create3, 2, create2);
        SecP128R1Field.multiply(create2, create, create2);
        SecP128R1Field.squareN(create2, 10, create);
        SecP128R1Field.multiply(create, create2, create);
        SecP128R1Field.squareN(create, 10, create3);
        SecP128R1Field.multiply(create3, create2, create3);
        SecP128R1Field.square(create3, create2);
        SecP128R1Field.multiply(create2, iArr, create2);
        SecP128R1Field.squareN(create2, 95, create2);
        SecP128R1Field.square(create2, create3);
        if (Nat128.eq(iArr, create3)) {
            return new SecP128R1FieldElement(create2);
        }
        return null;
    }

    public ECFieldElement square() {
        int[] create = Nat128.create();
        SecP128R1Field.square(this.x, create);
        return new SecP128R1FieldElement(create);
    }

    public ECFieldElement subtract(ECFieldElement eCFieldElement) {
        int[] create = Nat128.create();
        SecP128R1Field.subtract(this.x, ((SecP128R1FieldElement) eCFieldElement).x, create);
        return new SecP128R1FieldElement(create);
    }

    public boolean testBitZero() {
        return Nat128.getBit(this.x, 0) == 1;
    }

    public BigInteger toBigInteger() {
        return Nat128.toBigInteger(this.x);
    }
}
