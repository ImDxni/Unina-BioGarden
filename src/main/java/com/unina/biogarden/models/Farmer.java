package com.unina.biogarden.models;

public class Farmer {
    private final int id;
    private final String nome;
    private final String cognome;
    private final String email;

    public Farmer(int id, String nome, String cognome, String email) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    public String getFullName() {
        return nome + " " + cognome;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString(){
        return getFullName();
    }

}
