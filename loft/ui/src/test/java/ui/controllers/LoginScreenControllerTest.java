package ui.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import core.User;
import filehandling.ReadAndWrite;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import ui.App;

/**
 * This class contains the JUnit tests for the LoginScreenController class. It
 * tests the functionality of the login screen, including logging in with wrong
 * credentials, clicking the "register new profile" button, and successfully
 * logging in with correct credentials.
 */
public class LoginScreenControllerTest extends ApplicationTest {

    private static String testFileLocation = System.getProperty("user.home")
            + System.getProperty("file.separator") + "testUserData.json";

    private Parent root;

    /**
     * Sets up the test environment to support headless mode.
     */
    @BeforeAll
    public static void setupHeadless() {
        App.supportHeadless();
    }

    @Override
    public void start(Stage stage) throws IOException {
        root = App.customStart(stage, "LoginScreen.fxml", new LoginScreenController());
    }

    @BeforeAll
    public static void cleanStart() {
        deleteTestfile();
        ReadAndWrite.setFileLocation(testFileLocation);
    }

    @AfterAll
    public static void cleanUp() {
        deleteTestfile();
    }

    @Test
    public void testLoginWrongCredentials() {
        write("test");
        write("\t");
        write("test");
        Node button = lookup("Log in").queryAll().stream()
                .filter(n -> n instanceof Button)
                .findFirst()
                .get();
        clickOn(button);
        assertTrue(((Text) lookup("#errorMessage").query()).getText().equals("Login invalid"),
                "Error message should be \"Login invalid\"");
        Node passwordField = lookup("#passwordField").query();

        ((TextField) passwordField).clear();
        clickOn(passwordField);
        write("hunter2");
        clickOn(button);
        assertTrue(((Text) lookup("#errorMessage").query()).getText().equals("Login invalid"),
                "Error message should be \"Login invalid\"");
    }

    @Test
    public void testClickRegisterNewProfile() {
        clickOn("Or register new profile");
        ObservableList<Node> rootChildren = root.getChildrenUnmodifiable();
        assertTrue(rootChildren.size() == 1, "There should only be one child of the root");
        assertTrue(rootChildren.get(0).getId().equals("baseAnchor"),
                "The child should be the baseAnchor");

        assertTrue(((Parent) rootChildren.get(0))
                .getChildrenUnmodifiable()
                .stream()
                .anyMatch(x -> x instanceof Text
                        && ((Text) x).getText().equals("Already have an account? Log in")),
                "The text \"Already have an account? Log in\" should be present");
    }

    @Test
    public void testSuccessfulLogin() {
        deleteTestfile();
        User user = new User("Test User", "tester", "hunter2", "tester@example.com");

        ReadAndWrite.registerUser(user);

        write("tester");
        write("\t");
        write("hunter2");
        Node button = lookup("Log in").queryAll().stream()
                .filter(n -> n instanceof Button)
                .findFirst()
                .get();
        clickOn(button);

        User loggedIn = SceneSwitcher.getUser();
        assertTrue(loggedIn.getName().equals(user.getName()),
                "The logged in user should be the same as the registered user");
        assertTrue(loggedIn.getPasswordHash().equals(user.getPasswordHash()),
                "The logged in user should be the same as the registered user");
        assertTrue(loggedIn.getEmail().equals(user.getEmail()),
                "The logged in user should be the same as the registered user");
        assertTrue(loggedIn.getWorkouts().equals(user.getWorkouts()),
                "The logged in user should be the same as the registered user");

        // Check that we are at the homepage by checking that the text "LØFT" is present
        // (it is split into two Text nodes)
        checkOnScene("LØ", "FT");
    }

    private void checkOnScene(String... stringsToBePresent) {
        ObservableList<Node> rootChildren = root.getChildrenUnmodifiable();
        // After logging in, we should be at the homepage, there should only be one
        // child of the root (the baseAnchor)
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
