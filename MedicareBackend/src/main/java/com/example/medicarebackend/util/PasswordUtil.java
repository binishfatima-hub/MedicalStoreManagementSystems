package com.example.medicarebackend.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Passwords are never stored as plain text.
 * We convert the password into a SHA-256 hash and store that hash.
 *
 * The same password always produces the same hash, so at login time we
 * hash whatever the user typed and compare the two hashes.
 */
public class PasswordUtil {

    /** Converts a plain password into a SHA-256 hex string. */
    public static String hash(String plainPassword) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] bytes = digest.digest(plainPassword.getBytes("UTF-8"));

            StringBuilder builder = new StringBuilder();

            for (byte b : bytes) {
                // %02x  =  two-digit lower case hex
                builder.append(String.format("%02x", b));
            }

            return builder.toString();

        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Unable to hash password", e);
        }
    }

    /** Returns true when the typed password matches the stored hash. */
    public static boolean matches(String plainPassword, String storedHash) {

        if (plainPassword == null || storedHash == null) {
            return false;
        }

        return hash(plainPassword).equalsIgnoreCase(storedHash);
    }
}
