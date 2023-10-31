package ui.controllers;

import filehandling.DirectLoftAccess;
import filehandling.LoftAccess;
import javafx.scene.Parent;
import org.junit.jupiter.api.BeforeAll;
import org.testfx.framework.junit5.ApplicationTest;
import ui.App;

/**
 * This abstract class serves as a base for all controller test classes. It
 * extends ApplicationTest and provides a static LoftAccess instance, a test
 * file location, and a Parent root. It also sets up the test environment to
 * support headless mode.
 */
public abstract class ControllerTestBase extends ApplicationTest {

    protected static LoftAccess loftAccess;
    protected static String testFileLocation;
    protected Parent root;

    // Set up the LoftAccess instance and the test file location.
    // This will only run once per test-run.
    static {
        testFileLocation = System.getProperty("user.home")
                + System.getProperty("file.separator") + "testUserData.json";
        loftAccess = new DirectLoftAccess();
        SceneSwitcher.setAccess(loftAccess);
        DirectLoftAccess.setFileLocation(testFileLocation);
    }

    /**
     * Sets up the test environment to support headless mode.
     */
    @BeforeAll
    public static void setupHeadless() {
        App.supportHeadless();
    }

}
