package ui.controllers;

import core.Exercise;
import core.User;
import core.Workout;
import core.WorkoutSorting;
import filehandling.ReadAndWrite;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

/**
 * Controller class for the highscore screen. The highscore screen displays the
 * highest weight lifted for each exercise.
 * The user can click on an exercise to see the person best of the exercise and
 * most weight lifted in an set of the exercise.
 */
public class HighscoreScreenController extends SceneSwitcher {

    @FXML
    private ListView<Exercise> exerciseListView;

    @FXML
    private Text header;

    @FXML
    private Text body;

    private WorkoutSorting workoutSorting;

    /**
     * Initializes the highscore screen by loading the user's workouts and
     * creating a WorkoutSorting object.
     *
     * @param location  the location is not used in the method
     *
     * @param resources the resources is not used in the method
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = ReadAndWrite.returnUserClassFromFile(getUser());
        workoutSorting = new WorkoutSorting(user.getWorkouts());
        exerciseListView.setOnMouseClicked(event -> {
            header.setText("");
            body.setText("");

            Exercise selected = exerciseListView.getSelectionModel().getSelectedItem();
            loadExercise(selected);
        });
        loadExerciseHistory();
    }
 
    /**
     * Loads the exercise history of the user into the exercise list view.
     * Checks if the exercise name is already in the list view to avoid duplicates.
     */
    private void loadExerciseHistory() {
        List<Workout> workouts = workoutSorting.getMostRecentWorkouts();
        for (Workout workout : workouts) {
            for (Exercise exercise : workout.getExercises()) {
                if (!exerciseListView.getItems().stream()
                        .anyMatch(e -> e.getName().equals(exercise.getName()))) {
                    exerciseListView.getItems().add(exercise);
                }
            }
        }
    }

    /**
     * Loads the given exercise into the stats text displays, showing the exercise
     * pr and best set.
     *
     * @param exercise the exercise to load into the workout list view
     */
    private void loadExercise(Exercise exercise) {
        if (exercise == null) {
            return;
        }
        header.setText(exercise.getName());
        body.setText(workoutSorting.getFormatForHighscore(exercise.getName()));
    }

    /**
     * Switches to the home screen.
     */
    @FXML
    private void handleReturnPress() {
        insertPane("HomeScreen.fxml");
    }
}
