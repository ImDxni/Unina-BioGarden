package com.unina.biogarden.models;

/**
 * Rappresenta un agricoltore nel sistema BioGarden.
 * Questa classe immutabile incapsula i dettagli di un agricoltore,
 * come l'identificatore, il nome, il cognome e l'indirizzo email.
 * Fornisce metodi per accedere a queste informazioni e per ottenere il nome completo.
 * @author Il Tuo Nome
 */
public class Farmer {
    private final int id;
    private final String nome;
    private final String cognome;
    private final String email;

    /**
     * Costruisce una nuova istanza di {@code Farmer}.
     *
     * @param id L'identificatore univoco dell'agricoltore.
     * @param nome Il nome dell'agricoltore.
     * @param cognome Il cognome dell'agricoltore.
     * @param email L'indirizzo email dell'agricoltore.
     */
    public Farmer(int id, String nome, String cognome, String email) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    /**
     * Restituisce il nome completo dell'agricoltore, combinando nome e cognome.
     * @return Una stringa che rappresenta il nome completo dell'agricoltore.
     */
    public String getFullName() {
        return nome + " " + cognome;
    }

    /**
     * Restituisce il nome dell'agricoltore.
     * @return Il nome dell'agricoltore.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce l'ID univoco dell'agricoltore.
     * @return L'ID dell'agricoltore.
     */
    public int getId() {
        return id;
    }

    /**
     * Restituisce il cognome dell'agricoltore.
     * @return Il cognome dell'agricoltore.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Restituisce l'indirizzo email dell'agricoltore.
     * @return L'indirizzo email dell'agricoltore.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Restituisce una rappresentazione stringa di questo oggetto {@code Farmer},
     * che corrisponde al suo nome completo.
     * @return Il nome completo dell'agricoltore.
     */
    @Override
    public String toString() {
        return getFullName();
    }
}