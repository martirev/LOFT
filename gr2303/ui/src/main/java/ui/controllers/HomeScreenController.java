package ui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;

/**
 * This class is the controller for the HomeScreen.fxml file. It extends the
 * SceneSwitcher class and handles the press of the "Workout" button by loading
 * the WorkoutScreen.fxml file.
 */
public class HomeScreenController extends SceneSwitcher {
    /**
     * Handles the press of the "Workout" button. This loads the WorkoutScreen.
     */
    @FXML
    private void handleWorkoutPress() {
        insertPane("WorkoutScreen.fxml");
    }

    @FXML
    private void handleJournalPress() {
        insertPane("JournalScreen.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
