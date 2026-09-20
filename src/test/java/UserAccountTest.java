import com.example.farmbook.PasswordHash;
import com.example.farmbook.SqliteConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for saving and finding users in the database, creates test users with "test_" so they
 * can be deleted after to not affect real accounts.
 */
public class UserAccountTest {

    private final Connection connection = SqliteConnection.getInstance();

    @BeforeEach
    void setUp() throws SQLException {
        // Make sure the users table exists
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "username TEXT NOT NULL UNIQUE,"
                    + "email TEXT NOT NULL UNIQUE,"
                    + "password TEXT"
                    + ")");
        }
        cleanUp(); // remove anything left over from a previous run
    }

    @AfterEach
    void cleanUp() throws SQLException {
        // Only removes accounts made by the tests.
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM users WHERE username LIKE 'test\\_%' ESCAPE '\\'")) {
            stmt.executeUpdate();
        }
    }

    /**
     *  Saves a user to the database with a hashed password.
     */
    private void addUser(String username, String email, String password) throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO users (username, email, password) VALUES (?, ?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, PasswordHash.hash(password));
            stmt.executeUpdate();
        }
    }

    /**
     * Returns true if a user with this username and password is in the database.
     */
    private boolean canLogin(String username, String password) throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT 1 FROM users WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, PasswordHash.hash(password));
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    //register
    @Test
    void registeredUserCanBeFound() throws Exception {
        addUser("test_farmer", "test_farmer@example.com", "password123");

        assertTrue(canLogin("test_farmer", "password123"));
    }

    @Test
    void duplicateUsernameIsRejected() throws Exception {
        addUser("test_farmer", "test_farmer@example.com", "password123");

        assertThrows(SQLException.class,
                () -> addUser("test_farmer", "test_other@example.com", "password456"));
    }

    @Test
    void duplicateEmailIsRejected() throws Exception {
        addUser("test_farmer", "test_farmer@example.com", "password123");

        assertThrows(SQLException.class,
                () -> addUser("test_farmer2", "test_farmer@example.com", "password456"));
    }

    //login
    @Test
    void loginWithWrongPasswordFails() throws Exception {
        addUser("test_farmer", "test_farmer@example.com", "password123");

        assertFalse(canLogin("test_farmer", "wrongpassword"));
    }

    @Test
    void loginWithUnregisteredUserFails() throws Exception {
        assertFalse(canLogin("test_nobody", "password123"));
    }
}