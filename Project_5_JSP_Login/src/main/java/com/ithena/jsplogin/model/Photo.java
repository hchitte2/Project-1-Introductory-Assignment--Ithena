package com.ithena.jsplogin.model;

/** A profile photo as stored in the database. */
public record Photo(byte[] bytes, String contentType) {
}
