package com.unina.biogarden.exceptions;

/**
 * Exception thrown when a login attempt fails, typically due to incorrect credentials.
 * This is a checked exception, meaning it must be declared in a method's throws clause
 * or handled within a try-catch block.
 */
public class LoginFallitoException extends Exception {

    /**
     * Constructs a new LoginFallitoException with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method).
     */
    public LoginFallitoException(String message) {
        super(message);
    }
}