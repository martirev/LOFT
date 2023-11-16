package ui.controllers;

import core.User;
import filehandling.DirectLoftAccess;
import filehandling.RemoteLoftAccess;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * This class is the controller for the login screen of the application. It
 * handles user input for logging in and linkes to registering a new profile.
 */
public class LoginScreenController extends Animation {

    @FXML
    private Text errorMessage;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Pane apiPane;

    @FXML
    private Text usingText;

    @FXML
    private Text connectionInfo;

    @FXML
    private TextField urlField;

    @FXML
    private RadioButton remoteButton;

    @FXML
    private RadioButton directButton;

    /**
     * Initializes the LoginScreenController by starting animations and setting up
     * the UI based on whether the application is using local or remote data.
     * If using local data, the UI is updated accordingly and the method returns.
     * If using remote data, the UI is updated with the remote URL and connection
     * status.
     * If the remote server is not online, the UI is updated to indicate this.
     *
     * @param location  The location used to resolve relative paths for the root
     *                  object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startAnimations();
        if (!SceneSwitcher.usingServer()) {
            useLocal("Using local data", "#ed8152");
            directButton.setSelected(true);
            apiPane.setVisible(false);
            return;
        }
        remoteButton.setSelected(true);
        apiPane.setVisible(true);

        RemoteLoftAccess remoteAccess = (RemoteLoftAccess) loftAccess;
        urlField.setText(remoteAccess.getUrl());
        if (RemoteLoftAccess.serverAlive(URI.create(remoteAccess.getUrl()))) {
            useRemote("Connected", URI.create(remoteAccess.getUrl()));
            return;
        }

        useLocal("Server not online", "red");
    }

    @FXML
    private void handleRegisterNewProfile() {
        stopAllAnimations();
        insertPane("RegisterScreen.fxml");
    }

    /**
     *  Method for handling login request through checking if the user info is valid.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = loftAccess.getUser(username, password);

        if (user == null) {
            errorMessage.setText("Login invalid");
            return;
        }
        user.setPassword(password);

        setUser(user);
        stopAllAnimations();
        insertPane("HomeScreen.fxml");
    }

    @FXML
    private void radiobuttonSelected() {
        if (remoteButton.isSelected()) {
            apiPane.setVisible(true);
        } else {
            apiPane.setVisible(false);
            useLocal("Waiting to connect", "#ed8152");
        }
    }

    @FXML
    private void connect() {
        String url = urlField.getText();
        if (url.isEmpty()) {
            useLocal("Invalid URL", "red");
            return;
        }

        try {
            if (!url.endsWith("/loft/")) {
                throw new IllegalArgumentException("Invalid URL");
            }
            URI uri = URI.create(url);
            connectToUrl(uri);
        } catch (IllegalArgumentException e) {
            useLocal("Invalid URL", "red");
        }
    }

    /**
     * Connects to the given URL and sets the access to the remote server if
     * successful.
     *
     * @param url the URL to connect to
     */
    private void connectToUrl(URI url) {
        if (!RemoteLoftAccess.serverAlive(url)) {
            useLocal("Server not online", "red");
        } else {
            useRemote("Connected", url);
        }
    }

    /**
     * Sets the access to the remote server and updates the UI to reflect this.
     *
     * @param message the message to display
     * @param url     the URL of the remote server
     */
    private void useRemote(String message, URI url) {
        SceneSwitcher.setAccess(new RemoteLoftAccess(url));
        usingText.setText("Using REST API");
        connectionInfo.setText(message);
        connectionInfo.setStyle("-fx-fill: green");
    }

    /**
     * Sets the access to the local file system and updates the UI to reflect.
     *
     * @param message the message to display
     * @param color   the color of the message
     */
    private void useLocal(String message, String color) {
        SceneSwitcher.setAccess(new DirectLoftAccess());
        usingText.setText("Using local data");
        connectionInfo.setText(message);
        connectionInfo.setStyle("-fx-fill: " + color);
    }
}
