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
 * the users home directory.
 * The two methods writeWorkoutToUser and returnUserClassFromFile are of most
 * interest.
 * The file is stored in json format. Using the Gson library we can parse the
 * classes from/to json.
 */
public class ReadAndWrite {
    /**
     * For the first version of the application we will only have one user. However,
     * we have structured
     * the code in a way that it is easy to add multiple users in the future.
     */
    private static String fileFolderLocation = System.getProperty("user.home") + System.getProperty("file.separator");
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
     * The method to be used for writing a workout class to the userData file in
     * json format.
     * 
     * @param workout The workout to add to the current user
     */
    public void writeWorkoutToUser(Workout workout) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<User> users = getUsers();

        if (users.size() == 0) {
            users.add(new User());
        }

        users.get(0).addWorkout(workout);

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
            String text = new String(Files.readAllBytes(Paths.get(fileLocation)), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            users = gson.fromJson(text, UsersHolder.class).getUsers();
        } catch (IOException e) {
            // It is fine if no file exists. We will create a new one.
        }
        return users;
    }

    /**
     * The method to be used for reading the userData file in json format. If no
     * file or user exists, it will return a new User.
     * 
     * @return The user data from the file
     */
    public User returnUserClassFromFile() {
        List<User> users = getUsers();

        if (users.size() == 0) {
            return new User();
        }
        return users.get(0);
    }
}
