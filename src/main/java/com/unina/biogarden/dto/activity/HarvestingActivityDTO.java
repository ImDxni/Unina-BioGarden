package com.unina.biogarden.dto.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) che rappresenta un'attività di raccolta nel sistema BioGarden.
 * Estende {@link ActivityDTO} aggiungendo dettagli specifici per la raccolta,
 * come la quantità prevista, la quantità effettiva e l'unità di misura.
 * @author Il Tuo Nome
 */
public class HarvestingActivityDTO extends ActivityDTO {
    private final int expectedQuantity;
    private final int actualQuantity;
    private final String unit;

    /**
     * Costruisce una nuova istanza di {@code HarvestingActivityDTO}.
     *
     * @param id L'identificatore univoco dell'attività di raccolta.
     * @param date La data in cui l'attività di raccolta è stata o sarà svolta.
     * @param status Lo stato attuale dell'attività di raccolta (es. completata, in sospeso).
     * @param expectedQuantity La quantità di prodotto che si prevedeva di raccogliere.
     * @param actualQuantity La quantità di prodotto effettivamente raccolta.
     * @param unit L'unità di misura per le quantità (es. "kg", "pezzi").
     * @param coltureID L'ID della coltivazione a cui l'attività di raccolta è associata.
     * @param lotID L'ID del lotto in cui si trova la coltivazione.
     * @param farmerID L'ID dell'agricoltore responsabile dell'attività di raccolta.
     */
    public HarvestingActivityDTO(int id, LocalDate date, ActivityStatus status, int expectedQuantity, int actualQuantity, String unit, int coltureID, int lotID, int farmerID) {
        super(id, date, status, ActivityType.HARVEST, coltureID, lotID, farmerID);
        this.expectedQuantity = expectedQuantity;
        this.actualQuantity = actualQuantity;
        this.unit = unit;
    }

    /**
     * Restituisce la quantità di prodotto prevista per la raccolta.
     * @return La quantità prevista.
     */
    public int getExpectedQuantity() {
        return expectedQuantity;
    }

    /**
     * Restituisce la quantità di prodotto effettivamente raccolta.
     * @return La quantità effettiva raccolta.
     */
    public int getActualQuantity() {
        return actualQuantity;
    }

    /**
     * Restituisce l'unità di misura utilizzata per le quantità di raccolta.
     * @return L'unità di misura.
     */
    public String getUnit() {
        return unit;
    }
}