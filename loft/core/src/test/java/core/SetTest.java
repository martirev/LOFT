package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class contains unit tests for the Set class.
 */
public class SetTest {

    private Set set;

    @BeforeEach
    public void setUp() {
        set = new Set(10, 70);
    }

    @Test
    public void testGetReps() {
        assertEquals(10, set.getReps());
    }

    @Test
    public void testGetWeight() {
        assertEquals(70, set.getWeight());
    }

    @Test
    public void testHashCode() {
        Set set1 = new Set(10, 70);
        assertEquals(set1.hashCode(), set.hashCode());

        Set set2 = new Set(10, 7);
        assertNotEquals(set2.hashCode(), set1.hashCode());
        Set set3 = new Set(1, 70);
        assertNotEquals(set3.hashCode(), set1.hashCode());
    }

    @Test
    public void testEquals() {
        Set set1 = new Set(10, 70);
        assertEquals(set1, set1);

        assertNotEquals(set1, null);
        assertNotEquals(set1, new String());

        Set set2 = new Set(10, 7);
        assertNotEquals(set1, set2);
        Set set3 = new Set(1, 70);
        assertNotEquals(set1, set3);

        Set set4 = new Set(10, 70);
        assertEquals(set1, set4);
    }
}
