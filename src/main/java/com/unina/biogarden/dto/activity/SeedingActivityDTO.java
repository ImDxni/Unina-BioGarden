package com.unina.biogarden.dto.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) che rappresenta un'attività di semina nel sistema BioGarden.
 * Estende {@link ActivityDTO} aggiungendo dettagli specifici per la semina,
 * come la quantità di semi e l'unità di misura.
 * @author Il Tuo Nome
 */
public class SeedingActivityDTO extends ActivityDTO {

    private final int quantity;
    private final String unit;

    /**
     * Costruisce una nuova istanza di {@code SeedingActivityDTO}.
     *
     * @param id L'identificatore univoco dell'attività di semina.
     * @param date La data in cui l'attività di semina è stata o sarà svolta.
     * @param status Lo stato attuale dell'attività di semina (es. completata, in sospeso).
     * @param quantity La quantità di semi utilizzata per la semina.
     * @param unit L'unità di misura per la quantità di semi (es. "grammi", "pezzi").
     * @param coltureID L'ID della coltivazione a cui l'attività di semina è associata.
     * @param lotID L'ID del lotto in cui si trova la coltivazione.
     * @param farmerID L'ID dell'agricoltore responsabile dell'attività di semina.
     */
    public SeedingActivityDTO(int id, LocalDate date, ActivityStatus status, int quantity, String unit, int coltureID, int lotID, int farmerID) {
        super(id, date, status, ActivityType.SEEDING, coltureID, lotID, farmerID);
        this.quantity = quantity;
        this.unit = unit;
    }

    /**
     * Restituisce la quantità di semi utilizzata per l'attività di semina.
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
}