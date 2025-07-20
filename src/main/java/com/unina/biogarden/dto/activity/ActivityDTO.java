package com.unina.biogarden.dto.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) che rappresenta un'attività generica nel sistema BioGarden.
 * Questa classe incapsula i dati comuni a tutti i tipi di attività,
 * come l'identificatore, la data, lo stato, il tipo e gli ID associati
 * a lotto, coltivazione e agricoltore.
 * È una classe base per DTO di attività più specifiche come {@link HarvestingActivityDTO}
 * e {@link SeedingActivityDTO}.
 * @author Il Tuo Nome
 */
public class ActivityDTO {
    private int id;
    private final LocalDate date;
    private final ActivityStatus status;
    private final ActivityType type;
    private final int lotID, coltureID, farmerID;

    /**
     * Costruisce una nuova istanza di {@code ActivityDTO}.
     * @param id L'identificatore univoco dell'attività.
     * @param date La data in cui l'attività è stata o sarà svolta.
     * @param status Lo stato attuale dell'attività (es. completata, in sospeso).
     * @param type Il tipo di attività (es. semina, raccolta, irrigazione).
     * @param coltureID L'ID della coltivazione a cui l'attività è associata.
     * @param lotID L'ID del lotto in cui si trova la coltivazione.
     * @param farmerID L'ID dell'agricoltore responsabile dell'attività.
     */
    public ActivityDTO(int id, LocalDate date, ActivityStatus status, ActivityType type, int coltureID, int lotID, int farmerID) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.type = type;
        this.coltureID = coltureID;
        this.lotID = lotID;
        this.farmerID = farmerID;
    }

    /**
     * Restituisce l'ID dell'attività.
     * @return L'ID dell'attività.
     */
    public int getId() {
        return id;
    }

    /**
     * Restituisce la data dell'attività.
     * @return La data in cui l'attività è stata o sarà svolta.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Restituisce lo stato dell'attività.
     * @return Lo stato attuale dell'attività.
     */
    public ActivityStatus getStatus() {
        return status;
    }

    /**
     * Restituisce il tipo di attività.
     * @return Il tipo specifico di attività.
     */
    public ActivityType getType() {
        return type;
    }

    /**
     * Restituisce l'ID della coltivazione associata a questa attività.
     * @return L'ID della coltivazione.
     */
    public int getColtureID() {
        return coltureID;
    }

    /**
     * Restituisce l'ID del lotto associato a questa attività.
     * @return L'ID del lotto.
     */
    public int getLotID() {
        return lotID;
    }

    /**
     * Restituisce l'ID dell'agricoltore responsabile di questa attività.
     * @return L'ID dell'agricoltore.
     */
    public int getFarmerID() {
        return farmerID;
    }
}