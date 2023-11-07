package filehandling;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.Exercise;
import core.Set;
import core.User;
import core.Workout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class contains JUnit tests for the ReadAndWrite class. It tests the
 * constructor, writeWorkoutToUser method, and returnUserClassFromFile method.
 * The tests include checking if the ReadAndWrite object is an instance of User,
 * writing and reading workouts to and from a file, and checking if the workouts
 * are equal.
 */
public class ReadAndWriteTest {

    private static String testFolderLocation = System.getProperty("user.home")
            + System.getProperty("file.separator");
    private static String testFileLocation = testFolderLocation + "testUserData.json";
    private Random rand = new Random();

    private User user;

    private Workout workout1 = new Workout();
    private Workout workout2 = new Workout();

    private Exercise bench = new Exercise("Bench Press");
    private Exercise squat = new Exercise("Squats");
    private Exercise deadlift = new Exercise("Deadlifts");

    private Set set1;
    private Set set2;
    private Set set3;
    private Set set4;

    @AfterEach
    public void cleanUp() {
        deleteTestfile();
    }

    /**
     * This method sets up the necessary objects for the tests to run.
     */
    @BeforeEach
    public void setUp() {
        ReadAndWrite.setFileLocation(testFileLocation);
        deleteTestfile();

        user = new User("Test User", "tester", "hunter2", "tester@example.com");
        set1 = new Set(rand.nextInt(20), rand.nextInt(120));
        set2 = new Set(rand.nextInt(20), rand.nextInt(120));
        set3 = new Set(rand.nextInt(20), rand.nextInt(120));
        set4 = new Set(rand.nextInt(20), rand.nextInt(120));

        bench.addSet(set1);
        bench.addSet(set2);

        squat.addSet(set3);
        squat.addSet(set4);

        deadlift.addSet(set1);
        deadlift.addSet(set2);
        deadlift.addSet(set3);
        deadlift.addSet(set4);

        workout1.addExercise(bench);
        workout1.addExercise(squat);

        workout2.addExercise(deadlift);
        workout2.addExercise(squat);
        workout2.addExercise(bench);

        user.addWorkout(workout1);
        user.addWorkout(workout2);
    }

    @Test
    public void testConstructor() {
        assertInstanceOf(User.class,
                ReadAndWrite.returnUserClassFromFile(user),
                "The user should either be already saved, or be created and now be of type user.");
    }

    @Test
    public void testSetFileLocation() {
        String fileLocation = "example/location.json";

        ReadAndWrite.setFileLocation(fileLocation);
        assertTrue(ReadAndWrite.getFileLocation().equals(fileLocation),
                "The file location should be the same as the one set.");
    }

    @Test
    public void testGetUser() {
        ReadAndWrite.writeWorkoutToUser(workout1, user);
        User fileUser1 = ReadAndWrite.getUser(user.getUsername(), user.getPassword());
        assertTrue(user.equals(fileUser1), "The users should be equal.");

        User fileUser2 = ReadAndWrite.getUser(user.getUsername() + "1", user.getPassword());
        assertNull(fileUser2, "There should not be any matching user.");

        User fileUser3 = ReadAndWrite.getUser(user.getUsername(), user.getPassword() + "1");
        assertNull(fileUser3, "There should not be any matching user.");
    }

    @Test
    public void testRegisterUser() {
        ReadAndWrite.registerUser(user);
        User fileUser = ReadAndWrite.getUser(user.getUsername(), user.getPassword());
        assertTrue(user.equals(fileUser), "The users should be equal.");
    }

    @Test
    public void testWriteWorkoutToUserError() {
        assertThrows(IllegalArgumentException.class,
                () -> ReadAndWrite.setFileLocation(testFolderLocation),
                "Should throw illegal argument exception when trying to set a folder as location.");
    }

    @Test
    public void testWriteAndReadToFormFile() {
        ReadAndWrite.writeWorkoutToUser(workout1, user);
        ReadAndWrite.writeWorkoutToUser(workout2, user);
        User fileUser = ReadAndWrite.returnUserClassFromFile(user);
        for (int i = 0; i < user.getNumberOfWorkouts(); i++) {
            assertTrue(user.getWorkouts().get(i).equals(fileUser.getWorkouts().get(i)),
                    "Workouts are not equal");
        }
    }

    @Test
    public void testUsernameExists() {
        ReadAndWrite.writeWorkoutToUser(workout1, user);
        assertTrue(ReadAndWrite.usernameExists(user.getUsername()),
                "The username should exist in the file");

        assertFalse(ReadAndWrite.usernameExists("thisUsernameDoesntExist"),
                "The username should not exist in the file");
    }

    @Test
    public void testIllegalUsername() {
        User user2 = new User("John Doe", "test", "test123", "johnDoe@gmail.com");
        ReadAndWrite.registerUser(user2);
        assertThrows(IllegalArgumentException.class, () -> {
            ReadAndWrite.updateUserInfo(user, user2); });
    }

    @Test
    public void testUserUpdated() {
        User user2 = new User("John Doe", "johnDoe123", "test123", "johnDoe123@gmail.com");
        ReadAndWrite.updateUserInfo(user, user2);
        assertTrue(ReadAndWrite.getUser("johnDoe123", "test123").equals(user2));
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
