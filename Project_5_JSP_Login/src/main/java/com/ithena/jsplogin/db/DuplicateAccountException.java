package com.ithena.jsplogin.db;

/** Thrown when a User ID or email address is already registered. */
public class DuplicateAccountException extends Exception {

    public DuplicateAccountException(String message) {
        super(message);
    }
}
