package com.unina.biogarden.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class LotRow extends RecursiveTreeObject<LotRow> {
    private final SimpleStringProperty name;
    private final SimpleStringProperty area;

    public LotRow(String name, String area) {
        this.name = new SimpleStringProperty(name);
        this.area = new SimpleStringProperty(area);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty areaProperty() {
        return area;
    }
}

