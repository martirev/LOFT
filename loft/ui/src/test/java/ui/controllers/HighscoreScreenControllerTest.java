package ui.controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.Exercise;
import core.Set;
import core.User;
import core.Workout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.util.WaitForAsyncUtils;
import ui.App;

/**
 * Test class for the HighscoreScreenController.
 */
public class HighscoreScreenControllerTest extends ControllerTestBase {

    private static Workout workout1;
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

        root = App.customStart(stage, "HighscoreScreen.fxml", new HighscoreScreenController());
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
        deleteTestfile();
        user = new User("Test person", "tester", "hunter2", "tester@example.com");

        workout1 = new Workout(LocalDate.of(2023, 10, 1));
        final Exercise exercise1 = new Exercise("beNch prEss");
        final Exercise exercise2 = new Exercise("Squat");
        final Exercise exercise3 = new Exercise("sQUat");

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

        Set squarSet5 = new Set(5, 100);
        Set squaSet6 = new Set(5, 100);
        exercise3.addSet(squarSet5);
        exercise3.addSet(squaSet6);

        workout1.addExercise(exercise1);
        workout1.addExercise(exercise2);
        workout1.addExercise(exercise3);

        loftAccess.writeWorkoutToUser(workout1, user);
    }

    @Test
    public void testPrForBechPress() {
        clickOn("Bench Press");
        assertTrue(checkTitle("Bench Press"));
        assertTrue(checkBody("\n\tPersonal record:\n\t" + 150 + " kg\n"));
        assertTrue(checkBody("\n\tHighest weight in a set:\n\t" + 1500 + " kg\n"));
        assertTrue(checkBody("\n\tDays since last exercise:\n\t"
                + (int) ChronoUnit.DAYS.between(workout1.getDate(), LocalDate.now()) + "\n"));
        assertTrue(checkBody("\n\tTotal reps ever:\n\t" + 28 + "\n"));
        assertTrue(checkBody("\n\tTotal weight ever:\n\t" + 3560 + " kg"));
    }

    @Test
    public void testReturn() {
        clickOn("Return");
        checkOnScene("LØ", "FT");
    }

    @Test
    public void testLengthOfExerciseList() {
        ListView<Exercise> listView = lookup("#exerciseListView").query();
        assertEquals(2, listView.getItems().size());
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

    @Test
    public void testEmptyListView() {
        Platform.runLater(() -> {
            ListView<Exercise> listView = lookup("#exerciseListView").query();
            listView.getItems().clear();
        });
        WaitForAsyncUtils.waitForFxEvents();
        assertDoesNotThrow(() -> clickOn("#exerciseListView"));
    }

    private boolean checkTitle(String expectedText) {
        Text textNode = lookup("#header").query();
        return textNode != null && textNode.getText() != null
                && textNode.getText().contains(expectedText);
    }

    private boolean checkBody(String expectedText) {
        Text textNode = lookup("#body").query();
        return textNode != null && textNode.getText() != null
                && textNode.getText().contains(expectedText);
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
