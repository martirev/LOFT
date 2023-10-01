package core;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Workout class represents a workout consisting of a list of exercises and
 * a date.
 */
public class Workout {

    private List<Exercise> exercises = new ArrayList<Exercise>();
    private String date;

    /**
     * Constructor for a workout that sets the date to today.
     */
    public Workout() {
        this.date = LocalDate.now().toString();
    }

    /**
     * Constructor for a workout that lets you specify the date.
     *
     * @param date the date of the workout
     */
    public Workout(LocalDate date) {
        this.date = date.toString();
    }

    /**
     * Adds an exercise to the workout.
     *
     * @param exercise the exercise to add to the workout
     */
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    /**
     * A method to get the all exercises in the workout.
     *
     * @return A list of exercises in the workout
     */
    public List<Exercise> getExercises() {
        return new ArrayList<Exercise>(exercises);
    }

    /**
     * A method to get the total weight of all exercises in the workout.
     *
     * @return the total weight of all exercises in the workout
     */
    public int getTotalWeight() {
        return exercises.stream().mapToInt(Exercise::getTotalWeight).sum();
    }

    /**
     * A method to get the date of the workout.
     *
     * @return the date of the workout
     */
    public LocalDate getDate() {
        return LocalDate.parse(date);
    }

    /**
     * Method for comparing two workouts.
     *
     * @param obj the object to compare to
     * @return boolean true if the workouts are equal, false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Workout other = (Workout) obj;
        if (exercises == null) {
            if (other.getExercises() != null) {
                return false;
            }
        } else if (!exercises.equals(other.getExercises())) {
            return false;
        }
        if (getDate() == null) {
            if (other.getDate() != null) {
                return false;
            }
        } else if (!getDate().equals(other.getDate())) {
            return false;
        }
        return true;
    }
}
