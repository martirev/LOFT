package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class contains JUnit tests for the WorkoutSorting class. It tests the
 * functionality of the methods in the WorkoutSorting class, which sorts and
 * retrieves data from a list of Workout objects.
 */
public class WorkoutSortingTest {

    private Workout workout1;
    private Workout workout2;
    private Exercise exercise1;
    private Exercise exercise2;
    private Exercise exercise3;
    private Exercise exercise4;
    private Exercise exercise5;
    private List<Workout> workouts = new ArrayList<Workout>();

    /**
     * Sets up needed stuff before each test method runs.
     */
    @BeforeEach
    public void setUp() {
        workout1 = new Workout();
        workout2 = new Workout(LocalDate.of(2019, 1, 1));
        exercise1 = new Exercise("Bench Press");
        exercise2 = new Exercise("Squats");
        exercise3 = new Exercise("Bench Press");
        exercise4 = new Exercise("Squats");
        exercise5 = new Exercise("Curls");

        final Set benchSet1 = new Set(10, 150);
        final Set benchSet2 = new Set(8, 130);
        final Set benchSet3 = new Set(6, 110);
        final Set benchSet4 = new Set(4, 90);
        exercise1.addSet(benchSet1);
        exercise1.addSet(benchSet2);
        exercise1.addSet(benchSet3);
        exercise1.addSet(benchSet4);

        final Set squatSet1 = new Set(10, 200);
        final Set squatSet2 = new Set(8, 180);
        final Set squatSet3 = new Set(6, 160);
        final Set squatSet4 = new Set(4, 140);
        exercise2.addSet(squatSet1);
        exercise2.addSet(squatSet2);
        exercise2.addSet(squatSet3);
        exercise2.addSet(squatSet4);

        workout1.addExercise(exercise1);
        workout1.addExercise(exercise2);

        final Set benchSet5 = new Set(4, 130);
        final Set benchSet6 = new Set(4, 120);
        exercise3.addSet(benchSet5);
        exercise3.addSet(benchSet6);

        final Set squatSet5 = new Set(5, 170);
        final Set squatSet6 = new Set(5, 180);
        exercise4.addSet(squatSet5);
        exercise4.addSet(squatSet6);

        final Set curlsSet1 = new Set(5, 100);
        exercise5.addSet(curlsSet1);

        workout2.addExercise(exercise3);
        workout2.addExercise(exercise4);
        workout2.addExercise(exercise5);

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
    public void testGetSameExercisesNoInput() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        assertEquals(3, workoutSorting.getSameExercises().size());
        assertTrue(workoutSorting.getSameExercises().containsKey("Bench Press"),
                "Same exercises should contain Bench Press");
        assertTrue(workoutSorting.getSameExercises().containsKey("Squats"),
                "Same exercises should contain Squats");

        Map<String, List<Exercise>> sameExercises = workoutSorting.getSameExercises();
        for (String name : sameExercises.keySet()) {
            for (Exercise exercise : sameExercises.get(name)) {
                assertTrue(exercise.getName().equals(name), "Exercise name should be " + name);
            }
        }
    }

    @Test
    public void testGetSameExercisesString() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        List<Exercise> exercises1 = workoutSorting.getSameExercises("Bench Press");
        assertEquals(2, exercises1.size());
        assertTrue(exercises1.contains(exercise1), "exercises1 should contain exercise1");
        assertTrue(exercises1.contains(exercise3), "exercises1 should contain exercise3");
        assertFalse(exercises1.contains(exercise2), "exercises1 should not contain exercise2");

