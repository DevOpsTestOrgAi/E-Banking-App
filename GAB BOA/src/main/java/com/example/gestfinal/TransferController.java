package com.example.gestfinal;

import com.example.gestfinal.Classes.UserCredentials;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class TransferController {
    @FXML
    private TextField usernametextfield;
    @FXML
    private Label loginmessagelabel;

    private static final UserCredentials userCredentials = null;

    public void loginButtonOnAction(ActionEvent e) throws IOException {
        if (!usernametextfield.getText().isBlank()) {
            validateLogin(e);
        }
    }

    public void validateLogin(ActionEvent event) throws IOException {
        //UserCredentials userCredentials = new UserCredentials();
        String ref = usernametextfield.getText();

        if (isValidUsername(ref)) {
            UserCredentials.setRef(ref);
            UserCredentials.setTransferType("WALLET_TO_GAB");
            Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pin.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 520, 400);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();        } else {
            loginmessagelabel.setText("Format REF invalide");
        }
    }

    private boolean isValidUsername(String username) {
        return username.startsWith("837") && username.length() == 13 && username.matches("\\d+");
    }


}
