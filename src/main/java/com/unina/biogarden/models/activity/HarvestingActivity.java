package com.unina.biogarden.models.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

/**
 * Rappresenta un'attività di raccolta nel sistema BioGarden.
 * Estende la classe astratta {@link Activity} e include dettagli specifici
 * come la quantità prevista, la quantità effettiva e l'unità di misura del raccolto.
 * @author Il Tuo Nome
 */
public class HarvestingActivity extends Activity {
    private int plannedQuantity;
    private int actualQuantity;
    private String unit;

    /**
     * Costruisce una nuova istanza di {@code HarvestingActivity} con stato iniziale {@link ActivityStatus#PLANNED}.
     *
     * @param id L'identificatore univoco dell'attività di raccolta.
     * @param date La data in cui l'attività è pianificata o è stata svolta.
     * @param farmerID L'ID dell'agricoltore responsabile dell'attività.
     * @param farmer Il nome dell'agricoltore responsabile.
     * @param plannedQuantity La quantità di prodotto prevista per la raccolta.
     * @param actualQuantity La quantità di prodotto effettivamente raccolta.
     * @param unit L'unità di misura delle quantità (es. "kg", "pezzi").
     */
    public HarvestingActivity(int id, LocalDate date, int farmerID, String farmer, int plannedQuantity, int actualQuantity, String unit) {
        super(id, date, ActivityType.HARVEST, ActivityStatus.PLANNED, farmerID, farmer);
        this.plannedQuantity = plannedQuantity;
        this.actualQuantity = actualQuantity;
        this.unit = unit;
    }

    /**
     * Costruisce una nuova istanza di {@code HarvestingActivity} con uno stato specificato.
     *
     * @param id L'identificatore univoco dell'attività di raccolta.
     * @param date La data in cui l'attività è pianificata o è stata svolta.
     * @param status Lo stato attuale dell'attività (es. completata, in corso).
     * @param farmerID L'ID dell'agricoltore responsabile dell'attività.
     * @param farmer Il nome dell'agricoltore responsabile.
     * @param plannedQuantity La quantità di prodotto prevista per la raccolta.
     * @param actualQuantity La quantità di prodotto effettivamente raccolta.
     * @param unit L'unità di misura delle quantità (es. "kg", "pezzi").
     */
    public HarvestingActivity(int id, LocalDate date, ActivityStatus status, int farmerID, String farmer, int plannedQuantity, int actualQuantity, String unit) {
        super(id, date, ActivityType.HARVEST, status, farmerID, farmer);
        this.plannedQuantity = plannedQuantity;
        this.actualQuantity = actualQuantity;
        this.unit = unit;
    }

    /**
     * Restituisce la quantità di prodotto prevista per la raccolta.
     * @return La quantità prevista.
     */
    public int getPlannedQuantity() {
        return plannedQuantity;
    }

    /**
     * Restituisce la quantità di prodotto effettivamente raccolta.
     * @return La quantità effettiva.
     */
    public int getActualQuantity() {
        return actualQuantity;
    }

    /**
     * Restituisce l'unità di misura utilizzata per le quantità.
     * @return L'unità di misura (es. "kg").
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Imposta la quantità di prodotto prevista per la raccolta.
     * @param plannedQuantity La nuova quantità prevista.
     */
    public void setPlannedQuantity(int plannedQuantity) {
        this.plannedQuantity = plannedQuantity;
    }

    /**
     * Imposta la quantità di prodotto effettivamente raccolta.
     * @param actualQuantity La nuova quantità effettiva.
     */
    public void setActualQuantity(int actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    /**
     * Imposta l'unità di misura per le quantità.
     * @param unit La nuova unità di misura.
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Fornisce una stringa dettagliata che descrive l'attività di raccolta,
     * includendo la quantità prevista e quella effettiva con la rispettiva unità di misura.
     * @return Una stringa con i dettagli dell'attività di raccolta.
     */
    @Override
    public String getDetails() {
        return "Prevista: " + plannedQuantity + " " + unit + ", Effettiva: " + actualQuantity + " " + unit;
    }
}