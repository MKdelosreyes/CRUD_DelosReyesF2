package com.example.csit228f2_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    HelloApplication mainApp = new HelloApplication();
    public ToggleButton tbNight;
    @FXML
    private Button btnLogout;

//    @FXML
//    private void onNightModeClick() {
//        if (tbNight.isSelected()) {
//            // night mode
//            tbNight.getScene().getStylesheets().add(
//                    getClass().getResource("styles.css").toExternalForm());
//        } else {
//            tbNight.getScene().getStylesheets().clear();
//        }
//    }

    @FXML
    public void onLogOutClick() throws Exception {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.close();
        mainApp.showLoginPage(stage);
    }

}