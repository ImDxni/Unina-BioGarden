package com.unina.biogarden;

import com.unina.biogarden.database.ConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class BioGarden extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BioGarden.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.getIcons().add(new Image(getClass().getResourceAsStream("../../../logo.png")));

        stage.setTitle("Bio Garden");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        ConnectionManager.shutdown();
    }
}