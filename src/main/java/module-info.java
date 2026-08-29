module com.example.farmbook {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.farmbook to javafx.fxml;
    exports com.example.farmbook;
}