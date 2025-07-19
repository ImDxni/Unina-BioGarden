package com.unina.biogarden.models.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

public class HarvestingActivity extends Activity{
    private final int plannedQuantity;
    private final int actualQuantity;
    private final String unit;

    public HarvestingActivity(int id, LocalDate date,int farmerID,String farmer, int plannedQuantity, int actualQuantity, String unit) {
        super(id, date, ActivityType.HARVEST, ActivityStatus.PLANNED, farmerID, farmer);
        this.plannedQuantity = plannedQuantity;
        this.actualQuantity = actualQuantity;
        this.unit = unit;
    }

    public HarvestingActivity(int id, LocalDate date, ActivityStatus status,int farmerID, String farmer, int plannedQuantity, int actualQuantity, String unit) {
        super(id, date, ActivityType.HARVEST, status, farmerID, farmer);
        this.plannedQuantity = plannedQuantity;
        this.actualQuantity = actualQuantity;
        this.unit = unit;
    }

    public int getPlannedQuantity() { return plannedQuantity; }
    public int getActualQuantity() { return actualQuantity; }
    public String getUnit() { return unit; }

    @Override
    public String getDetails() {
        return "Prevista: " + plannedQuantity + " " + unit + ", Effettiva: " + actualQuantity + " " + unit;
    }
}
