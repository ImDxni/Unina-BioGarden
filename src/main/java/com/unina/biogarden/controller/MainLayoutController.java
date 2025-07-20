package com.unina.biogarden.controller;

import com.unina.biogarden.session.Session;
import com.unina.biogarden.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller principale per il layout dell'applicazione.
 * Gestisce la navigazione tra le diverse sezioni dell'interfaccia utente
 * caricando dinamicamente il contenuto all'interno del {@code mainContent} {@code StackPane}.
 * Si occupa inoltre di gestire lo stato attivo degli elementi del menu e il logout dell'utente.
 * @author Il Tuo Nome
 */
public class MainLayoutController {

    @FXML
    private VBox sideContent;
    @FXML
    private StackPane mainContent;

    @FXML
    private HBox activeItem;

    @FXML
    public void initialize(){
        HBox item = (HBox) sideContent.getChildren().get(0);
        loadPage(item);
    }


    /**
     * Cambia la pagina visualizzata nel {@code mainContent} in base all'elemento del menu cliccato.
     * Aggiorna lo stile dell'elemento del menu attivo.
     * @param mouseEvent L'evento del mouse che ha scatenato la chiamata.
     */
    public void changePage(MouseEvent mouseEvent) {
        HBox item = getMenuItemFromEvent(mouseEvent);
        if (item != null && !item.equals(activeItem)) {
            loadPage(item);
        }
    }

    /**
     * Recupera l'elemento del menu {@code HBox} dall'evento del mouse.
     * Percorre la gerarchia dei nodi per trovare il genitore di tipo {@code HBox} che rappresenta la voce di menu.
     * @param event L'evento del mouse.
     * @return L'{@code HBox} che rappresenta la voce di menu cliccata, o {@code null} se non trovata.
     */
    private HBox getMenuItemFromEvent(MouseEvent event) {
        Node node = (Node) event.getTarget();
        while (node != null && !(node instanceof HBox)) {
            node = node.getParent();
        }
        return (HBox) node;
    }

    /**
     * Gestisce il cambio dell'elemento del menu attivo.
     * Rimuove lo stile "selected" dall'elemento precedentemente attivo
     * e lo aggiunge al nuovo elemento attivo.
     * @param item Il nuovo elemento del menu da impostare come attivo.
     */
    private void switchActiveItem(HBox item) {
        if (activeItem != null && activeItem != item) {
            removeStyles(activeItem);
        }

        activeItem = item;

        addStyle(activeItem);
    }

    /**
     * Rimuove gli stili CSS "selected" da un elemento del menu e dai suoi figli.
     * @param item L'{@code HBox} da cui rimuovere gli stili.
     */
    private void removeStyles(HBox item) {
        item.getStyleClass().remove("selected");
        for (Node child : item.getChildren()) {
            child.getStyleClass().remove("selected");
        }
    }

    /**
     * Aggiunge gli stili CSS "selected" a un elemento del menu e ai suoi figli di tipo {@code Label}.
     * @param item L'{@code HBox} a cui aggiungere gli stili.
     */
    private void addStyle(HBox item) {
        item.getStyleClass().add("selected");
        for (Node child : item.getChildren()) {
            if (child instanceof Label)
                child.getStyleClass().add("selected");
        }
    }

    /**
     * Gestisce l'azione di chiusura dell'applicazione o di logout dell'utente.
     * Effettua il logout dalla sessione corrente e reindirizza alla vista di login.
     * In caso di errore durante il caricamento della vista di login, stampa lo stack trace.
     */
    public void onClose() {
        Session.logout();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/login-view.fxml"));
            Stage stage = (Stage) mainContent.getScene().getWindow();
            Utils.setSceneWithStylesheet(stage, loader.load());
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento della vista di login dopo il logout.");
            e.printStackTrace();
        }
    }

    /**
     * Carica la pagina corrispondente all'elemento del menu selezionato.
     * Utilizza il nome dell'ID dell'elemento per determinare il file FXML da caricare.
     * Rimuove il contenuto precedente e aggiunge il nuovo contenuto al {@code mainContent}.
     * In caso di errore durante il caricamento, stampa un messaggio di errore.
     * @param item L'{@code HBox} che rappresenta l'elemento del menu selezionato.
     */
    private void loadPage(HBox item) {
        switchActiveItem(item);
        String fxmlFile = item.getId() + "-view.fxml"; // Assume che l'ID del menu corrisponda al nome del file FXML (es. "projects-view.fxml")
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/side/" + fxmlFile));
            Node newContent = loader.load();
            mainContent.getChildren().clear();
            mainContent.getChildren().add(newContent);
        } catch (Exception e) {
            System.err.println("Errore durante il caricamento della pagina: " + fxmlFile);
            e.printStackTrace();
        }
    }

}