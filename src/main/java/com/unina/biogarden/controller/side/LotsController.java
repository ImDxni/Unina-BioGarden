package com.unina.biogarden.controller.side;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.unina.biogarden.controller.form.NewLotFormController;
import com.unina.biogarden.dao.LottoDAO;
import com.unina.biogarden.dao.ProgettoDAO;
import com.unina.biogarden.dto.LottoDTO;
import com.unina.biogarden.dto.ProgettoDTO;
import com.unina.biogarden.models.ProjectRow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.unina.biogarden.utils.Utils.showAlert;

public class LotsController {
    @FXML
    private VBox lotsContainer;

    @FXML
    private VBox projectsPerLotSection;

    @FXML
    private JFXTreeTableView<ProjectRow> projectsPerLotTable;
    @FXML
    private JFXTreeTableColumn<ProjectRow, String> nameCol;
    @FXML
    private JFXTreeTableColumn<ProjectRow, String> lotCol;
    @FXML
    private JFXTreeTableColumn<ProjectRow, String> seasonCol;
    @FXML
    private JFXTreeTableColumn<ProjectRow, String> statusCol;


    private final LottoDAO DAO = new LottoDAO();
    private final ProgettoDAO progettoDAO = new ProgettoDAO();
    private Pane selectedLotPane = null;

    @FXML
    public void initialize() {
        projectsPerLotTable.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);
        ProjectRow.initCellFactory(nameCol, lotCol, seasonCol, statusCol);

        Platform.runLater(() -> {
            loadLotsIntoSidebar();
        });

        projectsPerLotSection.setVisible(false);
        projectsPerLotSection.setManaged(false);

    }

    private void loadProjectsForLot(LottoDTO lot) {
        if (lot == null) {
            projectsPerLotSection.setVisible(false);
            projectsPerLotSection.setManaged(false);
            return;
        }

        try {
            Collection<ProgettoDTO> progettiDTO = progettoDAO.fetchProjectsByLot(lot.id());
            ObservableList<ProjectRow> data = progettiDTO.stream()
                    .map(p -> new ProjectRow(
                            p.nome(),
                            p.idLotto(),
                            p.dataInizio(),
                            p.dataFine()
                    ))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            TreeItem<ProjectRow> projectRoot = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
            projectsPerLotTable.setRoot(projectRoot);
            projectsPerLotTable.setShowRoot(false);

            projectsPerLotSection.setVisible(true);
            projectsPerLotSection.setManaged(true);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore Caricamento Progetti", "Impossibile caricare i progetti per il lotto selezionato.");
            e.printStackTrace();
            projectsPerLotSection.setVisible(false);
            projectsPerLotSection.setManaged(false);
        }
    }

    private void loadLotsIntoSidebar() {
        lotsContainer.getChildren().clear(); // Pulisci i lotti esistenti
        selectedLotPane = null;

        Collection<LottoDTO> lots = DAO.getAllLots();

        LottoDTO firstLot = null;
        Pane firstLotPane = null;

        for (LottoDTO lot : lots) {
            Pane box = createLotBox(lot);
            lotsContainer.getChildren().add(box);

            if (firstLot == null) {
                firstLot = lot;
                firstLotPane = box;
            }
        }

        if (firstLot != null) {
            firstLotPane.getStyleClass().add("selected");
            selectedLotPane = firstLotPane;
            loadProjectsForLot(firstLot);

        } else {
            projectsPerLotSection.setVisible(false);
            projectsPerLotSection.setManaged(false);
        }
    }

    private Pane createLotBox(LottoDTO lot) {
        Label name = new Label(lot.nome());
        name.setStyle("-fx-font-weight: 600; -fx-font-size: 14px; -fx-text-fill: #222;");

        Label area = new Label(lot.area() + " km²");
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


    @FXML
    private void handleNewLotCreation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/form/lot-form-view.fxml")); // Percorso corretto
            Parent root = loader.load();
            root.requestLayout();
            root.layout();
            NewLotFormController formController = loader.getController();

            formController.setOnLotCreated(() -> {
                Platform.runLater(this::loadLotsIntoSidebar);
            });

            Stage stage = new Stage();
            stage.setTitle("Crea Nuovo Lotto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Impossibile aprire il form di creazione lotto");
            alert.setContentText("Si è verificato un errore durante il caricamento del modulo. Controlla il percorso del file FXML.");
            alert.showAndWait();
        }
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

}