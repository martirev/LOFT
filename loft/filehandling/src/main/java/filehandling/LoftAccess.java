package filehandling;

import core.User;
import core.Workout;

/**
 * The LoftAccess interface provides methods for registering users, writing
 * workouts to users, retrieving user information, getting user from
 * stored data, and checking if a username already exists.
 */
public interface LoftAccess {
    public void registerUser(User user);

    public void writeWorkoutToUser(Workout workout, User user);

    public User getUser(String username, String password);

    public User getUpdatedUser(User user);

    public boolean usernameExists(String username);
}
