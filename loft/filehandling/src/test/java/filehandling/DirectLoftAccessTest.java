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
 * This class contains unit tests for the DirectLoftAccess class. It tests the
 * functionality of the DirectLoftAccess class by testing its methods for
 * reading and writing to a file, registering a user, checking if a username
 * exists, and getting a user.
 */
public class DirectLoftAccessTest {

    private static String testFolderLocation = System.getProperty("user.home")
            + System.getProperty("file.separator");
    private static String testFileLocation = testFolderLocation + "testUserData.json";
    private Random rand = new Random();

    private LoftAccess loftAccess = new DirectLoftAccess();

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
        DirectLoftAccess.setFileLocation(testFileLocation);
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
                loftAccess.getUpdatedUser(user),
                "The user should either be already saved, or be created and now be of type user.");
    }

    @Test
    public void testSetFileLocation() {
        String fileLocation = "example/location.json";

        DirectLoftAccess.setFileLocation(fileLocation);
        assertTrue(DirectLoftAccess.getFileLocation().equals(fileLocation),
                "The file location should be the same as the one set.");
    }

    @Test
    public void testGetUser() {
        loftAccess.writeWorkoutToUser(workout1, user);
        User fileUser1 = loftAccess.getUser(user.getUsername(), user.getPassword());
        assertTrue(user.equals(fileUser1), "The users should be equal.");

        User fileUser2 = loftAccess.getUser(user.getUsername() + "1", user.getPassword());
        assertNull(fileUser2, "There should not be any matching user.");

        User fileUser3 = loftAccess.getUser(user.getUsername(), user.getPassword() + "1");
        assertNull(fileUser3, "There should not be any matching user.");
    }

    @Test
    public void testRegisterUser() {
        loftAccess.registerUser(user);
        User fileUser = loftAccess.getUser(user.getUsername(), user.getPassword());
        assertTrue(user.equals(fileUser), "The users should be equal.");
    }

    @Test
    public void testWriteWorkoutToUserError() {
        assertThrows(IllegalArgumentException.class,
                () -> DirectLoftAccess.setFileLocation(testFolderLocation),
                "Should throw illegal argument exception when trying to set a folder as location.");
    }

    @Test
    public void testWriteAndReadToFormFile() {
        loftAccess.writeWorkoutToUser(workout1, user);
        loftAccess.writeWorkoutToUser(workout2, user);
        User fileUser = loftAccess.getUpdatedUser(user);
        for (int i = 0; i < user.getNumberOfWorkouts(); i++) {
            assertTrue(user.getWorkouts().get(i).equals(fileUser.getWorkouts().get(i)),
                    "Workouts are not equal");
        }
    }

    @Test
    public void testUsernameExists() {
        loftAccess.writeWorkoutToUser(workout1, user);
        assertTrue(loftAccess.usernameExists(user.getUsername()),
                "The username should exist in the file");

        assertFalse(loftAccess.usernameExists("thisUsernameDoesntExist"),
                "The username should not exist in the file");
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
