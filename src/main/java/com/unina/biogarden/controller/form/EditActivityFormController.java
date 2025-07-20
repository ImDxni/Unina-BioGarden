package com.unina.biogarden.controller.form;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;
import com.unina.biogarden.models.Farmer;
import com.unina.biogarden.models.activity.Activity;
import com.unina.biogarden.models.activity.HarvestingActivity;
import com.unina.biogarden.models.activity.SeedingActivity;
import com.unina.biogarden.service.ProjectService;
import com.unina.biogarden.service.UserService;
import com.unina.biogarden.utils.Utils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.Arrays;

import static com.unina.biogarden.utils.Utils.firstCapitalLetter;

public class EditActivityFormController extends AbstractForm {

    @FXML
    private DatePicker datePicker;
    @FXML
    private Label activityTypeLabel;
    @FXML
    private JFXComboBox<Farmer> farmerComboBox;
    @FXML
    private JFXComboBox<ActivityStatus> activityStatusComboBox; // NUOVO FXML FIELD
    @FXML
    private VBox specificFieldsContainer;

    // Campi per Semina
    private JFXTextField sowingQuantityField;
    private JFXTextField sowingUnitField;

    // Campi per Raccolta
    private JFXTextField harvestPlannedQuantityField;
    private JFXTextField harvestActualQuantityField;
    private JFXTextField harvestUnitField;

