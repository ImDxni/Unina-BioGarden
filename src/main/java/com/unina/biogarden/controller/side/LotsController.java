package com.unina.biogarden.controller.side;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.unina.biogarden.dao.LottoDAO;
import com.unina.biogarden.dto.LottoDTO;
import com.unina.biogarden.models.ProjectRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class LotsController {
    @FXML
    private VBox lotsContainer;

    @FXML
    private VBox projectsPerLotSection;

    @FXML
    private JFXTreeTableView<ProjectRow> projectsPerLotTable;

    @FXML
    private JFXTreeTableColumn<ProjectRow, String> projNameCol;

    @FXML
    private JFXTreeTableColumn<ProjectRow, String> projStartCol;

    @FXML
    private JFXTreeTableColumn<ProjectRow, String> projEndCol;

    @FXML
    private JFXTreeTableColumn<ProjectRow, String> projActionsCol;

    private final LottoDAO DAO = new LottoDAO();
    private Pane selectedLotPane = null;

    @FXML
    public void initialize() {
        projectsPerLotTable.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);
        initCellFactory();

        Collection<LottoDTO> lots = DAO.getAllLots();

        // Variabile per tenere traccia del primo lotto
        LottoDTO firstLot = null;
        Pane firstLotPane = null;


        for (LottoDTO lot : lots) {
            Pane box = createLotBox(lot);
            lotsContainer.getChildren().add(box);

            // Se è il primo lotto, memorizzalo
            if (firstLot == null) {
                firstLot = lot;
                firstLotPane = box;
            }
        }

        // Seleziona automaticamente il primo lotto se presente
        if (firstLot != null) {
            // Applica la classe 'selected' al primo lotto
            firstLotPane.getStyleClass().add("selected");
            selectedLotPane = firstLotPane; // Imposta come lotto attualmente selezionato

            // Carica i progetti per il primo lotto
            loadProjectsForLot(firstLot);
        } else {
            // Se non ci sono lotti, assicurati che la sezione progetti sia nascosta
            projectsPerLotSection.setVisible(false);
            projectsPerLotSection.setManaged(false);
        }
    }

    private void loadProjectsForLot(LottoDTO lot) {
        ObservableList<ProjectRow> data = FXCollections.observableArrayList(
                new ProjectRow("Progetto Ortaggi per " + lot.nome(), "Lotto 1", "01/03/2025", "30/06/2025", "In Corso", "Vedi"),
                new ProjectRow("Progetto Frutta per " + lot.nome(), "Lotto 2", "15/04/2025", "15/08/2025", "Pianificazione", "Vedi"),
                new ProjectRow("Progetto Fiori per " + lot.nome(), "Lotto 3", "01/05/2025", "31/07/2025", "Completato", "Vedi")
        );

        TreeItem<ProjectRow> projectRoot = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        projectsPerLotTable.setRoot(projectRoot);
        projectsPerLotTable.setShowRoot(false);

        projectsPerLotSection.setVisible(true);
        projectsPerLotSection.setManaged(true);
    }

    private Pane createLotBox(LottoDTO lot) {
        Label name = new Label(lot.nome());
        name.setStyle("-fx-font-weight: 600; -fx-font-size: 14px; -fx-text-fill: #222;");

        Label area = new Label(lot.area()+" km²");
        area.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        VBox content = new VBox(name, area);
        content.setSpacing(4);
        content.setLayoutX(12);
        content.setLayoutY(12);

        Pane box = new Pane(content);
        box.setPrefSize(110, 80);
        box.getStyleClass().add("lot-pane");

        box.setOnMouseClicked(e -> {
            if (selectedLotPane != null) {
                selectedLotPane.getStyleClass().remove("selected");
            }
            box.getStyleClass().add("selected");
            selectedLotPane = box;

            loadProjectsForLot(lot);
        });

        return box;
    }

    private Callback<TreeTableColumn.CellDataFeatures<ProjectRow, String>, ObservableValue<String>> getSafeCellValueFactory(
            Function<ProjectRow, SimpleStringProperty> extractor) {
        return param -> {
            TreeItem<ProjectRow> item = param.getValue();
            if (item != null && item.getValue() != null) {
                return extractor.apply(item.getValue());
            }
            return new SimpleStringProperty("");
        };
    }

    private void initCellFactory() {
        projNameCol.setCellValueFactory(getSafeCellValueFactory(ProjectRow::nameProperty));
        projStartCol.setCellValueFactory(getSafeCellValueFactory(ProjectRow::startDateProperty));
        projEndCol.setCellValueFactory(getSafeCellValueFactory(ProjectRow::endDateProperty));
        projActionsCol.setCellValueFactory(getSafeCellValueFactory(ProjectRow::actionsProperty));
    }
}