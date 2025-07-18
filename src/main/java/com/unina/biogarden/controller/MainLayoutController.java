package com.unina.biogarden.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class MainLayoutController {

    @FXML
    private StackPane mainContent;

    @FXML
    private HBox activeItem;

    public void changePage(MouseEvent mouseEvent) {
        HBox item = getMenuItemFromEvent(mouseEvent);
        if (item != null) {
            switchActiveItem(item);
            String fxmlFile = item.getId() + ".fxml"; // Assuming the FXML files are named after the IDs of the menu items
            try {
                Node newContent = FXMLLoader.load(getClass().getResource(fxmlFile));
                mainContent.getChildren().clear();
                mainContent.getChildren().add(newContent);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle the error, e.g., show an alert or log it
            }
        }
    }
    private HBox getMenuItemFromEvent(MouseEvent event) {
        Node node = (Node) event.getTarget();
        while (node != null && !(node instanceof HBox)) {
            node = node.getParent();
        }
        return (HBox) node;
    }


    private void switchActiveItem(HBox item){
        if(activeItem != null && activeItem != item){
            removeStyles(activeItem);
        }

        activeItem = item;

        addStyle(activeItem);
    }

    private void removeStyles(HBox item) {
        item.getStyleClass().remove("selected");
        for (Node child : item.getChildren()) {
            child.getStyleClass().remove("selected");
        }
    }

    private void addStyle(HBox item) {
        item.getStyleClass().add("selected");
        for (Node child : item.getChildren()) {
            if (child instanceof Label)
                child.getStyleClass().add("selected");
        }
    }

    public void onClose() {
        //TODO handle close action, e.g., show a confirmation dialog or save state
    }


}
