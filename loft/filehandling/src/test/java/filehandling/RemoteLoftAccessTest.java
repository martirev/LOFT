package filehandling;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.google.gson.Gson;
import core.User;
import core.Workout;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class contains unit tests for the RemoteLoftAccess class. It tests the
 * functionality of the RemoteLoftAccess class methods such as formUrlEncode,
 * getUriWithParams, constructors, and all http request methods.
 */
public class RemoteLoftAccessTest {

    private WireMockConfiguration config;
    private WireMockServer wireMockServer;

    private LoftAccess loftAccess;
    private int port;

    private static User user;

    @BeforeAll
    public static void setUp() {
        user = new User("User name", "username", "password", "email@domain.com");
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
    public void testGetUrl() {
        assertEquals("http://localhost:8080/loft/", ((RemoteLoftAccess) loftAccess).getUrl()
                .toString(), "getUrl should return the correct URL");
        assertEquals("", new RemoteLoftAccess(null).getUrl(),
                "getUrl should return an empty string if the URL is null");
    }

    @Test
    public void testformUrlEncode() {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", user.getName());
        params.put("password", user.getPassword());
        params.put("email", user.getEmail());

        String expected = params.keySet().stream()
                .map(e -> e + "=" + URLEncoder.encode(params.get(e), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        assertTrue(expected.equals(
                RemoteLoftAccess.formUrlEncode("name", user.getName(),
                        "password", user.getPassword(), "email", user.getEmail())),
                "formUrlEncode should return the correct string");

        assertThrows(IllegalArgumentException.class,
                () -> RemoteLoftAccess.formUrlEncode("name"),
                "formUrlEncode should throw an exception if the number of arguments is odd");
    }

    @Test
    public void testGetUriWithParams() {
        URI base = URI.create("http://localhost:8080/loft");
        assertEquals(base, RemoteLoftAccess.getUriWithParams(base),
                "getUriWithParams should return the base URI if no params are given");

        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", user.getName());
        params.put("password", user.getPassword());
        params.put("email", user.getEmail());

        String expected = params.keySet().stream()
                .map(e -> e + "=" + URLEncoder.encode(params.get(e), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        URI expectedUri = URI.create("http://localhost:8080/loft?" + expected);
        String[] paramsArray = new String[params.size() * 2];
        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramsArray[i++] = entry.getKey();
            paramsArray[i++] = entry.getValue();
        }
        assertEquals(expectedUri, RemoteLoftAccess.getUriWithParams(base, paramsArray),
                "getUriWithParams with params should return the correct URI");
    }

    @Test
    public void testServerAlive() {
        stubFor(get(urlEqualTo("/loft/"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")));

        assertTrue(RemoteLoftAccess.serverAlive(port),
                "serverAlive should return true if the server is alive");

        stubFor(get(urlEqualTo("/loft/"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(notFound()));
        assertFalse(RemoteLoftAccess.serverAlive(port),
                "serverAlive should return false if the server is not alive");

        wireMockServer.stop();
        assertFalse(RemoteLoftAccess.serverAlive(port),
                "serverAlive should return false if the server is not alive");
    }

    @Test
    public void testRegisterUser() {
        stubFor(post(urlEqualTo("/loft/users/username/register"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("false")));
        assertFalse(loftAccess.registerUser(user),
                "registerUser should return false if the user is registered from before");

        stubFor(post(urlEqualTo("/loft/users/username/register"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(404)));
        assertFalse(loftAccess.registerUser(user),
                "registerUser should return false if the server returns 404");

        stubFor(post(urlEqualTo("/loft/users/username/register"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")));
        assertTrue(loftAccess.registerUser(user),
                "registerUser should return true if the user is not registered from before");

        wireMockServer.stop();
        assertFalse(loftAccess.registerUser(user));
    }

    @Test
    public void testWriteWorkoutToUser() {
        LocalDate date = LocalDate.of(2020, 1, 1);
        Workout workout = new Workout(date);

        String urlParams = "name=" + URLEncoder.encode(user.getName(), StandardCharsets.UTF_8)
                + "&password=" + URLEncoder.encode(user.getPassword(), StandardCharsets.UTF_8)
                + "&email=" + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);

        stubFor(put(urlEqualTo("/loft/users/username/workouts?" + urlParams))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("true")));
        assertTrue(loftAccess.writeWorkoutToUser(workout, user),
                "writeWorkoutToUser should return true if the workout is written to the user");

        stubFor(put(urlEqualTo("/loft/users/username/workouts?" + urlParams))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("false")));
        assertFalse(loftAccess.writeWorkoutToUser(workout, user),
                "writeWorkoutToUser should return false if the workout is not written to the user");

        stubFor(put(urlEqualTo("/loft/users/username/workouts?" + urlParams))
                .withHeader("Accept", equalTo("application/json"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(404)));
        assertFalse(loftAccess.writeWorkoutToUser(workout, user),
                "writeWorkoutToUser should return false if the server returns 404");

        wireMockServer.stop();
        assertFalse(loftAccess.writeWorkoutToUser(workout, user),
                "writeWorkoutToUser should return false if the server is not alive");
    }

    @Test
    public void testGetUser() {
        stubFor(get(urlEqualTo("/loft/users/username?password=password"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(new Gson().toJson(user))));
        assertEquals(user, loftAccess.getUser(user.getUsername(), user.getPassword()),
                "getUser should return the correct user if the server returns 200");

        stubFor(get(urlEqualTo("/loft/users/username?password=wrongPassword"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(new Gson().toJson(null))));
        assertNull(loftAccess.getUser(user.getUsername(), "wrongPassword"),
                "getUser should return null if the server returns 200 but the password is wrong");

        stubFor(get(urlEqualTo("/loft/users/username?password=password"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(404)));
        assertNull(loftAccess.getUser(user.getUsername(), user.getPassword()),
                "getUser should return null if the server returns 404");

        wireMockServer.stop();
        assertNull(loftAccess.getUser(user.getUsername(), user.getPassword()),
                "getUser should return null if the server is not alive");
    }

    @Test
    public void testUsernameExists() {
        stubFor(get(urlEqualTo("/loft/check-username/username"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("true")));
        assertTrue(loftAccess.usernameExists(user.getUsername()),
                "usernameExists should return true if the username exists");

        stubFor(get(urlEqualTo("/loft/check-username/username"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("false")));
        assertFalse(loftAccess.usernameExists(user.getUsername()),
                "usernameExists should return false if the username does not exist");

        stubFor(get(urlEqualTo("/loft/check-username/username"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(404)));
        assertThrows(RuntimeException.class,
                () -> loftAccess.usernameExists(user.getUsername()),
                "usernameExists should throw an exception if the server returns 404");

        wireMockServer.stop();
        assertThrows(RuntimeException.class,
                () -> loftAccess.usernameExists(user.getUsername()),
                "usernameExists should throw an exception if the server is not alive");
    }
}
