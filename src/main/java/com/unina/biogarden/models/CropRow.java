package com.unina.biogarden.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CropRow extends RecursiveTreeObject<CropRow> {
    private final StringProperty name;
    private final StringProperty growthTime; // Tempo di maturazione in giorni
    private final StringProperty seeded; // Campi per "Seminati" e "Raccolti" (non direttamente dal DTO)
    private final StringProperty harvested;

    public CropRow(String name, int growthTime) {
        this.name = new SimpleStringProperty(name);
        this.growthTime = new SimpleStringProperty(growthTime + " giorni"); // Formattazione
        this.seeded = new SimpleStringProperty("0"); // Placeholder, da popolare dinamicamente se i dati sono disponibili
        this.harvested = new SimpleStringProperty("0"); // Placeholder
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

    // Potresti voler aggiungere metodi per aggiornare seeded e harvested se i dati vengono caricati dopo
    public void setSeeded(String seededCount) {
        this.seeded.set(seededCount);
    }

    public void setHarvested(String harvestedCount) {
        this.harvested.set(harvestedCount);
    }
}