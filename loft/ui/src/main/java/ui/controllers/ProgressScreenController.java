package ui.controllers;

import core.Exercise;
import core.User;
import core.WorkoutSorting;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/**
 * The ProgressScreenController class is responsible for controlling the
 * Progress Screen UI. It extends the SceneSwitcher class and initializes the
 * LineCharts for the total weight and top exercises. The class also provides
 * methods for populating the charts with data and switching to other screens.
 */
public class ProgressScreenController extends SceneSwitcher {

    @FXML
    private LineChart<String, Number> totalWeightChart;

    @FXML
    private LineChart<String, Number> benchPressChart;

    @FXML
    private LineChart<String, Number> deadliftChart;

    @FXML
    private LineChart<String, Number> squatChart;

    private WorkoutSorting workoutSorting;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User prev = getUser();
        User user = loftAccess.getUser(prev.getUsername(), prev.getPassword());

        workoutSorting = new WorkoutSorting(user.getWorkouts());
        populateTopExercisesCharts();
        populateTotalWeightChart();
    }

    /**
     * Method for going through all workouts and adding the total lifted weight to a
     * total weight chart. The data is displayed in a LineChart with weight on the
     * y-axis and date of the exercise on the x-axis.
     */
    public void populateTotalWeightChart() {
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

    /**
     * Populates the bench, deadlift and squat charts with data. The charts display
     * the highest personal record (PR) for each exercise per day. The data is
     * displayed in a LineChart with weight on the y-axis and date of the exercise
     * on the x-axis
     */
    public void populateTopExercisesCharts() {
        XYChart.Series<String, Number> benchSeries = new XYChart.Series<>();
        benchSeries.setName("Highest bench PR per day");
        XYChart.Series<String, Number> deadliftSeries = new XYChart.Series<>();
        deadliftSeries.setName("Highest deadlift PR per day");
        XYChart.Series<String, Number> squatSeries = new XYChart.Series<>();
        squatSeries.setName("Highest squat PR per day");

        for (LocalDate date : workoutSorting.getUniqueDates()) {
            HashMap<String, List<Exercise>> sameExercises = workoutSorting.getSameExercises();
            for (Map.Entry<String, List<Exercise>> entry : sameExercises.entrySet()) {
                String name = entry.getKey();
                Exercise exercise = entry.getValue().get(0);

                int pr = workoutSorting.getPrOnDay(exercise, date);
                if (pr == 0) {
                    continue;
                }

                switch (name.toLowerCase().replaceAll("\s", "")) {
                    case "benchpress":
                        benchSeries.getData().add(new XYChart.Data<>(date.toString(), pr));
                        break;
                    case "deadlift":
                        deadliftSeries.getData().add(new XYChart.Data<>(date.toString(), pr));
                        break;
                    case "squat":
                        squatSeries.getData().add(new XYChart.Data<>(date.toString(), pr));
                        break;
                    default:
                        break;
                }
            }
        }

        benchPressChart.getData().add(benchSeries);
        deadliftChart.getData().add(deadliftSeries);
        squatChart.getData().add(squatSeries);
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
