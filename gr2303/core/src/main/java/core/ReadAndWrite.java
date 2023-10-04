package core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a class for reading and writing to the loftUserData file stored in
 * the users home directory. The two methods writeWorkoutToUser and
 * returnUserClassFromFile are of most interest. The file is stored in json
 * format. Using the Gson library we can parse the classes from/to json.
 */
public class ReadAndWrite {
    /**
     * For the first version of the application we will only have one user. However,
     * we have structured
     * the code in a way that it is easy to add multiple users in the future.
     */
    private static String fileFolderLocation = System.getProperty("user.home")
            + System.getProperty("file.separator");
    private String fileLocation;

    /**
     * The constructor for the ReadAndWrite class. It will use the default file
     * location.
     */
    public ReadAndWrite() {
        this(fileFolderLocation + "userData.json");
    }

    /**
     * The constructor for the ReadAndWrite class. It will use the file location
     * specified in the parameter.
     * Used for the tests
     *
     * @param location The location of the file
     */
    public ReadAndWrite(String location) {
        this.fileLocation = location;
    }

    /**
     * Registers a new user by adding it to the list of existing users and writing
     * the updated list to a file.
     *
     * @param user the user to be registered
     */
    public void registerUser(User user) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<User> users = getUsers();
        users.add(user);

        try (Writer file = new FileWriter(fileLocation, StandardCharsets.UTF_8)) {
            gson.toJson(new UsersHolder(users), file);
        } catch (IOException e) {
            System.err.println("Writing to file failed");
        }
    }

    private List<User> createUser(User user) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<User> users = getUsers();
        users.add(user);

        try (Writer file = new FileWriter(fileLocation, StandardCharsets.UTF_8)) {
            gson.toJson(new UsersHolder(users), file);
        } catch (IOException e) {
            System.err.println("Writing to file failed");
            return null;
        }
        return users;
    }

    /**
     * The method to be used for writing a workout class to the userData file in
     * json format.
     *
     * @param workout The workout to add to the current user
     */
    public void writeWorkoutToUser(Workout workout, User user) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<User> users = getUsers();
        User tmpUser = getUser(user, users);
        if (tmpUser == null) {
            users = createUser(user);
            tmpUser = getUser(user, users);
        }
        tmpUser.addWorkout(workout);

        try (Writer file = new FileWriter(fileLocation, StandardCharsets.UTF_8)) {
            gson.toJson(new UsersHolder(users), file);
        } catch (IOException e) {
            System.err.println("Writing to file failed");
        }
    }

    /**
     * Gets users from the userData file in json format. If no file exists, it will
     * return an empty list.
     *
     * @return List of users
     */
    private List<User> getUsers() {
        List<User> users = new ArrayList<User>();
        try {
            String text = new String(Files.readAllBytes(Paths.get(fileLocation)),
                    StandardCharsets.UTF_8);
            Gson gson = new Gson();
            users = gson.fromJson(text, UsersHolder.class).getUsers();
        } catch (IOException e) {
            // It is fine if no file exists. We will create a new one later.
        }
        return users;
    }

    /**
     * Returns a User object if the given user matches an existing user in the list
     * of users.
     *
     * @param user  the user to retrieve
     * @param users the list of users to search through
     * @return a User object if the given user matches an existing user in the
     *         system, null otherwise. This will be the same user as the parameter
     *         but with updated data.
     */
    private User getUser(User user, List<User> users) {
        return getUser(user.getUsername(), user.getPassword(), users);
    }

    /**
     * Returns a User object if the given username and password match an existing
     * user in the users-list.
     *
     * @param username the username of the user to retrieve
     * @param password the password of the user to retrieve
     * @param users    the list of users to search through
     * @return a User object if the given username and password match an existing
     *         user in the system, null otherwise
     */
    private User getUser(String username, String password, List<User> users) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Returns a User object if the given username and password match an existing
     * user in the system.
     *
     * @param username the username of the user to retrieve
     * @param password the password of the user to retrieve
     * @return a User object if the given username and password match an existing
     *         user in the system, null otherwise
     */
    public User getUser(String username, String password) {
        List<User> users = getUsers();
        return getUser(username, password, users);
    }

    /**
     * The method to be used for reading the userData file in json format. If no
     * file or user exists, it will return a new User.
     *
     * @return The user data from the file
     */
    public User returnUserClassFromFile(User user) {
        User updatedUser = getUser(user.getUsername(), user.getPassword());
        if (updatedUser == null) {
            createUser(user);
            updatedUser = getUser(user.getUsername(), user.getPassword());
        }
        return updatedUser;
    }

    /**
     * Checks if a username already exists.
     *
     * @param username the username to check for existence
     * @return true if the username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        return getUsers().stream().anyMatch(user -> user.getUsername().equals(username));
    }
}