        List<Exercise> exercises2 = workoutSorting.getSameExercises("This exercise does not exist");
        assertEquals(0, exercises2.size());
    }

    @Test
    public void testGetSameExercisesExercise() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        Exercise exercise1 = new Exercise("This exercise does not exist");
        List<Exercise> exercises1 = workoutSorting.getSameExercises(exercise1);
        assertEquals(0, exercises1.size());

        Exercise exercise2 = new Exercise("Bench Press");
        List<Exercise> exercises2 = workoutSorting.getSameExercises(exercise2);
        assertEquals(2, exercises2.size());
    }

    @Test
    public void testGetExercisesPr() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        assertEquals(150, workoutSorting.getExercisesPr("Bench Press"));
        assertEquals(200, workoutSorting.getExercisesPr("Squats"));
        assertEquals(0, workoutSorting.getExercisesPr("Running"));
    }

    @Test
    public void testGetPrOnDay() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        assertEquals(150, workoutSorting.getPrOnDay(exercise1, workout1.getDate()));
        assertEquals(130, workoutSorting.getPrOnDay(exercise3, workout2.getDate()));

        Workout workout3 = new Workout();
        workout3.addExercise(exercise1);
        Workout workout4 = new Workout();
        workout4.addExercise(exercise3);
        List<Workout> workoutList = new ArrayList<>(Arrays.asList(workout3, workout4));
        WorkoutSorting workoutSorting2 = new WorkoutSorting(workoutList);
        assertEquals(150, workoutSorting2.getPrOnDay(exercise1, workout3.getDate()));
    }

    @Test
    public void testSearchForExercises() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);

        Collection<String> exercisesContainsE = workoutSorting.searchForExercises("e");
        checkContainsEachOther(exercisesContainsE, Arrays.asList("Bench Press"));

        Collection<String> exercisesContainsS = workoutSorting.searchForExercises("s");
        checkContainsEachOther(exercisesContainsS, Arrays.asList("Bench Press", "Squats", "Curls"));

        Collection<String> exercisesContainsBench = workoutSorting.searchForExercises("bench");
        checkContainsEachOther(exercisesContainsBench, Arrays.asList("Bench Press"));
    }

    @Test
    public void testGetTotalWeightOnDay() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        assertEquals(3250, workoutSorting.getTotalWeightOnDay(workout2.getDate()));
    }

    @Test
    public void testGetUniqueDates() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        List<LocalDate> list = new ArrayList<>();
        list.add(workout2.getDate());
        list.add(workout1.getDate());

        List<LocalDate> unique = workoutSorting.getUniqueDates();
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), unique.get(i));
        }
    }

    @Test
    public void testGetWeightPerDay() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        HashMap<LocalDate, Integer> hashmap = new HashMap<>();
        hashmap.put(workout1.getDate(), workout1.getTotalWeight());
        hashmap.put(workout2.getDate(), workout2.getTotalWeight());
        assertEquals(hashmap, workoutSorting.getWeightPerDay());
    }

    @Test
    public void testGetHeaviestLiftedSet() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        assertEquals(2000, workoutSorting.getHeaviestLiftedSet("Squats"));
        assertEquals(1500, workoutSorting.getHeaviestLiftedSet("Bench Press"));
        assertEquals(0, workoutSorting.getHeaviestLiftedSet("Running"));
    }

    @Test
    public void testGetTotalWeightEver() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        assertEquals(4560, workoutSorting.getTotalWeightEver("Bench Press"));
        assertEquals(6710, workoutSorting.getTotalWeightEver("Squats"));
        assertEquals(0, workoutSorting.getTotalWeightEver("Pull ups"));

    }

    @Test
    public void testGetFormat() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        String highscoreFormat = workoutSorting.getFormatForHighscore("Bench Press");
        assertTrue(highscoreFormat.contains("\n\tPersonal record:\n\t" + 150 + " kg\n"));
        assertTrue(highscoreFormat.contains("\n\tHighest weight in a set:\n\t" + 1500 + " kg\n"));
        assertTrue(highscoreFormat.contains("\n\tDays since last exercise:\n\t"
                + (int) ChronoUnit.DAYS.between(workout1.getDate(), LocalDate.now()) + "\n"));
        assertTrue(highscoreFormat.contains("\n\tTotal reps ever:\n\t" + 36 + "\n"));
        assertTrue(highscoreFormat.contains("\n\tTotal weight ever:\n\t" + 4560 + " kg"));
    }

    @Test
    public void testDaysSinceExercise() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        assertThrows(IllegalArgumentException.class,
                () -> workoutSorting.daysSinceExercise("Lateral Raise"),
                "Should throw exception since exercise does not exist");

        assertEquals((int) ChronoUnit.DAYS.between(workout1.getDate(), LocalDate.now()),
                workoutSorting.daysSinceExercise("Bench Press"));

        assertEquals((int) ChronoUnit.DAYS.between(workout1.getDate(), LocalDate.now()),
                workoutSorting.daysSinceExercise("Squats"));

        assertThrows(IllegalArgumentException.class,
                () -> workoutSorting.daysSinceExercise(null),
                "Should throw exception since exercise is null");

        assertEquals((int) ChronoUnit.DAYS.between(workout2.getDate(), LocalDate.now()),
                workoutSorting.daysSinceExercise("Curls"));

    }

    @Test
    public void testGetTotalReps() {
        WorkoutSorting workoutSorting = new WorkoutSorting(workouts);
        assertEquals(36, workoutSorting.getTotalReps("Bench Press"));
        assertEquals(38, workoutSorting.getTotalReps("Squats"));
        assertEquals(0, workoutSorting.getTotalReps("Dips"));
    }

    /**
     * Checks if two collections contain each other.
     *
     * @param collection1 the first collection to check
     * @param collection2 the second collection to check
     * @param <T>         the type of elements in the collections
     */
    private <T> void checkContainsEachOther(Collection<T> collection1, Collection<T> collection2) {
        assertTrue(collection1.containsAll(collection2),
                "collection1 should contain all of collection2");
        assertTrue(collection2.containsAll(collection1),
                "collection2 should contain all of collection1");
    }
}
