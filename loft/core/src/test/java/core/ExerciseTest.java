package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class contains unit tests for the Exercise class.
 */
public class ExerciseTest {

    private Exercise exercise;

    @BeforeEach
    public void setUp() {
        exercise = new Exercise("Bench Press");
    }

    @Test
    public void testAddSet() {
        Set set1 = new Set(8, 100);
        Set set2 = new Set(6, 100);
        Set set3 = new Set(6, 100);

        exercise.addSet(set1);
        exercise.addSet(set2);
        exercise.addSet(set3);

        assertEquals(3, exercise.getSets().size(), "Should be 3 sets");
    }

    @Test
    public void testSetIllegalName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Exercise(null),
                "Should not be possible to set name to null");
        assertThrows(IllegalArgumentException.class,
                () -> new Exercise(""),
                "Should not be possible to set name to empty string");
    }

    @Test
    public void testGetName() {
        assertTrue(exercise.getName().equals("Bench Press"),
                "Exercise name should be Bench Press");
        assertTrue(exercise.toString().equals("Bench Press"),
                "Exercise name should be Bench Press");
    }

    @Test
    public void testFormatExerciseName() {
        Exercise exercise = new Exercise("bench press");
        assertTrue(exercise.getName().equals("Bench Press"),
                "Exercise name should be Bench Press");
        Exercise lateralRaises = new Exercise("Lateral      raises     ");
        assertTrue(lateralRaises.getName().equals("Lateral Raises"),
                "Exercise name should be Lateral Raises");
        Exercise dumbellCurls = new Exercise("duMbell c-uRls?");
        assertTrue(dumbellCurls.getName().equals("Dumbell C-urls?"),
                "Exercise name should be Dumbell C-urls?");
    }

    @Test
    public void testGetTotalWeight() {
        Set set1 = new Set(8, 100);
        Set set2 = new Set(6, 100);
        Set set3 = new Set(6, 100);

        exercise.addSet(set1);
        exercise.addSet(set2);
        exercise.addSet(set3);

        assertEquals(2000, exercise.getTotalWeight(), "Total weight should be 2000");
    }

    @Test
    public void testListPrivacy() {
        Set set1 = new Set(8, 100);
        Set set2 = new Set(6, 100);
        Set set3 = new Set(6, 100);

        exercise.addSet(set1);
        exercise.addSet(set2);
        exercise.addSet(set3);

        assertEquals(3, exercise.getSets().size(), "Should be 3 sets");
        exercise.getSets().clear();
        assertEquals(3, exercise.getSets().size(), "getSets() should not reveal the actual list");
    }

    @Test
    public void testGetLocalPr() {
        Set set1 = new Set(8, 100);
        Set set2 = new Set(6, 90);
        Set set3 = new Set(6, 70);

        exercise.addSet(set1);
        exercise.addSet(set2);
        exercise.addSet(set3);

        assertEquals(100, exercise.getLocalPr(), "Local PR should be 100");
    }

    @Test
    public void testConstructor() {
        Exercise exercise = new Exercise("Bench Press");
        assertTrue(exercise.getName().equals("Bench Press"), "Exercise name should be Bench Press");

        assertThrows(IllegalArgumentException.class, () -> new Exercise(null),
                "Should not be possible to create an exercise with null name");
    }

    @Test
    public void testConstructorWithSets() {
        Set set1 = new Set(8, 100);
        Set set2 = new Set(6, 100);
        Set set3 = new Set(6, 100);

        Exercise exercise = new Exercise("Bench Press", set1, set2, set3);

        assertEquals(3, exercise.getSets().size(), "Should be 3 sets");
    }

    @Test
    public void testHashCode() {
        Exercise exercise1 = new Exercise("Bench Press");
        assertEquals(exercise1.hashCode(), exercise.hashCode());

        Exercise exercise2 = new Exercise("Squats");
        assertNotEquals(exercise2.hashCode(), exercise1.hashCode());

        Exercise exercise3 = new Exercise("Bench Press");
        assertEquals(exercise1.hashCode(), exercise3.hashCode());

        exercise3.addSet(new Set(8, 100));
        assertNotEquals(exercise1.hashCode(), exercise3.hashCode());

        exercise1.addSet(new Set(8, 100));
        assertEquals(exercise1.hashCode(), exercise3.hashCode());
    }

    @Test
    public void testEquals() {
        Exercise exercise1 = new Exercise("Bench Press");
        assertEquals(exercise1, exercise1);

        assertNotEquals(exercise1, null);
        assertNotEquals(exercise1, new String());

        Exercise exercise2 = new Exercise("Squats");
        assertNotEquals(exercise1, exercise2);

        Exercise exercise3 = new Exercise("Bench Press");
        assertEquals(exercise1, exercise3);

        exercise3.addSet(new Set(8, 100));
        assertNotEquals(exercise1, exercise3);
    }
}
