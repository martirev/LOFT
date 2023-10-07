package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class contains unit tests for the User class.
 */
public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("Ola Nordmann", "test", "test", "test@example.com");
    }

    @Test
    public void testGetNumberOfWorkouts() {
        assertEquals(0, user.getNumberOfWorkouts());

        user.addWorkout(new Workout());
        assertEquals(1, user.getNumberOfWorkouts());

        Workout workout = new Workout();
        workout.addExercise(new Exercise("test", new Set(10, 20)));
        user.addWorkout(workout);
        assertEquals(2, user.getNumberOfWorkouts());
    }

    @Test
    public void testPasswordHash() {
        assertTrue(user.getPasswordHash()
                .equals("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"));
    }

    @Test
    public void testHash() {
        assertTrue(User.hash(user.getEmail())
                .equals("973dfe463ec85785f5f95af5ba3906eedb2d931c24e69824a89ea65dba4e813b"));
        assertTrue(User.hash(user.getName())
                .equals("54d055865b74ba9b9026315bc5964bce09a83d51f6da3e9715e710bef648997e"));
    }

}
