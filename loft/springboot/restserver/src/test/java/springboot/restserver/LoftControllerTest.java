package springboot.restserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.User;
import core.Workout;
import filehandling.DirectLoftAccess;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * This class contains the unit tests for the LoftController class. It tests
 * the functionality of the REST API endpoints provided by the LoftController
 * class. The tests are performed using the TestRestTemplate class to simulate
 * HTTP requests and responses. The tests cover the following functionality:
 * - Checking if the Loft application is online
 * - Checking if a user exists
 * - Retrieving a user
 * - Registering a user
 * - Saving a workout
 * - Updating a user
 */
@SpringBootTest(classes = { LoftApplication.class, LoftController.class },
        webEnvironment = WebEnvironment.DEFINED_PORT)
public class LoftControllerTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate rest = new TestRestTemplate();

    private static String testFolderLocation = System.getProperty("user.home")
            + System.getProperty("file.separator");
    private static String testFileLocation = testFolderLocation + "testUserData.json";
    private DirectLoftAccess directAccess = new DirectLoftAccess();

    @BeforeAll
    public static void setUp() {
        deleteTestfile();
        DirectLoftAccess.setFileLocation(testFileLocation);
    }

    @AfterEach
    public void cleanUp() {
        deleteTestfile();
    }

    @Test
    public void testAlive() {
        assertTrue(rest.getForObject(getUrl(), Boolean.class), "Loft should be online");
    }

    @Test
    public void testUserNameExists() {
        assertFalse(
                rest.getForObject(getUrl() + "/check-username/foobar", Boolean.class),
                "User foobar should not exist");
        User user = new User("Foo Bar", "foobar", "foobar", "foo@bar.baz");
        directAccess.registerUser(user);
        assertTrue(
                rest.getForObject(getUrl() + "/check-username/foobar", Boolean.class),
                "User foobar should exist");
    }

    @Test
    public void testGetUser() {
        assertNull(rest.getForObject(getUrl() + "/users/foobar?password=foobar", User.class),
                "User foobar should not exist");

        User user = new User("Foo Bar", "foobar", "foobar", "foo@bar.baz");
        directAccess.registerUser(user);

        checkMissingGetParam("/users/foobar");

        User returnedUser = rest.getForObject(getUrl() + "/users/foobar?password=foobar",
                User.class);

        assertTrue(user.equals(returnedUser), "User foobar should be returned when authenticated");
    }

    private void checkMissingGetParam(String endpoint) {
        ResponseEntity<String> result = rest.getForEntity(getUrl() + endpoint, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode(),
                "Should return 400 when missing required parameter(s)");
    }

    @Test
    public void testRegisterUser() {
        assertFalse(directAccess.usernameExists("foobar"));

        checkWontRegister("password=foobar&email=foo@bar.baz", "foobar");
        checkWontRegister("name=Foo&email=foo@bar.baz", "foobar");
        checkWontRegister("name=Foo&password=foobar", "foobar");

        rest.postForObject(getUrl()
                + "/users/foobar/register?name=Foo&password=foobar&email=foo@bar.baz",
                null, String.class);
        assertTrue(directAccess.usernameExists("foobar"));
    }

    private void checkWontRegister(String params, String username) {
        rest.postForObject(getUrl() + "/users/" + username + "register?" + params,
                null, String.class);
        assertFalse(directAccess.usernameExists(username),
                "Should not register user with missing parameter(s)");
    }

    @Test
    public void testSaveWorkout() {
        User user = new User("Foo Bar", "foobar", "foobar", "foo@bar.baz");
        directAccess.registerUser(user);

        String params = "name=Foo Bar&password=foobar";
        Map<String, String> emptyMap = new HashMap<String, String>();

        rest.put(getUrl() + "users/foobar/workouts?" + params, new Workout(), emptyMap);
        assertEquals(0, directAccess.getUser(user.getUsername(), user.getPassword())
                .getWorkouts().size(), "Should not save workout without all params");

        params += "&email=foo@bar.baz";

        rest.put(getUrl() + "users/foobar/workouts?" + params, null, emptyMap);
        assertEquals(0, directAccess.getUser(user.getUsername(), user.getPassword())
                .getWorkouts().size(), "Should not save workout without body");

        System.out.println(params);
        rest.put(getUrl() + "users/foobar/workouts?" + params, new Workout(), emptyMap);
        assertEquals(1, directAccess.getUser(user.getUsername(), user.getPassword())
                .getWorkouts().size(), "Should save workout with body");
    }

    @Test
    public void testUpdateUser() {
        User user = new User("Foo Bar", "foobar", "foobar", "foo@bar.baz");
        directAccess.registerUser(user);
        String userParams = "name=" + urlEncode(user.getName())
                + "&password=" + urlEncode(user.getPassword())
                + "&email=" + urlEncode(user.getEmail());

        User user2 = new User("Bar Baz", "barbaz", "barbaz", "bar@baz.foo");

        String user2Params = "newUsername="
                + urlEncode(user2.getUsername())
                + "&newName=" + urlEncode(user2.getName())
                + "&newPassword=" + urlEncode(user2.getPassword());

        rest.put(getUrl() + "users/foobar?" + userParams + "&" + user2Params,
                null, new HashMap<String, String>());
        assertNotNull(directAccess.getUser(user.getUsername(), user.getPassword()),
                "Should not update user with missing parameter(s)");
        assertNull(directAccess.getUser(user2.getUsername(), user2.getPassword()),
                "Should not update user with missing parameter(s)");

        user2Params += "&newEmail=" + urlEncode(user2.getEmail());

        rest.put(getUrl() + "users/foobar?" + userParams + "&" + user2Params,
                null, new HashMap<String, String>());
        assertEquals(user2, directAccess.getUser(user2.getUsername(), user2.getPassword()),
                "Should update user with all parameters");
    }

    private String urlEncode(String string) {
        return URLEncoder.encode(string, StandardCharsets.UTF_8);
    }

    private String getUrl() {
        return "http://localhost:" + port + "/loft/";
    }

    /**
     * Deletes the test file if it exists.
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
