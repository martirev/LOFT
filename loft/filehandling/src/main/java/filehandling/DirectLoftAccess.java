package filehandling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.User;
import core.Workout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DirectLoftAccess is a class that implements the LoftAccess interface and
 * provides direct access to the userData.json file. It allows for registering
 * new users, adding workouts to existing users, and retrieving user data from
 * the file. The file location can be set using the setFileLocation method, and
 * retrieved using the getFileLocation method.
 */
public class DirectLoftAccess implements LoftAccess {
    private static String fileFolderLocation = System.getProperty("user.home")
            + System.getProperty("file.separator");
    private static String fileLocation = fileFolderLocation + "userData.json";

    /**
     * Sets the file location for DirectLoftAccess class.
     *
     * @param fileLocation the file location to be set.
     * @throws IllegalArgumentException if the file location is a directory
     */
    public static void setFileLocation(String fileLocation) {
        if ((new File(fileLocation)).isDirectory()) {
            throw new IllegalArgumentException(
                    "File location " + fileLocation + " is a directory, not a file");
        }
        DirectLoftAccess.fileLocation = fileLocation;
    }

    /**
     * Gets the file location for DirectLoftAccess class.
     */
    public static String getFileLocation() {
        return fileLocation;
    }

    @Override
    public boolean registerUser(User user) {
        try {
            registerUserGetUsers(user);
        } catch (IllegalStateException e) {
            return false;
        }
        return true;
    }

    /**
     * Adds a new user to the list of existing users and writes the updated list to
     * a file. Same as {@link #registerUser(User)} but returns the updated list of
     * users as well.
     *
     * @param user the user to be added
     * @return the updated list of users, or null if writing to file failed
     * @throws IllegalStateException if the user already exists
     */
    private static List<User> registerUserGetUsers(User user) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<User> users = getUsers();
        if (users.stream()
                .anyMatch(nullUser -> nullUser.getUsername().equals(user.getUsername()))) {
            throw new IllegalStateException("User already exists");
        }
        users.add(user);

        try (Writer file = new FileWriter(fileLocation, StandardCharsets.UTF_8)) {
            gson.toJson(new UsersHolder(users), file);
        } catch (IOException e) {
            System.err.println("Writing to file failed");
            return null;
        }
        return users;
    }

    @Override
    public boolean writeWorkoutToUser(Workout workout, User user) {
        if (user == null) {
            return false;
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<User> users = getUsers();
        User tmpUser = getUser(user, users);
        if (tmpUser == null) {
            users = registerUserGetUsers(user);
            tmpUser = getUser(user, users);
        }
        if (tmpUser == null) {
            return false;
        }
        tmpUser.addWorkout(workout);

        try (Writer file = new FileWriter(fileLocation, StandardCharsets.UTF_8)) {
            gson.toJson(new UsersHolder(users), file);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Gets users from the userData file in json format. If no file exists, it will
     * return an empty list.
     *
     * @return List of users
     */
    private static List<User> getUsers() {
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
    private static User getUser(User user, List<User> users) {
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
    private static User getUser(String username, String password, List<User> users) {
        String passwordHash = User.hash(password);
        for (User user : users) {
            if (user.getUsername().equals(username)
                    && user.getPasswordHash().equals(passwordHash)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User getUser(String username, String password) {
        List<User> users = getUsers();
        return getUser(username, password, users);
    }

    @Override
    public boolean usernameExists(String username) {
        return getUsers().stream().anyMatch(user -> user.getUsername().equals(username));
    }

    /**
     * Updates the info about the user and overwrites the old info in the userData.json file.
     *
     * @param oldUser the old user
     * @param newUser the updates user
     */
    public static void updateUserInfo(User oldUser, User newUser) {
        if (!oldUser.getUsername().equals(newUser.getUsername()) 
                && usernameExists(newUser.getUsername())) {
            throw new IllegalArgumentException("This username is already taken");
        }
        List<User> users = getUsers();
        List<User> newUsers = users.stream()
                .filter(user -> !user.equals(oldUser))
                .collect(Collectors.toList());
        newUsers.add(newUser); 
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer file = new FileWriter(fileLocation, StandardCharsets.UTF_8)) {
            gson.toJson(new UsersHolder(newUsers), file);
        } catch (IOException e) {
            System.err.println("Failed writing user updates to file");
        }
    }
}
