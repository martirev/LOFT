package ui.controllers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ui.App;

/**
 * This class contains the unit tests for the HomeScreenController class. It
 * extends the ApplicationTest class to test the JavaFX components. The tests
 * include checking if the user can navigate to the register workout screen, the
 * journal screen, and if an exception is thrown when trying to navigate to a
 * non-existing screen.
 */
public class HomeScreenControllerTest extends ControllerTestBase {

    private HomeScreenController controller;

    @Override
    public void start(Stage stage) throws IOException {
        controller = new HomeScreenController();
        root = App.customStart(stage, "HomeScreen.fxml", controller);
    }

    /**
     * Sets up the necessary environment for testing by deleting the test file,
     * registering a test user, and setting the user for the scene switcher.
     */
    @BeforeAll
    public static void cleanStart() {
        deleteTestfile();
        User user = new User("Test user", "username", "password", "mail@email.com");
        loftAccess.registerUser(user);
        SceneSwitcher.setUser(user);
    }

    @AfterAll
    public static void cleanUp() {
        deleteTestfile();
    }

    @Test
    public void testGoToRegisterWorkout() {
        clickOn("New workout");
        checkOnScene("New Workout", "Current workout");
    }

    @Test
    public void testGoToJournal() {
        clickOn("Journal");
        checkOnScene("Workout Journal", "Workout History", "Workout Details");
    }

    @Test
    public void testGoToHighscore() {
        clickOn("Highscore");
        checkOnScene("Highscores", "Exercises", "Stats");
    }

    @Test
    public void testInvalidScreen() {
        assertThrows(IllegalArgumentException.class,
                () -> controller.insertPane("nonExistingScreen.fxml"),
                "Should throw IllegalArgumentException when screen doesn't exist");
    }

    /**
     * Checks if the given text nodes are present in the scene.
     *
     * @param textNodesToBePresent The text nodes to be checked for presence.
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

    /**
     * Deletes the test file located at the specified file path, if it exists.
     */
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
