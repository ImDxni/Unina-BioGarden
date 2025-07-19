package com.unina.biogarden.models.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

public abstract class Activity {
    private final int id;
    private final LocalDate date;
    private final ActivityType type;
    private final ActivityStatus status;

    private final int farmerID;
    private final String farmer;

    public Activity(int id, LocalDate date, ActivityType type, ActivityStatus status, int farmerID, String farmer) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.status = status;
        this.farmerID = farmerID;
        this.farmer = farmer;
    }

    public int getFarmerID() {
        return farmerID;
    }
    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public ActivityType getType() {
        return type;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public String getFarmer() {
        return farmer;
    }

    public abstract String getDetails();
}
