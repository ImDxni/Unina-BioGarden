package com.unina.biogarden.dto;

import com.unina.biogarden.enumerations.ColtureStatus;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) che rappresenta una coltivazione nel sistema BioGarden.
 * Questa è una classe record immutabile che incapsula i dettagli di una specifica coltivazione,
 * inclusi il suo identificatore, la data di inizio, lo stato attuale, e gli ID
 * del progetto e del tipo di coltura associati, insieme al nome del tipo di coltura.
 *
 * @param id L'identificatore univoco della coltivazione.
 * @param startDate La data di inizio della coltivazione.
 * @param status Lo stato attuale della coltivazione (e.g., in attesa, in crescita, completata).
 * @param projectId L'ID del progetto a cui questa coltivazione appartiene.
 * @param cropId L'ID del tipo di coltura (e.g., pomodoro, basilico) di questa coltivazione.
 * @param cropName Il nome del tipo di coltura (e.g., "Pomodoro", "Basilico").
 * @author Il Tuo Nome
 */
public record ColtureDTO(
        int id,
        LocalDate startDate,
        ColtureStatus status,
        int projectId,
        int cropId,
        String cropName) {
}