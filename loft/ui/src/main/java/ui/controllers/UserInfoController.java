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
 * This is the controller class for UserinfoScreen. It handles
 * requests form the user to see user information and lets the user
 * change name, username, password and email connected to the user.
 * The controller also allows the user to log out from the app
 */
public class UserInfoController extends SceneSwitcher {
    @FXML
    private Text errorMessage;

    @FXML
    private TextField name;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password0;

    @FXML
    private PasswordField password1;

    @FXML
    private PasswordField password2;

    @FXML
    private TextField email;

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

    /**
     * Populates the textfields for name, username and email.
     */
    public void populateUserInfoFields() {
        User user = getUser();
        name.setText(user.getName());
        username.setText(user.getUsername());
        email.setText(user.getEmail());
    }

    @FXML
    private void handleReturnPress() {
        User user = getUser();
        if (isChanged(user)) {
            errorMessage.setText("Save changes before returning");
            return;
        }
        insertPane("HomeScreen.fxml");
    }

    @FXML
    private void handlelogOutPress() {
        User user = getUser();
        if (isChanged(user)) {
            errorMessage.setText("Save changes before logging out");
            return;
        }
        SceneSwitcher.setUser(null);
        insertPane("LoginScreen.fxml");

    }

    /**
     * Handles saving updated user information to userData.json.
     */
    @FXML
    public void handleSaveChangesPress() {
        String name = this.name.getText();
        String username = this.username.getText();
        String password0 = this.password0.getText();
        String password1 = this.password1.getText();
        String password2 = this.password2.getText();
        String email = this.email.getText();
        if (name.isEmpty() || username.isEmpty() || password0.isEmpty() || password1.isEmpty()
                || password2.isEmpty() || email.isEmpty()) {
            errorMessage.setText("Please fill out all fields");
            return;
        }
        if ((password1.isEmpty() && !password2.isEmpty())
                || (!password1.isEmpty() && password2.isEmpty())) {
            errorMessage.setText("Please fill out all fields!");
        }
        if (!password1.equals(password2)) {
            errorMessage.setText("Passwords do not match");
            return;
        }
        if (!password0.equals(getUser().getPassword())) {
            errorMessage.setText("The old password is wrong");
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
        if (!email.contains("@") || !email.contains(".")) {
            errorMessage.setText("Please enter a valid email");
            return;
        }
        User oldUser = getUser();
        User newUser = new User(name, username, password1, email);
        getUser().getWorkouts().stream().forEach(workout -> newUser.addWorkout(workout));
        try {
            ReadAndWrite.updateUserInfo(oldUser, newUser);
        } catch (IllegalArgumentException e) {
            errorMessage.setText("Username is taken");
            return;
        }
        setUser(newUser);
        insertPane("LoginScreen.fxml");
    }

    private boolean isChanged(User user) {
        if (!user.getName().equals(name.getText())
                || !user.getUsername().equals(username.getText())
                || !user.getEmail().equals(email.getText())) {
            return true;
        }
        if (!password1.getText().equals("")
                || !password2.getText().equals("")) {
            return true;
        }
        return false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateUserInfoFields();
        animation.playAnimationBar(bar);
        animation.playAnimationWeight(weightRightRectangle1);
        animation.playAnimationWeight(weightRightRectangle2);
        animation.playAnimationWeight(weightLeftRectangle1);
        animation.playAnimationWeight(weightLeftRectangle2);
        animation.playAnimationArm(lowerLeftLine1, lowerLeftLine2,
                lowerRightLine1, lowerRightLine2, upperRightLine1, upperRightLine2,
                upperLeftLine1, upperLeftLine2);
    }
}
