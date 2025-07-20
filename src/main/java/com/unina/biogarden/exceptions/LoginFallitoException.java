package com.unina.biogarden.exceptions;

/**
 * Eccezione lanciata quando un tentativo di login fallisce, tipicamente a causa di credenziali non corrette.
 * Questa è un'eccezione controllata (checked exception), il che significa che deve essere dichiarata
 * nella clausola {@code throws} di un metodo o gestita all'interno di un blocco {@code try-catch}.
 * @author Il Tuo Nome
 */
public class LoginFallitoException extends Exception {

    /**
     * Costruisce una nuova istanza di {@code LoginFallitoException} con il messaggio di dettaglio specificato.
     * Il messaggio è inteso a fornire maggiori informazioni sulla causa del fallimento del login.
     *
     * @param message Il messaggio di dettaglio (che può essere recuperato in seguito tramite il metodo {@link Throwable#getMessage()}).
     */
    public LoginFallitoException(String message) {
        super(message);
    }
}