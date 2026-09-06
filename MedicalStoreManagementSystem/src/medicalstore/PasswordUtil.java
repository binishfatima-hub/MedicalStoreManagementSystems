package medicalstore;

import java.security.MessageDigest;

/**
 * Same hashing as the website backend, so the desktop app and the
 * admin panel can share the SAME admin row in the database.
 *
 * Passwords are never stored as plain text - only their SHA-256 hash is.
 */
public class PasswordUtil {

    /** Converts a plain password into a SHA-256 hex string. */
    public static String hash(String plainPassword) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] bytes = digest.digest(plainPassword.getBytes("UTF-8"));

            StringBuilder builder = new StringBuilder();

            for (byte b : bytes) {
                builder.append(String.format("%02x", b));
            }

            return builder.toString();

        } catch (Exception e) {
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
