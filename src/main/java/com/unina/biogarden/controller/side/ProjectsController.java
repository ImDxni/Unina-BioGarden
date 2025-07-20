package com.unina.biogarden.controller.side;

import com.jfoenix.controls.JFXButton;
import com.unina.biogarden.controller.form.CreateColtureFormController;
import com.unina.biogarden.controller.form.CreateProjectFormController;
import com.unina.biogarden.models.Colture;
import com.unina.biogarden.models.Project;
import com.unina.biogarden.service.ProjectService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static com.unina.biogarden.utils.Utils.showAlert;

/**
 * Controller per la gestione e visualizzazione dei progetti e delle coltivazioni associate.
 * Permette di visualizzare i progetti, aggiungere nuove coltivazioni a un progetto
 * e visualizzare le attività di una specifica coltivazione.
 * @author Il Tuo Nome
 */
public class ProjectsController {

    @FXML
    private VBox mainActivitiesContainer;

    private final ProjectService service = new ProjectService();

    /**
     * Inizializza il controller dopo che il suo FXML è stato completamente caricato.
     * Carica e visualizza i progetti esistenti nella schermata principale.
     */
    @FXML
    public void initialize() {
        loadActivities();
    }

    /**
     * Carica tutti i progetti e le rispettive coltivazioni dal servizio
     * e li visualizza dinamicamente nel {@code mainActivitiesContainer}.
     * Ogni progetto è rappresentato da un blocco che include le sue coltivazioni.
     */
    private void loadActivities() {
        mainActivitiesContainer.getChildren().clear();

        for (Project project : service.getProjects()) {
            project.setColtures(service.getColtures(project.getId()));
            Node projectBlock = project.buildProjectPane(this::handleAddCultivation, this::handleCultivationClick);
            mainActivitiesContainer.getChildren().add(projectBlock);
        }
    }

    /**
     * Gestisce il click su una coltivazione specifica.
     * Apre una nuova finestra modale che mostra i dettagli e le attività della coltivazione selezionata.
     * @param cultivation La coltivazione su cui è stato cliccato.
     */
    private void handleCultivationClick(Colture cultivation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/pane/activities-pane.fxml"));
            Parent root = loader.load();
            CultivationActivitiesController controller = loader.getController();

            controller.setCultivation(cultivation);

            Stage stage = new Stage();
            stage.setTitle("Gestione Attività - " + cultivation.getCrop().nameProperty().get());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainActivitiesContainer.getScene().getWindow());
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Errore Caricamento Vista", "Impossibile caricare la vista delle attività per la coltivazione.");
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'azione di aggiunta di una nuova coltivazione a un progetto.
     * Apre un form modale per la creazione di una coltura, pre-impostando il progetto di destinazione
     * e impostando un callback per ricaricare i progetti dopo la creazione della coltura.
     * @param actionEvent L'evento che ha scatenato la chiamata, solitamente il click su un bottone.
     */
    public void handleAddCultivation(ActionEvent actionEvent) {
        JFXButton clickedButton = (JFXButton) actionEvent.getSource();
        int projectId = (Integer) clickedButton.getUserData();
        Project projectToAddTo = service.fetchProjectById(projectId);

        if (projectToAddTo != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/form/colture-form-view.fxml"));
                Parent root = loader.load();
                CreateColtureFormController formController = loader.getController();

                formController.setTargetProject(projectToAddTo);
                formController.setOnColtureCreated(this::loadActivities);

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Aggiungi Nuova Coltura al Progetto: " + projectToAddTo.nameProperty().get());
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(mainActivitiesContainer.getScene().getWindow());
                dialogStage.setScene(new Scene(root));
                dialogStage.showAndWait();

            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Errore Caricamento Form", "Impossibile caricare il form di aggiunta coltura.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Gestisce l'azione del bottone "Nuovo Progetto".
     * Apre un form modale per la creazione di un nuovo progetto.
     * Imposta un callback per ricaricare la lista dei progetti una volta che il nuovo progetto è stato creato.
     */
    @FXML
    private void handleNewProjectButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/form/project-form-view.fxml"));
            Parent root = loader.load();

            CreateProjectFormController controller = loader.getController();
            controller.setOnProjectCreated(this::loadActivities);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nuovo Progetto");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore Apertura Form", "Impossibile aprire il form di creazione progetto.");
        }
    }
}