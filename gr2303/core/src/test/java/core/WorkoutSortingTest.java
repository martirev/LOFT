package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WorkoutSortingTest {

    private Workout workout1;
    private Workout workout2;
    private Exercise exercise1;
    private Exercise exercise2;
    private Exercise exercise3;
    private Exercise exercise4;
    private List<Workout> workouts = new ArrayList<Workout>();

    @BeforeEach
    public void setUp() {

        workout1 = new Workout();
        workout2 = new Workout(LocalDate.of(2019, 1, 1));
        exercise1 = new Exercise("Bench Press");
        exercise2 = new Exercise("Squats");
        exercise3 = new Exercise("Bench Press");
        exercise4 = new Exercise("Squats");

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

        workout1.addExercise(exercise1);
        workout1.addExercise(exercise2);

        Set benchSet5 = new Set(4, 130);
        Set benchSet6 = new Set(4, 120);

        Set squatSet5 = new Set(5, 170);
        Set squatSet6 = new Set(5, 180);

        exercise3.addSet(benchSet5);
        exercise3.addSet(benchSet6);

        exercise4.addSet(squatSet5);
        exercise4.addSet(squatSet6);

        workout2.addExercise(exercise3);
        workout2.addExercise(exercise4);

        workouts.add(workout1);
        workouts.add(workout2);
    }

    @Test
    public void testGetMostRecentWorkouts() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        List<Workout> mostRecentWorkouts = workoutSorting.getMostRecentWorkouts();

        assertEquals(2, mostRecentWorkouts.size());
        assertEquals(workout1, mostRecentWorkouts.get(0));
        assertEquals(workout2, mostRecentWorkouts.get(1));
    }

    @Test
    public void testGetSameExersices() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        Map<String, List<Exercise>> sameExercises = workoutSorting.getSameExersices();
        assertEquals(2, workoutSorting.getSameExersices().size());
        assertTrue(workoutSorting.getSameExersices().containsKey("Bench Press"));
        assertTrue(workoutSorting.getSameExersices().containsKey("Squats"));
        for (String name : sameExercises.keySet()) {
            for (Exercise exercise : sameExercises.get(name)) {
                assertTrue(exercise.getName().equals(name));
            }
        }
    }

    @Test
    public void testGetExercisesPr() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        assertEquals(150, workoutSorting.getExercisesPr("Bench Press"));
        assertEquals(200, workoutSorting.getExercisesPr("Squats"));
    }

    @Test
    public void testGetExercises() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        List<Exercise> exercises = workoutSorting.getSameExersices("Bench Press");
        assertEquals(2, exercises.size());
        assertTrue(exercises.contains(exercise1));
        assertTrue(exercises.contains(exercise3));
        assertFalse(exercises.contains(exercise2));
    }

}
