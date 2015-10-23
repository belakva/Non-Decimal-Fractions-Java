package com.none;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MultiBaseFractionsCalcTest extends Assert {

    private final MultiBaseFractionsCalc multiBaseFractionsCalc = new MultiBaseFractionsCalc(10);

    @Test
    public void testGetPrimeFactorsOfPositiveNumberUpTo36() throws Exception {
        final Set<Integer> expectedPrimesOf34 = new HashSet<>();
        expectedPrimesOf34.add(2);
        expectedPrimesOf34.add(17);
        final Set<Integer> actualPrimesOf34 = multiBaseFractionsCalc.getPrimeFactorsOfPositiveNumberUpTo36(34);

        assertEquals(expectedPrimesOf34, actualPrimesOf34);
    }

    @Test
    public void testBuildTerminatingStringOfPostPointDigitsInBase() throws Exception {

        final String expectedPostPoint = "2";

        final BigInteger numerator = BigInteger.ONE;
        final BigInteger denominator = BigInteger.valueOf(4);
        final int base = 8;

        final String actualPostPoint = multiBaseFractionsCalc.buildTerminatingStringOfPostPointDigitsInBase(numerator, denominator, base);

        assertEquals(expectedPostPoint, actualPostPoint);
    }

    @Test
    public void testBuildRecurringStringOfPostPointDigitsInBase() throws Exception {

        final String expectedPostPoint = "08(3)";

        final BigInteger numerator = BigInteger.ONE;
        final BigInteger denominator = BigInteger.valueOf(12);
        int base = 10;
        int fixedPartLength = 2;
        int periodLength = 1;

        final String actualPostPoint = multiBaseFractionsCalc.buildRecurringStringOfPostPointDigitsInBase(
                numerator,denominator,base,fixedPartLength,periodLength);

        assertEquals(expectedPostPoint, actualPostPoint);

    }

    @Test
    public void testBuildRoundedUpFractionInBase() throws Exception {

        final String expectedBuilder357 = "357";

        final String inputForResult357 = "3565";
        final int baseForResult357 = 10;

        final String actualResult357 = multiBaseFractionsCalc.buildRoundedUpFractionInBase(baseForResult357, inputForResult357);

        assertEquals(expectedBuilder357, actualResult357);

        final String expectedBuilder100 = "100";

        final String inputForResult100 = "zzz";
        final int baseForResult100 = 36;

        String actualResult100 = multiBaseFractionsCalc.buildRoundedUpFractionInBase(baseForResult100, inputForResult100);

        assertEquals(expectedBuilder100, actualResult100);

    }

    @Test
    public void testLengthOfPeriodForDenominatorInBase() throws Exception {

        final int expectedLength = 6;

        final BigInteger denominator = BigInteger.valueOf(7);
        final int base = 10;

        final int actualLength = multiBaseFractionsCalc.lengthOfPeriodForDenominatorInBase(denominator,base);

        assertEquals(expectedLength,actualLength);

    }

    @Test
    public void testFindPeriodForTwoPositiveNumbers() throws Exception {

        final String expected = "0.08(3)";

        final FractionWithBase f = new FractionWithBase(BigDecimal.ONE, new BigDecimal("12"), 10);

        final String actual = multiBaseFractionsCalc.findPeriodForFractionInBase(f);

        assertEquals(expected,actual);
    }

}