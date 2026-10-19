package com.example.farmbook;

public final class SessionState {

    private static boolean authenticated = false;
    private static String currentUsername = null;

    private SessionState() {
    }

    public static void login() {
        authenticated = true;
    }

    /** Logs in and remembers which user it is, so Settings can show their account. */
    public static void login(String username) {
        authenticated = true;
        currentUsername = username;
    }

    public static void logout() {
        authenticated = false;
        currentUsername = null;
    }

    public static boolean isAuthenticated() {
        return authenticated;
    }

    /** The logged-in user's name, or null if nobody has logged in with a name. */
    public static String getCurrentUsername() {
        return currentUsername;
    }
}
