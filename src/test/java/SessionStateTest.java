package com.example.farmbook;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SessionStateTest {

    @BeforeEach
    public void setUp() {
        SessionState.logout();
    }

    @AfterEach
    public void tearDown() {
        SessionState.logout();
    }

    @Test
    public void logoutClearsAuthenticatedSession() {
        SessionState.login();
        assertTrue(SessionState.isAuthenticated());

        SessionState.logout();

        assertFalse(SessionState.isAuthenticated());
    }

    @Test
    public void repeatedLogoutKeepsSessionUnauthenticated() {
        SessionState.login();
        SessionState.logout();

        assertDoesNotThrow(() -> SessionState.logout());
        assertFalse(SessionState.isAuthenticated());
    }

    @Test
    public void userCanLoginAgainAfterLogout() {
        SessionState.login();
        SessionState.logout();
        assertFalse(SessionState.isAuthenticated());

        SessionState.login();

        assertTrue(SessionState.isAuthenticated());
    }
}