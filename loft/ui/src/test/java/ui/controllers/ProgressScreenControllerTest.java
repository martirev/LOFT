package ui.controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import core.Exercise;
import core.Set;
import core.User;
import core.Workout;
import filehandling.ReadAndWrite;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import ui.App;

/**
 * This class contains JUnit tests for the ProgressScreenController class. The
 * tests cover the functionality of the ProgressScreenController class, which is
 * responsible for displaying the progress screen of the application. The tests
 * ensure that the charts on the progress screen are populated correctly and
 * that the correct data is displayed.
 */
public class ProgressScreenControllerTest extends ApplicationTest {

    private static final String testFileLocation = System.getProperty("user.home")
            + System.getProperty("file.separator") + "testUserData.json";

    private Parent root;

    private XYChart<String, Number> totalweightChart;
    private XYChart<String, Number> exerciseChart;

    private static Exercise bench1;
    private static Exercise bench2;
    private static Exercise squat;
    private static Exercise deadlift;
    private static User user;

    private static List<LocalDate> dates = List.of(LocalDate.of(2023, 5, 16),
            LocalDate.of(2023, 4, 4));

    /**
     * Sets up the test environment to support headless mode.
     */
    @BeforeAll
    public static void setupHeadless() {
        App.supportHeadless();
    }

    @Override
    public void start(Stage stage) throws IOException {
        user = new User("Test person", "tester", "hunter2", "tester@test.com");
        SceneSwitcher.setUser(user);

        root = App.customStart(stage, "ProgressScreen.fxml", new ProgressScreenController());
    }

    @AfterAll
    public static void teardown() {
        deleteTestFile();
    }

    /**
     * Sets up the test by creating a test user and writing test data to the
     * testUserData.json file. The test data consists of three workouts, two
     * of which are on the same day.
     */
    @BeforeAll
    public static void setUp() {
        ReadAndWrite.setFileLocation(testFileLocation);
        deleteTestFile();
        user = new User("Test person", "tester", "hunter2", "tester@example.com");
        SceneSwitcher.setUser(user);

        bench1 = new Exercise("Benchpress");
        squat = new Exercise("Squat");
        deadlift = new Exercise("Deadlift");
        bench2 = new Exercise("Benchpress");

        final Workout workout1 = new Workout(dates.get(0));
        final Workout workout2 = new Workout(dates.get(0));
        final Workout workout3 = new Workout(dates.get(1));

        Set bench1Set1 = new Set(10, 150);
        Set bench1Set2 = new Set(8, 130);
        Set bench1Set3 = new Set(6, 110);
        bench1.addSet(bench1Set1);
        bench1.addSet(bench1Set2);
        bench1.addSet(bench1Set3);

        Set bench2Set1 = new Set(10, 180);
        Set bench2Set2 = new Set(8, 190);
        Set bench2Set3 = new Set(6, 200);
        bench2.addSet(bench2Set1);
        bench2.addSet(bench2Set2);
        bench2.addSet(bench2Set3);

        Set squatSet1 = new Set(10, 200);
        Set squatSet2 = new Set(8, 180);
        Set squatSet3 = new Set(6, 160);
        squat.addSet(squatSet1);
        squat.addSet(squatSet2);
        squat.addSet(squatSet3);

        Set deadliftSet1 = new Set(10, 120);
        Set deadliftSet2 = new Set(8, 140);
        deadlift.addSet(deadliftSet1);
        deadlift.addSet(deadliftSet2);

        workout1.addExercise(bench1);
        workout1.addExercise(squat);
        workout1.addExercise(deadlift);

        workout2.addExercise(bench2);
        workout2.addExercise(squat);

        workout3.addExercise(bench1);
        workout3.addExercise(squat);

        ReadAndWrite.writeWorkoutToUser(workout1, user);
        ReadAndWrite.writeWorkoutToUser(workout2, user);
        ReadAndWrite.writeWorkoutToUser(workout3, user);
    }

    @Test
    public void testTotalWeightChart() {
        totalweightChart = lookup("#totalWeightChart").query();

        assertNotNull(totalweightChart.getData(),
                "The total weight chart should not be null");
        assertEquals(2, totalweightChart.getData().get(0).getData().size(),
                "There should be 2 data points");

        for (Data<String, Number> s : totalweightChart.getData().get(0).getData()) {

            if (s.getXValue().equals(dates.get(0).toString())) {
                assertEquals(18840, s.getYValue(),
                        "The total weight on the first date should be 15200");
            } else if (s.getXValue().equals(dates.get(1).toString())) {
                assertEquals(7600, s.getYValue(),
                        "The total weight on the second date should be 7600");
            } else {
                fail("The total weight chart should only contain the two dates");
            }
        }
    }

    @Test
    public void testTotalWeightListView() {
        TextArea totalWeightListView = lookup("#totalWeightListView").query();

        assertFalse(totalWeightListView.getText().isBlank(),
                "The total weight list view should not be blank");
        assertTrue(totalWeightListView.getText().contains("Weight: 26440 kg"),
                "The total weight should be 26520 kg");
        assertTrue(totalWeightListView.getText().contains("Benchpress: 200 kg"),
                "The Bench Press exercise should be 200 kg lifted");
        assertTrue(totalWeightListView.getText().contains("Squat: 200 kg"),
                "The Squats exercise should be 200 kg lifted");
        assertTrue(totalWeightListView.getText().contains("Deadlift: 140 kg"),
                "The Deadlift exercise should be 140 kg lifted");
    }

