package com.example.farmbook;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHash {

    /**
     * Turns a password into a SHA-256 hash so the real password is never stored in the database.
     * @param password the plain text password entered by the user
     * @return the hashed password as a 64 char hex string
     * @throws NoSuchAlgorithmException If SHA-256 is not available
     */
    public static String hash(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    /**
     * Converts the hashed bytes into readable hex text so it can be stored in the database.
     * @param hash the bytes produced by the SHA-256 hash
     * @return the hash written as a hex string
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
