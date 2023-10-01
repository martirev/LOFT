package core;

import java.util.ArrayList;

/**
 * The User class represents a user of the application. For the first version of
 * the application we will only have one user. This is why we have a dummy name
 * for the user. The idea is to have multiple users in the future.
 */
public class User {
    private String name = "dummyName";
    private ArrayList<Workout> workouts = new ArrayList<Workout>();

    /**
     * Returns the name of the user.
     *
     * @return name in a String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the number of workouts the user has.
     *
     * @return number of workouts in int
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
