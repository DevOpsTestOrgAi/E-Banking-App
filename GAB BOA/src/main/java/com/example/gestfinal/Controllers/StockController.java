package com.example.gestfinal.Controllers;

import com.example.gestfinal.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class StockController {

        @FXML
        private TextField Nom;

        @FXML
        private TextField Prix;
        @FXML
        private TextField Description;

        @FXML
        private Label message;




    @FXML
    void switchScene(ActionEvent event) throws IOException {
    Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 785, 546);
                    stage.setTitle("Hello!");
                    stage.setScene(scene);
                    stage.show();
    }
}


