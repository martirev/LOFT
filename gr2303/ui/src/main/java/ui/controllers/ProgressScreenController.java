package ui.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Collections;

import core.Exercise;
import core.ReadAndWrite;
import core.User;
import core.Workout;
import core.WorkoutSorting;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;


public class ProgressScreenController extends SceneSwitcher {

    @FXML
    private LineChart<String, Number> totalWeightChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private LineChart<String, Number> benchPressChart;

    @FXML
    private LineChart<String, Number> deadliftChart;

    @FXML
    private LineChart<String, Number> squatChart;

    WorkoutSorting workoutSorting;

    private ReadAndWrite readAndWrite;

    public ProgressScreenController() {
        readAndWrite = new ReadAndWrite();
    }

    public ProgressScreenController(String location) {
        readAndWrite = new ReadAndWrite(location);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = readAndWrite.returnUserClassFromFile();
        workoutSorting = new WorkoutSorting(user.getWorkouts());
        populateTopExercisesCharts();
        populateTotalWeightChart();
    }

    /** 
     * <p>
     * Method for going through all workouts and adding the total lifted weight to a total weight chart
     * </p>
     *  
     * <p>
     *The data is displayed in a LineChart with weight on the y-axis and date of the exercise on the x-axis
     *</p>
    *
    *<p>
    *@param workouts
    *</p>
    */
    @FXML
    public void populateTotalWeightChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total lifted weight per workout");
        List<Workout> list = workoutSorting.getMostRecentWorkouts();
        Collections.reverse(list);

        for (Map.Entry<LocalDate, Integer> entry : workoutSorting.getWeightPerDay().entrySet()) {
            LocalDate date = entry.getKey();
            int weight = entry.getValue();
            series.getData().add(new XYChart.Data<>(date.toString(), weight));
        }
        totalWeightChart.getData().add(series);
    }
    

    /** 
     * <p>
     * Method for going through all exercises inside a workout. If the name of the exercise match one of the cases the total weight is
     * added to its respective chart
     * </p>
     *  
     * <p>
     *The data is displayed in a LineChart with weight on the y-axis and date of the exercise on the x-axis
     *</p>
    *
    *<p>
    *@param workouts
    *</p>
    */
    @FXML
    public void populateTopExercisesCharts() {
        XYChart.Series<String, Number> benchSeries = new XYChart.Series<>();
        benchSeries.setName("Highest bench PR per workout");
        XYChart.Series<String, Number> deadliftSeries = new XYChart.Series<>();
        deadliftSeries.setName("Highest deadlift PR per workout");
        XYChart.Series<String, Number> squatSeries = new XYChart.Series<>();
        squatSeries.setName("Highest squat PR per workout");

        List<Workout> list = workoutSorting.getMostRecentWorkouts();
        Collections.reverse(list);
        for(Workout workout : list) {
            LocalDate date = workout.getDate();
            for(Exercise exercise : workout.getExercises()) {
                 switch (exercise.getName().toLowerCase().replaceAll("\s", "")) {
                    case "benchpress":
                        benchSeries.getData().add(new XYChart.Data<>(date.toString(), workoutSorting.getPrOnDay(exercise, date)));
                        break;
                    case "deadlift" :
                        deadliftSeries.getData().add(new XYChart.Data<>(date.toString(), workoutSorting.getPrOnDay(exercise, date)));
                        break;
                    case "squat":
                        squatSeries.getData().add(new XYChart.Data<>(date.toString(), workoutSorting.getPrOnDay(exercise, date)));
                        break;
                }
            }
        }
        benchPressChart.getData().add(benchSeries);
        deadliftChart.getData().add(deadliftSeries);
        squatChart.getData().add(squatSeries);
    }

    @FXML
    private void handleReturnPress() {
        insertPane("HomeScreen.fxml");
    }

    @FXML
    private void switchToJournal() {
        insertPane("JournalScreen.fxml");
    }
}
