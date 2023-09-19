package ui.controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import core.Exercise;
import core.Set;
import core.Workout;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.App;

public class WorkoutScreenControllerTest extends ApplicationTest {

    private static String testFileLocation = System.getProperty("user.home") + System.getProperty("file.separator")
            + "testUserData.json";
    private WorkoutScreenController controller;

    private Parent root;

    @Override
    public void start(Stage stage) throws IOException {
        SceneSwitcher.testFileLocation = testFileLocation;

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("WorkoutScreen.fxml"));
        controller = new WorkoutScreenController(testFileLocation);
        fxmlLoader.setController(controller);
        root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
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
            createTestExercise(wantedSetsCount, "Test Exercise " + i);
        }

        Workout workout = controller.getWorkout();
        assertTrue(workout.getExercises().size() == 2);

        for (int i = 0; i < 2; i++) {
            Exercise exercise = workout.getExercises().get(i);
            List<Set> sets = exercise.getSets();
            assertTrue(sets.size() == wantedSetsCount);
            assertTrue(exercise.getName().equals("Test Exercise " + (i + 1)));
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
        assertDoesNotThrow(() -> clickLabels("Test Exercise"),
                "Clicking \"Test Exercise\" should not throw an exception, since it is supposed to be saved in the dropdown menu");
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

        // Check that we are at the homepage by checking that the text "LØFT" is present (it is split into two Text nodes)
        assertTrue(((Parent) rootChildren.get(0)).getChildrenUnmodifiable().stream()
                .anyMatch(x -> x instanceof Text && ((Text) x).getText().equals("LØ")));
        assertTrue(((Parent) rootChildren.get(0)).getChildrenUnmodifiable().stream()
                .anyMatch(x -> x instanceof Text && ((Text) x).getText().equals("FT")));
    }

    private void deleteTestfile() {
        try {
            Files.delete(Path.of(testFileLocation));
        } catch (IOException e) {
            System.out.println("Error deleting file");
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
        createTestExerciseFirstHalf(numberOfSets, "Test Exercise");
    }

    private void createTestExerciseFirstHalf(int numberOfSets, String name) {
        clickIds("#searchBar");
        write(name);
        clickLabels("Add as new", "Edit");
        clickIds("#setField");
        write("" + numberOfSets);
    }

    private void createTestExerciseSecondHalf(ObservableList<Node> gridChildren, int numberOfSets) {
        for (int i = 0; i < numberOfSets * 3; i += 3) {
            clickOn(gridChildren.get(i + 1));
            write("" + (i + 1));
            clickOn(gridChildren.get(i + 2));
            write("" + (i + 2));
        }
        clickIds("#addButton");
    }

    private void createTestExercise(int wantedSetCount) {
        createTestExercise(wantedSetCount, wantedSetCount, "Test Exercise");
    }

    private void createTestExercise(int wantedSetCount, String name) {
        createTestExercise(wantedSetCount, wantedSetCount, name);
    }

    private void createTestExercise(int numberOfSets, int expectedNumberOfSets, String name) {
        createTestExerciseFirstHalf(numberOfSets, name);
        GridPane grid = lookup("#grid").query();

        ObservableList<Node> gridChildren = grid.getChildren();
        assertTrue(gridChildren.size() == 3 * expectedNumberOfSets,
                "Grid should have " + 3 * expectedNumberOfSets + " children (cells), but has " + gridChildren.size());
        createTestExerciseSecondHalf(gridChildren, numberOfSets);
    }
}
