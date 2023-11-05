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
 * The ReadAndWrite class provides methods for reading and writing user data to
 * a file in JSON format. It includes methods for registering new users, adding
 * workouts to existing users, and retrieving user data. The file location can
 * be set using the setFileLocation method.
 */
public abstract class ReadAndWrite {
    private static String fileFolderLocation = System.getProperty("user.home")
            + System.getProperty("file.separator");
    private static String fileLocation = fileFolderLocation + "userData.json";

    /**
     * Private constructor to prevent instantiation.
     */
    private ReadAndWrite() {
    }

    /**
     * Sets the file location for ReadAndWrite class.
     *
     * @param fileLocation the file location to be set.
     * @throws IllegalArgumentException if the file location is a directory
     */
    public static void setFileLocation(String fileLocation) {
        if ((new File(fileLocation)).isDirectory()) {
            throw new IllegalArgumentException(
                    "File location " + fileLocation + " is a directory, not a file");
        }
        ReadAndWrite.fileLocation = fileLocation;
    }

    /**
     * Gets the file location for ReadAndWrite class.
     */
    public static String getFileLocation() {
        return fileLocation;
    }

    /**
     * Registers a new user by adding it to the list of existing users and writing
     * the updated list to a file.
     *
     * @param user the user to be registered
     */
    public static void registerUser(User user) {
        registerUserGetUsers(user);
    }

    /**
     * Adds a new user to the list of existing users and writes the updated list to
     * a file. Same as {@link #registerUser(User)} but returns the updated list of
     * users as well.
     *
     * @param user the user to be added
     * @return the updated list of users, or null if writing to file failed
     */
    private static List<User> registerUserGetUsers(User user) {
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
    public static void writeWorkoutToUser(Workout workout, User user) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<User> users = getUsers();
        User tmpUser = getUser(user, users);
        if (tmpUser == null) {
            users = registerUserGetUsers(user);
            tmpUser = getUser(user, users);
        }
        if (tmpUser == null) {
            throw new IllegalStateException("User should exist at this point.");
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
        if (users == null) {
            return null;
        }
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

    /**
     * Returns a User object if the given username and password match an existing
     * user in the system.
     *
     * @param username the username of the user to retrieve
     * @param password the password of the user to retrieve
     * @return a User object if the given username and password match an existing
     *         user in the system, null otherwise
     */
    public static User getUser(String username, String password) {
        List<User> users = getUsers();
        return getUser(username, password, users);
    }

    /**
     * The method to be used for reading the userData file in json format. If no
     * file or user exists, it will return a new User.
     *
     * @return The user data from the file
     */
    public static User returnUserClassFromFile(User user) {
        User updatedUser = getUser(user.getUsername(), user.getPassword());
        if (updatedUser == null) {
            registerUser(user);
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
    public static boolean usernameExists(String username) {
        return getUsers().stream().anyMatch(user -> user.getUsername().equals(username));
    }

    /**
     * Updates the info about the user and overwrites the old info in the userData.json file.
     *
     * @param oldUser the old user
     * @param newUser the updates user
     */
    public static void updateUserInfo(User oldUser, User newUser) {
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
