package com.unina.biogarden.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Crop extends RecursiveTreeObject<Crop> {
    private final StringProperty name;
    private final StringProperty growthTime;
    private final StringProperty seeded;
    private final StringProperty harvested;

    public Crop(String name, int growthTime) {
        this.name = new SimpleStringProperty(name);
        this.growthTime = new SimpleStringProperty(growthTime + " giorni");
        this.seeded = new SimpleStringProperty("0");
        this.harvested = new SimpleStringProperty("0");
    }

    // Getters per le Property
    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty growthTimeProperty() {
        return growthTime;
    }

    public StringProperty seededProperty() {
        return seeded;
    }

    public StringProperty harvestedProperty() {
        return harvested;
    }

    public void setSeeded(String seededCount) {
        this.seeded.set(seededCount);
    }

    public void setHarvested(String harvestedCount) {
        this.harvested.set(harvestedCount);
    }
}