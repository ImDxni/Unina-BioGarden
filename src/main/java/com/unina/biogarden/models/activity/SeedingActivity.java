package com.unina.biogarden.models.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

public class SeedingActivity extends Activity{
    private final int quantity;
    private final String unit;


    public SeedingActivity(int id, LocalDate date, ActivityStatus status, int farmerID,String farmer, int quantity, String unit) {
        super(id, date, ActivityType.SEEDING, status, farmerID, farmer);
        this.quantity = quantity;
        this.unit = unit;
    }

    public SeedingActivity(int id, LocalDate date,int farmerID, String farmer, int quantity, String unit) {
        super(id, date, ActivityType.SEEDING, ActivityStatus.PLANNED, farmerID, farmer);
        this.quantity = quantity;
        this.unit = unit;
    }
    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String getDetails() {
        return "Quantità: " + quantity + " " + unit;
    }
}
