package com.none;

import org.junit.Test;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class PollardRhoTest {

    private final PollardRho pr = new PollardRho();

    @Test
    public void testGetPrimes() throws Exception {

        final Set<Integer> expectedPrimes999 = new HashSet<>();
        expectedPrimes999.add(3);
        expectedPrimes999.add(0); // Второй простой множитель 999 - 37, что больше 36 (верхней границы допустимых
                                  // оснований систем счисления), значит мы должны получить 0.

        final BigInteger n999 = BigInteger.valueOf(999);

        final Set<Integer> actualPrimes999 = pr.getPrimes(n999);

        assertEquals(expectedPrimes999,actualPrimes999);

        final Set<Integer> expectedPrimes34 = new HashSet<>();
        expectedPrimes34.add(2);
        expectedPrimes34.add(17);

        final BigInteger n34 = BigInteger.valueOf(34);

        final Set<Integer> actualPrimes34 = pr.getPrimes(n34);

        assertEquals(expectedPrimes34,actualPrimes34);

    }
}