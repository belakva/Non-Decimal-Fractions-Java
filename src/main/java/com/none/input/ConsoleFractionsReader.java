package com.none.input;

import com.none.FractionWithBase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


public class ConsoleFractionsReader implements FractionsReader {

    private final Scanner input = new Scanner(System.in).useLocale(Locale.ENGLISH);

    @Override
    public List<FractionWithBase> getFractions() {
        ArrayList<FractionWithBase> fractions = new ArrayList<>();

        boolean bError = true;
        int numberOfFractions = 0;

        while (bError) {
            System.out.print("How many fractions you would like to examine:");
            if (input.hasNextInt()) {
                numberOfFractions = input.nextInt();
                bError = false;
            } else {
                System.err.println("INPUT ERROR. Please, enter an integer. Otherwise we can't get through." + "\n" + "\n");
                input.next();
            }
        }

        input.nextLine();
        bError = true;
        int inputInt;

        while (bError) {
            for (int i = 0; i < numberOfFractions; i++) {
                int iPlusOne = i + 1;
                System.out.print("Please, enter NOMINATOR, DENOMINATOR and BASE (separated by space) of fraction " + iPlusOne + " :");

                BigDecimal n;
                BigDecimal d;
                int base;

                if (input.hasNextBigDecimal()) {
                    n = input.nextBigDecimal();
                } else {
                    if (input.next() != null) {
                        System.err.println("INPUT ERROR. Please, use number to define numerator. List of fractions cleared. Please, start again." + "\n");
                    }
                    fractions.clear();
                    break;
                }

                if (input.hasNextBigDecimal()) {
                    d = input.nextBigDecimal();
                } else {
                    if (input.next() != null) {
                        System.err.println("INPUT ERROR. Please, use number to define denominator. List of fractions cleared. Please, start again." + "\n");
                    }
                    fractions.clear();
                    break;

                }

                if (input.hasNextInt()) {
                    inputInt = input.nextInt();
                    if (inputInt > 1 && inputInt < 37) {
                        base = inputInt;
                    } else {
                        System.err.println("INPUT ERROR. Please, use integer between 2 and 36 to define base. List of fractions cleared. Please, start again." + "\n" + "\n");
                        fractions.clear();
                        break;
                    }
                } else {
                    if (input.next() != null) {
                        System.err.println("INPUT ERROR. Please, use integer between 2 and 36 to define base. List of fractions cleared. Please, start again." + "\n");
                    }
                    fractions.clear();
                    break;

                }

                fractions.add(i, new FractionWithBase(n, d, base));

            }

            if (fractions.size() < numberOfFractions) {
                input.nextLine();
            } else {
                bError = false;
            }
        }

        return fractions;
    }
}
