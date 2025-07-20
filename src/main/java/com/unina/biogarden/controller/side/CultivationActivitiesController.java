package com.unina.biogarden.controller.side;

import com.unina.biogarden.controller.form.CreateActivityFormController;
import com.unina.biogarden.controller.form.EditActivityFormController;
import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.models.activity.Activity;
import com.unina.biogarden.models.Colture;
import com.unina.biogarden.service.ProjectService;
import com.unina.biogarden.utils.Utils;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import static com.unina.biogarden.utils.Utils.firstCapitalLetter;

public class CultivationActivitiesController {

    @FXML
    private Label cultivationNameLabel;
    @FXML
    private TableView<Activity> activitiesTable;
    @FXML
    private TableColumn<Activity, LocalDate> colDate;
    @FXML
    private TableColumn<Activity, String> colStatus;
    @FXML
    private TableColumn<Activity, String> colType;
    @FXML
    private TableColumn<Activity, String> colDetails;
    @FXML
    private TableColumn<Activity, String> colGrower;
    @FXML
    private TableColumn<Activity, Void> colActions; // Colonna per i pulsanti di azione

    private Colture currentCultivation; // La coltivazione di cui stiamo gestendo le attività
    private ProjectService projectService = new ProjectService(); // Il servizio per interagire con i dati

    /**
     * Metodo chiamato per inizializzare il controller dopo che il FXML è stato caricato.
     */
    @FXML
    public void initialize() {
        // Configura le cell factory per le colonne della tabella
        colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        colDate.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });

        colType.setCellValueFactory(cellData -> new SimpleObjectProperty<>(firstCapitalLetter(cellData.getValue().getType().getDescription())));
        colDetails.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDetails()));
        colGrower.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFarmer()));
        colStatus.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus().getLabel()));
        colActions.setCellFactory(param -> new TableCell<Activity, Void>() {
            private final JFXButton editButton = new JFXButton("Modifica");
            private final JFXButton deleteButton = new JFXButton("Elimina");
            private final HBox pane = new HBox(5, editButton, deleteButton);

            {
                editButton.setStyle("-fx-background-color: #64B5F6; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 12px;");
                deleteButton.setStyle("-fx-background-color: #EF5350; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 12px;");

                editButton.setOnAction(event -> {
                    Activity activity = getTableView().getItems().get(getIndex());
                    handleEditActivity(activity);
                    loadActivities();
                });

                deleteButton.setOnAction(event -> {
                    Activity activity = getTableView().getItems().get(getIndex());
                    handleDeleteActivity(activity);
                    loadActivities();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    /**
     * Imposta la coltivazione di cui mostrare le attività.
     * Questo metodo deve essere chiamato dal controller chiamante (es. ActivitiesController).
     *
     * @param cultivation La coltivazione selezionata.
     */
    public void setCultivation(Colture cultivation) {
        this.currentCultivation = cultivation;
        cultivationNameLabel.setText("Attività per: " + cultivation.getCrop().nameProperty().get());
        loadActivities();
    }

    /**
     * Carica le attività della coltivazione corrente nella tabella.
     */
    private void loadActivities() {
        activitiesTable.setItems(FXCollections.observableArrayList()); // Pulisce la tabella prima di caricare i nuovi dati
        if (currentCultivation != null) {
            Collection<Activity> activities = projectService.fetchActivities(currentCultivation.getId()); // Assumi un getId() per Colture
            activitiesTable.setItems(FXCollections.observableArrayList(activities));
        }
    }

    /**
     * Gestisce il click sul pulsante "Aggiungi Attività".
     * Apre un form per la creazione di una nuova attività.
     */
    @FXML
    private void handleAddActivity() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/form/activity-form-view.fxml"));
            Parent root = loader.load();
            CreateActivityFormController controller = loader.getController();

            // Passa la coltivazione corrente al form per l'associazione
            controller.setCurrentCultivation(currentCultivation);
            // Imposta una callback per aggiornare la tabella dopo la creazione
            controller.setOnActivityCreated(this::loadActivities);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Aggiungi Nuova Attività");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(activitiesTable.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

        } catch (IOException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Errore caricamento form", "Impossibile caricare il form di aggiunta attività.");
            e.printStackTrace();
        }
    }

    /**
     * Gestisce la modifica di un'attività esistente.
     *
     * @param activity L'attività da modificare.
     */
    private void handleEditActivity(Activity activity) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/form/update-activity-form.fxml"));
            Parent root = loader.load();
            EditActivityFormController controller = loader.getController();

            controller.setActivity(activity); // Passa l'attività al controller del form di modifica
            controller.setOnActivityUpdated(this::loadActivities); // Imposta la callback per ricaricare la tabella

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifica Attività: " + activity.getType());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(activitiesTable.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

        } catch (IOException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Errore caricamento form", "Impossibile caricare il form di modifica attività.");
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'eliminazione di un'attività.
     *
     * @param activity L'attività da eliminare.
     */
    private void handleDeleteActivity(Activity activity) {
        projectService.deleteActivity(activity); // Assumi un metodo deleteActivity nel tuo servizio
    }
}