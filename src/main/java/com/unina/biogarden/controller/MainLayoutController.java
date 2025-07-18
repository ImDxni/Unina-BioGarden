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
import javafx.stage.Stage;

import java.io.IOException;

public class MainLayoutController {

    @FXML
    private StackPane mainContent;

    @FXML
    private HBox activeItem;

    public void changePage(MouseEvent mouseEvent) {
        HBox item = getMenuItemFromEvent(mouseEvent);
        if (item != null && !item.equals(activeItem)) {
            switchActiveItem(item);
            String fxmlFile = item.getId() + "-view.fxml";
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/side/"+fxmlFile));
                Node newContent = loader.load();
                mainContent.getChildren().clear();
                mainContent.getChildren().add(newContent);
            } catch (Exception e) {
                e.printStackTrace();
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
        Session.logout();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/login-view.fxml"));
            Stage stage = (Stage) mainContent.getScene().getWindow();
            Utils.setSceneWithStylesheet(stage, loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
