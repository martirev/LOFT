package ui.controllers;

import core.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * This class is the controller for the Register Screen UI. It handles the user
 * input for registering a new account. The class contains FXML fields for the
 * username and password fields.
 */
public class RegisterScreenController extends SceneSwitcher {

    @FXML
    private Text errorMessage;

    @FXML
    private TextField name;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password1;

    @FXML
    private PasswordField password2;

    @FXML
    private TextField email;

    /**
     * Handles the user input for registering a new account. Validates the input and
     * registers the user if the input is valid.
     */
    @FXML
    public void handleRegister() {
        String name = this.name.getText();
        String username = this.username.getText();
        String password1 = this.password1.getText();
        String password2 = this.password2.getText();
        String email = this.email.getText();

        if (name.isEmpty() || username.isEmpty() || password1.isEmpty() || password2.isEmpty()
                || email.isEmpty()) {
            errorMessage.setText("Please fill out all fields");
            return;
        }

        if (password1.length() < 4) {
            errorMessage.setText("Password must be at least 4 characters");
            return;
        }

        if (username.length() < 4) {
            errorMessage.setText("Username must be at least 4 characters");
            return;
        }

        if (!password1.matches("^[a-zA-Z0-9-_@#!]+$")) {
            errorMessage.setText("Pasword can only contain letters, numbers, "
                    + "and the symbols _, @, # and !");
            return;
        }

        if (!password1.equals(password2)) {
            errorMessage.setText("Passwords do not match");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            errorMessage.setText("Please enter a valid email");
            return;
        }

        if (loftAccess.usernameExists(username)) {
            errorMessage.setText("Username is already taken");
            return;
        }

        User newUser = new User(name, username, password1, email);
        loftAccess.registerUser(newUser);
        insertPane("LoginScreen.fxml");
    }

    @FXML
    public void handleLogInPressed() {
        insertPane("LoginScreen.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
