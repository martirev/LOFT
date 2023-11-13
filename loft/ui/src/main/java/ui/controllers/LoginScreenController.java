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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
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

    @FXML
    private Line bar;

    @FXML
    private Rectangle weightRightRectangle1;

    @FXML
    private Rectangle weightRightRectangle2;

    @FXML
    private Rectangle weightLeftRectangle1;

    @FXML
    private Rectangle weightLeftRectangle2;

    @FXML
    private Line upperRightLine1;

    @FXML
    private Line upperRightLine2;

    @FXML
    private Line upperLeftLine1;

    @FXML
    private Line upperLeftLine2;

    @FXML
    private Line lowerRightLine1;

    @FXML
    private Line lowerRightLine2;

    @FXML
    private Line lowerLeftLine1;

    @FXML
    private Line lowerLeftLine2;

    private Animation animation = new Animation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        animation.playAnimationBar(bar);
        animation.playAnimationWeight(weightRightRectangle1);
        animation.playAnimationWeight(weightRightRectangle2);
        animation.playAnimationWeight(weightLeftRectangle1);
        animation.playAnimationWeight(weightLeftRectangle2);
        animation.playAnimationArm(lowerLeftLine1, lowerLeftLine2,
                lowerRightLine1, lowerRightLine2, upperRightLine1, upperRightLine2,
                upperLeftLine1, upperLeftLine2);

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
        animation.pauseAllAnimations();
        insertPane("RegisterScreen.fxml");
    }

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
        animation.pauseAllAnimations();
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
