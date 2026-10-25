
package com.example.farmbook;

/** Tracks the currently authenticated FarmBook user and login session. */
public final class SessionState {

    private static boolean authenticated = false;
    private static String currentUsername = null;
    private static long sessionVersion = 0;

    private SessionState() {
    }

    /** Legacy login path; does not grant access to any named user's records. */
    public static synchronized void login() {
        sessionVersion++;
        authenticated = true;
        currentUsername = null;
    }

    /** Starts a new session for a named user. */
    public static synchronized void login(String username) {
        sessionVersion++;
        authenticated = true;
        currentUsername = username;
    }

    /** Revokes the current session, including any DAOs bound to it. */
    public static synchronized void logout() {
        sessionVersion++;
        authenticated = false;
        currentUsername = null;
    }

    public static synchronized boolean isAuthenticated() {
        return authenticated;
    }

    public static synchronized String getCurrentUsername() {
        return currentUsername;
    }

    public static synchronized long getSessionVersion() {
        return sessionVersion;
    }

    /** Checks the username and session version together. */
    public static synchronized boolean isCurrentSession(
            String username, long version) {
        return authenticated
                && currentUsername != null
                && currentUsername.equals(username)
                && sessionVersion == version;
    }
}
