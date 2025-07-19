package com.unina.biogarden.controller.side;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.unina.biogarden.controller.form.CreateProjectFormController;
import com.unina.biogarden.models.Project;
import com.unina.biogarden.service.ProjectService;
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

public class ProjectsController {

    @FXML
    private JFXTreeTableView<Project> projectTable;
    @FXML
    private JFXTreeTableColumn<Project, String> nameCol;
    @FXML
    private JFXTreeTableColumn<Project, String> lotCol;
    @FXML
    private JFXTreeTableColumn<Project, String> seasonCol;
    @FXML
    private JFXTreeTableColumn<Project, String> statusCol;

    public void initialize() {
        projectTable.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);

        Project.initCellFactory(nameCol, lotCol, seasonCol, statusCol);
        fetchAndPopulateProjects();
    }

    private void fetchAndPopulateProjects() {
        try {
            ProjectService service = new ProjectService();

            ObservableList<Project> data = FXCollections.observableArrayList(service.getProjects());

            TreeItem<Project> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
            projectTable.setRoot(root);
            projectTable.setShowRoot(false);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore Caricamento Progetti", "Impossibile caricare i progetti dal database.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNewProjectButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/form/project-form-view.fxml"));
            Parent root = loader.load();

            CreateProjectFormController controller = loader.getController();
            controller.setOnProjectCreated(this::fetchAndPopulateProjects);

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