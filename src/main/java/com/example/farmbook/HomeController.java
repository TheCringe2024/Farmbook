package com.example.farmbook;

import javafx.fxml.FXML;

import java.io.IOException;

public class HomeController {

    @FXML
    protected void handleSettings() throws IOException {
        HelloApplication.showSettings();
    }
}