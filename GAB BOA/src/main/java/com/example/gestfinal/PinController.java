package com.example.gestfinal;

import com.example.gestfinal.Classes.UserCredentials;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class PinController implements Initializable {
    @FXML
    private TextField usernametextfield;
    @FXML
    private Label loginmessagelabel;
    private static UserCredentials userCredentials;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginmessagelabel.setText("Veuillez entrer le PIN");
    }

    public void handleNumberButton(ActionEvent event) {
        Button button = (Button) event.getSource();
        String buttonText = button.getText();
        String currentText = usernametextfield.getText();
        usernametextfield.setText(currentText + buttonText);
    }

    public void clear(ActionEvent event) {
        usernametextfield.setText("");
    }

    public void Ok(ActionEvent event) {
        usernametextfield.setText(UserCredentials.getRef() + " " + UserCredentials.getPin());
    }

    @FXML
    public void cancel(ActionEvent event) {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    public void loginButtonOnAction(ActionEvent event) throws IOException {
        if (!usernametextfield.getText().isBlank()) {
            validateLogin(event);
        }
    }

    public void validateLogin(ActionEvent event) throws IOException {
        String pin = usernametextfield.getText();

        if (isValidUsername(pin)) {
            UserCredentials.setPin(pin);
            UserCredentials.setTransferType("WALLET_TO_GAB");
            String jsonUserCredentials = UserCredentials.toJsonString();

            // Print userCredentials object in JSON format
            System.out.println("User Credentials JSON: " + jsonUserCredentials);

            String baseUrl = "http://35.246.120.111/api/client/serverTransfer";
            URL apiUrl = new URL(baseUrl);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonUserCredentials.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }

            // Get the response from the API
            int responseCode = connection.getResponseCode();

            // Read the response (Success or Error)
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR
                            ? connection.getInputStream()
                            : connection.getErrorStream(), StandardCharsets.UTF_8))) {

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Convert JSON response to ApiResponse object
                Gson gson = new Gson();
                ApiResponse apiResponse = gson.fromJson(response.toString(), ApiResponse.class);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("message.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 520, 400);
                    MsgController msgController = fxmlLoader.getController();

                    // Set the message using the ApiResponse object
                    msgController.setLoginMessage(apiResponse.getMessage());

                    Stage stage = new Stage();
                    stage.setTitle("Message");
                    stage.setScene(scene);
                    stage.show();
                } else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    // Specific handling for 500 - Internal Server Error
                    loginmessagelabel.setText("La reference est incorrecte");
                } else {
                    loginmessagelabel.setText("Un probleme est survenu: " + responseCode);
                }
            }

            connection.disconnect();
        } else {
            loginmessagelabel.setText("Format REF invalide");
        }
    }




    private boolean isValidUsername(String username) {
        return username.length() == 4 && username.matches("\\d+");
    }

    public static class ApiResponse {
        private String message;
        private boolean isServed;
        private int transferID;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isServed() {
            return isServed;
        }

        public void setServed(boolean served) {
            isServed = served;
        }

        public int getTransferID() {
            return transferID;
        }

        public void setTransferID(int transferID) {
            this.transferID = transferID;
        }
    }

}
