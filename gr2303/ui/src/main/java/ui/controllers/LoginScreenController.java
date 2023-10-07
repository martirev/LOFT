package ui.controllers;

import core.ReadAndWrite;
import core.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * This class is the controller for the login screen of the application. It
 * handles user input for logging in and linkes to registering a new profile.
 */
public class LoginScreenController extends SceneSwitcher {

    @FXML
    private Text errorMessage;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private ReadAndWrite readAndWrite;

    
    public LoginScreenController() {
        readAndWrite = new ReadAndWrite();
    }

    public LoginScreenController(String fileLocation) {
        readAndWrite = new ReadAndWrite(fileLocation);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void handleRegisterNewProfile() {
        insertPane("RegisterScreen.fxml");
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = readAndWrite.getUser(username, password);
        if (user == null) {
            errorMessage.setText("Login invalid");
            return;
        }
        user.setPassword(password);

        setUser(user);
        insertPane("HomeScreen.fxml");
    }

}
