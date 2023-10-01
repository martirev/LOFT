package ui.controllers;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public abstract class SceneSwitcher implements Initializable {
    @FXML
    protected AnchorPane baseAnchor;

    protected SceneSwitcher controller;
    protected static String testFileLocation;

    /**
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
            e.printStackTrace();
            System.exit(1);
        }
    }
}
