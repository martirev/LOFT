package ui.controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import java.util.List;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import ui.App;

/**
 * This class contains tests for the WorkoutScreenController class.
 */
public class WorkoutScreenControllerTest extends ApplicationTest {

    private static String testFileLocation = System.getProperty("user.home")
            + System.getProperty("file.separator") + "testUserData.json";
    private WorkoutScreenController controller;
    private static User user;

    private Parent root;

    /**
     * Sets up the test environment by deleting an existing test file and setting
     * the file location to the test file location. Also creates a test user and
     * sets it as the current user.
     */
    @BeforeAll
    public static void setUp() {
        deleteTestfile();
        ReadAndWrite.setFileLocation(testFileLocation);
        user = new User("Test person", "tester", "hunter2", "tester@test.com");
        SceneSwitcher.setUser(user);
    }

    @AfterEach
    public void cleanUp() {
        deleteTestfile();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("WorkoutScreen.fxml"));
        controller = new WorkoutScreenController();
        fxmlLoader.setController(controller);
        root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void testFormatter() {
        createTestExerciseFirstHalf(1);
        clickOn("#setField");
        write("1a2b3c");
        TextField setField = lookup("#setField").query();
        assertTrue("123".equals(setField.getText()));
    }

    @Test
    public void testEmptyAddAsNew() {
        clickLabels("Add As New");
        assertTrue(lookup("#editButton").query().isDisabled(),
                "The edit button should be disabled when the search bar is empty");
    }

    @Test
    public void testEmptyWorkoutFinish() {
        clickLabels("Finish workout");
        assertTrue(root
                .getChildrenUnmodifiable()
                .stream()
                .anyMatch(x -> x instanceof Text && ((Text) x).getText()
                        .equals("Current workout")));
    }

    @Test
    public void testAddNewFieldTypes() {
        final int wantedSetsCount = 200;
        final int expectedSetsCount = 18;
        createTestExerciseFirstHalf(wantedSetsCount);
        GridPane grid = lookup("#grid").query();

        ObservableList<Node> gridChildren = grid.getChildren();
        assertTrue(gridChildren.size() == expectedSetsCount * 3);
        for (int i = 0; i < expectedSetsCount * 3; i += 3) {
            assertTrue(gridChildren.get(i) instanceof Text);
            assertTrue(gridChildren.get(i + 1) instanceof TextField);
            assertTrue(gridChildren.get(i + 2) instanceof TextField);
        }
    }

    @Test
    public void testAddNew() {
        final int wantedSetsCount = 4;
        createTestExercise(wantedSetsCount);

        Workout workout = controller.getWorkout();
        assertTrue(workout.getExercises().size() == 1);
        List<Set> sets = workout.getExercises().get(0).getSets();
        assertTrue(sets.size() == wantedSetsCount);
        for (int i = 0; i < wantedSetsCount * 3; i += 3) {
            assertTrue(sets.get(i / 3).getWeight() == i + 1);
            assertTrue(sets.get(i / 3).getReps() == i + 2);
        }
    }

    @Test
    public void testAddMultipleExercises() {
        final int wantedSetsCount = 2;
        for (int i = 1; i < 3; i++) {
            createTestExercise(wantedSetsCount, "Test " + i);
        }

        Workout workout = controller.getWorkout();
        assertTrue(workout.getExercises().size() == 2);

        for (int i = 0; i < 2; i++) {
            Exercise exercise = workout.getExercises().get(i);
            List<Set> sets = exercise.getSets();
            assertTrue(sets.size() == wantedSetsCount);
            assertTrue(exercise.getName().equals("Test " + (i + 1)));
            for (int j = 0; j < wantedSetsCount * 3; j += 3) {
                assertTrue(sets.get(j / 3).getWeight() == j + 1);
                assertTrue(sets.get(j / 3).getReps() == j + 2);
            }
        }
    }

    @Test
    public void testStoredExercise() {
        createTestExercise(2);
        clickLabels("Finish workout");
        clickIds("#newWorkoutRectangle");
        assertDoesNotThrow(() -> clickLabels("Test"),
                "Clicking \"Test\" should not throw an exception, "
                        + "since it is supposed to be saved in the dropdown menu");
        assertDoesNotThrow(() -> clickOn("#editButton"),
                "Clicking \"Edit\" should not throw an exception, "
                        + "since \"Test\" is supposed to be saved in the dropdown menu");
        deleteTestfile();
    }

    @Test
    public void testReturn() {
        clickLabels("Return");
        ObservableList<Node> rootChildren = root.getChildrenUnmodifiable();

        // After returning to the homepage, there should only be one child of the root
        // (the baseAnchor)
        assertTrue(rootChildren.size() == 1);
        assertTrue(rootChildren.get(0).getId().equals("baseAnchor"));

        // Check that we are at the homepage by checking that the text "LØFT" is present
        // (it is split into two Text nodes)
        assertTrue(((Parent) rootChildren.get(0)).getChildrenUnmodifiable().stream()
                .anyMatch(x -> x instanceof Text && ((Text) x).getText().equals("LØ")));
        assertTrue(((Parent) rootChildren.get(0)).getChildrenUnmodifiable().stream()
                .anyMatch(x -> x instanceof Text && ((Text) x).getText().equals("FT")));
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

    private void clickIds(String... ids) {
        for (String id : ids) {
            clickOn(id);
        }
    }

    private void clickLabels(String... labels) {
        for (String label : labels) {
            clickOn(LabeledMatchers.hasText(label));
        }
    }

    private void createTestExerciseFirstHalf(int numberOfSets) {
        createTestExerciseFirstHalf(numberOfSets, "Test");
    }

    private void createTestExerciseFirstHalf(int numberOfSets, String name) {
        Button button = lookup("#editButton").query();
        button.fire();

        assertFalse(lookup("#exerciseSettings").query().isVisible(),
                "Exercise settings should not be visible");

        clickIds("#searchBar");
        write(name);
        clickLabels("Add As New", "Edit");
        clickIds("#setField");
        write("" + numberOfSets);
    }

    private void createTestExerciseSecondHalf(ObservableList<Node> gridChildren, int numberOfSets) {
        ListView<Workout> view = lookup("#workoutListView").query();
        int size = view.getItems().size();
        Button button = lookup("#addButton").query();
        button.fire();
        assertEquals(size, view.getItems().size(), "The workout listview should not change size");

        for (int i = 0; i < numberOfSets * 3; i += 3) {
            write("\t");
            write("" + (i + 1));
            button.fire();
            assertEquals(size, view.getItems().size(),
                    "The workout listview should not change size");
            write("\t");
            write("" + (i + 2));
        }
        clickIds("#addButton");
    }

    private void createTestExercise(int wantedSetCount) {
        createTestExercise(wantedSetCount, wantedSetCount, "Test");
    }

    private void createTestExercise(int wantedSetCount, String name) {
        createTestExercise(wantedSetCount, wantedSetCount, name);
    }

    private void createTestExercise(int numberOfSets, int expectedNumberOfSets, String name) {
        createTestExerciseFirstHalf(numberOfSets, name);
        GridPane grid = lookup("#grid").query();

        ObservableList<Node> gridChildren = grid.getChildren();
        assertTrue(gridChildren.size() == 3 * expectedNumberOfSets,
                "Grid should have "
                        + 3 * expectedNumberOfSets
                        + " children (cells), but has "
                        + gridChildren.size());
        createTestExerciseSecondHalf(gridChildren, numberOfSets);
    }
}
