package com.example.farmbook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the email and password rules with a fake DAO, so no real database is used.
 */
public class AccountServiceTest {

    private FakeAccountDAO dao;
    private AccountService service;

    @BeforeEach
    void setUp() throws SQLException {
        dao = new FakeAccountDAO();
        service = new AccountService(dao);
        dao.addUser("vy", "vy@example.com", "secret1");
        dao.addUser("sam", "sam@example.com", "hunter22");
    }

    @Test
    void changeEmail_valid_savesNewEmail() throws SQLException {
        assertEquals(AccountService.Result.OK, service.changeEmail("vy", "new@example.com"));
        assertEquals("new@example.com", dao.getEmail("vy"));
    }

    @Test
    void changeEmail_badFormat_isRejectedAndNothingChanges() throws SQLException {
        for (String bad : new String[]{null, "", "plain", "no@dot", "sp ace@x.com", "@x.com"}) {
            assertEquals(AccountService.Result.INVALID_EMAIL, service.changeEmail("vy", bad));
        }
        assertEquals("vy@example.com", dao.getEmail("vy"));
    }

    @Test
    void changeEmail_usedByAnotherUser_isRejected() throws SQLException {
        assertEquals(AccountService.Result.EMAIL_TAKEN, service.changeEmail("vy", "sam@example.com"));
        assertEquals("vy@example.com", dao.getEmail("vy"));
    }

    @Test
    void changeEmail_sameAsOwnCurrent_isOk() {
        assertEquals(AccountService.Result.OK, service.changeEmail("vy", "vy@example.com"));
    }

    @Test
    void changeEmail_unknownUser_isRejected() {
        assertEquals(AccountService.Result.USER_NOT_FOUND, service.changeEmail("ghost", "g@example.com"));
    }

    @Test
    void changePassword_valid_savesNewPassword() throws SQLException {
        assertEquals(AccountService.Result.OK, service.changePassword("vy", "secret1", "newpass1", "newpass1"));
        assertTrue(dao.isValidLogin("vy", "newpass1"));
        assertFalse(dao.isValidLogin("vy", "secret1"));
    }

    @Test
    void changePassword_wrongCurrent_isRejected() throws SQLException {
        assertEquals(AccountService.Result.WRONG_PASSWORD, service.changePassword("vy", "wrong", "newpass1", "newpass1"));
        assertTrue(dao.isValidLogin("vy", "secret1"));
    }

    @Test
    void changePassword_confirmationDoesNotMatch_isRejected() throws SQLException {
        assertEquals(AccountService.Result.PASSWORDS_DO_NOT_MATCH, service.changePassword("vy", "secret1", "newpass1", "different"));
        assertTrue(dao.isValidLogin("vy", "secret1"));
    }

    @Test
    void changePassword_tooShort_isRejected() throws SQLException {
        assertEquals(AccountService.Result.PASSWORD_TOO_SHORT, service.changePassword("vy", "secret1", "abc", "abc"));
        assertTrue(dao.isValidLogin("vy", "secret1"));
    }

    @Test
    void changePassword_sameAsCurrent_isRejected() {
        assertEquals(AccountService.Result.SAME_PASSWORD, service.changePassword("vy", "secret1", "secret1", "secret1"));
    }

    @Test
    void databaseFailure_isReportedNotThrown() {
        dao.failing = true;
        assertEquals(AccountService.Result.DATABASE_ERROR, service.changeEmail("vy", "x@example.com"));
        assertEquals(AccountService.Result.DATABASE_ERROR, service.changePassword("vy", "secret1", "newpass1", "newpass1"));
    }

    /** In-memory stand-in for the users table. */
    private static class FakeAccountDAO implements IAccountDAO {
        private final Map<String, String> emails = new HashMap<>();
        private final Map<String, String> passwords = new HashMap<>();
        boolean failing = false;

        private void check() throws SQLException {
            if (failing) {
                throw new SQLException("database down");
            }
        }

        @Override
        public void addUser(String username, String email, String password) throws SQLException {
            check();
            emails.put(username, email);
            passwords.put(username, password);
        }

        @Override
        public boolean isValidLogin(String username, String password) throws SQLException {
            check();
            return password != null && password.equals(passwords.get(username));
        }

        @Override
        public boolean usernameExists(String username) throws SQLException {
            check();
            return emails.containsKey(username);
        }

        @Override
        public boolean emailExists(String email) throws SQLException {
            check();
            return emails.containsValue(email);
        }

        @Override
        public String getEmail(String username) throws SQLException {
            check();
            return emails.get(username);
        }

        @Override
        public void updateEmail(String username, String newEmail) throws SQLException {
            check();
            emails.put(username, newEmail);
        }

        @Override
        public void updatePassword(String username, String newPassword) throws SQLException {
            check();
            passwords.put(username, newPassword);
        }
    }
}
