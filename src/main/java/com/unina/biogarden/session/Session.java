package com.unina.biogarden.session;

import com.unina.biogarden.dto.UserDTO;

/**
 * Gestisce la sessione utente corrente nell'applicazione BioGarden.
 * Questa classe implementa il pattern **Singleton** per mantenere i dettagli
 * dell'utente attualmente loggato e fornisce metodi per il login, il logout,
 * il recupero dell'utente corrente e la verifica dello stato di autenticazione.
 * @author Il Tuo Nome
 */
public class Session {
    private static UserDTO currentuser;

    /**
     * Costruttore privato per prevenire l'istanziazione diretta,
     * garantendo il rispetto del pattern Singleton.
     */
    private Session() {
    }

    /**
     * Imposta l'utente corrente per la sessione, effettuando il login.
     *
     * @param utente L'oggetto {@link UserDTO} che rappresenta l'utente
     * che ha effettuato l'accesso con successo.
     */
    public static void login(UserDTO utente) {
        currentuser = utente;
    }

    /**
     * Recupera l'utente attualmente loggato nella sessione.
     *
     * @return L'oggetto {@link UserDTO} dell'utente corrente,
     * o {@code null} se nessun utente è loggato.
     */
    public static UserDTO getUtente() {
        return currentuser;
    }

    /**
     * Cancella l'utente corrente dalla sessione, effettuando il logout.
     * Dopo questa operazione, {@link #isAuthenticated()} restituirà {@code false}.
     */
    public static void logout() {
        currentuser = null;
    }

    /**
     * Verifica se c'è un utente attualmente autenticato nella sessione.
     *
     * @return {@code true} se un utente è loggato (cioè, {@code currentuser} non è null),
     * {@code false} altrimenti.
     */
    public static boolean isAuthenticated() {
        return currentuser != null;
    }
}