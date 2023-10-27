package ui.controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
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
 * This class contains the JUnit tests for the JournalScreenController class. It
 * tests the functionality of the JournalScreenController class by setting up
 * test data and verifying that the expected results are produced.
 */
public class JournalScreenControllerTest extends ApplicationTest {

    private static final String testFileLocation = System.getProperty("user.home")
            + System.getProperty("file.separator") + "testUserData.json";

    private Parent root;

    private static Workout workout1;
    private static Workout workout2;
    private static User user;

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

        root = App.customStart(stage, "JournalScreen.fxml", new JournalScreenController());
    }

    @AfterAll
    public static void tearDown() {
        deleteTestfile();
    }

    /**
     * Sets up the test data before running the tests.
     */
    @BeforeAll
    public static void setUp() {
        ReadAndWrite.setFileLocation(testFileLocation);
        deleteTestfile();
        user = new User("Test person", "tester", "hunter2", "tester@example.com");

        workout1 = new Workout(LocalDate.of(2023, 10, 1));
        workout2 = new Workout(LocalDate.of(2019, 1, 1));
        final Exercise exercise1 = new Exercise("Bench Press");
        final Exercise exercise2 = new Exercise("Squats");

        Set benchSet1 = new Set(10, 150);
        Set benchSet2 = new Set(8, 130);
        Set benchSet3 = new Set(6, 110);
        Set benchSet4 = new Set(4, 90);
        exercise1.addSet(benchSet1);
        exercise1.addSet(benchSet2);
        exercise1.addSet(benchSet3);
        exercise1.addSet(benchSet4);

        Set squatSet1 = new Set(10, 200);
        Set squatSet2 = new Set(8, 180);
        Set squatSet3 = new Set(6, 160);
        Set squatSet4 = new Set(4, 140);
        exercise2.addSet(squatSet1);
        exercise2.addSet(squatSet2);
        exercise2.addSet(squatSet3);
        exercise2.addSet(squatSet4);

        workout1.addExercise(exercise1);
        workout1.addExercise(exercise2);

        final Exercise exercise3 = new Exercise("Shoulder Press");
        final Exercise exercise4 = new Exercise("Lateral Raises");

        Set shoulderPress1 = new Set(4, 130);
        Set shoulderPress2 = new Set(4, 120);
        exercise3.addSet(shoulderPress1);
        exercise3.addSet(shoulderPress2);

        Set lateralRaises1 = new Set(5, 170);
        Set lateralRaises2 = new Set(5, 180);
        exercise4.addSet(lateralRaises1);
        exercise4.addSet(lateralRaises2);

        workout2.addExercise(exercise3);
        workout2.addExercise(exercise4);

        ReadAndWrite.writeWorkoutToUser(workout1, user);
        ReadAndWrite.writeWorkoutToUser(workout2, user);
    }

    @Test
    public void testJournalLength() {
        ListView<Workout> listView = lookup("#workoutHistoryListView").query();
        assertEquals(2, listView.getItems().size());
    }

    @Test
    public void testJournalOrder() {
        ListView<Workout> listView = lookup("#workoutHistoryListView").query();
        assertTrue(listView.getItems().get(0).getDate().toString().equals("2023-10-01"));
        assertTrue(listView.getItems().get(1).getDate().toString().equals("2019-01-01"));
    }

    @Test
    public void testJournalExerciseLength() {
        clickOn(workout1.toString());
        ListView<TextArea> workoutListView = lookup("#workoutListView").query();
        assertEquals(2, workoutListView.getItems().size());
    }

    @Test
    public void testJournalExerciseOrder() {
        clickOn(workout1.toString());
        ListView<TextArea> workoutListView = lookup("#workoutListView").query();
        assertTrue("Bench Press".equals(
                workoutListView.getItems().get(0).getText().split("\n")[0]));
        assertTrue("Squats".equals(workoutListView.getItems().get(1).getText().split("\n")[0]));
        clickOn(workout2.toString());
        assertTrue("Shoulder Press".equals(
                workoutListView.getItems().get(0).getText().split("\n")[0]));
        assertTrue("Lateral Raises".equals(
                workoutListView.getItems().get(1).getText().split("\n")[0]));
    }

    @Test
    public void testLoadNonExistingWorkout() {
        ListView<TextArea> workoutListView = lookup("#workoutListView").query();
        assertEquals(0, workoutListView.getItems().size(),
                "There should be no workouts shown");

        // There is no listelement in the middle of the list, so this wont click any
        // workout
        assertDoesNotThrow(() -> clickOn("#workoutHistoryListView"),
                "Clicking an empty field should not throw an exception");

        workoutListView = lookup("#workoutListView").query();
        assertEquals(0, workoutListView.getItems().size(),
                "There should still be no workouts shown");
    }

    @Test
    public void testGoToProgress() {
        clickOn("Progress");
        checkOnScene("Progress Journal", "Total weight");
    }

    @Test
    public void testReturn() {
        clickOn("Return");
        checkOnScene("LØ", "FT");
    }

    /**
     * Checks if the given text nodes are present in the scene.
     *
     * @param textNodesToBePresent The text nodes to be checked.
     */
    private void checkOnScene(String... textNodesToBePresent) {
        ObservableList<Node> rootChildren = root.getChildrenUnmodifiable();
        assertTrue(rootChildren.size() == 1, "There should only be one child of the root");
        assertTrue(rootChildren.get(0).getId().equals("baseAnchor"),
                "The child should be the baseAnchor");

        for (String s : textNodesToBePresent) {
            assertTrue(((Parent) rootChildren.get(0))
                    .getChildrenUnmodifiable()
                    .stream()
                    .anyMatch(x -> x instanceof Text
                            && ((Text) x).getText().equals(s)),
                    "The text \"" + s + "\" should be present");
        }
    }

    private static void deleteTestfile() {
        try {
            if ((new File(testFileLocation)).exists()) {
                Files.delete(Path.of(testFileLocation));
            }
        } catch (IOException e) {
            System.err.println("Error deleting file");
        }
    }
}
