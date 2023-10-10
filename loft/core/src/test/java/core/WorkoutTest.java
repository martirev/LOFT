package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class contains JUnit tests for the Workout class.
 */
public class WorkoutTest {

    private Workout workout;
    private Exercise exercise1;
    private Exercise exercise2;

    /**
     * This method sets up needed stuff before each test method is executed.
     */
    @BeforeEach
    public void setUp() {
        workout = new Workout();
        exercise1 = new Exercise("Bench Press");
        exercise2 = new Exercise("Squats");
    }

    @Test
    public void testAddExercise() {
        assertEquals(0, workout.getTotalWeight(),
                "Total weight should be 0 as no exercises are added yet");

        Set benchSet1 = new Set(10, 150);
        Set benchSet2 = new Set(8, 130);
        Set benchSet3 = new Set(6, 110);
        Set benchSet4 = new Set(4, 90);
        exercise1.addSet(benchSet1);
        exercise1.addSet(benchSet2);
        exercise1.addSet(benchSet3);
        exercise1.addSet(benchSet4);

        Set squatSet1 = new Set(10, 200);
        Set squatSet2 = new Set(8, 180);
        Set squatSet3 = new Set(6, 160);
        Set squatSet4 = new Set(4, 140);
        exercise2.addSet(squatSet1);
        exercise2.addSet(squatSet2);
        exercise2.addSet(squatSet3);
        exercise2.addSet(squatSet4);

        workout.addExercise(exercise1);
        workout.addExercise(exercise2);
        assertEquals(8520, workout.getTotalWeight(), "Total weight should be 8520");
        assertEquals(LocalDate.now(), workout.getDate(), "The date should be today");
    }

    @Test
    public void testGetExercises() {
        workout.addExercise(exercise1);
        workout.addExercise(exercise2);

        assertTrue(workout.getExercises().containsAll(Arrays.asList(exercise1, exercise2)),
                "Should contain both exercises");
    }

    @Test
    public void testListPrivacy() {
        workout.addExercise(exercise1);
        workout.addExercise(exercise2);

        List<Exercise> exercises = workout.getExercises();
        exercises.clear();

        assertTrue(workout.getExercises().containsAll(Arrays.asList(exercise1, exercise2)),
                "getExercises() should not reveal the actual list");
    }

    @Test
    public void testgetTotalWeight() {
        Set squatSet1 = new Set(10, 200);
        Set squatSet2 = new Set(8, 180);
        Set squatSet3 = new Set(6, 160);
        Set squatSet4 = new Set(4, 140);

        exercise2.addSet(squatSet1);
        exercise2.addSet(squatSet2);
        exercise2.addSet(squatSet3);
        exercise2.addSet(squatSet4);
        workout.addExercise(exercise2);
        assertEquals(4960, workout.getTotalWeight(), "Total weight should be 4960");
    }

    @Test
    public void testGetDate() {
        LocalDate date = LocalDate.of(2020, 1, 1);
        Workout workout = new Workout(date);

        assertThrows(IllegalArgumentException.class, () -> new Workout(null),
                "Date cannot be null");
        assertEquals(date, workout.getDate(), "Date should be 2020-01-01");
    }

    @Test
    public void testGetTotalSets() {
        assertTrue(workout.toString().contains("Number of sets: 0"));
        Set squatSet1 = new Set(10, 200);
        Set squatSet2 = new Set(8, 180);

        exercise2.addSet(squatSet1);
        exercise2.addSet(squatSet2);
        workout.addExercise(exercise2);
        assertTrue(workout.toString().contains("Number of sets: 2"));
    }

    @Test
    public void testEquals() {
        Workout workout1 = new Workout();
        assertEquals(workout1, workout1);
        assertNotEquals(workout1, null);

        assertNotEquals(workout1, new String());

        Workout workout2 = new Workout();
        workout2.addExercise(exercise1);
        assertNotEquals(workout1, workout2);

        Workout workout3 = new Workout(LocalDate.of(2020, 1, 1));
        assertNotEquals(workout1, workout3);

        Workout workout4 = new Workout();
        workout1.addExercise(exercise1);
        workout4.addExercise(exercise1);
        assertEquals(workout1, workout4);
    }

    @Test
    public void testHashCode() {
        Workout workout1 = new Workout();
        Workout workout2 = new Workout();
        assertEquals(workout1.hashCode(), workout2.hashCode());

        Workout workout3 = new Workout(LocalDate.of(2020, 1, 1));
        assertNotEquals(workout1.hashCode(), workout3.hashCode());

        workout1.addExercise(exercise1);
        assertNotEquals(workout1.hashCode(), workout2.hashCode());

        workout2.addExercise(exercise1);
        assertEquals(workout1.hashCode(), workout2.hashCode());
    }
}
