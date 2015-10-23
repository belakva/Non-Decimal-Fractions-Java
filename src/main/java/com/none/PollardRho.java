package com.none;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;


// Данный класс может быть легко адаптирован для работы с системами счислениях с основаниями больше 36. Для этого
// просто уберем из метода getPrimes() следующий блок кода:
//
// if (f.compareTo(THIRTY_ONE) > 0) {
//
//        primeFactorsOfN.add(0);
//        return primeFactorsOfN;
// }
//
// Или заменим THIRTY_ONE на самое большое простое число, которое может быть простым множителем основания
// системы счисления в заданном диапозоне.
//


public class PollardRho
{
    private final static BigInteger ZERO = new BigInteger("0");
    private final static BigInteger ONE  = new BigInteger("1");
    private final static BigInteger TWO  = new BigInteger("2");
    private final static BigInteger THREE  = new BigInteger("3");
    private final static BigInteger FIVE  = new BigInteger("5");
    private final static BigInteger SEVEN  = new BigInteger("7");
    private final static BigInteger THIRTY_ONE  = new BigInteger("31");
    private final static SecureRandom random = new SecureRandom();

    private BigInteger f(BigInteger X, final BigInteger C) {

        return X.multiply(X).add(C);
    }

    /** get divisor **/
    private BigInteger rho(final BigInteger N) {
        if (N.mod(TWO).equals(ZERO)) return TWO;
        if (N.mod(THREE).equals(ZERO)) return THREE;
        if (N.mod(FIVE).equals(ZERO)) return FIVE;
        if (N.mod(SEVEN).equals(ZERO)) return SEVEN;

        final BigInteger c  = new BigInteger(N.bitLength(), random);
        BigInteger x1 = new BigInteger(N.bitLength(), random);
        BigInteger x2 = x1;
        BigInteger divisor;

        do {
            x1 = f(x1,c).mod(N);
            x2 = f(f(x2,c),c).mod(N);
            divisor = x1.subtract(x2).gcd(N);
        } while (divisor.equals(ONE));

        return divisor;
    }

    /** get all factors **/
    private BigInteger factor(final BigInteger N) {

        if (N.equals(ONE)) return ZERO;
        if (N.isProbablePrime(20)) { return N; }

        return factor(rho(N));
    }

    public HashSet<Integer> getPrimes(BigInteger n) {

        HashSet<Integer> primeFactorsOfN = new HashSet<>();

        BigInteger f;
        while (!n.equals(ONE)) {
            f = factor(n);
            if (!f.equals(ZERO)) {

                // Нам не нужно выяснять ВСЕ простые множители числа, так как, как только среди множителей появится
                // число больше 31, мы будем точно знать, что выражение (primeFactorsOfB.containsAll(primeFactorsOfD))
                // возратит false. Известно, что, если это выражение возвращает false, сразу же вызывается
                // primeFactorsOfD.retainAll(primeFactorsOfB). Таким образом, у нас нет необходимости продолжать
                // поиск простых множителей, если среди них мы обнаружили число > 31.

                if (f.compareTo(THIRTY_ONE) > 0)
                {
                    primeFactorsOfN.add(0); // Заменяем все числа больше 31 на 0, чтобы избежать overflow примитива int.
                    return primeFactorsOfN;
                }

                primeFactorsOfN.add(f.intValue());
                n = n.divide(f);
            }
        }

        return primeFactorsOfN;
    }

}