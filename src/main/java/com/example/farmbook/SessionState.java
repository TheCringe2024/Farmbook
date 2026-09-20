package com.example.farmbook;

public final class SessionState {

    private static boolean authenticated = false;

    private SessionState() {
    }

    public static void login() {
        authenticated = true;
    }

    public static void logout() {
        authenticated = false;
    }

    public static boolean isAuthenticated() {
        return authenticated;
    }
}