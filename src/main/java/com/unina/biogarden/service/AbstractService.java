package com.unina.biogarden.service;

import java.util.Collection;

/**
 * Classe astratta generica che definisce il contratto base per i servizi nell'applicazione BioGarden.
 * Un servizio è responsabile della logica di business e dell'interazione con il livello di persistenza (DAO).
 * Fornisce metodi astratti per l'inserimento di entità e il recupero di tutte le entità di un certo tipo.
 *
 * @param <T> Il tipo di entità gestito dal servizio.
 * @author Il Tuo Nome
 */
public abstract class AbstractService<T> {

    /**
     * Inserisce una nuova entità nel sistema.
     * Le implementazioni concrete di questo metodo devono gestire la logica di validazione
     * e l'interazione con il corrispondente Data Access Object (DAO) per la persistenza.
     *
     * @param entity L'entità di tipo {@code T} da inserire.
     * @return L'entità inserita, potenzialmente con attributi aggiornati (es. ID generato).
     * @throws Exception Se si verifica un errore durante l'operazione di inserimento,
     * come una violazione di vincoli di unicità o altri problemi di business logic.
     */
    public abstract T insert(T entity) throws Exception;

    /**
     * Recupera tutte le entità di tipo {@code T} dal sistema.
     * Le implementazioni concrete di questo metodo devono interagire con il DAO
     * per ottenere la collezione completa di entità.
     *
     * @return Una {@link Collection} di tutte le entità di tipo {@code T} presenti nel sistema.
     */
    public abstract Collection<T> fetchAll();
}