package com.unina.biogarden.dto.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

public class SeedingActivityDTO extends ActivityDTO{

    private final int quantity;
    private final String unit;

    public SeedingActivityDTO(int id, LocalDate date, ActivityStatus status, int quantity, String unit, int coltureID, int lotID, int farmerID) {
        super(id, date, status, ActivityType.SEEDING, coltureID,lotID,farmerID);
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

}
