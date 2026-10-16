package com.example.farmbook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests which greeting the dashboard shows at each hour.
 */
public class GreetingTest {

    @Test
    void morning_isFiveToEleven() {
        assertEquals("Good morning", Greeting.forHour(5));
        assertEquals("Good morning", Greeting.forHour(8));
        assertEquals("Good morning", Greeting.forHour(11));
    }

    @Test
    void afternoon_isTwelveToSixteen() {
        assertEquals("Good afternoon", Greeting.forHour(12));
        assertEquals("Good afternoon", Greeting.forHour(14));
        assertEquals("Good afternoon", Greeting.forHour(16));
    }

    @Test
    void evening_isSeventeenToFour() {
        assertEquals("Good evening", Greeting.forHour(17));
        assertEquals("Good evening", Greeting.forHour(23));
        assertEquals("Good evening", Greeting.forHour(0));
        assertEquals("Good evening", Greeting.forHour(4));
    }

    @Test
    void hourOutsideTheDay_isRejected() {
        assertThrows(IllegalArgumentException.class, () -> Greeting.forHour(-1));
        assertThrows(IllegalArgumentException.class, () -> Greeting.forHour(24));
    }
}
