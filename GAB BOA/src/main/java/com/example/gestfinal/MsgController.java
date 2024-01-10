package com.example.gestfinal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class MsgController {
    @FXML
    private TextArea loginmessagelabel;

    public void setLoginMessage(String message) {
        loginmessagelabel.setText(message);
    }

    @FXML
  private void Ok(ActionEvent event) {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

//    private void Ok(ActionEvent event) throws IOException {
//
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("transfer.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 520, 400);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }
}
