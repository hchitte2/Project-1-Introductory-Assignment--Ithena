package com.ithena.jsplogin.model;

/** Raw values submitted on the registration form, before validation. */
public record Registration(
        String firstName,
        String lastName,
        String email,
        String dateOfBirth,
        String userId,
        String password,
        String confirmPassword,
        byte[] photo) {
}