    private Activity currentActivity;
    private Runnable onActivityUpdated;
    private final ProjectService projectService = new ProjectService();
    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        try {
            farmerComboBox.setItems(FXCollections.observableArrayList(userService.fetchAllFarmer()));
            // Popola la ComboBox dello stato dell'attività
            activityStatusComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(ActivityStatus.values())));
        } catch (RuntimeException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Errore Caricamento", "Impossibile caricare i dati: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setActivity(Activity activity) {
        this.currentActivity = activity;

        // Popola i campi generici
        datePicker.setValue(currentActivity.getDate());
        activityTypeLabel.setText(firstCapitalLetter(currentActivity.getType().getDescription()));

        // Seleziona il coltivatore
        Farmer currentFarmer = null;
        for (Farmer farmer : farmerComboBox.getItems()) {
            if (farmer.getFullName().equals(currentActivity.getFarmer())) {
                currentFarmer = farmer;
                break;
            }
        }
        if (currentFarmer != null) {
            farmerComboBox.getSelectionModel().select(currentFarmer);
        } else {
            Utils.showAlert(Alert.AlertType.WARNING, "Coltivatore Non Trovato", "Il coltivatore associato all'attività (" + currentActivity.getFarmer() + ") non è stato trovato nell'elenco disponibile.");
            // Potresti voler deselezionare la ComboBox o impostare un valore di default qui
        }

        // Seleziona lo stato dell'attività
        activityStatusComboBox.getSelectionModel().select(currentActivity.getStatus());


        // Popola i campi specifici in base al tipo di attività
        updateSpecificFields(currentActivity.getType());

        // Prepopola i campi specifici
        switch (currentActivity.getType()) {
            case SEEDING:
                SeedingActivity sowing = (SeedingActivity) currentActivity;
                sowingQuantityField.setText(String.valueOf(sowing.getQuantity()));
                sowingUnitField.setText(sowing.getUnit());
                break;
            case HARVEST:
                HarvestingActivity harvest = (HarvestingActivity) currentActivity;
                harvestPlannedQuantityField.setText(String.valueOf(harvest.getPlannedQuantity()));
                harvestActualQuantityField.setText(String.valueOf(harvest.getActualQuantity()));
                harvestUnitField.setText(harvest.getUnit());
                break;
            case IRRIGATION:
                // Nessun campo specifico da prepopolare
                break;
        }
    }

    public void setOnActivityUpdated(Runnable onActivityUpdated) {
        this.onActivityUpdated = onActivityUpdated;
    }

    private void updateSpecificFields(ActivityType activityType) {
        specificFieldsContainer.getChildren().clear();

        if (activityType == null) return;

        switch (activityType) {
            case SEEDING:
                sowingQuantityField = new JFXTextField();
                sowingQuantityField.setPromptText("Quantità Semi");
                sowingQuantityField.setStyle("-jfx-focus-color: #4CAF50; -jfx-unfocus-color: #9E9E9E;");
                sowingQuantityField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        sowingQuantityField.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                });

                sowingUnitField = new JFXTextField();
                sowingUnitField.setPromptText("Unità di Misura (es. grammi)");
                sowingUnitField.setStyle("-jfx-focus-color: #4CAF50; -jfx-unfocus-color: #9E9E9E;");

                specificFieldsContainer.getChildren().addAll(
                        new Label("Quantità Semi:"), sowingQuantityField,
                        new Label("Unità di Misura:"), sowingUnitField
                );
                break;
            case HARVEST:
                harvestPlannedQuantityField = new JFXTextField();
                harvestPlannedQuantityField.setPromptText("Quantità Prevista");
                harvestPlannedQuantityField.setStyle("-jfx-focus-color: #4CAF50; -jfx-unfocus-color: #9E9E9E;");
                harvestPlannedQuantityField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        harvestPlannedQuantityField.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                });

                harvestActualQuantityField = new JFXTextField();
                harvestActualQuantityField.setPromptText("Quantità Effettiva");
                harvestActualQuantityField.setStyle("-jfx-focus-color: #4CAF50; -jfx-unfocus-color: #9E9E9E;");
                harvestActualQuantityField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        harvestActualQuantityField.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                });

                harvestUnitField = new JFXTextField();
                harvestUnitField.setPromptText("Unità di Misura (es. kg)");
                harvestUnitField.setStyle("-jfx-focus-color: #4CAF50; -jfx-unfocus-color: #9E9E9E;");

                specificFieldsContainer.getChildren().addAll(
                        new Label("Quantità Prevista:"), harvestPlannedQuantityField,
                        new Label("Quantità Effettiva:"), harvestActualQuantityField,
                        new Label("Unità di Misura:"), harvestUnitField
                );
                break;
            case IRRIGATION:
                // Nessun campo aggiuntivo
                break;
        }
    }

    @FXML
    protected void handleCreate(ActionEvent event) {
        LocalDate date = datePicker.getValue();
        ActivityType activityType = currentActivity.getType(); // Tipo fisso
        Farmer selectedFarmer = farmerComboBox.getSelectionModel().getSelectedItem();
        ActivityStatus selectedStatus = activityStatusComboBox.getSelectionModel().getSelectedItem(); // Recupera lo stato selezionato

        if (date == null || selectedFarmer == null || selectedStatus == null) { // Aggiungi validazione per lo stato
            Utils.showAlert(Alert.AlertType.WARNING, "Campi Mancanti", "Per favore, compila la data, seleziona il coltivatore e lo stato dell'attività.");
            return;
        }
        if (currentActivity == null) {
            Utils.showAlert(Alert.AlertType.ERROR, "Errore Interno", "Attività di riferimento non trovata per la modifica.");
            return;
        }

        try {
            currentActivity.setDate(date);
            currentActivity.setFarmer(selectedFarmer.getFullName());
            currentActivity.setStatus(selectedStatus); // Imposta il nuovo stato

            switch (activityType) {
                case SEEDING:
                    if (sowingQuantityField == null || sowingQuantityField.getText().isEmpty() || sowingUnitField == null || sowingUnitField.getText().isEmpty()) {
                        Utils.showAlert(Alert.AlertType.WARNING, "Campi Vuoti", "Per favore, inserisci quantità e unità di misura per la semina.");
                        return;
                    }
                    SeedingActivity sowing = (SeedingActivity) currentActivity;
                    sowing.setQuantity(Integer.parseInt(sowingQuantityField.getText()));
                    sowing.setUnit(sowingUnitField.getText());
                    break;
                case IRRIGATION:
                    // Nessun campo specifico da aggiornare
                    break;
                case HARVEST:
                    if (harvestPlannedQuantityField == null || harvestPlannedQuantityField.getText().isEmpty() ||
                            harvestActualQuantityField == null || harvestActualQuantityField.getText().isEmpty() ||
                            harvestUnitField == null || harvestUnitField.getText().isEmpty()) {
                        Utils.showAlert(Alert.AlertType.WARNING, "Campi Vuoti", "Per favor, inserisci quantità previste, effettive e unità di misura per la raccolta.");
                        return;
                    }
                    HarvestingActivity harvest = (HarvestingActivity) currentActivity;
                    harvest.setPlannedQuantity(Integer.parseInt(harvestPlannedQuantityField.getText()));
                    harvest.setActualQuantity(Integer.parseInt(harvestActualQuantityField.getText()));
                    harvest.setUnit(harvestUnitField.getText());
                    break;
            }

            projectService.updateActivity(currentActivity);

            Utils.showAlert(Alert.AlertType.INFORMATION, "Successo", "Attività modificata con successo.");
            if (onActivityUpdated != null) {
                onActivityUpdated.run();
            }
            closeStage(event);

        } catch (NumberFormatException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Input Non Valido", "Per favore, inserisci valori numerici validi per le quantità (solo numeri interi).");
        } catch (RuntimeException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Errore Salvataggio", "Si è verificato un errore durante il salvataggio dell'attività: " + e.getMessage());
            e.printStackTrace();
        }
    }

}