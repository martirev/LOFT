package core;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * This is a class for reading and writing to the loftUserData file stored in
 * the users home directory.
 * The two methods writeWorkoutToUser and returnUserClassFromFile are of most
 * interest.
 * The file is stored in json format. Using the UserAdapter class we can
 * reconstruct classes form the
 * json file, and write classes in json format.
 * 
 */
public class ReadAndWrite {
    /**
     * For the first version of the application we will only have one user. However,
     * we have structured
     * the code in a way that it is easy to add multiple users in the future.
     * To parse the classes from/to json we are reconstructing/deconstructing the
     * attributes through the
     * UserAdapter class using the gson library.
     */
    private static String fileFolderLocation = System.getProperty("user.home") + System.getProperty("file.separator");
    private UserAdapter userAdapter = new UserAdapter();
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
     * Used fofr the tests
     * 
     * @param location String
     */
    public ReadAndWrite(String location) {
        this.fileLocation = location;
    }

    /**
     * The method to be used for writing a workout class to the userData file in
     * json format.
     * 
     * @param workout
     */
    public void writeWorkoutToUser(Workout workout) {
        User existingData = returnUserClassFromFile();
        User user;

        if (existingData == null) {
            user = new User();
        } else {
            user = existingData;
        }
        user.addWorkout(workout);

        try (Writer file = new FileWriter(fileLocation)) {
            JsonWriter jsonWriter = new JsonWriter(file);
            userAdapter.write(jsonWriter, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method to be used for reading the userData file in json format.
     * 
     * @return User class
     */
    public User returnUserClassFromFile() {
        try (Reader reader = new FileReader(fileLocation)) {
            if (reader.ready()) {
                JsonReader jsonReader = new JsonReader(reader);
                User user = userAdapter.read(jsonReader);
                return user;
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            // If it is not found, it will be created at a later point, so this is fine.
            return new User();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        // local tests

        ReadAndWrite readAndWrite = new ReadAndWrite();
        User user = new User();
        Workout workout = new Workout();
        Exercise exercise = new Exercise("test");
        Set set1 = new Set(10, 10);
        Set set2 = new Set(10, 10);

        exercise.addSet(set1);
        exercise.addSet(set2);

        workout.addExercise(exercise);
        user.addWorkout(workout);

        readAndWrite.writeWorkoutToUser(user.getWorkouts().get(0));
    }
}