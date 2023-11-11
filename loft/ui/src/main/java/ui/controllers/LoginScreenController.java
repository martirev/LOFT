package ui.controllers;

import core.User;
import filehandling.ReadAndWrite;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
    }

    @FXML
    private void handleRegisterNewProfile() {
        insertPane("RegisterScreen.fxml");
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = ReadAndWrite.getUser(username, password);

        if (user == null) {
            errorMessage.setText("Login invalid");
            return;
        }
        user.setPassword(password);

        setUser(user);
        insertPane("HomeScreen.fxml");
    }

}
