package com.unina.biogarden.dto.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

public class HarvestingActivityDTO extends ActivityDTO{
    private final int expectedQuantity;
    private final int actualQuantity;
    private final String unit;
    public HarvestingActivityDTO(int id, LocalDate date, ActivityStatus status, int expectedQuantity, int actualQuantity, String unit, int coltureID, int lotID, int farmerID) {
        super(id, date, status, ActivityType.HARVEST, coltureID,lotID,farmerID);
        this.expectedQuantity = expectedQuantity;
        this.actualQuantity = actualQuantity;
        this.unit = unit;
    }

    public int getExpectedQuantity() {
        return expectedQuantity;
    }
    public int getActualQuantity() {
        return actualQuantity;
    }
    public String getUnit() {
        return unit;
    }
}
