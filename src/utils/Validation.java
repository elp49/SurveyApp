package utils;

import java.util.List;

public class Validation {
    public static boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }

    public static boolean isNullOrEmpty(List<String> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isInRange(int num, int min, int max) {
        return num >= min && num <= max;
    }

    public static boolean isAlphabeticLetter(String s) {
        return s.matches("([a-z]|([A-Z]))");
    }
}
