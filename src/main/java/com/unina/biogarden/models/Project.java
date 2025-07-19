package com.unina.biogarden.models;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.Collection;
import java.util.function.Function;

public class Project extends RecursiveTreeObject<Project> {
    private final SimpleStringProperty name, lot, season, status;

    public Project(String name, String lotName, LocalDate startDate, LocalDate endDate) {
        this.name = new SimpleStringProperty(name);

        this.lot = new SimpleStringProperty(lotName);
        this.season = new SimpleStringProperty(getSeason(startDate));
        this.status = new SimpleStringProperty(getStatus(startDate, endDate));
    }

    private String getSeason(LocalDate date) {
        if (date == null) return "N/A";
        int month = date.getMonthValue();
        if (month >= 3 && month <= 5) return "Primavera";
        if (month >= 6 && month <= 8) return "Estate";
        if (month >= 9 && month <= 11) return "Autunno";
        return "Inverno";
    }

    private String getStatus(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return "N/A";
        LocalDate now = LocalDate.now();
        if (now.isBefore(startDate)) return "Pianificato";
        if (now.isAfter(endDate)) return "Completato";
        return "In Corso";
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty lotProperty() {
        return lot;
    }

    public SimpleStringProperty seasonProperty() {
        return season;
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }


    private static Callback<TreeTableColumn.CellDataFeatures<Project, String>, ObservableValue<String>> getSafeCellValueFactory(
            Function<Project, SimpleStringProperty> extractor) {
        return param -> {
            TreeItem<Project> item = param.getValue();
            if (item != null && item.getValue() != null) {
                return extractor.apply(item.getValue());
            }
            return new SimpleStringProperty("");
        };
    }

    public static void initCellFactory(JFXTreeTableColumn<Project, String> nameCol,
                                       JFXTreeTableColumn<Project, String> lotCol,
                                       JFXTreeTableColumn<Project, String> seasonCol,
                                       JFXTreeTableColumn<Project, String> statusCol) {

        nameCol.setCellValueFactory(getSafeCellValueFactory(Project::nameProperty));
        lotCol.setCellValueFactory(getSafeCellValueFactory(Project::lotProperty));
        seasonCol.setCellValueFactory(getSafeCellValueFactory(Project::seasonProperty));
        statusCol.setCellValueFactory(getSafeCellValueFactory(Project::statusProperty));
    }


}
