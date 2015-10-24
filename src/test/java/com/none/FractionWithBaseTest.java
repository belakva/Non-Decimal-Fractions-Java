package com.none;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class FractionWithBaseTest {

    private final FractionWithBase fr = new FractionWithBase(new BigDecimal("13.34"), new BigDecimal ("5.2"), 10);

    @Test
    public void testNumeratorAndDenominatorToBigInt() throws Exception {

        BigInteger expectedNumerator = new BigInteger("1334");
        BigInteger expectedDenominator = new BigInteger("520");

        assertEquals(expectedNumerator, fr.getNumeratorBigInt());
        assertEquals(expectedDenominator, fr.getDenominatorBigInt());

    }
}