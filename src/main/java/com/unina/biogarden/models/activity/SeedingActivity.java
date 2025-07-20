package com.unina.biogarden.models.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

public class SeedingActivity extends Activity{
    private int quantity;
    private String unit;


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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String getDetails() {
        return "Quantità: " + quantity + " " + unit;
    }
}
