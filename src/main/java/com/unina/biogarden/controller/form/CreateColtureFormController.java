package com.unina.biogarden.controller.form;

import com.unina.biogarden.exceptions.ColtureAlreadyExists;
import com.unina.biogarden.models.Crop;
import com.unina.biogarden.models.Farmer;
import com.unina.biogarden.models.Project;
import com.unina.biogarden.service.ProjectService;
import com.unina.biogarden.service.UserService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import com.jfoenix.controls.JFXComboBox;
import static com.unina.biogarden.utils.Utils.showAlert;

public class CreateColtureFormController extends AbstractForm {

    @FXML
    private JFXComboBox<Crop> cropComboBox;

    private final ProjectService service = new ProjectService();

    private Project targetProject;
    private Runnable onColtureCreated;

    @FXML
    public void initialize() {
        try {
            cropComboBox.setItems(FXCollections.observableArrayList(service.getCrops()));

        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Errore Caricamento", "Impossibile caricare i dati: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setTargetProject(Project targetProject) {
        this.targetProject = targetProject;
    }

    public void setOnColtureCreated(Runnable onColtureCreated) {
        this.onColtureCreated = onColtureCreated;
    }

    @FXML
    @Override
    protected void handleCreate(ActionEvent event) {
        Crop selectedCrop = cropComboBox.getSelectionModel().getSelectedItem();

        if (targetProject == null) {
            showAlert(Alert.AlertType.ERROR, "Errore Interno", "Il progetto di destinazione non è stato impostato. Contatta l'amministratore.");
            return;
        }

        if (selectedCrop == null) {
            showAlert(Alert.AlertType.WARNING, "Selezione Obbligatoria", "Per favore, seleziona una coltura.");
            return;
        }

        try {

            service.addColture(targetProject, selectedCrop);

            if (onColtureCreated != null) {
                onColtureCreated.run();
            }

            closeStage(event);

        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Errore Creazione Coltura", "Si è verificato un errore durante l'aggiunta della coltura: " + e.getMessage());
            e.printStackTrace();
        } catch (ColtureAlreadyExists e) {
            showAlert(Alert.AlertType.ERROR, "Coltura Esistente", e.getMessage());
        }
    }
}