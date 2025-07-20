package com.unina.biogarden.controller.side;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.unina.biogarden.controller.form.CreateCropFormController;
import com.unina.biogarden.models.Crop;
import com.unina.biogarden.service.ProjectService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static com.unina.biogarden.utils.Utils.showAlert;

/**
 * Controller per la gestione e visualizzazione delle colture.
 * Questo controller gestisce una tabella che mostra i dettagli delle colture
 * e permette l'apertura di un form per la creazione di nuove colture.
 * @author Il Tuo Nome
 */
public class CropsController {

    @FXML
    private JFXTreeTableView<Crop> cropsTable;
    @FXML
    private JFXTreeTableColumn<Crop, String> nameCol;
    @FXML
    private JFXTreeTableColumn<Crop, String> growthTimeCol;
    @FXML
    private JFXTreeTableColumn<Crop, String> seededCol;
    @FXML
    private JFXTreeTableColumn<Crop, String> harvestedCol;

    private final ProjectService service = new ProjectService();

    /**
     * Inizializza il controller dopo che il suo FXML è stato completamente caricato.
     * Configura la politica di ridimensionamento delle colonne, inizializza le factory delle celle
     * e carica i dati delle colture nella tabella.
     */
    @FXML
    public void initialize() {
        cropsTable.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);
        initCellFactory();
        fetchAndPopulateCrops();
    }

    /**
     * Inizializza le cell factory per ogni colonna della tabella delle colture.
     * Collega le proprietà dei modelli {@code Crop} alle colonne della tabella.
     */
    private void initCellFactory() {
        nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
        growthTimeCol.setCellValueFactory(param -> param.getValue().getValue().growthTimeProperty());
        seededCol.setCellValueFactory(param -> new SimpleStringProperty("N/A"));
        harvestedCol.setCellValueFactory(param -> new SimpleStringProperty("N/A"));
    }

    /**
     * Recupera i dati delle colture dal servizio e popola la tabella {@code cropsTable}.
     * In caso di errore durante il caricamento, mostra un messaggio di avviso.
     */
    private void fetchAndPopulateCrops() {
        try {
            ObservableList<Crop> data = FXCollections.observableArrayList(service.getCrops());
            TreeItem<Crop> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
            cropsTable.setRoot(root);
            cropsTable.setShowRoot(false);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore Caricamento Colture", "Impossibile caricare le colture dal database.");
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'azione del bottone "Nuova Coltura".
     * Carica il form per la creazione di una nuova coltura, lo rende modale
     * e imposta un callback per aggiornare la tabella delle colture una volta che il form è stato chiuso
     * e una nuova coltura è stata creata.
     */
    @FXML
    private void handleNewCropButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/form/crop-form-view.fxml"));
            Parent root = loader.load();

            CreateCropFormController controller = loader.getController();
            controller.setOnCropCreated(this::fetchAndPopulateCrops);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nuova Coltura");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore Apertura Form", "Impossibile aprire il form di creazione coltura.");
        }
    }
}