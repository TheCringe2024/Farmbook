package com.example.farmbook;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controls the Settings screen: shows the account and lets the user change email and password.
 */
public class SettingsController {

    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private TextField newEmailField;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label emailMessage;
    @FXML private Label passwordMessage;

    private final SqliteAccountDAO dao = new SqliteAccountDAO();
    private final AccountService service = new AccountService(dao);

    /** Shows the logged-in user's details when the screen opens. */
    @FXML
    public void initialize() {
        String username = SessionState.getCurrentUsername();
        if (username == null) {
            usernameLabel.setText("Username: (not available - please log in again)");
            emailLabel.setText("");
            return;
        }
        usernameLabel.setText("Username: " + username);
        showEmail(username);
    }

    private void showEmail(String username) {
        try {
            String email = dao.getEmail(username);
            emailLabel.setText("Email: " + (email == null ? "(not found)" : email));
        } catch (SQLException e) {
            emailLabel.setText("Email: (could not load)");
        }
    }

    @FXML
    protected void handleChangeEmail() {
        String username = SessionState.getCurrentUsername();
        if (username == null) {
            show(emailMessage, "Please log in again.", false);
            return;
        }
        AccountService.Result result = service.changeEmail(username, newEmailField.getText().trim());
        if (result == AccountService.Result.OK) {
            newEmailField.clear();
            showEmail(username);
        }
        show(emailMessage, messageFor(result, "Email updated."), result == AccountService.Result.OK);
    }

    @FXML
    protected void handleChangePassword() {
        String username = SessionState.getCurrentUsername();
        if (username == null) {
            show(passwordMessage, "Please log in again.", false);
            return;
        }
        AccountService.Result result = service.changePassword(username,
                currentPasswordField.getText(), newPasswordField.getText(), confirmPasswordField.getText());
        if (result == AccountService.Result.OK) {
            currentPasswordField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();
        }
        show(passwordMessage, messageFor(result, "Password updated."), result == AccountService.Result.OK);
    }

    /** Turns a result into a plain message the user can act on. */
    private String messageFor(AccountService.Result result, String okMessage) {
        switch (result) {
            case OK: return okMessage;
            case INVALID_EMAIL: return "That doesn't look like a valid email.";
            case EMAIL_TAKEN: return "That email is already used by another account.";
            case USER_NOT_FOUND: return "Account not found. Please log in again.";
            case WRONG_PASSWORD: return "Your current password is wrong.";
            case PASSWORDS_DO_NOT_MATCH: return "The two new passwords don't match.";
            case PASSWORD_TOO_SHORT: return "New password must be at least " + AccountService.MIN_PASSWORD_LENGTH + " characters.";
            case SAME_PASSWORD: return "New password must be different from the current one.";
            default: return "Something went wrong. Please try again.";
        }
    }

    private void show(Label label, String text, boolean success) {
        label.setStyle("-fx-text-fill: " + (success ? "#7CFC9A" : "#ff8080") + ";");
        label.setText(text);
    }

    /** Goes back to the home page. */
    @FXML
    protected void handleBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("homepage-view.fxml"));
        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 1000, 800));
    }

    @FXML
    protected void handleLogout() throws IOException {
        SessionState.logout();
        HelloApplication.showLogin();
    }
}
