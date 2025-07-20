package com.unina.biogarden.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) che rappresenta un progetto nel sistema BioGarden.
 * Questa è una classe record immutabile che incapsula i dettagli di un progetto specifico,
 * inclusi il suo identificatore, il nome, le date di inizio e fine, e l'ID del lotto associato.
 *
 * @param id L'identificatore univoco del progetto.
 * @param nome Il nome del progetto.
 * @param dataInizio La data di inizio del progetto.
 * @param dataFine La data di fine prevista del progetto.
 * @param idLotto L'ID del lotto di terreno su cui si svolge il progetto.
 * @author Il Tuo Nome
 */
public record ProjectDTO(int id, String nome, LocalDate dataInizio, LocalDate dataFine, int idLotto) {

}