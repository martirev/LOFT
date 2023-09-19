package ui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;

public class HomeScreenController extends SceneSwitcher {
    /**
     * Handles the press of the "Workout" button. This loads the WorkoutScreen.
     */
    @FXML
    private void handleWorkoutPress() {
        insertPane("WorkoutScreen.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
