package com.unina.biogarden.dto;

/**
 * Data Transfer Object (DTO) che rappresenta un tipo di coltura (o "Crop") nel sistema BioGarden.
 * Questa è una classe record immutabile che incapsula i dettagli di un tipo specifico di coltura,
 * inclusi il suo identificatore, il nome e il tempo di maturazione in giorni.
 *
 * @param id L'identificatore univoco del tipo di coltura.
 * @param nome Il nome del tipo di coltura (es. "Pomodoro", "Basilico").
 * @param giorniMaturazione Il numero di giorni stimato per la maturazione di questa coltura.
 * @author Il Tuo Nome
 */
public record CropDTO(int id, String nome, int giorniMaturazione) {

}