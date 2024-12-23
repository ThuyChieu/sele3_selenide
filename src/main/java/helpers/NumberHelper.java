package helpers;

import java.util.Random;

public class NumberHelper {
    public static int parseCurrencyToInt(String currencyString) {
        String updateString = currencyString.replaceAll("[₫,.]", "").trim();
        return Integer.parseInt(updateString);
    }

    public static int getRandomNumber(int maximumValue) {
        Random rand = new Random();
        return rand.nextInt(maximumValue);
    }
}
