package ui.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import core.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ui.App;

/**
 * This class contains the JUnit tests for the RegisterScreenController class.
 * It tests the functionality of the registration process, including error
 * messages for invalid input and successful registration.
 */
public class RegisterScreenControllerTest extends ControllerTestBase {

    private Collection<String> fields = Arrays.asList("#name", "#username",
            "#password1", "#password2", "#email");

    @Override
    public void start(Stage stage) throws IOException {
        root = App.customStart(stage, "RegisterScreen.fxml", new RegisterScreenController());
    }

    @BeforeAll
    public static void setUp() {
        deleteTestfile();
    }

    @AfterAll
    public static void cleanUp() {
        deleteTestfile();
    }

    @Test
    public void testNotAllFieldsFilledOut() {
        for (String field : fields) {
            fillFields("test");
            lookup(field).queryTextInputControl().setText("");
            clickRegister();
            Text errorMessage = lookup("#errorMessage").query();
            assertTrue(errorMessage.getText().equals("Please fill out all fields"));
        }
    }

    private void fillFields(String text) {
        for (String field : fields) {
            lookup(field).queryTextInputControl().setText(text);
        }
    }

    @Test
    public void testPasswordsNotMatching() {
        writeSeparator("\t", "T", "test", "test1", "test2", "t@t.no");
        clickRegister();
        Text errorMessage = lookup("#errorMessage").query();
        assertTrue(errorMessage.getText().equals("Passwords do not match"));
    }

    @Test
    public void testPasswordTooShort() {
        writeSeparator("\t", "T", "test", "t", "t", "t@t.no");
        clickRegister();
        Text errorMessage = lookup("#errorMessage").query();
        assertTrue(errorMessage.getText().equals("Password must be at least 4 characters"));
    }

    @Test
    public void testUsernameTooShort() {
        writeSeparator("\t", "T", "t", "test", "test", "t@t.no");
        clickRegister();
        Text errorMessage = lookup("#errorMessage").query();
        assertTrue(errorMessage.getText().equals("Username must be at least 4 characters"));
    }

    @Test
    public void testPasswordIllegal() {
        writeSeparator("\t", "T", "test", "?æøå", "?æøå", "t@t.no");
        clickRegister();
        Text errorMessage = lookup("#errorMessage").query();
        assertTrue(errorMessage.getText()
                .equals("Pasword can only contain letters,"
                        + " numbers, and the symbols _, @, # and !"));
    }

    @Test
    public void testIllegalEmail1() {
        writeSeparator("\t", "test", "test", "test", "test", "test");
        clickRegister();
        Text errorMessage = lookup("#errorMessage").query();
        assertTrue(errorMessage.getText().equals("Please enter a valid email"));
    }

    @Test
    public void testIllegalEmail2() {
        writeSeparator("\t", "test", "test", "test", "test", "te@st");
        clickRegister();
        Text errorMessage = lookup("#errorMessage").query();
        assertTrue(errorMessage.getText().equals("Please enter a valid email"));
    }

    @Test
    public void testUsernameTaken() {
        User user = new User("Test User", "test", "test", "t@t.no");
        loftAccess.registerUser(user);

        writeSeparator("\t", "Another user", "test", "1234", "1234", "te@st.no");
        clickRegister();
        Text errorMessage = lookup("#errorMessage").query();
        assertTrue(errorMessage.getText().equals("Username is already taken"));
        deleteTestfile();
    }

    @Test
    public void testSuccessfulRegistration() {
        writeSeparator("\t", "Test User", "test", "test", "test", "t@t.no");
        clickRegister();
        checkOnScene("Or register new profile");
        deleteTestfile();
    }

    @Test
    public void testClickLogin() {
        clickOn("Already have an account? Log in");
        checkOnScene("Or register new profile");
    }

    /**
     * Checks if the given strings are present in the current scene.
     * After registering, we should be at the log in screen, and there should only
     * be one child of the root (the baseAnchor).
     *
     * @param stringsToBePresent the strings to check for in the scene
     */
    private void checkOnScene(String... stringsToBePresent) {
        ObservableList<Node> rootChildren = root.getChildrenUnmodifiable();
        // After registering, we should be at the log in screen, and there should only
        // be one child of the root (the baseAnchor)
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

    private void clickRegister() {
        clickOn(lookup("Register").queryAll().stream()
                .filter(n -> n instanceof Button)
                .findFirst()
                .get());
    }

    private void writeSeparator(String separator, String... strings) {
        for (int i = 0; i < strings.length; i++) {
            write(strings[i]);
            if (i < strings.length - 1) {
                write(separator);
            }
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
