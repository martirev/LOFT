package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void testConstructorThrow() {
        assertThrows(IllegalArgumentException.class, () -> new User(null, "test", "test", "test"),
                "Name cannot be null");
        assertThrows(IllegalArgumentException.class, () -> new User("test", null, "test", "test"),
                "Username cannot be null");
        assertThrows(IllegalArgumentException.class, () -> new User("test", "test", null, "test"),
                "Password cannot be null");
        assertThrows(IllegalArgumentException.class, () -> new User("test", "test", "test", null),
                "Email cannot be null");
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
    public void testGetters() {
        assertTrue(user.getName().equals("Ola Nordmann"), "Name should be Ola Nordmann");
        assertTrue(user.getUsername().equals("test"), "Username should be test");
        assertTrue(user.getEmail().equals("test@example.com"), "Email should be test@example.com");
        assertTrue(user.getPassword().equals("test"), "Password should be test");
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

    @Test
    public void testSetPassword() {
        assertTrue(user.getPassword().equals("test"), "Password should be test");
        assertTrue(user.getPasswordHash()
                .equals("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"));

        user.setPassword("hunter2");
        assertTrue(user.getPassword().equals("hunter2"), "Password should be hunter2");
        assertTrue(user.getPasswordHash()
                .equals("f52fbd32b2b3b86ff88ef6c490628285f482af15ddcb29541f94bcf526a3f6c7"));
    }

    @Test
    public void testGetWorkouts() {
        Workout workout1 = new Workout();
        Workout workout2 = new Workout();
        Workout workout3 = new Workout();

        user.addWorkout(workout1);
        user.addWorkout(workout2);
        user.addWorkout(workout3);

        assertEquals(3, user.getWorkouts().size());

        user.getWorkouts().clear();
        assertEquals(3, user.getWorkouts().size(),
                "getWorkouts() should not reveal the actual list");
    }

    @Test
    public void testHashCode() {
        assertEquals(user.hashCode(), user.hashCode(), "Hashcode should be the same for same user");

        User user2 = new User("test", "test1", "test", "test");
        assertNotEquals(user.hashCode(), user2.hashCode(),
                "Hashcode should not be the same for two users with same "
                        + "passwordhash but different username");

        User user3 = new User("test", "test", "test1", "test");
        assertNotEquals(user.hashCode(), user3.hashCode(),
                "Hashcode should not be the same for two users with same "
                        + "username but different passwordhash");

        User user4 = new User("test", "test", "test", "test");
        assertEquals(user.hashCode(), user4.hashCode(),
                "Hashcode should be the same for two users with same "
                        + "username and passwordhash");
    }

    @Test
    public void testEquals() {
        assertEquals(user, user, "User should be equal to itself");

        assertNotEquals(user, null);
        assertNotEquals(user, new String());

        User user2 = new User("Ola Nordmann", "test1", "test", "test@example.com");
        assertNotEquals(user, user2, "Users should not be equal");

        User user3 = new User("Ola Nordmann", "test", "test1", "test@example.com");
        assertNotEquals(user, user3, "Users should not be equal");

        User user4 = new User("Kari Nordmann", "test", "test", "example@mail.com");
        assertEquals(user, user4,
                "Users should be equal as we only care about username "
                        + "and passwordhash to check for equal users");
    }

}
