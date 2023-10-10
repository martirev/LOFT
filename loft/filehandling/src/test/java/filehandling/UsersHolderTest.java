package filehandling;

import static org.junit.jupiter.api.Assertions.assertTrue;

import core.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class contains unit tests for the UsersHolder class.
 */
public class UsersHolderTest {

    private List<User> users;

    /**
     * Sets up the users for the test.
     */
    @BeforeEach
    public void setUp() {
        users = new ArrayList<User>();
        users.add(new User("Test1", "test1", "hunter1", "test1@test1.com"));
        users.add(new User("Test2", "test2", "hunter2", "test2@test2.com"));
    }

    @Test
    public void testConstructorAndGet() {
        UsersHolder usersHolder = new UsersHolder(users);
        assertTrue(users.containsAll(usersHolder.getUsers()));
        assertTrue(usersHolder.getUsers().containsAll(users));
    }

    @Test
    public void testConstructorPrivacy() {
        List<User> usersCopy = new ArrayList<User>(users);
        UsersHolder usersHolder = new UsersHolder(usersCopy);

        usersCopy.clear();
        assertTrue(users.containsAll(usersHolder.getUsers()));
        assertTrue(usersHolder.getUsers().containsAll(users));
    }

    @Test
    public void testGetterPrivacy() {
        UsersHolder usersHolder = new UsersHolder(users);

        usersHolder.getUsers().clear();
        assertTrue(users.containsAll(usersHolder.getUsers()));
        assertTrue(usersHolder.getUsers().containsAll(users));
    }
}
