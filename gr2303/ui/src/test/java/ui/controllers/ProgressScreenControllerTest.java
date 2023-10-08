package ui.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import core.Exercise;
import core.Set;
import core.Workout;
import core.WorkoutSorting;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import ui.App;

public class ProgressScreenControllerTest extends ApplicationTest {

    private static String testFileLocation = System.getProperty("user.home")
            + System.getProperty("file.separator") + "testUserData.json";

    private static ProgressScreenController controller = new ProgressScreenController();
    private static WorkoutSorting workoutSorting;

    private Parent root;

    private static Workout workout1; 
    private static Workout workout2;
    private static Workout workout3; 

    XYChart.Series<String, Number> totalweightSeries = new XYChart.Series<>();
    XYChart.Series<String, Number> benchSeries = new XYChart.Series<>();
    XYChart.Series<String, Number> deadliftSeries = new XYChart.Series<>();
    XYChart.Series<String, Number> squatSeries = new XYChart.Series<>();

    private XYChart<String, Number> totalweightChart = lookup("totalWeightChart").query();
    private XYChart<String, Number> benchChart = lookup("benchPressChart").query();
    private XYChart<String, Number> deadliftChart = lookup("deadliftChart").query();
    private XYChart<String, Number> squatChart = lookup("squatChart").query();

    @Override
    public void start(Stage stage) throws IOException {
        SceneSwitcher.setFileLocation(testFileLocation);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("ProgressScreen.fxml"));
        controller = new ProgressScreenController(testFileLocation);
        fxmlLoader.setController(controller);
        root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @AfterAll
    public static void teardown() {
        deleteTestFile();
    }

    @BeforeAll
    public static void setUp() {
        deleteTestFile();

        workout1 = new Workout(LocalDate.of(2023, 05, 16));
        workout2 = new Workout(LocalDate.of(2023, 05, 16));
        workout3 = new Workout(LocalDate.of(2023, 4, 4));

        final Exercise bench = new Exercise("Bench");
        final Exercise squat = new Exercise("squat");
        final Exercise inclineBench = new Exercise("inclineBench");

        Set benchSet1 = new Set(10, 150);
        Set benchSet2 = new Set(8, 130);
        Set benchSet3 = new Set(6, 110);
        bench.addSet(benchSet1);
        bench.addSet(benchSet2);
        bench.addSet(benchSet3);

        Set squatSet1 = new Set(10, 200);
        Set squatSet2 = new Set(8, 180);
        Set squatSet3 = new Set(6, 160);
        squat.addSet(squatSet1);
        squat.addSet(squatSet2);
        squat.addSet(squatSet3);

        Set inclineSet1 = new Set(10, 40);
        Set inclineSet2 = new Set(8, 50);
        Set inclineSet3 = new Set(8, 50);
        inclineBench.addSet(inclineSet1);
        inclineBench.addSet(inclineSet2);
        inclineBench.addSet(inclineSet3);

        workout1.addExercise(inclineBench);
        workout1.addExercise(bench);

        workout2.addExercise(inclineBench);
        workout2.addExercise(squat);

        workout3.addExercise(bench);
        workout3.addExercise(squat);
        List<Workout> list = new ArrayList<>();
        list.add(workout1);
        list.add(workout2);
        list.add(workout3);
        workoutSorting = new WorkoutSorting(list);

        controller.populateTotalWeightChart();
        controller.populateTopExercisesCharts();
    }

    @Test
    public void testNotEmptyTotalWeightChart() {
        assertFalse(totalweightChart.getData().size() == 0);
    }

    @Test
    public void testNotEmptyTotalExerciseChart() {
        assertFalse(benchChart.getData().size() == 0);
        assertFalse(deadliftChart.getData().size() == 0);
        assertFalse(squatChart.getData().size() == 0);
    }

    @Test
    public void workoutOnSameDay() {
        
    }

    @Test
    public void testLabelOnXaxis() {
        totalweightSeries.setName("totalweightSeries");
        benchSeries.setName("benchSeries");
        deadliftSeries.setName("deadliftSeries");
        squatSeries.setName("squatSeries");
        assertEquals("totalweightSeries", totalweightSeries.getName());
        assertEquals("benchSeries", benchSeries.getName());
        assertEquals("deadliftSeries", deadliftSeries.getName());
        assertEquals("squatSeries", squatSeries.getName());
    }


    public static void deleteTestFile() {
        try {
            if (new File(testFileLocation).exists()) {
                Files.delete(Path.of(testFileLocation));
            }
        } catch (IOException e) {
            System.err.println("Error deleting test file");
        }
    }
}
