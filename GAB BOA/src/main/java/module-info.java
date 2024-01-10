module com.example.gestfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    // requires mysql.connector.j;
    // requires com.jfoenix;
    requires com.google.gson;

    // Existing opens and exports
    opens com.example.gestfinal to javafx.fxml, com.google.gson;
    exports com.example.gestfinal;
    exports com.example.gestfinal.Classes;
    opens com.example.gestfinal.Classes to javafx.fxml, com.google.gson;
    exports com.example.gestfinal.Controllers;
    opens com.example.gestfinal.Controllers to javafx.fxml, com.google.gson;
}
