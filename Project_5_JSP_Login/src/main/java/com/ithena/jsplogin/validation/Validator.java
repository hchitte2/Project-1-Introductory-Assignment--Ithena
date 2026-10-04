package com.ithena.jsplogin.validation;

import com.ithena.jsplogin.model.Registration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/** Form validation rules. Each method returns field name -> error message (empty map = valid). */
public final class Validator {

    public static final int MAX_PHOTO_BYTES = 2 * 1024 * 1024;

    private static final Pattern NAME = Pattern.compile("^\\p{L}[\\p{L} .'-]{0,49}$");
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern USER_ID = Pattern.compile("^[A-Za-z0-9._-]{3,20}$");
    private static final LocalDate EARLIEST_DOB = LocalDate.of(1900, 1, 1);

    private Validator() {
    }

    public static Map<String, String> validateRegistration(Registration r) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (blank(r.firstName())) {
            errors.put("firstName", "Enter your first name.");
        } else if (!NAME.matcher(r.firstName().trim()).matches()) {
            errors.put("firstName", "First name can use letters, spaces, hyphens, and apostrophes (up to 50).");
        }

        if (blank(r.lastName())) {
            errors.put("lastName", "Enter your last name.");
        } else if (!NAME.matcher(r.lastName().trim()).matches()) {
            errors.put("lastName", "Last name can use letters, spaces, hyphens, and apostrophes (up to 50).");
        }

        if (blank(r.email())) {
            errors.put("email", "Enter your email address.");
        } else if (r.email().trim().length() > 254 || !EMAIL.matcher(r.email().trim()).matches()) {
            errors.put("email", "Enter an email address like name@example.com.");
        }

        dateOfBirthError(r.dateOfBirth()).ifPresent(msg -> errors.put("dateOfBirth", msg));

        if (blank(r.userId())) {
            errors.put("userId", "Choose a User ID.");
        } else if (!USER_ID.matcher(r.userId().trim()).matches()) {
            errors.put("userId", "User ID must be 3–20 characters: letters, numbers, dots, hyphens, or underscores.");
        }

        passwordError(r.password(), r.confirmPassword()).ifPresent(msg -> errors.put("password", msg));

        if (r.photo() == null || r.photo().length == 0) {
            errors.put("photo", "Choose a profile photo.");
        } else if (r.photo().length > MAX_PHOTO_BYTES) {
            errors.put("photo", "Photo must be 2 MB or smaller.");
        } else if (ImageTypes.detect(r.photo()).isEmpty()) {
            errors.put("photo", "Photo must be a JPG, PNG, GIF, or WebP image.");
        }

        return errors;
    }

    public static Map<String, String> validatePasswordReset(String userId, String email, String dateOfBirth,
                                                            String password, String confirmPassword) {
        Map<String, String> errors = new LinkedHashMap<>();
        if (blank(userId)) {
            errors.put("userId", "Enter your User ID.");
        }
        if (blank(email)) {
            errors.put("email", "Enter the email address you registered with.");
        }
        dateOfBirthError(dateOfBirth).ifPresent(msg -> errors.put("dateOfBirth", msg));
        passwordError(password, confirmPassword).ifPresent(msg -> errors.put("password", msg));
        return errors;
    }

    /** Parses an ISO date (yyyy-MM-dd, as sent by {@code <input type="date">}); empty if invalid. */
    public static Optional<LocalDate> parseDate(String value) {
        if (blank(value)) {
            return Optional.empty();
        }
        try {
            return Optional.of(LocalDate.parse(value.trim()));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public static String normalizeUserId(String userId) {
        return userId == null ? "" : userId.trim().toLowerCase();
    }

    public static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private static Optional<String> dateOfBirthError(String value) {
        if (blank(value)) {
            return Optional.of("Enter your date of birth.");
        }
        Optional<LocalDate> date = parseDate(value);
        if (date.isEmpty()) {
            return Optional.of("Enter a valid date of birth.");
        }
        if (!date.get().isBefore(LocalDate.now()) || date.get().isBefore(EARLIEST_DOB)) {
            return Optional.of("Date of birth must be in the past and after 1900.");
        }
        return Optional.empty();
    }

    private static Optional<String> passwordError(String password, String confirm) {
        if (password == null || password.isEmpty()) {
            return Optional.of("Choose a password.");
        }
        if (password.length() < 8 || password.length() > 128
                || password.chars().noneMatch(Character::isLetter)
                || password.chars().noneMatch(Character::isDigit)) {
            return Optional.of("Password must be at least 8 characters and include a letter and a number.");
        }
        if (!password.equals(confirm)) {
            return Optional.of("Passwords do not match.");
        }
        return Optional.empty();
    }

    private static boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
