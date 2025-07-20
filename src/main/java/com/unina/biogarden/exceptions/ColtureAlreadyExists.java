package com.unina.biogarden.exceptions;

public class ColtureAlreadyExists extends Exception {

    /**
     * Constructs a new ColtureAlreadyExists exception with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method).
     */
    public ColtureAlreadyExists(String message) {
        super(message);
    }
}
