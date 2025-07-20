package com.unina.biogarden.models;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Rappresenta un progetto di coltivazione nel sistema BioGarden.
 * Questa classe estende {@link RecursiveTreeObject} per l'utilizzo in {@code JFoenix TreeTableView}
 * e gestisce le proprietà reattive (JavaFX Properties) per il nome del progetto,
 * il lotto associato, la stagione e lo stato del progetto. Include anche una collezione
 * di {@link Colture} associate al progetto.
 * @author Il Tuo Nome
 */
public class Project extends RecursiveTreeObject<Project> {
    private final int id;
    private final SimpleStringProperty name, lot, season, status;

    private final Collection<Colture> coltures = new ArrayList<>();

    /**
     * Costruisce una nuova istanza di {@code Project}.
     * Le proprietà di stagione e stato vengono calcolate in base alle date fornite.
     *
     * @param id L'identificatore univoco del progetto.
     * @param name Il nome del progetto.
     * @param lotName Il nome del lotto a cui il progetto è associato.
     * @param startDate La data di inizio del progetto.
     * @param endDate La data di fine prevista del progetto.
     */
    public Project(int id, String name, String lotName, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.lot = new SimpleStringProperty(lotName);
        this.season = new SimpleStringProperty(getSeason(startDate));
        this.status = new SimpleStringProperty(getStatus(startDate, endDate));
    }

    /**
     * Costruisce una nuova istanza di {@code Project} includendo una collezione di coltivazioni.
     * Le proprietà di stagione e stato vengono calcolate in base alle date fornite.
     *
     * @param id L'identificatore univoco del progetto.
     * @param name Il nome del progetto.
     * @param lotName Il nome del lotto a cui il progetto è associato.
     * @param startDate La data di inizio del progetto.
     * @param endDate La data di fine prevista del progetto.
     * @param coltures Una collezione di oggetti {@link Colture} associati a questo progetto.
     */
    public Project(int id, String name, String lotName, LocalDate startDate, LocalDate endDate, Collection<Colture> coltures) {
        this(id, name, lotName, startDate, endDate);
        this.coltures.addAll(coltures);
    }

    /**
     * Determina la stagione in base a una data specifica.
     * @param date La data da cui estrarre la stagione.
     * @return Una stringa che rappresenta la stagione ("Primavera", "Estate", "Autunno", "Inverno") o "N/A" se la data è null.
     */
    private String getSeason(LocalDate date) {
        if (date == null) return "N/A";
        int month = date.getMonthValue();
        if (month >= 3 && month <= 5) return "Primavera";
        if (month >= 6 && month <= 8) return "Estate";
        if (month >= 9 && month <= 11) return "Autunno";
        return "Inverno";
    }

    /**
     * Determina lo stato del progetto in base alle date di inizio e fine e alla data corrente.
     * @param startDate La data di inizio del progetto.
     * @param endDate La data di fine del progetto.
     * @return Una stringa che rappresenta lo stato ("Pianificato", "In Corso", "Completato") o "N/A" se le date sono null.
     */
    private String getStatus(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return "N/A";
        LocalDate now = LocalDate.now();
        if (now.isBefore(startDate)) return "Pianificato";
        if (now.isAfter(endDate)) return "Completato";
        return "In Corso";
    }

    /**
     * Restituisce la proprietà del nome del progetto.
     * @return La {@link SimpleStringProperty} del nome.
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    /**
     * Restituisce la proprietà del lotto associato al progetto.
     * @return La {@link SimpleStringProperty} del nome del lotto.
     */
    public SimpleStringProperty lotProperty() {
        return lot;
    }

    /**
     * Restituisce la proprietà della stagione del progetto.
     * @return La {@link SimpleStringProperty} della stagione.
     */
    public SimpleStringProperty seasonProperty() {
        return season;
    }

    /**
     * Restituisce la proprietà dello stato del progetto.
     * @return La {@link SimpleStringProperty} dello stato.
     */
    public SimpleStringProperty statusProperty() {
        return status;
    }

    /**
     * Restituisce l'ID univoco del progetto.
     * @return L'ID del progetto.
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta la collezione di coltivazioni associate a questo progetto, rimpiazzando le esistenti.
     * @param coltures La nuova {@link Collection} di {@link Colture} da associare.
     */
    public void setColtures(Collection<Colture> coltures) {
        this.coltures.clear();
        this.coltures.addAll(coltures);
    }

