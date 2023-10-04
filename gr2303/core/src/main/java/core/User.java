package core;

import java.util.ArrayList;

/**
 * The User class represents a user of the application. For the first version of
 * the application we will only have one user. This is why we have a dummy name
 * for the user. The idea is to have multiple users in the future.
 */
public class User {
    private final String name;
    private final String username;
    private final String password;
    private final String email;
    private ArrayList<Workout> workouts;

    /**
     * Constructs a new User object with the given name, username, password and
     * email.
     *
     * @param name     the name of the user
     * @param username the username of the user
     * @param password the password of the user
     * @param email    the email of the user
     */
    public User(String name, String username, String password, String email) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        workouts = new ArrayList<Workout>();
    }

    /**
     * Returns the name of the user.
     *
     * @return the user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the username of the user.
     *
     * @return the user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of the user.
     *
     * @return the user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the email of the user.
     *
     * @return the user's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the number of workouts the user has.
     *
     * @return number of workouts as int
     */
    public int getNumberOfWorkouts() {
        return workouts.size();
    }

    /**
     * Returns the workouts of the user.
     *
     * @return workouts in an ArrayList
     */
    public ArrayList<Workout> getWorkouts() {
        return new ArrayList<>(workouts);
    }

    /**
     * Adds a workout to the user.
     *
     * @param workout Workout class
     */
    public void addWorkout(Workout workout) {
        workouts.add(workout);
    }
}
