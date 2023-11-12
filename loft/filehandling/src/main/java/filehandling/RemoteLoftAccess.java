package filehandling;

import com.google.gson.Gson;
import core.User;
import core.Workout;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * This class provides a remote access to the Loft system. It implements the
 * LoftAccess interface and communicates with the Loft system through HTTP
 * requests. It uses the Gson library to serialize and deserialize JSON objects.
 */
public class RemoteLoftAccess implements LoftAccess {

    private URI endpointBaseUri;

    private static final String APPLICATION_JSON = "application/json";

    private static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";

    private static final String ACCEPT_HEADER = "Accept";

    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    private static final Gson gson = new Gson();

    public RemoteLoftAccess(URI endpointBaseUri) {
        this.endpointBaseUri = endpointBaseUri;
    }

    /**
     * Returns the URL of the remote loft access endpoint.
     *
     * @return the URL of the remote loft access endpoint
     */
    public String getUrl() {
        return endpointBaseUri == null ? "" : endpointBaseUri.toString();
    }

    /**
     * Checks if the server is alive by sending a request to the specified port.
     *
     * @param port the port number to send the request to
     * @return true if the server is alive and responds to the request, false
     *         otherwise
     */
    public static boolean serverAlive(int port) {
        return serverAlive(URI.create("http://localhost:" + port + "/loft/"));
    }

    /**
     * Checks if the server at the specified URI is alive by sending a GET request
     * and checking the response status code.
     *
     * @param uri the URI of the server to check
     * @return true if the server is alive and responds with a 200 status code,
     *         false otherwise
     */
    public static boolean serverAlive(URI uri) {
        try {
            HttpRequest request = HttpRequest
                    .newBuilder(uri)
                    .header(ACCEPT_HEADER, APPLICATION_JSON)
                    .GET().build();
            HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return false;
            }

            Boolean alive = gson.fromJson(response.body(), Boolean.class);
            return alive;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    /**
     * Encodes the given string using the UTF-8 charset.
     *
     * @param s the string to be encoded
     * @return the encoded string
     */
    static String uriParam(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    /**
     * Encodes the given parameters as a URL-encoded string.
     *
     * @param params the parameters to encode, as alternating key-value pairs
     * @return the URL-encoded string
     * @throws IllegalArgumentException if the number of parameters is odd
     */
    static String formUrlEncode(String... params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Must have an even number of parameters");
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < params.length; i += 2) {
            s.append(uriParam(params[i]))
                    .append("=")
                    .append(uriParam(params[i + 1]));
            if (i + 2 < params.length) {
                s.append("&");
            }
        }
        return s.toString();
    }

    /**
     * Constructs a URI with the given endpoint and query parameters. The query
     * parameters are given as a list of key-value pairs, where the first element
     * is the key of the first parameter, the second element is the value of the
     * first parameter, and so on.
     *
     * @param endpoint the endpoint URI to append the query parameters to
     * @param params   the query parameters to append to the endpoint URI
     * @return the constructed URI with the appended query parameters
     */
    static URI getUriWithParams(URI endpoint, String... params) {
        StringBuilder uri = new StringBuilder(endpoint.toString());
        if (params.length > 0) {
            uri.append("?");
            uri.append(formUrlEncode(params));
        }
        return URI.create(uri.toString());
    }

    /**
     * Adds the given user's name, password, and email as query parameters to the
     * endpoint URI.
     *
     * @param endpoint the endpoint URI to append the query parameters to
     * @param user     the user whose name, password, and email to append as query
     *                 parameters
     * @return the constructed URI with the appended query parameters
     * @see #getUriWithParams(URI, String...)
     */
    static URI paramifyUser(URI endpoint, User user) {
        return getUriWithParams(endpoint, "name", user.getName(),
                "password", user.getPassword(), "email", user.getEmail());
    }

    @Override
    public boolean registerUser(User user) {
        URI endpoint = endpointBaseUri.resolve("users/" + user.getUsername() + "/register");
        String form = formUrlEncode("name", user.getName(),
                "password", user.getPassword(), "email", user.getEmail());

        HttpRequest request = HttpRequest.newBuilder(endpoint)
                .header(ACCEPT_HEADER, APPLICATION_JSON)
                .header(CONTENT_TYPE_HEADER, APPLICATION_FORM_URLENCODED)
                .POST(BodyPublishers.ofString(form)).build();
        try {
            HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return false;
            }
            return gson.fromJson(response.body(), Boolean.class);
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    @Override
    public boolean writeWorkoutToUser(Workout workout, User user) {
        String workoutString = gson.toJson(workout);
        URI endpoint = endpointBaseUri.resolve("users/" + user.getUsername() + "/workouts");
        URI endpointParams = paramifyUser(endpoint, user);

        HttpRequest request = HttpRequest.newBuilder(endpointParams)
                .header(ACCEPT_HEADER, APPLICATION_JSON)
                .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .PUT(BodyPublishers.ofString(workoutString)).build();
        try {
            HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return false;
            }
            return gson.fromJson(response.body(), Boolean.class);
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    @Override
    public User getUser(String username, String password) {
        URI endpoint = endpointBaseUri.resolve("users/" + username);
        URI endpointParams = getUriWithParams(endpoint, "password", password);

        HttpRequest request = HttpRequest.newBuilder(endpointParams)
                .header(ACCEPT_HEADER, APPLICATION_JSON)
                .GET().build();
        try {
            HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return null;
            }

            User user = gson.fromJson(response.body(), User.class);
            if (user != null) {
                user.setPassword(password);
            }
            return user;
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    @Override
    public boolean usernameExists(String username) {
        URI uri = endpointBaseUri.resolve("check-username/" + username);
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header(ACCEPT_HEADER, APPLICATION_JSON)
                .GET().build();
        try {
            HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to check if username exists: "
                        + response.body());
            }
            return gson.fromJson(response.body(), Boolean.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateUserInfo(User oldUser, User newUser) {
        URI endpoint = endpointBaseUri.resolve("users/" + oldUser.getUsername());
        URI endpointParams = paramifyUser(endpoint, oldUser);
        String newUserString = formUrlEncode("newName", newUser.getName(),
                "newUsername", newUser.getUsername(), "newPassword", newUser.getPassword(),
                "newEmail", newUser.getEmail());
        URI finalEndpoint = URI.create(endpointParams.toString() + "&" + newUserString);

        HttpRequest request = HttpRequest.newBuilder(finalEndpoint)
                .header(ACCEPT_HEADER, APPLICATION_JSON)
                .header(CONTENT_TYPE_HEADER, APPLICATION_FORM_URLENCODED)
                .PUT(BodyPublishers.noBody()).build();
        try {
            HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return false;
            }
            return gson.fromJson(response.body(), Boolean.class);
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}
