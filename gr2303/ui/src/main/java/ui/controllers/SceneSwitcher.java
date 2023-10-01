package ui.controllers;

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

    protected SceneSwitcher controller;
    protected static String testFileLocation;

    /**
     * A method to switch scenes.
     *
     * @param fxmlFilename The name of the FXML file to load, which lives inside
     *                     resources/ui/. For example, "WorkoutScreen.fxml".
     */
    protected void insertPane(String fxmlFilename) {
        switch (fxmlFilename) {
            case "HomeScreen.fxml":
                controller = new HomeScreenController();
                break;
            case "WorkoutScreen.fxml":
                if (testFileLocation != null && !testFileLocation.isEmpty()) {
                    controller = new WorkoutScreenController(testFileLocation);
                } else {
                    controller = new WorkoutScreenController();
                }
                break;
            default:
                System.err.println("Error: Invalid FXML filename");
                System.exit(1);
        }
        try {
            baseAnchor.getChildren().clear();
            URL url = getClass().getResource("/ui/" + fxmlFilename);

            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(controller);

            Parent parent = loader.load();
            baseAnchor.getChildren().add(parent);
        } catch (IOException e) {
            System.err.println("Problem with loading: " + fxmlFilename);
            System.exit(1);
        }
    }
}
