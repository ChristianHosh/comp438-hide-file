package com.comp438.hidefile.controller;

import com.comp438.hidefile.service.StegnoService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML
    private TextField textField_coverImage;

    @FXML
    private TextArea textArea_secretMessage;

    @FXML
    private Button button_decodeStegno;

    @FXML
    private Button button_generateStegno;


    private final StegnoService stegnoService = new StegnoService();

    void disableControls(boolean disable) {
        textArea_secretMessage.setDisable(disable);
        button_generateStegno.setDisable(disable);
        button_decodeStegno.setDisable(disable);
    }

    @FXML
    void handleChooseFile() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Choose a cover image");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("PNG Files", "*.jpg", "*.png"));

        File currentFile = fileChooser.showOpenDialog(new Stage());
        if (currentFile == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You must choose a file");
            alert.show();
            return;
        }

        stegnoService.setCurrentFile(currentFile);

        textField_coverImage.setText(currentFile.getAbsolutePath());
        disableControls(false);
    }

    @FXML
    void handleClearFile() {
        textField_coverImage.setText("");
        textArea_secretMessage.setText("");

        disableControls(true);
    }

    @FXML
    void handleDecodeStegno() {
        if (stegnoService.getCurrentFile() == null)
            return;

        String decodedText = stegnoService.decode();
        System.out.println("Extracted Text => " + decodedText);
    }

    @FXML
    void handleEncodeStegno() {
        if (stegnoService.getCurrentFile() == null)
            return;

        BufferedImage encodedImage = stegnoService.encode(textArea_secretMessage.getText());

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Save encoded image");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));

        File file = fileChooser.showSaveDialog(new Stage());

        try {
            ImageIO.write(encodedImage, "png", file);

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }


}