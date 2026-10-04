package com.ithena.jsplogin.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordHasherTest {

    @Test
    void hashIsNotThePlainPasswordAndHasExpectedFormat() {
        String hash = PasswordHasher.hash("secret123");
        assertFalse(hash.contains("secret123"));
        assertTrue(hash.matches("pbkdf2-sha512\\$\\d+\\$[A-Za-z0-9+/=]+\\$[A-Za-z0-9+/=]+"), hash);
    }

    @Test
    void verifyAcceptsTheRightPasswordAndRejectsOthers() {
        String hash = PasswordHasher.hash("secret123");
        assertTrue(PasswordHasher.verify("secret123", hash));
        assertFalse(PasswordHasher.verify("Secret123", hash));
        assertFalse(PasswordHasher.verify("", hash));
        assertFalse(PasswordHasher.verify(null, hash));
    }

    @Test
    void samePasswordGetsADifferentSaltEachTime() {
        assertNotEquals(PasswordHasher.hash("secret123"), PasswordHasher.hash("secret123"));
    }

    @Test
    void malformedOrMissingHashesNeverVerify() {
        assertFalse(PasswordHasher.verify("secret123", null));
        assertFalse(PasswordHasher.verify("secret123", "secret123"));
        assertFalse(PasswordHasher.verify("secret123", "pbkdf2-sha512$abc$!!$!!"));
        assertFalse(PasswordHasher.verify("secret123", "md5$1$abc$def"));
    }
}
