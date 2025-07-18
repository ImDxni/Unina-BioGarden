package com.unina.biogarden.controller;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;

import java.util.Arrays;
import java.util.List;

public class ProjectsController {

    @FXML
    private JFXTreeTableView<ProjectRow> projectTable;
    @FXML
    private JFXTreeTableColumn<ProjectRow, String> nameCol;
    @FXML
    private JFXTreeTableColumn<ProjectRow, String> lotCol;
    @FXML
    private JFXTreeTableColumn<ProjectRow, String> seasonCol;
    @FXML
    private JFXTreeTableColumn<ProjectRow, String> statusCol;
    @FXML
    private JFXTreeTableColumn<ProjectRow, String> actionsCol;

    public void initialize() {
        System.out.println("ProjectsController initialize!");
        projectTable.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);
        nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
        lotCol.setCellValueFactory(param -> param.getValue().getValue().lotProperty());
        seasonCol.setCellValueFactory(param -> param.getValue().getValue().seasonProperty());
        statusCol.setCellValueFactory(param -> param.getValue().getValue().statusProperty());
        actionsCol.setCellValueFactory(param -> param.getValue().getValue().actionsProperty());
        System.out.println("Colonne: " + projectTable.getColumns().size());
        ObservableList<ProjectRow> data = FXCollections.observableArrayList(
                new ProjectRow("Orto Estate", "A", "Summer", "Active", "View"),
                new ProjectRow("Fioritura Primaverile", "B", "Spring", "Completed", "View")
        );
        System.out.println("Righe: " + data.size());
        TreeItem<ProjectRow> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        projectTable.setRoot(root);
        projectTable.setShowRoot(false);
    }

    public static class ProjectRow extends RecursiveTreeObject<ProjectRow> {
        private final SimpleStringProperty name, lot, season, status, actions;

        public ProjectRow(String n, String l, String s, String st, String a) {
            this.name = new SimpleStringProperty(n);
            this.lot = new SimpleStringProperty(l);
            this.season = new SimpleStringProperty(s);
            this.status = new SimpleStringProperty(st);
            this.actions = new SimpleStringProperty(a);
        }

        public SimpleStringProperty nameProperty() { return name; }
        public SimpleStringProperty lotProperty() { return lot; }
        public SimpleStringProperty seasonProperty() { return season; }
        public SimpleStringProperty statusProperty() { return status; }
        public SimpleStringProperty actionsProperty() { return actions; }
    }
}
