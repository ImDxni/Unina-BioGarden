package com.unina.biogarden.session;

import com.unina.biogarden.dto.UtenteDTO;

/**
 * Manages the current user session.
 * This class implements a singleton pattern to hold the details of the currently logged-in user
 * and provides methods for login, logout, retrieving the current user, and checking authentication status.
 */
public class Session {
    private static UtenteDTO currentuser;

    /**
     * Private constructor to prevent direct instantiation, enforcing the singleton pattern.
     */
    private Session() {}

    /**
     * Sets the current user for the session, effectively logging them in.
     *
     * @param utente The {@link UtenteDTO} object representing the user who has successfully logged in.
     */
    public static void login(UtenteDTO utente) {
        currentuser = utente;
    }

    /**
     * Retrieves the currently logged-in user.
     *
     * @return The {@link UtenteDTO} object of the current user, or null if no user is logged in.
     */
    public static UtenteDTO getUtente() {
        return currentuser;
    }

    /**
     * Clears the current user from the session, effectively logging them out.
     */
    public static void logout() {
        currentuser = null;
    }

    /**
     * Checks if there is a user currently authenticated in the session.
     *
     * @return true if a user is logged in (i.e., {@code currentuser} is not null), false otherwise.
     */
    public static boolean isAuthenticated() {
        return currentuser != null;
    }
}