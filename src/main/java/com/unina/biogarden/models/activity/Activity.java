package com.unina.biogarden.models.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

/**
 * Classe astratta base che rappresenta un'attività generica nel sistema BioGarden.
 * Fornisce gli attributi e i metodi comuni a tutti i tipi specifici di attività,
 * come l'ID, la data, il tipo, lo stato e le informazioni sull'agricoltore responsabile.
 * Richiede l'implementazione del metodo astratto {@code getDetails()} da parte delle sottoclassi
 * per fornire una descrizione specifica dell'attività.
 * @author Il Tuo Nome
 */
public abstract class Activity {
    private int id;
    private LocalDate date;
    private ActivityType type;
    private ActivityStatus status;

    private int farmerID;
    private String farmer;

    /**
     * Costruisce una nuova istanza di {@code Activity}.
     *
     * @param id L'identificatore univoco dell'attività.
     * @param date La data in cui l'attività è stata o sarà svolta.
     * @param type Il tipo di attività (es. semina, raccolta, irrigazione).
     * @param status Lo stato attuale dell'attività (es. pianificata, in corso, completata).
     * @param farmerID L'ID dell'agricoltore responsabile dell'attività.
     * @param farmer Il nome completo dell'agricoltore responsabile dell'attività.
     */
    public Activity(int id, LocalDate date, ActivityType type, ActivityStatus status, int farmerID, String farmer) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.status = status;
        this.farmerID = farmerID;
        this.farmer = farmer;
    }

    /**
     * Restituisce l'ID dell'agricoltore responsabile dell'attività.
     * @return L'ID dell'agricoltore.
     */
    public int getFarmerID() {
        return farmerID;
    }

    /**
     * Restituisce l'ID univoco dell'attività.
     * @return L'ID dell'attività.
     */
    public int getId() {
        return id;
    }

    /**
     * Restituisce la data in cui l'attività è stata o sarà svolta.
     * @return La data dell'attività.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Restituisce il tipo di attività.
     * @return Il tipo di attività.
     */
    public ActivityType getType() {
        return type;
    }

    /**
     * Restituisce lo stato attuale dell'attività.
     * @return Lo stato dell'attività.
     */
    public ActivityStatus getStatus() {
        return status;
    }

    /**
     * Imposta l'ID dell'attività.
     * @param id Il nuovo ID dell'attività.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Imposta la data dell'attività.
     * @param date La nuova data dell'attività.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Imposta il tipo di attività.
     * @param type Il nuovo tipo di attività.
     */
    public void setType(ActivityType type) {
        this.type = type;
    }

    /**
     * Imposta lo stato dell'attività.
     * @param status Il nuovo stato dell'attività.
     */
    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    /**
     * Imposta l'ID dell'agricoltore responsabile dell'attività.
     * @param farmerID Il nuovo ID dell'agricoltore.
     */
    public void setFarmerID(int farmerID) {
        this.farmerID = farmerID;
    }

    /**
     * Imposta il nome completo dell'agricoltore responsabile dell'attività.
     * @param farmer Il nuovo nome dell'agricoltore.
     */
    public void setFarmer(String farmer) {
        this.farmer = farmer;
    }

    /**
     * Restituisce il nome completo dell'agricoltore responsabile dell'attività.
     * @return Il nome dell'agricoltore.
     */
    public String getFarmer() {
        return farmer;
    }

    /**
     * Restituisce una stringa descrittiva dei dettagli specifici dell'attività.
     * Questo metodo deve essere implementato dalle classi concrete che estendono {@code Activity}.
     * @return Una stringa che descrive i dettagli specifici dell'attività.
     */
    public abstract String getDetails();
}