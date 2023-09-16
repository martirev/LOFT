package core;

//imports
import java.util.*;


public class User {

    //Fields
    private String name;
    private Collection<Workout> workouts;

    //Constructor
    public User(String name) {
        this.name = name;
        this.workouts = new ArrayList<Workout>();
    }

    //Getters
    public String getName() {
        return name;
    }

    public int getNumberOfWorkouts() {
        return workouts.size();
    }

    public Collection<Workout> getWorkouts() {
        return workouts;
    }

    //Logic
    public void addWorkout(Workout workout) {
        workouts.add(workout);
    }

}
