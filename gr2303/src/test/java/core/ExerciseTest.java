package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        assertEquals(3, exercise.getSets().size());
    }

    @Test
    public void testGetName() {
        assertTrue(exercise.getName().equals("Bench Press"));
    }

    @Test
    public void testGetTotalWeight() {
        Set set1 = new Set(8, 100);
        Set set2 = new Set(6, 100);
        Set set3 = new Set(6, 100);

        exercise.addSet(set1);
        exercise.addSet(set2);
        exercise.addSet(set3);

        assertEquals(2000, exercise.getTotalWeight());
    }

    @Test
    public void testListPrivacy() {
        Set set1 = new Set(8, 100);
        Set set2 = new Set(6, 100);
        Set set3 = new Set(6, 100);

        exercise.addSet(set1);
        exercise.addSet(set2);
        exercise.addSet(set3);

        assertEquals(3, exercise.getSets().size());
        exercise.getSets().clear();
        assertEquals(3, exercise.getSets().size());
    }

    @Test
    public void testGetLocalPr() {
        Set set1 = new Set(8, 100);
        Set set2 = new Set(6, 90);
        Set set3 = new Set(6, 70);

        exercise.addSet(set1);
        exercise.addSet(set2);
        exercise.addSet(set3);

        assertEquals(100, exercise.getLocalPr());
    }
}
