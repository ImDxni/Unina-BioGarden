package com.unina.biogarden.controller.side;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.unina.biogarden.models.ProjectRow;
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
/*
    public void initialize() {
        projectTable.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);

        initCellFactory();

        ObservableList<ProjectRow> data = FXCollections.observableArrayList(
                new ProjectRow("Orto Estate", "A", "Summer", "Active", "View"),
                new ProjectRow("Fioritura Primaverile", "B", "Spring", "Completed", "View")
        );

        //TODO FETCH DATA FROM DATABASE


        TreeItem<ProjectRow> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        projectTable.setRoot(root);
        projectTable.setShowRoot(false);
    }

    private void initCellFactory() {
        nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
        lotCol.setCellValueFactory(param -> param.getValue().getValue().lotProperty());
        seasonCol.setCellValueFactory(param -> param.getValue().getValue().seasonProperty());
        statusCol.setCellValueFactory(param -> param.getValue().getValue().statusProperty());
        actionsCol.setCellValueFactory(param -> param.getValue().getValue().actionsProperty());
    }
*/
}
