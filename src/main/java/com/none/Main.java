package com.none;

import com.none.input.FileFractionsReader;
import com.none.input.FractionsReader;
import com.none.input.Menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static int DEFAULT_PRECISION = 50;

    public static void main(String[] args) {

        List<FractionWithBase> fractions = new ArrayList<>();

        if (args.length > 0) {

            if (args.length == 1) {

                FileFractionsReader fileFractionsReader = new FileFractionsReader(args[0]);
                fractions = fileFractionsReader.getFractions();

                if (!fractions.isEmpty()) {
                    processFractions(fractions, DEFAULT_PRECISION);
                } else {
                    System.err.println("Proceeding to Console Menu...");
                }

            } else if (args.length == 2) {

                int precision = -1;

                try {
                    precision = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    System.err.println("INPUT ERROR. Argument " + args[0] + " must be an integer. Proceeding to Console Menu...");
                }

                if (precision > -1) {

                    FileFractionsReader fileFractionsReader = new FileFractionsReader(args[1]);
                    fractions = fileFractionsReader.getFractions();

                    if (!fractions.isEmpty()) {
                        processFractions(fractions, precision);
                    } else {
                        System.err.println("Proceeding to Console Menu...");
                    }
                } else {
                    System.err.println("INPUT ERROR. Precision can't be negative. Proceeding to Console Menu...");
                }

            } else if (args.length == 4) {

                BigDecimal numerator = BigDecimal.ZERO;
                BigDecimal denominator = BigDecimal.ZERO;
                int precision = -1;
                int base = 0;

                try {

                    precision = Integer.parseInt(args[0]);
                    base = Integer.parseInt(args[3]);

                } catch (NumberFormatException e) {
                    System.err.println("INPUT ERROR. First and last arguments must be positive integers.");
                }

                try {

                    numerator = new BigDecimal(args[1]);
                    denominator = new BigDecimal(args[2]);

                } catch (IllegalArgumentException e) {
                    System.err.println("INPUT ERROR. 2nd and 3rd arguments must be numbers.");
                }

                if (precision > -1 && base > 1 && base < 37 && !denominator.equals(BigDecimal.ZERO)) {

                    fractions.add(new FractionWithBase(numerator,denominator,base));
                    processFractions(fractions, precision);

                } else {
                    System.err.println("Precision can't be negative. Base must be an integer from 2 to 36 (including). Denominator can't be equal to 0. Proceeding to Console Menu...");
                }


            } else {
                System.out.println("Arguments passed in the command line do not fit my compatibilities. Proceeding to Console Menu...");
            }

        }

        Menu menu = new Menu();


        while (fractions.isEmpty()) {

            int precision = DEFAULT_PRECISION;
            FractionsReader fractionsReader = null;

            while (fractionsReader == null) {
                precision = menu.getPrecisionFromConsole();
                fractionsReader = menu.chooseFractionsReader();
            }

            fractions = fractionsReader.getFractions();

            if (!fractions.isEmpty()) {
                processFractions(fractions, precision);
            }
        }

    }

    private static void processFractions(List<FractionWithBase> fractions, int precision) {

        MultiBaseFractionsCalc multiBaseFractionsCalc = new MultiBaseFractionsCalc(precision);

        for (FractionWithBase f : fractions) {

            String result = multiBaseFractionsCalc.findPeriodForFractionInBase(f);

            if (f.getNumerator().signum() != f.getDenominator().signum())
                result = "-" + result;

            System.out.println(result);
        }
    }
}
