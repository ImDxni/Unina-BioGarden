package com.unina.biogarden.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Crop extends RecursiveTreeObject<Crop> {
    private final int id;
    private final StringProperty name;
    private final StringProperty growthTime;
    private final StringProperty seeded;
    private final StringProperty harvested;

    public Crop(int id, String name, int growthTime) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.growthTime = new SimpleStringProperty(growthTime + " Giorni");
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

    @Override
    public String toString() {
        return nameProperty().get() + " (" + growthTimeProperty().get() + ")";
    }

    public int getId() {
        return id;
    }
}