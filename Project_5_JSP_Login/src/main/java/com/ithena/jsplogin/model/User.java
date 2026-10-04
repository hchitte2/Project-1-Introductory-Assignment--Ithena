package com.ithena.jsplogin.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** A registered user's profile (no password or photo bytes). Getters are used by JSP EL. */
public final class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String userId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final LocalDate dateOfBirth;
    private final LocalDateTime createdAt;

    public User(String userId, String firstName, String lastName, String email,
                LocalDate dateOfBirth, LocalDateTime createdAt) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