    @Test
    public void testExerciseHistoryListView() {
        ListView<Exercise> exerciseHistoryListView = lookup("#exerciseHistoryListView").query();

        assertEquals(3, exerciseHistoryListView.getItems().size(),
                "There should be 3 workouts in the list view");

        for (Exercise s : exerciseHistoryListView.getItems()) {
            assertTrue(s.getName().equals(bench1.toString())
                    || s.getName().equals(squat.toString())
                    || s.getName().equals(deadlift.toString()),
                    "The list view should only contain the three exercises");
        }
    }

    @Test
    public void testExerciseChart() {
        exerciseChart = lookup("#exerciseChart").query();
        assertTrue(exerciseChart.getData().isEmpty(), "The exercise chart should be empty");

        clickOn(bench1.toString());
        exerciseChart = lookup("#exerciseChart").query();

        assertNotNull(exerciseChart.getData(),
                "The total weight chart should not be null");
        assertEquals(2, exerciseChart.getData().get(0).getData().size(),
                "There should be 2 data points");

        for (Data<String, Number> s : exerciseChart.getData().get(0).getData()) {
            if (s.getXValue().equals(dates.get(0).toString())) {
                assertEquals(200, s.getYValue(), "The pr on the first date should be 200");
            } else if (s.getXValue().equals(dates.get(1).toString())) {
                assertEquals(150, s.getYValue(), "The pr on the second date should be 150");
            } else {
                fail("The pr chart should only contain the two dates");
            }
        }

        clickOn(squat.toString());
        exerciseChart = lookup("#exerciseChart").query();

        assertNotNull(exerciseChart.getData(), "The total weight chart should not be null");
        assertEquals(2, exerciseChart.getData().get(0).getData().size(),
                "There should be 2 data points");

        for (Data<String, Number> s : exerciseChart.getData().get(0).getData()) {
            if (s.getXValue().equals(dates.get(0).toString())) {
                assertEquals(200, s.getYValue(), "The pr on the first date should be 200");
            } else if (s.getXValue().equals(dates.get(1).toString())) {
                assertEquals(200, s.getYValue(), "The pr on the second date should be 200");
            } else {
                fail("The pr chart should only contain the two dates");
            }
        }

        clickOn(deadlift.toString());
        exerciseChart = lookup("#exerciseChart").query();

        assertNotNull(exerciseChart.getData(),
                "The total weight chart should not be null");
        assertEquals(1, exerciseChart.getData().get(0).getData().size(),
                "There should be 1 data point");

        for (Data<String, Number> s : exerciseChart.getData().get(0).getData()) {
            if (s.getXValue().equals(dates.get(0).toString())) {
                assertEquals(140, s.getYValue(),
                        "The pr on the first date should be 140");
            } else {
                fail("The pr chart should only contain the one date");
            }
        }
    }

    @Test
    public void testExerciseListView() {
        TextArea totalWeightListView = lookup("#exerciseListView").query();
        assertTrue(totalWeightListView.getText().isBlank(),
                "The exercise list view should be blank");

        clickOn(bench1.toString());
        totalWeightListView = lookup("#exerciseListView").query();

        assertFalse(totalWeightListView.getText().isBlank(),
                "The exercise list view should not be blank");
        assertTrue(totalWeightListView.getText().contains("PR: 200 kg"),
                "The pr weight should be 200 kg");

        clickOn(squat.toString());
        totalWeightListView = lookup("#exerciseListView").query();

        assertFalse(totalWeightListView.getText().isBlank(),
                "The exercise list view should not be blank");
        assertTrue(totalWeightListView.getText().contains("PR: 200 kg"),
                "The pr weight should be 200 kg");

        clickOn(deadlift.toString());
        totalWeightListView = lookup("#exerciseListView").query();

        assertFalse(totalWeightListView.getText().isBlank(),
                "The exercise list view should not be blank");
        assertTrue(totalWeightListView.getText().contains("PR: 140 kg"),
                "The pr weight should be 140 kg");
    }

    @Test
    public void testNoExerciseSelect() {
        assertDoesNotThrow(() -> clickOn("#exerciseHistoryListView"),
                "Clicking on an empty part of the listview should not throw an exception");
    }

    @Test
    public void testReturn() {
        clickOn("Return");
        checkOnScene("LØ", "FT");
    }

    @Test
    public void testExerciseJournal() {
        clickOn("Journal");
        checkOnScene("Workout Journal");
    }

    /**
     * Deletes the test file used in the unit tests, if it exists.
     */
    public static void deleteTestFile() {
        try {
            if (new File(testFileLocation).exists()) {
                Files.delete(Path.of(testFileLocation));
            }
        } catch (IOException e) {
            System.err.println("Error deleting test file");
        }
    }

    /**
     * Checks if the given strings are present in the scene.
     *
     * @param stringsToBePresent the strings to be checked for presence in the scene
     */
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
