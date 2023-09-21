package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WorkoutTest {

    private Workout workout;
    private Exercise exercise1;
    private Exercise exercise2;

    @BeforeEach
    public void setUp() {
        workout = new Workout();
        exercise1 = new Exercise("Bench Press");
        exercise2 = new Exercise("Squats");
    }

    @Test
    public void testAddExercise() {
        assertEquals(0, workout.getTotalWeight());

        Set benchSet1 = new Set(10, 150);
        Set benchSet2 = new Set(8, 130);
        Set benchSet3 = new Set(6, 110);
        Set benchSet4 = new Set(4, 90);

        Set squatSet1 = new Set(10, 200);
        Set squatSet2 = new Set(8, 180);
        Set squatSet3 = new Set(6, 160);
        Set squatSet4 = new Set(4, 140);

        exercise1.addSet(benchSet1);
        exercise1.addSet(benchSet2);
        exercise1.addSet(benchSet3);
        exercise1.addSet(benchSet4);

        exercise2.addSet(squatSet1);
        exercise2.addSet(squatSet2);
        exercise2.addSet(squatSet3);
        exercise2.addSet(squatSet4);

        workout.addExercise(exercise1);
        workout.addExercise(exercise2);
        assertEquals(8520, workout.getTotalWeight());
        assertEquals(LocalDate.now(), workout.getDate());
    }

    @Test
    public void testGetExercises() {
        workout.addExercise(exercise1);
        workout.addExercise(exercise2);

        assertTrue(workout.getExercises().containsAll(Arrays.asList(exercise1, exercise2)));
    }

    @Test
    public void testListPrivacy() {
        workout.addExercise(exercise1);
        workout.addExercise(exercise2);

        List<Exercise> exercises = workout.getExercises();
        exercises.clear();

        assertTrue(workout.getExercises().containsAll(Arrays.asList(exercise1, exercise2)));
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
        assertEquals(4960, workout.getTotalWeight());
    }
}
