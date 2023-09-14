package core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}