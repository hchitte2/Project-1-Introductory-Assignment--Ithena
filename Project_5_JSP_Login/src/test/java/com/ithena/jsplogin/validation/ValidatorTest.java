package com.ithena.jsplogin.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ithena.jsplogin.TestImages;
import com.ithena.jsplogin.model.Registration;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ValidatorTest {

    private static Registration valid() {
        return new Registration("Ada", "Lovelace", "ada@example.com", "1990-12-10",
                "ada.l", "engine1843", "engine1843", TestImages.png());
    }

    @Test
    void validRegistrationHasNoErrors() {
        assertTrue(Validator.validateRegistration(valid()).isEmpty());
    }

    @Test
    void emptyRegistrationReportsEveryRequiredField() {
        Map<String, String> errors = Validator.validateRegistration(
                new Registration("", " ", null, "", "", "", "", null));
        assertEquals(Set.of("firstName", "lastName", "email", "dateOfBirth", "userId", "password", "photo"), errors.keySet());
    }

    @Test
    void rejectsBadFormats() {
        Registration r = new Registration("Ada<script>", "Lovelace", "not-an-email", "1990-13-45",
                "a!", "engine1843", "engine1843", TestImages.png());
        Map<String, String> errors = Validator.validateRegistration(r);
        assertEquals(Set.of("firstName", "email", "dateOfBirth", "userId"), errors.keySet());
    }

    @Test
    void acceptsNamesWithCombiningMarksAccentsAndCurlyApostrophes() {
        String[][] names = {
            {"प्रिया", "शर्मा"},            // Devanagari uses combining vowel signs (\p{M})
            {"Siobhán", "O\u2019Brien"},   // curly apostrophe, as typed by iOS smart punctuation
            {"Jose\u0301", "Núñez"},       // "José" in decomposed (NFD) form
            {"Mary-Jane", "St. John"},
        };
        for (String[] n : names) {
            Registration r = new Registration(n[0], n[1], "ada@example.com", "1990-12-10",
                    "ada.l", "engine1843", "engine1843", TestImages.png());
            assertTrue(Validator.validateRegistration(r).isEmpty(), n[0] + " " + n[1]);
        }
        assertEquals("Jos\u00e9", Validator.normalizeName(" Jose\u0301 "));
    }

    @Test
    void dateOfBirthMustBeInThePast() {
        String tomorrow = LocalDate.now().plusDays(1).toString();
        Registration r = new Registration("Ada", "Lovelace", "ada@example.com", tomorrow,
                "ada.l", "engine1843", "engine1843", TestImages.png());
        assertTrue(Validator.validateRegistration(r).containsKey("dateOfBirth"));
    }

    @Test
    void passwordRules() {
        assertEquals("Password must be at least 8 characters and include a letter and a number.",
                Validator.validatePasswordReset("u", "e", "1990-01-01", "short1", "short1").get("password"));
        assertTrue(Validator.validatePasswordReset("u", "e", "1990-01-01", "lettersonly", "lettersonly").containsKey("password"));
        assertTrue(Validator.validatePasswordReset("u", "e", "1990-01-01", "12345678", "12345678").containsKey("password"));
        assertEquals("Passwords do not match.",
                Validator.validatePasswordReset("u", "e", "1990-01-01", "engine1843", "engine1844").get("password"));
        assertTrue(Validator.validatePasswordReset("u", "e", "1990-01-01", "engine1843", "engine1843").isEmpty());
    }

    @Test
    void photoMustBeARealImageUnderTheSizeLimit() {
        byte[] html = "<html>not an image</html>".getBytes();
        Registration notImage = new Registration("Ada", "Lovelace", "ada@example.com", "1990-12-10",
                "ada.l", "engine1843", "engine1843", html);
        assertEquals("Photo must be a JPG, PNG, GIF, or WebP image.", Validator.validateRegistration(notImage).get("photo"));

        Registration tooBig = new Registration("Ada", "Lovelace", "ada@example.com", "1990-12-10",
                "ada.l", "engine1843", "engine1843", new byte[Validator.MAX_PHOTO_BYTES + 1]);
        assertEquals("Photo must be 2 MB or smaller.", Validator.validateRegistration(tooBig).get("photo"));
    }

    @Test
    void normalizesUserIdAndEmailToLowercase() {
        assertEquals("ada.l", Validator.normalizeUserId("  Ada.L "));
        assertEquals("ada@example.com", Validator.normalizeEmail(" ADA@Example.com"));
    }
}
