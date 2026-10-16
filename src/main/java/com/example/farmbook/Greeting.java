package com.example.farmbook;

/**
 * Chooses the greeting for the time of day.
 */
public class Greeting {

    /** Returns "Good morning", "Good afternoon" or "Good evening" for an hour from 0 to 23. */
    public static String forHour(int hour) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("hour must be 0 to 23, got " + hour);
        }
        if (hour >= 5 && hour < 12) {
            return "Good morning";
        }
        if (hour >= 12 && hour < 17) {
            return "Good afternoon";
        }
        return "Good evening";
    }
}
