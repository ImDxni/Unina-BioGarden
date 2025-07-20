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

public class ProjectsController {

    @FXML
    private VBox mainActivitiesContainer;

    private final ProjectService service = new ProjectService();


    @FXML
    public void initialize() {
        loadActivities();
    }

    private void loadActivities() {
        mainActivitiesContainer.getChildren().clear();

        for (Project project : service.getProjects()) {

            project.setColtures(service.getColtures(project.getId()));

            Node projectBlock = project.buildProjectPane(this::handleAddCultivation, this::handleCultivationClick);
            mainActivitiesContainer.getChildren().add(projectBlock);
        }
    }

    private void handleCultivationClick(Colture cultivation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/pane/activities-pane.fxml"));
            Parent root = loader.load();
            CultivationActivitiesController controller = loader.getController();

            // Passa la coltivazione selezionata al controller della vista delle attività
            controller.setCultivation(cultivation);

            // Crea un nuovo Stage per la vista delle attività
            Stage stage = new Stage();
            stage.setTitle("Gestione Attività - " + cultivation.getCrop().nameProperty().get());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL); // Puoi anche renderla una nuova finestra principale se preferisci
            stage.initOwner(mainActivitiesContainer.getScene().getWindow());
            stage.show(); // Mostra la nuova finestra

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Errore Caricamento Vista", "Impossibile caricare la vista delle attività per la coltivazione.");
            e.printStackTrace();
        }
    }

    public void handleAddCultivation(ActionEvent actionEvent) {
        JFXButton clickedButton = (JFXButton) actionEvent.getSource();
        int projectId = (Integer) clickedButton.getUserData();
        Project projectToAddTo = service.fetchProjectById(projectId);


        if (projectToAddTo != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/form/colture-form-view.fxml")); // Percorso corretto del FXML
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