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

/**
 * Controller per il form di modifica di un'attività esistente.
 * Permette di aggiornare la data, il coltivatore, lo stato e i campi specifici
 * di un'attività (Semina, Irrigazione, Raccolta).
 * @author Il Tuo Nome
 */
public class EditActivityFormController extends AbstractForm {

    @FXML
    private DatePicker datePicker;
    @FXML
    private Label activityTypeLabel;
    @FXML
    private JFXComboBox<Farmer> farmerComboBox;
    @FXML
    private JFXComboBox<ActivityStatus> activityStatusComboBox;
    @FXML
    private VBox specificFieldsContainer;

    private JFXTextField sowingQuantityField;
    private JFXTextField sowingUnitField;

    private JFXTextField harvestPlannedQuantityField;
    private JFXTextField harvestActualQuantityField;
    private JFXTextField harvestUnitField;

    private Activity currentActivity;
    private Runnable onActivityUpdated;
    private final ProjectService projectService = new ProjectService();
    private final UserService userService = new UserService();

    /**
     * Inizializza il controller dopo che il suo FXML è stato completamente caricato.
     * Popola la ComboBox dei coltivatori e la ComboBox dello stato dell'attività.
     */
    @FXML
    public void initialize() {
        try {
            farmerComboBox.setItems(FXCollections.observableArrayList(userService.fetchAllFarmer()));
            activityStatusComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(ActivityStatus.values())));
        } catch (RuntimeException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Errore Caricamento", "Impossibile caricare i dati: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Imposta l'attività corrente da modificare nel form.
     * Popola tutti i campi del form con i dati dell'attività fornita,
     * inclusi i campi generici e quelli specifici in base al tipo di attività.
     * @param activity L'attività da modificare.
     */
    public void setActivity(Activity activity) {
        this.currentActivity = activity;

        datePicker.setValue(currentActivity.getDate());
        activityTypeLabel.setText(firstCapitalLetter(currentActivity.getType().getDescription()));

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
        }

        activityStatusComboBox.getSelectionModel().select(currentActivity.getStatus());

        updateSpecificFields(currentActivity.getType());

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
                break;
        }
    }

    /**
     * Imposta un callback {@code Runnable} da eseguire dopo che l'attività è stata aggiornata con successo.
     * Questo è utile per ricaricare la tabella delle attività nella vista principale.
     * @param onActivityUpdated Un {@code Runnable} che verrà eseguito.
     */
    public void setOnActivityUpdated(Runnable onActivityUpdated) {
        this.onActivityUpdated = onActivityUpdated;
    }

    /**
     * Aggiorna dinamicamente i campi di input specifici nel form in base al tipo di attività.
     * Nasconde i campi non necessari e mostra quelli specifici per il tipo di attività.
     * @param activityType Il tipo di attività per cui visualizzare i campi specifici.
     */
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
                        sowingQuantityField.setText(newValue.replaceAll("\\D", ""));
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
                        harvestPlannedQuantityField.setText(newValue.replaceAll("\\D", ""));
                    }
                });

                harvestActualQuantityField = new JFXTextField();
                harvestActualQuantityField.setPromptText("Quantità Effettiva");
                harvestActualQuantityField.setStyle("-jfx-focus-color: #4CAF50; -jfx-unfocus-color: #9E9E9E;");
                harvestActualQuantityField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        harvestActualQuantityField.setText(newValue.replaceAll("\\D", ""));
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
                break;
        }
    }

    /**
     * Gestisce l'evento di salvataggio delle modifiche all'attività.
     * Valida gli input del form, aggiorna l'oggetto {@code currentActivity} con i nuovi valori,
     * e tenta di persistere le modifiche tramite il servizio.
     * In caso di successo, mostra un messaggio di conferma, esegue il callback {@code onActivityUpdated} e chiude il form.
     * Gestisce anche errori di validazione dell'input e altri errori di runtime.
     * @param event L'evento di azione che ha scatenato la chiamata, solitamente da un bottone "Salva".
     */
    @FXML
    protected void handleCreate(ActionEvent event) {
        LocalDate date = datePicker.getValue();
        ActivityType activityType = currentActivity.getType();
        Farmer selectedFarmer = farmerComboBox.getSelectionModel().getSelectedItem();
        ActivityStatus selectedStatus = activityStatusComboBox.getSelectionModel().getSelectedItem();

        if (date == null || selectedFarmer == null || selectedStatus == null) {
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
            currentActivity.setStatus(selectedStatus);

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