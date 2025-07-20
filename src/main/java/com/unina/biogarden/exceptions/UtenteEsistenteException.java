package com.unina.biogarden.exceptions;

/**
 * Eccezione lanciata quando si tenta di creare un utente con credenziali (es. email)
 * che sono già in uso da un utente esistente nel sistema.
 * @author Il Tuo Nome
 */
public class UtenteEsistenteException extends Exception {
    /**
     * Costruisce una nuova istanza di {@code UtenteEsistenteException} con il messaggio di dettaglio specificato.
     * Il messaggio è inteso a fornire maggiori informazioni sulla causa dell'eccezione.
     *
     * @param message Il messaggio di dettaglio (che può essere recuperato in seguito tramite il metodo {@link Throwable#getMessage()}).
     */
    public UtenteEsistenteException(String message) {
        super(message);
    }
}