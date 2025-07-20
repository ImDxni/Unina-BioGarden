package com.unina.biogarden.exceptions;

/**
 * Eccezione personalizzata lanciata quando si tenta di creare una coltivazione
 * che esiste già nel database, in violazione di un vincolo di unicità.
 * Questo indica tipicamente che una coltura di un certo tipo è già presente
 * per un determinato progetto.
 * @author Il Tuo Nome
 */
public class ColtureAlreadyExists extends Exception {

    /**
     * Costruisce una nuova istanza di {@code ColtureAlreadyExists} con il messaggio di dettaglio specificato.
     * Il messaggio è inteso a fornire maggiori informazioni sulla causa dell'eccezione.
     *
     * @param message Il messaggio di dettaglio (che può essere recuperato in seguito tramite il metodo {@link Throwable#getMessage()}).
     */
    public ColtureAlreadyExists(String message) {
        super(message);
    }
}