package com.none.input;

import java.util.Locale;
import java.util.Scanner;

public class Menu {

    private final Scanner input = new Scanner(System.in).useLocale(Locale.ENGLISH);

    public int getPrecisionFromConsole() {

        int userInput;
        while (true) {
            System.out.print("Hi, hh!" + "\n" +
                    "This program finds the result of division of two numbers in a numeric notation with base defined by user." + "\n" +
                    "The numbers to be divided are taken in numeric base 10 and the result is presented in the desired base." + "\n" +
                    "The variety of bases possible to choose from is any integer from 2 to 36 (including)." + "\n" +
                    "Type of BigDecimal is used to handle numerator and denominator inputs, so very precise calculations" + "\n" +
                    "can be made. First thing to do is to set up precision that you would like to achieve." + "\n" +
                    "\n" +
                    "Please, enter an integer to define how many digits after the floating point should be calculated" + "\n" +
                    "(usually, from 6 to 50 digits):");

            if (input.hasNextInt()) {
                userInput = input.nextInt();
                if (userInput >= 0) {
                    System.out.println("Awesome! Our calculations will be processed with precision of " + userInput +
                            " after the floating point." + "\n");
                    return userInput;
                } else {
                    System.out.println("INPUT ERROR. Please, enter a positive integer. Otherwise we can't get through." + "\n" + "\n");
                }

            } else {
                System.out.println("INPUT ERROR. Please, enter a positive integer. Otherwise we can't get through." + "\n" + "\n");
                input.next();
            }
        }
    }

    public FractionsReader chooseFractionsReader() {

        int userInput;
        while (true) {
            System.out.println(
                    "Now, please, choose, would you like to enter numbers for calculation right in this console (Press 1)" + "\n" +
                            "or I can read them from a file (Press 2)." + "\n" +
                            "If you would like to change the precision of the session, you also can do it (Press 3)." + "\n" +
                            "\n" +
                            "If choose option 2, be sure to put <fractions.txt> into src/main/resources in program directory with" + "\n" +
                            "numerator, denominator and base, separated by spaces and dot as decimal delimiter, as follows: n d b. Example:" + "\n" +
                            "1 2 8" + "\n" +
                            "1 12 10" + "\n" +
                            "16.77 -13.2 20" + "\n" +
                            "\n" +
                            "Choose now:");

            if (input.hasNextInt()) {
                userInput = input.nextInt();
                if (userInput == 1) {
                    return new ConsoleFractionsReader();
                } else if (userInput == 2) {
                    return new FileFractionsReader();
                } else if (userInput == 3) {
                    return null; // null = getPrecisionFromConsole() -> "while (fractionsReader == null)" in Main
                } else {
                    System.out.println("INPUT ERROR. Please, enter 1 or 2 or 3. Otherwise we can't get through." + "\n" + "\n");
                }

            } else {
                System.out.println("INPUT ERROR. Please, enter 1 or 2 or 3. Otherwise we can't get through." + "\n" + "\n");
                input.next();
            }
        }
    }
}
