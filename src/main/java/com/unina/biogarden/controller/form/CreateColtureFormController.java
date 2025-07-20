package com.unina.biogarden.controller.form;

import com.jfoenix.controls.JFXComboBox;
import com.unina.biogarden.exceptions.ColtureAlreadyExists;
import com.unina.biogarden.models.Crop;
import com.unina.biogarden.models.Project;
import com.unina.biogarden.service.ProjectService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import static com.unina.biogarden.utils.Utils.showAlert;

/**
 * Controller per il form di creazione di una nuova coltura da aggiungere a un progetto esistente.
 * Gestisce la selezione del tipo di coltura e l'interazione con il servizio per l'aggiunta.
 * @author Il Tuo Nome
 */
public class CreateColtureFormController extends AbstractForm {

    @FXML
    private JFXComboBox<Crop> cropComboBox;

    private final ProjectService service = new ProjectService();

    private Project targetProject;
    private Runnable onColtureCreated;

    /**
     * Inizializza il controller dopo che il suo FXML è stato completamente caricato.
     * Popola la ComboBox con la lista delle colture disponibili.
     */
    @FXML
    public void initialize() {
        try {
            cropComboBox.setItems(FXCollections.observableArrayList(service.getCrops()));

        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Errore Caricamento", "Impossibile caricare i dati: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Imposta il progetto di destinazione a cui la nuova coltura verrà aggiunta.
     * @param targetProject Il progetto a cui aggiungere la coltura.
     */
    public void setTargetProject(Project targetProject) {
        this.targetProject = targetProject;
    }

    /**
     * Imposta un callback {@code Runnable} da eseguire dopo che una coltura è stata creata con successo.
     * Questo è utile per aggiornare la vista chiamante o eseguire altre azioni post-creazione.
     * @param onColtureCreated Un {@code Runnable} che verrà eseguito.
     */
    public void setOnColtureCreated(Runnable onColtureCreated) {
        this.onColtureCreated = onColtureCreated;
    }

    /**
     * Gestisce l'evento di creazione di una nuova coltura.
     * Valida la selezione della coltura e il progetto di destinazione, quindi tenta di aggiungere la coltura al progetto.
     * In caso di successo, esegue il callback {@code onColtureCreated} e chiude il form.
     * In caso di errore (es. coltura già esistente o errore di runtime), mostra un messaggio di errore.
     * @param event L'evento di azione che ha scatenato la chiamata, solitamente da un bottone "Crea".
     */
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