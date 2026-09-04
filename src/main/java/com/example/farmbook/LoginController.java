package com.example.farmbook;

import javafx.fxml.FXML;

import java.io.IOException;

public class LoginController {

    @FXML
    protected void handleLogin() throws IOException {
        SessionState.login();
        HelloApplication.showHome();
    }
}