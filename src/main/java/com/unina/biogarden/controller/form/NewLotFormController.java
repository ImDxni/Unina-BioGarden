package com.unina.biogarden.controller.form;

import com.jfoenix.controls.JFXTextField;
import com.unina.biogarden.service.ProjectService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import static com.unina.biogarden.utils.Utils.showAlert;

/**
 * Controller per il form di creazione di un nuovo lotto.
 * Gestisce l'input del nome e dell'area del lotto,
 * interagendo con il servizio per persistere il nuovo lotto nel sistema.
 * @author Il Tuo Nome
 */
public class NewLotFormController extends AbstractForm {

    @FXML
    private JFXTextField nameField;

    @FXML
    private JFXTextField areaField;

    private Runnable createRunnable;

    private final ProjectService service = new ProjectService();

    /**
     * Imposta un callback {@code Runnable} da eseguire dopo che un nuovo lotto è stato creato con successo.
     * Questo è utile per aggiornare la vista chiamante o eseguire altre azioni post-creazione.
     * @param createRunnable Un {@code Runnable} che verrà eseguito.
     */
    public void setOnLotCreated(Runnable createRunnable) {
        this.createRunnable = createRunnable;
    }

    /**
     * Gestisce l'evento di creazione di un nuovo lotto.
     * Valida gli input del nome e dell'area del lotto, quindi tenta di creare il lotto.
     * In caso di successo, esegue il callback {@code createRunnable} e chiude il form.
     * Gestisce anche errori di validazione dell'input (campi vuoti, area non valida) e altri errori di runtime.
     * @param event L'evento di azione che ha scatenato la chiamata, solitamente da un bottone "Crea".
     */
    @FXML
    @Override
    protected void handleCreate(ActionEvent event) {
        String nome = nameField.getText();
        String areaText = areaField.getText();

        if (nome.isEmpty() || areaText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Per favore, compila tutti i campi.");
            return;
        }

        try {
            int area = Integer.parseInt(areaText);
            if (area <= 0) {
                showAlert(Alert.AlertType.ERROR, "Errore", "L'area deve essere un numero positivo.");
                return;
            }

            service.createLot(nome, area);

            try {
                if (createRunnable != null) {
                    createRunnable.run();
                }
                closeStage(event);
            } catch (IllegalStateException e) {
                showAlert(Alert.AlertType.ERROR, "Errore Creazione Lotto", e.getMessage());
            } catch (RuntimeException e) {
                showAlert(Alert.AlertType.ERROR, "Errore Database", "Si è verificato un errore durante la creazione del progetto: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "L'area deve essere un numero valido.");
        }
    }
}