package com.ithena.jsplogin.security;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Salted PBKDF2 password hashing (JDK only, no extra libraries).
 * Stored format: {@code pbkdf2-sha512$<iterations>$<base64 salt>$<base64 hash>}.
 */
public final class PasswordHasher {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final String PREFIX = "pbkdf2-sha512";
    private static final int ITERATIONS = 210_000; // OWASP recommendation for PBKDF2-HMAC-SHA512
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    /** Used to spend the same time on unknown user IDs, so response timing does not reveal which IDs exist. */
    private static final String DUMMY_HASH = hash("dummy-password-for-timing");

    private PasswordHasher() {
    }

    public static String hash(String password) {
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] key = derive(password, salt, ITERATIONS, KEY_BITS);
        Base64.Encoder b64 = Base64.getEncoder();
        return PREFIX + "$" + ITERATIONS + "$" + b64.encodeToString(salt) + "$" + b64.encodeToString(key);
    }

    /** Returns true if {@code password} matches {@code storedHash}. A null hash still costs one full hash computation. */
    public static boolean verify(String password, String storedHash) {
        if (password == null) {
            return false;
        }
        if (storedHash == null) {
            verify(password, DUMMY_HASH);
            return false;
        }
        String[] parts = storedHash.split("\\$");
        if (parts.length != 4 || !PREFIX.equals(parts[0])) {
            return false;
        }
        try {
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expected = Base64.getDecoder().decode(parts[3]);
            byte[] actual = derive(password, salt, iterations, expected.length * 8);
            return MessageDigest.isEqual(expected, actual);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] derive(String password, byte[] salt, int iterations, int keyBits) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyBits);
        try {
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2 is not available in this JVM", e);
        } finally {
            spec.clearPassword();
        }
    }
}
