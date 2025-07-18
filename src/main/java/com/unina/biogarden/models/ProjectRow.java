package com.unina.biogarden.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class ProjectRow extends RecursiveTreeObject<ProjectRow> {
    private final SimpleStringProperty name, lot, startDate,endDate, status, actions;

    public ProjectRow(String n, String l, String std,String endt, String st, String a) {
        this.name = new SimpleStringProperty(n);
        this.lot = new SimpleStringProperty(l);
        this.startDate= new SimpleStringProperty(std);
        this.endDate = new SimpleStringProperty(endt);
        this.status = new SimpleStringProperty(st);
        this.actions = new SimpleStringProperty(a);
    }

    public SimpleStringProperty nameProperty() { return name; }
    public SimpleStringProperty lotProperty() { return lot; }
    public SimpleStringProperty startDateProperty() { return startDate; }
    public SimpleStringProperty endDateProperty() { return endDate; }

    public SimpleStringProperty statusProperty() { return status; }
    public SimpleStringProperty actionsProperty() { return actions; }
}