    /**
     * Costruisce e restituisce un {@link Node} (un {@link VBox}) che rappresenta visivamente questo progetto
     * per l'interfaccia utente JavaFX. Carica un layout da FXML e popola i suoi elementi con i dati del progetto.
     * Permette di associare handler per l'aggiunta di nuove coltivazioni e per il click su coltivazioni esistenti.
     *
     * @param onAddCultivation Un {@link EventHandler} per l'azione di click sul pulsante "Aggiungi Coltivazione".
     * Il {@code userData} del pulsante conterrà l'ID del progetto.
     * @param onCultivationClick Un {@link Consumer} che riceve l'oggetto {@link Colture} quando un blocco coltivazione viene cliccato.
     * @return Un {@link Node} (VBox) che rappresenta il progetto con i suoi controlli e coltivazioni.
     * @throws RuntimeException Se si verifica un errore I/O durante il caricamento del file FXML.
     */
    public Node buildProjectPane(EventHandler<ActionEvent> onAddCultivation, Consumer<Colture> onCultivationClick) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/pane/project-pane.fxml"));
            VBox projectBlock = new VBox();

            loader.setRoot(projectBlock);
            loader.load();

            Label projectTitleLabel = (Label) projectBlock.lookup("#projectTitleLabel");
            JFXButton addCultivationButton = (JFXButton) projectBlock.lookup("#addCultivationButton");
            ScrollPane scrollPane = (ScrollPane) projectBlock.lookup(".scroll-pane");
            HBox cultivationsContainer = (HBox) scrollPane.getContent();

            projectTitleLabel.setText(nameProperty().get());

            // Associa l'ID del progetto al pulsante per un facile recupero nell'handler
            addCultivationButton.setUserData(getId());
            addCultivationButton.setOnAction(onAddCultivation);

            // Aggiunge dinamicamente i pannelli delle coltivazioni
            for (Colture cultivation : coltures) {
                Pane cultivationBlock = cultivation.createColturePane(e -> onCultivationClick.accept(cultivation));
                cultivationsContainer.getChildren().add(cultivationBlock);
            }

            return projectBlock;
        } catch (IOException e) {
            System.err.println("Errore caricamento FXML per ProjectPane: " + e.getMessage());
            e.printStackTrace();
            // Restituisce un VBox di errore per indicare che qualcosa è andato storto
            return new VBox(new Label("Errore caricamento progetto: " + nameProperty().get()));
        }
    }

    /**
     * Metodo helper per creare una {@link Callback} sicura per le {@link TreeTableColumn CellValueFactory}.
     * Gestisce i casi in cui {@link TreeItem} o il suo valore sono nulli, restituendo una stringa vuota.
     * @param extractor Una funzione che estrae la {@link SimpleStringProperty} dall'oggetto {@link Project}.
     * @return Una {@link Callback} pronta per essere usata con {@code setCellValueFactory}.
     */
    private static Callback<TreeTableColumn.CellDataFeatures<Project, String>, ObservableValue<String>> getSafeCellValueFactory(
            Function<Project, SimpleStringProperty> extractor) {
        return param -> {
            TreeItem<Project> item = param.getValue();
            if (item != null && item.getValue() != null) {
                return extractor.apply(item.getValue());
            }
            return new SimpleStringProperty("");
        };
    }

    /**
     * Inizializza le CellValueFactory per le colonne di una {@code JFXTreeTableView} che visualizza oggetti {@link Project}.
     * Questo metodo associa ogni colonna alla proprietà corrispondente dell'oggetto {@link Project} in modo sicuro.
     *
     * @param nameCol La colonna per il nome del progetto.
     * @param lotCol La colonna per il nome del lotto.
     * @param seasonCol La colonna per la stagione.
     * @param statusCol La colonna per lo stato del progetto.
     */
    public static void initCellFactory(JFXTreeTableColumn<Project, String> nameCol,
                                       JFXTreeTableColumn<Project, String> lotCol,
                                       JFXTreeTableColumn<Project, String> seasonCol,
                                       JFXTreeTableColumn<Project, String> statusCol) {

        nameCol.setCellValueFactory(getSafeCellValueFactory(Project::nameProperty));
        lotCol.setCellValueFactory(getSafeCellValueFactory(Project::lotProperty));
        seasonCol.setCellValueFactory(getSafeCellValueFactory(Project::seasonProperty));
        statusCol.setCellValueFactory(getSafeCellValueFactory(Project::statusProperty));
    }
}