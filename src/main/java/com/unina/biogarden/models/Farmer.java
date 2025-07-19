package com.unina.biogarden.models;

public class Farmer {
    private final String nome;
    private final String cognome;
    private final String email;

    public Farmer(String nome, String cognome, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return nome + " " + cognome;
    }
}
