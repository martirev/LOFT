package ui.controllers;

import core.User;
import filehandling.DirectLoftAccess;
import filehandling.LoftAccess;
import filehandling.RemoteLoftAccess;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

/**
 * An abstract class that implements Initializable and provides a method to
 * switch scenes. The class loads FXML files from the resources/ui/ directory
 * and sets the controller for the loaded FXML file. The class also provides a
 * protected AnchorPane baseAnchor and a protected SceneSwitcher controller. The
 * class is intended to be extended by other classes that implement the
 * controller for specific FXML files.
 */
public abstract class SceneSwitcher implements Initializable {
    @FXML
    protected AnchorPane baseAnchor;

    /**
     * The SceneSwitcher controller. For example JournalScreenController. This is
     * used to link fxml files to their correct controller.
     */
    protected SceneSwitcher controller;

    /**
     * The currently logged in user. This is set when the user logs in and is used
     * to keep track of the user throughout the application. This is why it is
     * static.
     */
    private static User user;

    static LoftAccess loftAccess = new DirectLoftAccess();

    /**
     * A method to switch scenes.
     *
     * @param fxmlFilename The name of the FXML file to load, which lives inside
     *                     resources/ui/. For example, "WorkoutScreen.fxml".
     */
    protected void insertPane(String fxmlFilename) {
        switch (fxmlFilename) {
            case "JournalScreen.fxml":
                controller = new JournalScreenController();
                break;
            case "HomeScreen.fxml":
                controller = new HomeScreenController();
                break;
            case "ProgressScreen.fxml":
                controller = new ProgressScreenController();
                break;
            case "WorkoutScreen.fxml":
                controller = new WorkoutScreenController();
                break;
            case "HighscoreScreen.fxml":
                controller = new HighscoreScreenController();
                break;
            case "LoginScreen.fxml":
                controller = new LoginScreenController();
                break;
            case "RegisterScreen.fxml":
                controller = new RegisterScreenController();
                break;
            case "UserInfoScreen.fxml":
                controller = new UserInfoScreenController();
                break;
            default:
                throw new IllegalArgumentException("Invalid FXML filename");
        }

        try {
            baseAnchor.getChildren().clear();
            URL url = SceneSwitcher.class.getResource("/ui/" + fxmlFilename);

            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(controller);

            Parent parent = loader.load();
            baseAnchor.getChildren().add(parent);
        } catch (IOException e) {
            System.err.println("Problem with loading: " + fxmlFilename);
            e.printStackTrace();
        }
    }

    /**
     * Sets the current user.
     *
     * @param user the user to set
     */
    static void setUser(User user) {
        SceneSwitcher.user = user;
    }

    /**
     * Returns the currently logged in user.
     *
     * @return the User object representing the currently logged in user.
     */
    static User getUser() {
        return user;
    }

    static void setAccess(LoftAccess access) {
        SceneSwitcher.loftAccess = access;
    }

    static boolean usingServer() {
        return loftAccess instanceof RemoteLoftAccess;
    }
}
