package ui.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import core.Exercise;
import core.ReadAndWrite;
import core.Set;
import core.User;
import core.Workout;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.App;

public class ProgressScreenControllerTest extends ApplicationTest {

    private ProgressScreenController controller;

    private static final String testFileLocation = System.getProperty("user.home")
            + System.getProperty("file.separator") + "testUserData.json";

    private Parent root;

    private static Workout workout1;
    private static Workout workout2;
    private static Workout workout3;

    private XYChart<String, Number> totalweightChart;
    private XYChart<String, Number> benchChart;
    private XYChart<String, Number> deadliftChart;
    private XYChart<String, Number> squatChart;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("ProgressScreen.fxml"));
        controller = new ProgressScreenController();
        fxmlLoader.setController(controller);
        root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
        totalweightChart = lookup("#totalWeightChart").query();
        benchChart = lookup("#benchPressChart").query();
        deadliftChart = lookup("#deadliftChart").query();
        squatChart = lookup("#squatChart").query();
    }

    @AfterAll
    public static void teardown() {
        deleteTestFile();
    }

    @BeforeAll
    public static void setUp() {
        ReadAndWrite.setFileLocation(testFileLocation);
        deleteTestFile();
        User user = new User("Test person", "tester", "hunter2", "tester@example.com");
        SceneSwitcher.setUser(user);

        workout1 = new Workout(LocalDate.of(2023, 05, 16));
        workout2 = new Workout(LocalDate.of(2023, 05, 16));
        workout3 = new Workout(LocalDate.of(2023, 4, 4));

        final Exercise bench = new Exercise("benchpress");
        final Exercise squat = new Exercise("squat");
        final Exercise bench2 = new Exercise("benchpress");
        final Exercise deadlift = new Exercise("deadift");

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

        Set bench2Set1 = new Set(10, 40);
        Set bench2Set2 = new Set(8, 300);
        Set bench2Set3 = new Set(8, 50);
        bench2.addSet(bench2Set1);
        bench2.addSet(bench2Set2);
        bench2.addSet(bench2Set3);

        Set deadlift1 = new Set(10, 120);
        deadlift.addSet(deadlift1);

        workout1.addExercise(bench2);
        workout1.addExercise(bench);
        workout1.addExercise(deadlift);

        workout2.addExercise(bench2);
        workout2.addExercise(squat);

        workout3.addExercise(bench);
        workout3.addExercise(squat);

        ReadAndWrite.writeWorkoutToUser(workout1, user);
        ReadAndWrite.writeWorkoutToUser(workout2, user);
        ReadAndWrite.writeWorkoutToUser(workout3, user);

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
    public void testTotalWeightWorkoutOnSameDay() {
        ObservableList<Data<String, Number>> data = totalweightChart.getData().get(0).getData();
        assertEquals(2, data.size(), "Total weight chart should have 2 data points");
        assertTrue(data.stream()
                .anyMatch(element -> element.getXValue().equals("2023-05-16")
                        && element.getYValue().equals(15200)),
                "Total weight chart should have a data point for 2023-05-16 with value 14000 (kg)");
        assertTrue(data.stream()
                .anyMatch(element -> element.getXValue().equals("2023-04-04")
                        && element.getYValue().equals(7600)),
                "Total weight chart should have a data point for 2023-04-04 with value 7600 (kg)");
    }

    @Test
    public void workoutOnSameDayTopExercise() {
        ObservableList<Data<String, Number>> data = benchChart.getData().get(0).getData();
        assertEquals(2, data.size());
        assertTrue(data.stream().anyMatch(element -> element.getXValue().equals("2023-05-16") 
        && element.getYValue().equals(300)), 
        "Bench press chart should have a data point for 2023-05-16 with value 300 (kg)");
        assertTrue(data.stream().anyMatch(element -> element.getXValue().equals("2023-04-04")
        && element.getYValue().equals(150)), 
        "Bench press chart should have data point for 2023-04-04 with value 150");
    }

    @Test
    public void testReturn() {
        clickOn("Return");
        checkOnScene("LØ", "FT");
    }

    @Test
    public void testExerciseJournal() {
        clickOn("Journal");
        checkOnScene("Exercise Journal");
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

    private void checkOnScene(String... stringsToBePresent) {
        ObservableList<Node> rootChildren = root.getChildrenUnmodifiable();
        
        assertTrue(rootChildren.size() == 1, "There should only be one child of the root");
        assertTrue(rootChildren.get(0).getId().equals("baseAnchor"),
                "The child should be the baseAnchor");

        for (String s : stringsToBePresent) {
            assertTrue(((Parent) rootChildren.get(0))
                    .getChildrenUnmodifiable()
                    .stream()
                    .anyMatch(x -> x instanceof Text
                            && ((Text) x).getText().equals(s)),
                    "The text \"" + s + "\" should be present");
        }
    }
}
