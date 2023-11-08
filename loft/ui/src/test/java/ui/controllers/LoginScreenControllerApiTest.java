package ui.controllers;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import filehandling.DirectLoftAccess;
import filehandling.RemoteLoftAccess;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import ui.App;

/**
 * This class contains the unit tests for the LoginScreenController class.
 * It extends the ControllerTestBase class and uses WireMock to mock the server.
 */
public class LoginScreenControllerApiTest extends ControllerTestBase {

    private WireMockConfiguration config;
    private WireMockServer wireMockServer;
    private int port;

    @Override
    public void start(Stage stage) throws IOException {
        root = App.customStart(stage, "LoginScreen.fxml", new LoginScreenController());
    }

    @BeforeAll
    public static void cleanStart() {
        deleteTestfile();
    }

    /**
     * Deletes the test file and sets up a new DirectLoftAccess instance for the
     * SceneSwitcher.
     */
    @AfterAll
    public static void cleanUp() {
        deleteTestfile();
        loftAccess = new DirectLoftAccess();
        SceneSwitcher.setAccess(loftAccess);
    }

    /**
     * Starts the WireMock server and sets up the necessary configurations before
     * each test.
     *
     * @throws URISyntaxException if the URI syntax is invalid, which should not
     *                            happen
     */
    @BeforeEach
    public void startWireMockServerAndSetup() throws URISyntaxException {
        config = WireMockConfiguration.wireMockConfig().port(8080);
        wireMockServer = new WireMockServer(config.portNumber());
        wireMockServer.start();
        WireMock.configureFor("localhost", config.portNumber());
        port = wireMockServer.port();
        loftAccess = new RemoteLoftAccess(new URI("http://localhost:" + port + "/loft/"));
    }

    @AfterEach
    public void stopWireMockServer() {
        wireMockServer.stop();
    }

    @Test
    public void testConnectNotOnline() {
        mockServerOffline();

        clickOn("#remoteButton");
        clickOn("#connectButton");
        assertEquals("Server not online", lookup("#connectionInfo").queryAs(Text.class).getText());
        assertEquals("Using local data", lookup("#usingText").queryAs(Text.class).getText());
    }

    @Test
    public void testConnect() {
        mockServerOnline();

        clickOn("#remoteButton");
        clickOn("#connectButton");
        assertEquals("Connected", lookup("#connectionInfo").queryAs(Text.class).getText());
        assertEquals("Using REST API", lookup("#usingText").queryAs(Text.class).getText());

        clickOn("#directButton");
        assertEquals("Using local data", lookup("#usingText").queryAs(Text.class).getText());
        assertEquals("Waiting to connect", lookup("#connectionInfo").queryAs(Text.class).getText());
    }

    @Test
    public void testConnectInvalidUrl() {
        mockServerOnline();

        clickOn("#remoteButton");
        clickOn("#urlField");
        FxRobot r = new FxRobot();
        r.press(KeyCode.END);
        r.release(KeyCode.END);
        r.press(KeyCode.BACK_SPACE);
        r.release(KeyCode.BACK_SPACE);
        clickOn("#connectButton");
        assertEquals("Invalid URL", lookup("#connectionInfo").queryAs(Text.class).getText());
        assertEquals("Using local data", lookup("#usingText").queryAs(Text.class).getText());

        clickOn("#remoteButton");
        clickOn("#urlField");
        clear();
        clickOn("#connectButton");
        assertEquals("Invalid URL", lookup("#connectionInfo").queryAs(Text.class).getText());
        assertEquals("Using local data", lookup("#usingText").queryAs(Text.class).getText());
    }

    @Test
    public void testStillRemote() {
        mockServerOnline();

        clickOn("#remoteButton");
        clickOn("#connectButton");
        assertEquals("Connected", lookup("#connectionInfo").queryAs(Text.class).getText());
        assertEquals("Using REST API", lookup("#usingText").queryAs(Text.class).getText());

        clickOn("Or register new profile");
        clickOn("Already have an account? Log in");
        assertEquals("Connected", lookup("#connectionInfo").queryAs(Text.class).getText());
        assertEquals("Using REST API", lookup("#usingText").queryAs(Text.class).getText());
    }

    @Test
    public void testStillLocal() {
        mockServerOffline();

        clickOn("Or register new profile");
        clickOn("Already have an account? Log in");
        assertEquals("Using local data", lookup("#usingText").queryAs(Text.class).getText());
    }

    /**
     * Clears the text field.
     */
    private void clear() {
        FxRobot r = new FxRobot();
        r.press(KeyCode.CONTROL);
        r.press(KeyCode.A);
        r.release(KeyCode.A);
        r.release(KeyCode.CONTROL);
        r.press(KeyCode.DELETE);
        r.release(KeyCode.DELETE);
    }

    /**
     * Mocks the server to be offline.
     */
    private void mockServerOffline() {
        stubFor(get(urlEqualTo("/loft/"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("false")));
    }

    /**
     * Mocks the server to be online.
     */
    private void mockServerOnline() {
        stubFor(get(urlEqualTo("/loft/"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")));
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
