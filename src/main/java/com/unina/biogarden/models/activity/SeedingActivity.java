package com.unina.biogarden.models.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

/**
 * Rappresenta un'attività di semina nel sistema BioGarden.
 * Estende la classe astratta {@link Activity} e include dettagli specifici
 * come la quantità di semi e l'unità di misura.
 * @author Il Tuo Nome
 */
public class SeedingActivity extends Activity {
    private int quantity;
    private String unit;

    /**
     * Costruisce una nuova istanza di {@code SeedingActivity} con uno stato specificato.
     *
     * @param id L'identificatore univoco dell'attività di semina.
     * @param date La data in cui l'attività è pianificata o è stata svolta.
     * @param status Lo stato attuale dell'attività (es. completata, in corso).
     * @param farmerID L'ID dell'agricoltore responsabile dell'attività.
     * @param farmer Il nome dell'agricoltore responsabile.
     * @param quantity La quantità di semi utilizzata per la semina.
     * @param unit L'unità di misura per la quantità di semi (es. "grammi", "pezzi").
     */
    public SeedingActivity(int id, LocalDate date, ActivityStatus status, int farmerID, String farmer, int quantity, String unit) {
        super(id, date, ActivityType.SEEDING, status, farmerID, farmer);
        this.quantity = quantity;
        this.unit = unit;
    }

    /**
     * Costruisce una nuova istanza di {@code SeedingActivity} con stato iniziale {@link ActivityStatus#PLANNED}.
     *
     * @param id L'identificatore univoco dell'attività di semina.
     * @param date La data in cui l'attività è pianificata o è stata svolta.
     * @param farmerID L'ID dell'agricoltore responsabile dell'attività.
     * @param farmer Il nome dell'agricoltore responsabile.
     * @param quantity La quantità di semi utilizzata per la semina.
     * @param unit L'unità di misura per la quantità di semi (es. "grammi", "pezzi").
     */
    public SeedingActivity(int id, LocalDate date, int farmerID, String farmer, int quantity, String unit) {
        super(id, date, ActivityType.SEEDING, ActivityStatus.PLANNED, farmerID, farmer);
        this.quantity = quantity;
        this.unit = unit;
    }

    /**
     * Restituisce la quantità di semi utilizzata per la semina.
     * @return La quantità di semi.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Restituisce l'unità di misura per la quantità di semi.
     * @return L'unità di misura.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Imposta la quantità di semi per l'attività di semina.
     * @param quantity La nuova quantità di semi.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Imposta l'unità di misura per la quantità di semi.
     * @param unit La nuova unità di misura.
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Fornisce una stringa dettagliata che descrive l'attività di semina,
     * includendo la quantità di semi e la rispettiva unità di misura.
     * @return Una stringa con i dettagli dell'attività di semina.
     */
    @Override
    public String getDetails() {
        return "Quantità: " + quantity + " " + unit;
    }
}