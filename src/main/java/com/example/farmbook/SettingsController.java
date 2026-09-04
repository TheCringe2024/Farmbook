package com.example.farmbook;

import javafx.fxml.FXML;

import java.io.IOException;

public class SettingsController {

    @FXML
    protected void handleLogout() throws IOException {
        SessionState.logout();
        HelloApplication.showLogin();
    }
}