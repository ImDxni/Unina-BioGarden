package com.unina.biogarden.exceptions;

/**
 * Exception thrown when an attempt is made to create a user that already exists.
 */
public class UtenteEsistenteException extends Exception {
    /**
     * Constructs a new UtenteEsistenteException with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method).
     */
    public UtenteEsistenteException(String message) {
        super(message);
    }
}