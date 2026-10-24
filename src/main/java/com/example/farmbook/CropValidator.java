package com.example.farmbook;

public class CropValidator {
    public static boolean hasBlankFields(String... fields) {
        for (String f : fields) {
            if (f == null || f.trim().isEmpty()) return true;
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}