package core;

//imports
import java.util.ArrayList;

public class User {

    //Fields
    private String name;
    private ArrayList<Workout> workouts = new ArrayList<Workout>();

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

    public ArrayList<Workout> getWorkouts() {
        return workouts;
    }

    //Logic
    public void addWorkout(Workout workout) {
        workouts.add(workout);
    }

}
