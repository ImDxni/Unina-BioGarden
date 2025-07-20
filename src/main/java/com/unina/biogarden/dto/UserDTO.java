package com.unina.biogarden.dto;

import com.unina.biogarden.enumerations.UserType;

/**
 * Data Transfer Object (DTO) che rappresenta un utente nel sistema BioGarden.
 * Questa è una classe record immutabile che incapsula i dati dell'utente,
 * fornendo un modo pulito per trasferirli tra i diversi strati dell'applicazione.
 *
 * @param id L'identificatore univoco dell'utente.
 * @param nome Il nome dell'utente.
 * @param cognome Il cognome dell'utente.
 * @param password La password dell'utente (generalmente hashata per sicurezza).
 * @param email L'indirizzo email dell'utente (deve essere unico).
 * @param tipo Il tipo o ruolo dell'utente (es. {@link UserType#ADMIN}, {@link UserType#RESEARCHER}, {@link UserType#FARMER}).
 * @author Il Tuo Nome
 */
public record UserDTO(
        int id,
        String nome,
        String cognome,
        String password,
        String email,
        UserType tipo
) {
    /**
     * Restituisce il nome completo dell'utente, combinando nome e cognome.
     * @return Una stringa che rappresenta il nome completo dell'utente.
     */
    public String getFullName() {
        return nome + " " + cognome;
    }
}