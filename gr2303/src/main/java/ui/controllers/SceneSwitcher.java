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

    /**
     * @param fxmlFilename The name of the FXML file to load, which lives inside
     *                     resources/ui/. For example, "WorkoutScreen.fxml".
     * @param controller   An instance of the controller class for the FXML file.
     *                     For example, "new WorkoutScreenController()".
     */
    protected void insertPane(String fxmlFilename, SceneSwitcher controller) {
        try {
            baseAnchor.getChildren().clear();
            URL url = getClass().getResource("/ui/" + fxmlFilename);

            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(controller);

            Parent parent = loader.load();
            baseAnchor.getChildren().add(parent);
        } catch (IOException e) {
            System.out.println("Problem with loading: " + fxmlFilename);
            System.exit(1);
        }
    }
}
