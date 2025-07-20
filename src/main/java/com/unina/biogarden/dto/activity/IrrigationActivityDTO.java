package com.unina.biogarden.dto.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) che rappresenta un'attività di irrigazione nel sistema BioGarden.
 * Estende {@link ActivityDTO} e non aggiunge attributi specifici, in quanto l'irrigazione
 * è caratterizzata principalmente dalla sua data, stato e associazione a una coltivazione.
 * @author Il Tuo Nome
 */
public class IrrigationActivityDTO extends ActivityDTO {

    /**
     * Costruisce una nuova istanza di {@code IrrigationActivityDTO}.
     * Questo costruttore permette di specificare esplicitamente il tipo di attività.
     *
     * @param id L'identificatore univoco dell'attività di irrigazione.
     * @param date La data in cui l'attività di irrigazione è stata o sarà svolta.
     * @param status Lo stato attuale dell'attività di irrigazione (es. completata, in sospeso).
     * @param type Il tipo di attività, che dovrebbe essere {@link ActivityType#IRRIGATION}.
     * @param coltureID L'ID della coltivazione a cui l'attività di irrigazione è associata.
     * @param lotID L'ID del lotto in cui si trova la coltivazione.
     * @param farmerID L'ID dell'agricoltore responsabile dell'attività di irrigazione.
     */
    public IrrigationActivityDTO(int id, LocalDate date, ActivityStatus status, ActivityType type, int coltureID, int lotID, int farmerID) {
        super(id, date, status, type, coltureID, lotID, farmerID);
    }

    /**
     * Costruisce una nuova istanza di {@code IrrigationActivityDTO}.
     * Questo costruttore imposta automaticamente il tipo di attività a {@link ActivityType#IRRIGATION}.
     *
     * @param id L'identificatore univoco dell'attività di irrigazione.
     * @param date La data in cui l'attività di irrigazione è stata o sarà svolta.
     * @param status Lo stato attuale dell'attività di irrigazione (es. completata, in sospeso).
     * @param coltureID L'ID della coltivazione a cui l'attività di irrigazione è associata.
     * @param lotID L'ID del lotto in cui si trova la coltivazione.
     * @param farmerID L'ID dell'agricoltore responsabile dell'attività di irrigazione.
     */
    public IrrigationActivityDTO(int id, LocalDate date, ActivityStatus status, int coltureID, int lotID, int farmerID) {
        super(id, date, status, ActivityType.IRRIGATION, coltureID, lotID, farmerID);
    }
}