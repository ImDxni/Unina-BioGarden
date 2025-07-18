package com.unina.biogarden.models;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.function.Function;

public class ProjectRow extends RecursiveTreeObject<ProjectRow> {
    private final SimpleStringProperty name, lot, season, status;

    public ProjectRow(String name, int idLotto, LocalDate startDate, LocalDate endDate) {
        this.name = new SimpleStringProperty(name);

        this.lot = new SimpleStringProperty("Lotto ID: " + idLotto);
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


    private static Callback<TreeTableColumn.CellDataFeatures<ProjectRow, String>, ObservableValue<String>> getSafeCellValueFactory(
            Function<ProjectRow, SimpleStringProperty> extractor) {
        return param -> {
            TreeItem<ProjectRow> item = param.getValue();
            if (item != null && item.getValue() != null) {
                return extractor.apply(item.getValue());
            }
            return new SimpleStringProperty("");
        };
    }

    public static void initCellFactory(JFXTreeTableColumn<ProjectRow, String> nameCol,
                                       JFXTreeTableColumn<ProjectRow, String> lotCol,
                                       JFXTreeTableColumn<ProjectRow, String> seasonCol,
                                       JFXTreeTableColumn<ProjectRow, String> statusCol) {

        nameCol.setCellValueFactory(getSafeCellValueFactory(ProjectRow::nameProperty));
        lotCol.setCellValueFactory(getSafeCellValueFactory(ProjectRow::lotProperty));
        seasonCol.setCellValueFactory(getSafeCellValueFactory(ProjectRow::seasonProperty));
        statusCol.setCellValueFactory(getSafeCellValueFactory(ProjectRow::statusProperty));
    }


}
