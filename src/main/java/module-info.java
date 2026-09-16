module com.example.farmbook {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.farmbook to javafx.fxml;
    exports com.example.farmbook;
}