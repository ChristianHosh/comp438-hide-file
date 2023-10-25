package com.comp438.hidefile.controller;

import com.comp438.hidefile.service.HashService;
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
    private Button button_checkHash;

    @FXML
    private Button button_decodeStegno;

    @FXML
    private Button button_generateHash;

    @FXML
    private Button button_generateStegno;

    @FXML
    private TextArea textArea_hash;

    @FXML
    private TextArea textArea_secretMessage;

    @FXML
    private TextField textField_coverImage;

    @FXML
    private TextField textField_hashImage;


    private final StegnoService stegnoService = new StegnoService();
    private final HashService hashService = new HashService();

    void disableStegnoControls(boolean disable) {
        textArea_secretMessage.setDisable(disable);
        button_generateStegno.setDisable(disable);
        button_decodeStegno.setDisable(disable);
    }

    void disableHashControls(boolean disable) {
        textArea_hash.setDisable(disable);
        button_checkHash.setDisable(disable);
        button_generateHash.setDisable(disable);
    }

    @FXML
    void handleChooseFile() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Choose a cover image");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Image Files", "*.jpeg", "*.jpg", "*.png"));

        File currentFile = fileChooser.showOpenDialog(new Stage());
        if (currentFile == null) {
            new Alert(Alert.AlertType.INFORMATION, "You must choose a file").show();
            return;
        }

        stegnoService.setCurrentFile(currentFile);

        textField_coverImage.setText(currentFile.getAbsolutePath());
        disableStegnoControls(false);
    }

    @FXML
    void handleClearFile() {
        textField_coverImage.setText("");
        textArea_secretMessage.setText("");

        stegnoService.setCurrentFile(null);
        disableStegnoControls(true);
    }

    @FXML
    void handleDecodeStegno() {
        if (stegnoService.getCurrentFile() == null)
            return;

        String decodedText = stegnoService.decode();

        textArea_secretMessage.setText(decodedText);
    }

    @FXML
    void handleEncodeStegno() {
        if (stegnoService.getCurrentFile() == null)
            return;

        BufferedImage encodedImage = stegnoService.encode(textArea_secretMessage.getText());

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Save encoded image");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Image Files", "*.png"));

        File file = fileChooser.showSaveDialog(new Stage());

        try {
            ImageIO.write(encodedImage, "png", file);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void handleChooseHashFile() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Choose an image");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Image Files", "*.jpeg", "*.jpg", "*.png"));

        File currentFile = fileChooser.showOpenDialog(new Stage());
        if (currentFile == null) {
            new Alert(Alert.AlertType.INFORMATION, "You must choose a file").show();
            return;
        }

        hashService.setCurrentFile(currentFile);

        textField_hashImage.setText(currentFile.getAbsolutePath());
        disableHashControls(false);
    }

    @FXML
    void handleClearHashFile() {
        textField_hashImage.setText("");
        textArea_hash.setText("");

        hashService.setCurrentFile(null);
        disableHashControls(true);
    }

    @FXML
    void handleGenerateHash() {
        if (hashService.getCurrentFile() == null)
            return;

        String hash = hashService.generate();

        textArea_hash.setText(hash);
    }

    @FXML
    void handleCheckHash() {
        if (hashService.getCurrentFile() == null)
            return;

        boolean isCorrect = hashService.check(textArea_hash.getText());

        new Alert(
                isCorrect ? Alert.AlertType.CONFIRMATION : Alert.AlertType.ERROR,
                isCorrect ? "Hash matches file" : "Hash doesn't match file"
        ).show();
    }

}