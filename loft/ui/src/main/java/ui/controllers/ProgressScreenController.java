package ui.controllers;

import core.Exercise;
import core.User;
import core.WorkoutSorting;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

/**
 * The ProgressScreenController class is responsible for controlling the
 * Progress Screen UI. It extends the SceneSwitcher class and initializes the
 * LineCharts for the total weight and top exercises. The class also provides
 * methods for populating the charts with data and switching to other screens.
 */
public class ProgressScreenController extends SceneSwitcher {

    @FXML
    private TextArea totalWeightListView;

    @FXML
    private LineChart<String, Number> totalWeightChart;

    @FXML
    private ListView<Exercise> exerciseHistoryListView;

    @FXML
    private TextArea exerciseListView;

    @FXML
    private LineChart<String, Number> exerciseChart;

    private WorkoutSorting workoutSorting;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User prev = getUser();
        User user = loftAccess.getUser(prev.getUsername(), prev.getPassword());

        workoutSorting = new WorkoutSorting(user.getWorkouts());

        populateTotalWeightChart();
        setTotalWeightListView();

        loadExerciseHistory();
        exerciseHistoryListView.setOnMouseClicked(event -> {
            Exercise selected = exerciseHistoryListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }

            exerciseChart.getData().clear();
            exerciseListView.clear();
            populateExerciseChart(selected);
            setExerciseListView(selected);
        });
    }

    /**
     * Method for loading all exercises from the user's workout history into the
     * exercise history ListView. The method uses the WorkoutSorting class to get
     * all exercises.
     */
    private void loadExerciseHistory() {
        List<Exercise> exercises = workoutSorting.getAllUniqueExerciseNames();
        for (Exercise exercise : exercises) {
            exerciseHistoryListView.getItems().add(exercise);
        }
    }

    /**
     * Method for setting the text in the exercise ListView. The method uses the
     * WorkoutSorting class to get the exercise's name and personal record.
     *
     * @param exercise The exercise to be displayed in the ListView.
     */
    private void setExerciseListView(Exercise exercise) {
        StringBuilder sb = new StringBuilder();

        sb.append(exercise.getName() + "\n");
        sb.append("\t - PR: " + workoutSorting.getExercisesPr(exercise.getName()) + " kg");

        exerciseListView.setText(sb.toString());
    }

    private void populateExerciseChart(Exercise exercise) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(exercise.getName() + " personal records per day");

        for (LocalDate date : workoutSorting.getUniqueDates()) {
            int pr = workoutSorting.getPrOnDay(exercise, date);
            if (pr != 0) {
                series.getData().add(new XYChart.Data<>(date.toString(), pr));
            }
        }
        exerciseChart.getData().add(series);
    }

    /**
     * Method for setting the text in the total weight ListView. The method uses
     * the WorkoutSorting class to get the total weight lifted and the top n < 3
     * exercises.
     */
    private void setTotalWeightListView() {
        List<Exercise> allExercisesSorted = workoutSorting.getAllUniqueExerciseNamesSortedByPr();

        StringBuilder sb = new StringBuilder();

        sb.append("Total weigth lifted: \n");
        sb.append("\t - Weight: " + workoutSorting.getTotalWeightLifted() + " kg \n\n");

        int size = Math.min(3, allExercisesSorted.size());
        sb.append("Top " + size + " PRs:\n");

        for (int i = 0; i < size; i++) {
            sb.append("\t - " + allExercisesSorted.get(i).getName() + ": "
                    + workoutSorting.getExercisesPr(allExercisesSorted.get(i).getName())
                    + " kg\n");
        }

        totalWeightListView.setText(sb.toString());
    }

    /**
     * Method for going through all workouts and adding the total lifted weight to a
     * total weight chart. The data is displayed in a LineChart with weight on the
     * y-axis and date of the exercise on the x-axis.
     */
    private void populateTotalWeightChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total lifted weight per day");

        Map<LocalDate, Integer> perDay = workoutSorting.getWeightPerDay();
        SortedSet<LocalDate> keys = new TreeSet<>(perDay.keySet());

        for (LocalDate date : keys) {
            int weight = perDay.get(date);
            series.getData().add(new XYChart.Data<>(date.toString(), weight));
        }

        totalWeightChart.getData().add(series);
    }

    @FXML
    public void handleReturnPress() {
        insertPane("HomeScreen.fxml");
    }

    @FXML
    public void switchToJournal() {
        insertPane("JournalScreen.fxml");
    }
}
