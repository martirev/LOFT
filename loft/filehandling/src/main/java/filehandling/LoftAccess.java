package filehandling;

import core.User;
import core.Workout;

/**
 * The LoftAccess interface provides methods for registering users, writing
 * workouts to users, retrieving user information, getting user from
 * stored data, and checking if a username already exists.
 */
public interface LoftAccess {
    /**
     * Registers a new user by adding it to the list of existing users and writing
     * the updated list to a file.
     *
     * @param user the user to be registered
     * @return true if the user was successfully registered, false otherwise
     */
    public boolean registerUser(User user);

    /**
     * The method to be used for writing a workout class to the userData file in
     * json format.
     *
     * @param workout The workout to add to the current user
     * @return true if the workout was successfully added, false otherwise
     */
    public boolean writeWorkoutToUser(Workout workout, User user);

    /**
     * Returns a User object if the given username and password match an existing
     * user in the system.
     *
     * @param username the username of the user to retrieve
     * @param password the password of the user to retrieve
     * @return a User object if the given username and password match an existing
     *         user in the system, null otherwise
     */
    public User getUser(String username, String password);

    /**
     * Checks if a username already exists.
     *
     * @param username the username to check for existence
     * @return true if the username exists, false otherwise
     */
    public boolean usernameExists(String username);

    /**
     * Updates the info about the user and overwrites the old info in the
     * userData.json file.
     * 
     * <p>
     * Note: Only the username and password of oldUser is of interest, and the new
     * user does not need to include workouts.
     * </p>
     *
     * @param oldUser the old user
     * @param newUser the updated user
     * @return true if the user was successfully updated, false otherwise
     */
    public boolean updateUserInfo(User oldUser, User newUser);
}
