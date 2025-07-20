package com.unina.biogarden.controller.form;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.unina.biogarden.enumerations.ActivityType;
import com.unina.biogarden.models.Colture;
import com.unina.biogarden.models.Farmer;
import com.unina.biogarden.models.activity.Activity;
import com.unina.biogarden.models.activity.HarvestingActivity;
import com.unina.biogarden.models.activity.IrrigationActivity;
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
 * Controller per il form di creazione di una nuova attività (Semina, Irrigazione, Raccolta)
 * associata a una specifica coltivazione. Gestisce l'interfaccia utente, la validazione degli input
 * e l'interazione con i servizi per la gestione del progetto e degli utenti.
 * @author Il Tuo Nome
 */
public class CreateActivityFormController extends AbstractForm {

    @FXML
    private DatePicker datePicker;
    @FXML
    private JFXComboBox<String> activityTypeComboBox;
    @FXML
    private JFXComboBox<Farmer> growerComboBox;
    @FXML
    private VBox specificFieldsContainer;

    private JFXTextField sowingQuantityField;
    private JFXTextField sowingUnitField;

    private JFXTextField harvestPlannedQuantityField;
    private JFXTextField harvestActualQuantityField;
    private JFXTextField harvestUnitField;

    private Colture currentCultivation;
    private Runnable onActivityCreated;
    private final ProjectService projectService = new ProjectService();
    private final UserService userService = new UserService();

    /**
     * Inizializza il controller dopo che il suo FXML è stato completamente caricato.
     * Configura le ComboBox per i tipi di attività e i coltivatori, e imposta la data predefinita.
     */
    @FXML
    public void initialize() {
        activityTypeComboBox.setItems(FXCollections.observableArrayList(Arrays.stream(ActivityType.values()).map(obj -> firstCapitalLetter(obj.getDescription())).toList()));

        activityTypeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateSpecificFields(newVal);
        });

        datePicker.setValue(LocalDate.now());

        try {
            growerComboBox.setItems(FXCollections.observableArrayList(userService.fetchAllFarmer()));
        } catch (RuntimeException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Errore Caricamento", "Impossibile caricare i coltivatori.");
            e.printStackTrace();
        }
    }

    /**
     * Imposta la coltivazione corrente per la quale verrà creata l'attività.
     * È fondamentale per associare l'attività al giusto oggetto {@code Colture}.
     * @param cultivation La coltivazione selezionata a cui associare la nuova attività.
     */
    public void setCurrentCultivation(Colture cultivation) {
        this.currentCultivation = cultivation;
    }

    /**
     * Imposta un callback {@code Runnable} da eseguire dopo che un'attività è stata creata con successo.
     * Questo è utile per aggiornare la vista chiamante o eseguire altre azioni post-creazione.
     * @param onActivityCreated Un {@code Runnable} che verrà eseguito.
     */
    public void setOnActivityCreated(Runnable onActivityCreated) {
        this.onActivityCreated = onActivityCreated;
    }

    /**
     * Aggiorna dinamicamente i campi di input specifici nel form in base al tipo di attività selezionato.
     * I campi non pertinenti vengono nascosti e quelli specifici per il tipo di attività vengono mostrati.
     * @param activityType Il tipo di attività selezionato (es. "Semina", "Irrigazione", "Raccolta").
     */
    private void updateSpecificFields(String activityType) {
        specificFieldsContainer.getChildren().clear();

        if (activityType == null) return;

        switch (activityType) {
            case "Semina":
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
            case "Raccolta":
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
            case "Irrigazione":
                break;
        }
    }

    /**
     * Gestisce l'evento di creazione di una nuova attività.
     * Valida gli input del form, crea l'oggetto attività appropriato e lo aggiunge alla coltivazione corrente.
     * In caso di successo, mostra un messaggio di conferma, esegue il callback {@code onActivityCreated} e chiude il form.
     * In caso di errore, mostra un messaggio di avviso o errore.
     * @param event L'evento di azione che ha scatenato la chiamata, solitamente da un bottone "Crea".
     */
    @FXML
    @Override
    protected void handleCreate(ActionEvent event) {
        LocalDate date = datePicker.getValue();
        String activityType = activityTypeComboBox.getSelectionModel().getSelectedItem();
        Farmer selectedGrower = growerComboBox.getSelectionModel().getSelectedItem();

        if (date == null || activityType == null || selectedGrower == null) {
            Utils.showAlert(Alert.AlertType.WARNING, "Campi Mancanti", "Per favore, compila tutti i campi: data, tipo di attività e coltivatore.");
            return;
        }
        if (currentCultivation == null) {
            Utils.showAlert(Alert.AlertType.ERROR, "Errore Interno", "Coltivazione di riferimento non trovata.");
            return;
        }

        Activity newActivity = null;

        try {
            switch (activityType) {
                case "Semina":
                    if (sowingQuantityField.getText().isEmpty() || sowingUnitField.getText().isEmpty()) {
                        Utils.showAlert(Alert.AlertType.WARNING, "Campi Vuoti", "Per favore, inserisci quantità e unità di misura per la semina.");
                        return;
                    }
                    int sowingQuantity = Integer.parseInt(sowingQuantityField.getText());
                    String sowingUnit = sowingUnitField.getText();
                    newActivity = new SeedingActivity(0, date, selectedGrower.getId(), selectedGrower.getFullName(), sowingQuantity, sowingUnit);
                    break;
                case "Irrigazione":
                    newActivity = new IrrigationActivity(0, date, selectedGrower.getId(), selectedGrower.getFullName());
                    break;
                case "Raccolta":
                    if (harvestPlannedQuantityField.getText().isEmpty() || harvestActualQuantityField.getText().isEmpty() || harvestUnitField.getText().isEmpty()) {
                        Utils.showAlert(Alert.AlertType.WARNING, "Campi Vuoti", "Per favore, inserisci quantità previste, effettive e unità di misura per la raccolta.");
                        return;
                    }
                    int planned = Integer.parseInt(harvestPlannedQuantityField.getText());
                    int actual = Integer.parseInt(harvestActualQuantityField.getText());
                    String harvestUnit = harvestUnitField.getText();
                    newActivity = new HarvestingActivity(0, date, selectedGrower.getId(), selectedGrower.getFullName(), planned, actual, harvestUnit);
                    break;
            }

            if (newActivity != null) {
                projectService.addActivityToColture(currentCultivation, newActivity);
                Utils.showAlert(Alert.AlertType.INFORMATION, "Successo", "Attività creata e aggiunta con successo.");
                if (onActivityCreated != null) {
                    onActivityCreated.run();
                }
                closeStage(event);
            }

        } catch (NumberFormatException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Input non valido", "Per favore, inserisci valori numerici validi per le quantità.");
        } catch (RuntimeException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Errore Creazione Attività", "Errore: " + e.getMessage());
            e.printStackTrace();
        }
    }
}