package com.comp438.hidefile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class HideFileApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HideFileApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        
        stage.setTitle("HideFile");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}