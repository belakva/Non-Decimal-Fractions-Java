package com.none;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class MultiBaseFractionsCalc {

    private final static int[] PRIMES_FOR_NUMBER_UP_TO_36 = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31};

    private final static String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyz";
    private final static String ROUND_UP_TO_WHOLE_FLAG = "upToWhole";

    private final static BigInteger ZERO = BigInteger.ZERO;
    private final static BigInteger ONE = BigInteger.ONE;
    private final static BigInteger THIRTY_SIX = new BigInteger("36");

    private final static PollardRho pr = new PollardRho();

    private final int precision;

    public MultiBaseFractionsCalc(int precision) {
        this.precision = precision;
    }

    public String findPeriodForFractionInBase(FractionWithBase f) {

        int base = f.getBase();
        BigInteger numerator = f.getNumeratorBigInt();
        BigInteger denominator = f.getDenominatorBigInt();

        // Проверим, является ли результат целым числом.
        BigInteger remainder = numerator.mod(denominator);

        if (numerator.mod(denominator).equals(ZERO)) {
            BigInteger result = numerator.divide(denominator);
            return result.toString(base);
        }

        // Если значение дроби > 1, выделим целую часть.

        BigInteger integralPart = ZERO;

        if (numerator.compareTo(denominator) > 0) {

            integralPart = (numerator.subtract(remainder)).divide(denominator); // e.g. (дробь 5/4) (5 - (5 % 2)) / 2 = (5 - 1) / 2 = 2
            numerator = numerator.subtract(integralPart.multiply(denominator)); // e.g. 5 - 2*2
        }

        // Сократим дробь, чтобы получить числитель, в котором не будет общих простых множителей со знаменателем.

        BigInteger gcd = numerator.gcd(denominator);

        while (!gcd.equals(ONE)) {
            denominator = denominator.divide(gcd);
            numerator = numerator.divide(gcd);
            gcd = numerator.gcd(denominator);
        }

        // Разложим знаменатель и базу системы счисления на простые множители.

        Set<Integer> primeFactorsOfB = getPrimeFactorsOfPositiveNumberUpTo36(base);

        Set<Integer> primeFactorsOfD = denominator.compareTo(THIRTY_SIX) > 0 ?
                pr.getPrimes(denominator) :
                getPrimeFactorsOfPositiveNumberUpTo36(denominator.intValue());

        if (primeFactorsOfB.containsAll(primeFactorsOfD)) {

            // Дробь терминальна. Делим и возвращаем ответ.

            final String postPoint = buildTerminatingStringOfPostPointDigitsInBase(numerator, denominator, base);

            if (postPoint.equals(ROUND_UP_TO_WHOLE_FLAG)) {

                // Дробь сократилась за счет округления
                integralPart = integralPart.add(ONE);
                return integralPart.toString(base);

            } else {

                return postPoint.length() > 0 ? integralPart.toString(base) + "." + postPoint : integralPart.toString(base);
            }


        } else {

            // Дробь периодическая.

            // Опеределим тип периодической дроби (чистая или смешанная).

            // Уберем из массива простых множителей знаменателя все те, которые не являются простыми множителями базы.
            // Если список останется пуст, значит у знаменателя и базы вообще не было общих простых множителей,
            // следовательно настоящая дробь - периодическая дробь чистого типа.

            primeFactorsOfD.retainAll(primeFactorsOfB);

            if (primeFactorsOfD.size() == 0) {

                // Периодическая дробь чистого типа

                // Определим длину периода как periodLength - наименьшая степень, при возведении в которую
                // B^periodLength mod d = 1.

                if (precision == 0) {
                    return integralPart.toString(base);
                }

                final String postPoint = buildRecurringStringOfPostPointDigitsInBase(numerator, denominator, base, 0,
                        lengthOfPeriodForDenominatorInBase(denominator, base));

                return postPoint.length() > 0 ? integralPart.toString(base) + "." + postPoint : integralPart.toString(base);


            } else {

                // Периодическая дробь смешанного типа

                // Представим d как D * Е, где D не содержит таких простых множителей, какие не являются
                // простыми множителями B. Тогда E = d/D.

                // На данном этапе в массиве primeFactorsOfD остались только простые множители, которые являются
                // общими для базы и знаменателя.

                BigInteger denominatorCopy = denominator;
                BigInteger D = ONE;

                for (int p : primeFactorsOfD) {
                    BigInteger bigP = BigInteger.valueOf(p);
                    while (denominatorCopy.mod(bigP).equals(ZERO)) {
                        denominatorCopy = denominatorCopy.divide(bigP);
                        D = D.multiply(bigP);
                    }
                }

                BigInteger E = denominator.divide(D);
                final int fixedPartLength =
                        buildTerminatingStringOfPostPointDigitsInBase(ONE, D, base).length();

                String postPoint;

                if (fixedPartLength >= precision) {

                    postPoint = buildTerminatingStringOfPostPointDigitsInBase(numerator, denominator, base);

                } else {

                    postPoint =
                            buildRecurringStringOfPostPointDigitsInBase(numerator, denominator, base, fixedPartLength,
                                    lengthOfPeriodForDenominatorInBase(E, base));
                }

                if (postPoint.equals(ROUND_UP_TO_WHOLE_FLAG)) {

                    // Дробь сократилась за счет округления непериодической части
                    // (установленная точность меньше длины неприодической части)

                    integralPart = integralPart.add(ONE);
                    return integralPart.toString(base);

                } else {

                    return postPoint.length() > 0 ? integralPart.toString(base) + "." + postPoint : integralPart.toString(base);
                }

            }
        }
    }

    Set<Integer> getPrimeFactorsOfPositiveNumberUpTo36(final int number) {

        Set<Integer> primeFactors = new HashSet<>();

        if (number != 30) {
            int fi = 0; // factor's index
            for (int p : PRIMES_FOR_NUMBER_UP_TO_36) {
                if (number % p == 0) {
                    fi++;
                    primeFactors.add(p);
                }
                if (fi == 2) break;
            }
        } else {
            primeFactors.add(2);
            primeFactors.add(3);
            primeFactors.add(5);
        }

        return primeFactors;

    }

    String buildTerminatingStringOfPostPointDigitsInBase
            (final BigInteger numerator, final BigInteger denominator, final int base) {

        // При конвертации результата деления, проведенного в системе счисления с основанием 10
        // и с установенной пользовтаелем точностью, в системы счисления с основанием больше 10, количество
        // знаков после точки в целевой системе счисления может быть меньше ожидаемого пользователем,
        // так как каждый знак вмещает большие значения, чем в десятичной системе. Чтобы получить
        // число с установленным количеством знаков в системах счисления с основанием больше 10,
        // проведем деление с большей точностью, а результат округлим до нужного знака.
        //
        // Число 4 как множитель для precision взято исходя из худшего результата,
        // когда происходит конвертация в систему счисления с основанием 36 (10 * 3.6 = 36).
        // Округлим 3.6, поулчим 4.

        int precisionInTargetBase = base > 10 ? precision * 4 : precision;

        BigDecimal fraction = new BigDecimal(numerator).divide(
                new BigDecimal(denominator), precisionInTargetBase, BigDecimal.ROUND_HALF_UP);

        final BigDecimal bigDecimalBase = BigDecimal.valueOf(base);
        final BigInteger bigIntegerBase = BigInteger.valueOf(base);

        fraction = fraction.multiply(bigDecimalBase);

        StringBuilder fractionStringBuilder = new StringBuilder();
        int trailingZeroesCounter = 0;
        int digitsBeyondPrecisionCount = 0;
        boolean roundUp = false;
        int i;

        for (i = 0; i < precisionInTargetBase; i++) {
            int charPos = fraction.toBigInteger().mod(bigIntegerBase).intValue();
            fractionStringBuilder.append(CHARSET.charAt(charPos));
            fraction = fraction.multiply(bigDecimalBase);

            if (charPos == 0) {
                trailingZeroesCounter++;
            } else {
                trailingZeroesCounter = 0;
            }

            if (i + 1 > precision) {

                digitsBeyondPrecisionCount++;
                if (charPos >= base / 2) roundUp = true;
                break;
            }
        }

        digitsBeyondPrecisionCount -= trailingZeroesCounter;
        fractionStringBuilder.setLength(fractionStringBuilder.length() - trailingZeroesCounter);

        if (digitsBeyondPrecisionCount > 0) {
            if (roundUp) {

                StringBuilder roundedUpFractionInBase = new StringBuilder(
                        buildRoundedUpFractionInBase(base, fractionStringBuilder.toString()));

                if (roundedUpFractionInBase.length() < fractionStringBuilder.length()) {
                    fractionStringBuilder.setLength(fractionStringBuilder.length() - roundedUpFractionInBase.length());

                    for (i = roundedUpFractionInBase.length() - 1; i > -1; i--) {
                        if (roundedUpFractionInBase.substring(i, i).equals("0")) {
                            roundedUpFractionInBase.setLength(roundedUpFractionInBase.length() - 1);
                        } else {
                            break;
                        }
                    }

                    fractionStringBuilder.append(roundedUpFractionInBase);

                } else {
                    // Вся часть дроби после точки сократилась.
                    return ROUND_UP_TO_WHOLE_FLAG;
                }

            } else {
                fractionStringBuilder.setLength(fractionStringBuilder.length() - digitsBeyondPrecisionCount);
            }
        }

        return fractionStringBuilder.toString();
    }

    String buildRecurringStringOfPostPointDigitsInBase
            (final BigInteger numerator, final BigInteger denominator, final int base,
             final int fixedPartLength, final int periodLength) {

        // При конвертации результата деления, проведенного в системе счисления с основанием 10
        // и с установенной пользовтаелем точностью, в системы счисления с основанием больше 10, количество
        // знаков после точки в целевой системе счисления может быть меньше ожидаемого пользователем,
        // так как каждый знак вмещает большие значения, чем в десятичной системе. Чтобы получить
        // число с установленным количеством знаков в системах счисления с основанием больше 10,
        // проведем деление с большей точностью, а результат округлим до нужного знака.
        //
        // Число 4 как множитель для precision взято исходя из худшего результата,
        // когда происходит конвертация в систему счисления с основанием 36 (10 * 3.6 = 36).
        // Округлим 3.6, поулчим 4.

        int precisionInTargetBase = base > 10 ? precision * 4 : precision;

        BigDecimal fraction = new BigDecimal(numerator).divide(
                new BigDecimal(denominator), precisionInTargetBase, BigDecimal.ROUND_DOWN);

        // Числа в периоде округляем вниз, так как, если округлять вверх, можем получить, что весь период округлится,
        // но в рамках данного задания такой результат не корректно отразил бы реальное положение вещей, ИМХО.
        // Могу этот момент и переписать с округлением вверх, если потребуется.

        final BigDecimal bigDecimalBase = BigDecimal.valueOf(base);
        final BigInteger bigIntegerBase = BigInteger.valueOf(base);

        fraction = fraction.multiply(bigDecimalBase);

        StringBuilder fractionStringBuilder = new StringBuilder();

        // Непериодическая часть
        int i;

        if (fixedPartLength > 0) {
            for (i = 0; i < fixedPartLength; i++) {
                int charPos = fraction.toBigInteger().mod(bigIntegerBase).intValue();
                fractionStringBuilder.append(CHARSET.charAt(charPos));
                fraction = fraction.multiply(bigDecimalBase);
            }
        }

        // Периодическая часть

        int trailingZeroesCounter = 0;
        fractionStringBuilder.append("(");

        if (fixedPartLength + periodLength <= precision) {
            for (i = 0; i < periodLength; i++) {
                int charPos = fraction.toBigInteger().mod(bigIntegerBase).intValue();
                fractionStringBuilder.append(CHARSET.charAt(charPos));
                fraction = fraction.multiply(bigDecimalBase);

                if (charPos == 0) {
                    trailingZeroesCounter++;
                } else {
                    trailingZeroesCounter = 0;
                }

            }
            fractionStringBuilder.setLength(fractionStringBuilder.length() - trailingZeroesCounter);

            if (trailingZeroesCounter == i) {

                // Период из нулей (вероятность этой ситуации стремиться к нулю). Уберем открывающуюся скобку.

                fractionStringBuilder.setLength(fractionStringBuilder.length() - 1);

            } else {
                fractionStringBuilder.append(")");
            }


        } else {

            int digitsCount = fixedPartLength;
            int digitsBeyondPrecisionCount = 0;

            // Отнимаем ту единицу от длины периода, которая служила указателем на то, что его
            // длина превысила допустимую точность.

            for (i = 0; i < periodLength - 1; i++) {
                int charPos = fraction.toBigInteger().mod(bigIntegerBase).intValue();
                fractionStringBuilder.append(CHARSET.charAt(charPos));
                fraction = fraction.multiply(bigDecimalBase);

                if (charPos == 0) {
                    trailingZeroesCounter++;
                } else {
                    trailingZeroesCounter = 0;
                }

                digitsCount++;

                if (digitsCount > precision) {
                    digitsBeyondPrecisionCount++;
                    break;
                }
            }

            digitsBeyondPrecisionCount -= trailingZeroesCounter;
            fractionStringBuilder.setLength(fractionStringBuilder.length() - trailingZeroesCounter);

            if (trailingZeroesCounter >= i) {

                // Период из нулей (вероятность этой ситуации стремиться к нулю). Уберем открывающуюся скобку.

                fractionStringBuilder.setLength(fractionStringBuilder.length() - 1);

            } else {

                if (digitsBeyondPrecisionCount > 0) {
                    fractionStringBuilder.setLength(fractionStringBuilder.length() - digitsBeyondPrecisionCount);
                }

                fractionStringBuilder.append("...)").append("\n").
                        append("Period length exceeds the maximum precision of calculations, which is set to ").
                        append(precision).append(" digits.");
            }

        }

        return fractionStringBuilder.toString();
    }

    String buildRoundedUpFractionInBase(final int base, final String fractionString) {

        BigInteger bigIntFraction = new BigInteger(fractionString, base);
        bigIntFraction = bigIntFraction.divide(BigInteger.valueOf(base)).add(ONE);

        return bigIntFraction.toString(base);

    }

    int lengthOfPeriodForDenominatorInBase(final BigInteger denominator, final int base) {

        //Пусть L - наименьшая степень, при возведении в которую B^L mod d = 1.
        //Тогда длина периода дроби n/d будет равна L.

        int periodLength = 0;
        final BigInteger bigBase = BigInteger.valueOf(base);
        BigInteger poweredBase = ONE;
        BigInteger remainder = ZERO;

        while (remainder.compareTo(ONE) != 0) {
            poweredBase = poweredBase.multiply(bigBase);
            remainder = poweredBase.mod(denominator);
            periodLength++;

            // Если длина периода первышает заданную точность вычислений, цикл нужно остановить.
            // Тогда добавим единицу к величине периода, чтобы в дальнейшем определить, что его длина
            // превысила допустимую.

            if (periodLength == precision && remainder.compareTo(ONE) != 0) {
                periodLength++;
                break;
            }
        }

        return periodLength;

    }
    
}
