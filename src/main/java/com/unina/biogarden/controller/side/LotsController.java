package com.unina.biogarden.controller.side;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.unina.biogarden.controller.form.NewLotFormController;
import com.unina.biogarden.models.Lot;
import com.unina.biogarden.models.Project;
import com.unina.biogarden.service.ProjectService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;

import static com.unina.biogarden.utils.Utils.showAlert;

/**
 * Controller per la gestione e visualizzazione dei lotti e dei progetti associati a ciascun lotto.
 * Permette di selezionare un lotto dalla sidebar per visualizzare i suoi progetti
 * e di creare nuovi lotti.
 * @author Il Tuo Nome
 */
public class LotsController {
    @FXML
    private VBox lotsContainer;

    @FXML
    private VBox projectsPerLotSection;

    @FXML
    private JFXTreeTableView<Project> projectsPerLotTable;
    @FXML
    private JFXTreeTableColumn<Project, String> nameCol;
    @FXML
    private JFXTreeTableColumn<Project, String> lotCol;
    @FXML
    private JFXTreeTableColumn<Project, String> seasonCol;
    @FXML
    private JFXTreeTableColumn<Project, String> statusCol;
    private Pane selectedLotPane = null;

    /**
     * Inizializza il controller dopo che il suo FXML è stato completamente caricato.
     * Configura la tabella dei progetti, inizializza le factory delle celle e carica i lotti nella sidebar.
     * Inizialmente, la sezione dei progetti per lotto è nascosta.
     */
    @FXML
    public void initialize() {
        projectsPerLotTable.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);
        Project.initCellFactory(nameCol, lotCol, seasonCol, statusCol);

        Platform.runLater(this::loadLotsIntoSidebar);

        projectsPerLotSection.setVisible(false);
        projectsPerLotSection.setManaged(false);
    }

    /**
     * Carica e visualizza i progetti associati a un lotto specifico nella tabella {@code projectsPerLotTable}.
     * Se il lotto è nullo, la sezione dei progetti viene nascosta.
     * @param lot Il lotto di cui caricare i progetti.
     */
    private void loadProjectsForLot(Lot lot) {
        if (lot == null) {
            projectsPerLotSection.setVisible(false);
            projectsPerLotSection.setManaged(false);
            return;
        }

        try {
            ProjectService service = new ProjectService();
            ObservableList<Project> data = FXCollections.observableArrayList(service.fetchProjectByLot(lot));

            TreeItem<Project> projectRoot = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
            projectsPerLotTable.setRoot(projectRoot);
            projectsPerLotTable.setShowRoot(false);

            projectsPerLotSection.setVisible(true);
            projectsPerLotSection.setManaged(true);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore Caricamento Progetti", "Impossibile caricare i progetti per il lotto selezionato.");
            e.printStackTrace();
            projectsPerLotSection.setVisible(false);
            projectsPerLotSection.setManaged(false);
        }
    }

    /**
     * Carica tutti i lotti disponibili dal servizio e li aggiunge dinamicamente alla sidebar {@code lotsContainer}.
     * Ogni lotto viene visualizzato come un pannello cliccabile. Il primo lotto viene selezionato di default.
     */
    private void loadLotsIntoSidebar() {
        lotsContainer.getChildren().clear();
        selectedLotPane = null;

        ProjectService service = new ProjectService();

        Collection<Lot> lots = service.fetchAllLots();

        Lot firstLot = null;
        Pane firstLotPane = null;

        for (Lot lot : lots) {
            Pane box;

            box = lot.createLotBox(e -> {
                if (selectedLotPane != null) {
                    selectedLotPane.getStyleClass().remove("selected");
                }

                Node targetNode = (Node) e.getTarget();
                Pane parentPane = findParentPane(targetNode);

                if (parentPane != null) {
                    parentPane.getStyleClass().add("selected");
                    selectedLotPane = parentPane;
                }

                loadProjectsForLot(lot);
            });

            lotsContainer.getChildren().add(box);

            if (firstLot == null) {
                firstLot = lot;
                firstLotPane = box;
            }
        }

        if (firstLot != null) {
            firstLotPane.getStyleClass().add("selected");
            selectedLotPane = firstLotPane;
            loadProjectsForLot(firstLot);

        } else {
            projectsPerLotSection.setVisible(false);
            projectsPerLotSection.setManaged(false);
        }
    }

    /**
     * Cerca ricorsivamente il primo genitore di tipo {@code Pane} per un dato nodo.
     * @param node Il nodo da cui iniziare la ricerca.
     * @return Il primo {@code Pane} genitore, o {@code null} se non trovato.
     */
    private Pane findParentPane(Node node) {
        while (node != null && !(node instanceof Pane)) {
            node = node.getParent();
        }
        return (Pane) node;
    }

    /**
     * Gestisce l'azione per la creazione di un nuovo lotto.
     * Apre un form modale per l'inserimento dei dettagli del nuovo lotto.
     * Una volta che il lotto è stato creato, la sidebar dei lotti viene aggiornata.
     */
    @FXML
    private void handleNewLotCreation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/form/lot-form-view.fxml"));
            Parent root = loader.load();
            root.requestLayout();
            root.layout();
            NewLotFormController formController = loader.getController();

            formController.setOnLotCreated(() -> {
                Platform.runLater(this::loadLotsIntoSidebar);
            });

            Stage stage = new Stage();
            stage.setTitle("Crea Nuovo Lotto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Impossibile aprire il form di creazione lotto");
            alert.setContentText("Si è verificato un errore durante il caricamento del modulo. Controlla il percorso del file FXML.");
            alert.showAndWait();
        }
    }
}