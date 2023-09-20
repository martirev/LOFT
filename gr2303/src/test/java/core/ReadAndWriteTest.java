package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReadAndWriteTest {
    
    private ReadAndWrite readAndWrite;

    private static String testFileLocation = System.getProperty("user.home") + System.getProperty("file.separator") + "testUserData.json";
    private Random rand = new Random();

    private User user = new User();
    
    private Workout workout1 = new Workout();
    private Workout workout2 = new Workout();

    private Exercise bench = new Exercise("Bench Press");
    private Exercise squat = new Exercise("Squats");
    private Exercise deadlift = new Exercise("Deadlifts");; 

    private Set set1;
    private Set set2;
    private Set set3;
    private Set set4;

    @BeforeEach
    public void setUp() {
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
    public void constructorTest1() {
        readAndWrite = new ReadAndWrite();
        assertInstanceOf(User.class, readAndWrite.returnUserClassFromFile(), "ReadAndWrite is not an instance of User");
    }
    @Test
    public void constructorTest2() {
        readAndWrite = new ReadAndWrite(testFileLocation);
        assertInstanceOf(User.class, readAndWrite.returnUserClassFromFile(), "ReadAndWrite is not an instance of User");
    }

    @Test
    public void writeAndReadToFormFileTest() {
        readAndWrite = new ReadAndWrite(testFileLocation);
        readAndWrite.writeWorkoutToUser(workout1);
        readAndWrite.writeWorkoutToUser(workout2);
        for (int i = 0; i < user.getNumberOfWorkouts(); i++) {
            assertTrue(user.getWorkouts().get(i).equals(readAndWrite.returnUserClassFromFile().getWorkouts().get(i)), "Workouts are not equal");

        }
        deleteTestfile();
    }

    private void deleteTestfile() {
        try {
            Files.delete(Path.of(testFileLocation));
        } catch (IOException e) {
            System.err.println("Error deleting file");
        }
    }
}
