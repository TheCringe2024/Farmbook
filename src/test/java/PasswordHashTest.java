import com.example.farmbook.PasswordHash;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PasswordHash and checks that passwords are hashed correctly
 * before they are stored or compared during register and login.
 */
public class PasswordHashTest {

    @Test
    void hashIsNotThePlainPassword() throws Exception {
        String hashed = PasswordHash.hash("password123");

        assertNotEquals("password123", hashed, "The stored password should not be the plain text password");
    }

    @Test
    void samePasswordGivesSameHash() throws Exception {
        // Login only works if hashing the same password twice gives the same result
        assertEquals(PasswordHash.hash("password123"), PasswordHash.hash("password123"));
    }

    @Test
    void differentPasswordsGiveDifferentHashes() throws Exception {
        assertNotEquals(PasswordHash.hash("password123"), PasswordHash.hash("password124"));
    }

    @Test
    void hashIs64HexCharacters() throws Exception {
        String hashed = PasswordHash.hash("password123");

        assertEquals(64, hashed.length(), "SHA-256 hashes should always be 64 characters long");
        assertTrue(hashed.matches("[0-9a-f]+"), "Hash should only contain hex characters");
    }

    @Test
    void knownPasswordGivesKnownHash() throws Exception {
        // Known SHA-256 value for "password" and checks the hashing and hex conversion are correct
        assertEquals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
                PasswordHash.hash("password"));
    }

    @Test
    void emptyPasswordStillHashes() throws Exception {
        assertEquals(64, PasswordHash.hash("").length());
    }
}