package com.none.input;

import com.none.FractionWithBase;
import com.none.MultiBaseFractionsCalc;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class FileFractionsReader implements FractionsReader {

    private final static BigDecimal THIRTY_SIX = new BigDecimal("36");

    private File fractionsFile;

    public FileFractionsReader() {
        fractionsFile = new File("src/main/resources/fractions.txt");
    }

    public FileFractionsReader(String fileName) {
        fractionsFile = new File(fileName);
    }

    @Override
    public List<FractionWithBase> getFractions() {

        List<FractionWithBase> fractions = new ArrayList<>();
        Scanner scanner;

        try {
            scanner = new Scanner(fractionsFile).useLocale(Locale.ENGLISH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("ERROR. File not found." + "\n" +
                    "Please, check the file and try again, or try other options." + "\n" + "\n");
            return fractions;
        }

        int ni = 0;
        int di = 0;
        int bi = 0;
        BigDecimal n = BigDecimal.ZERO;
        BigDecimal d = BigDecimal.ZERO;
        BigDecimal b;

        while (scanner.hasNextBigDecimal()) {
            if (ni == di && ni == bi) {
                n = scanner.nextBigDecimal();
                ni++;
            } else if (ni > di && ni > bi) {
                d = scanner.nextBigDecimal();
                di++;

            } else {

                b = scanner.nextBigDecimal();

                if (b.compareTo(THIRTY_SIX) <= 0 && b.compareTo(BigDecimal.ONE) > 0 && b.doubleValue() % 1 == 0) {

                    fractions.add(new FractionWithBase(n, d, b.intValue()));
                    bi++;

                } else {
                    break;
                }
            }
        }

        if (fractions.isEmpty()) {
            System.err.println("ERROR. I was not able to find any numbers in the target file." + "\n" +
                    "Please, check the file and try again, or try other options." + "\n" + "\n");
        } else if (ni != bi) {
            fractions.clear();
            System.err.println("ERROR. Something is defined wrong in the file." + "\n" +
                    "Check bases to be defined as integers between 2 and 36." + "\n" +
                    "Please, check the file and try again, or try other options." + "\n" + "\n");
        }

        return fractions;
    }
}
