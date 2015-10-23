package com.none;

import java.math.BigDecimal;
import java.math.BigInteger;

public class FractionWithBase {

    private final BigDecimal numerator;
    private final BigDecimal denominator;
    private final int base;

    private BigInteger numeratorBigInt;
    private BigInteger denominatorBigInt;

    public FractionWithBase(final BigDecimal n, final BigDecimal d, final int b) {
        this.numerator = n;
        this.denominator = d;
        this.base = b;

        numeratorAndDenominatorToBigInt();
    }

    public BigDecimal getNumerator() {
        return numerator;
    }

    public BigDecimal getDenominator() {
        return denominator;
    }

    public int getBase() {
        return base;
    }

    public BigInteger getNumeratorBigInt() {
        return numeratorBigInt;
    }

    public BigInteger getDenominatorBigInt() {
        return denominatorBigInt;
    }

    private void numeratorAndDenominatorToBigInt() {

        numeratorBigInt = new BigInteger((numerator.scale() > denominator.scale() ?
                numerator : numerator.movePointRight(denominator.scale())).toString().replace(".", "")).abs();
        denominatorBigInt = new BigInteger((denominator.scale() > numerator.scale() ?
                denominator : denominator.movePointRight(numerator.scale())).toString().replace(".", "")).abs();

    }
}
