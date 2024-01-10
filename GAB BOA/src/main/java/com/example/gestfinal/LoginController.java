package com.example.gestfinal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    @FXML
    private Button cancelbutton;
    @FXML
    private TextField usernametextfield;
    @FXML
    private PasswordField passwordtextfield;
    @FXML
    private Label loginmessagelabel;
    public void cancelbuttonaction(ActionEvent event) throws IOException {

        Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("transfer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


}