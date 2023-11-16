package core;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Workout class represents a workout inside a user. It contains two constructors;
 * one that sets the date to today and one that lets you specify the date.
 * <p>
 * The class contains the fields, a list of exercises and a date. The class provides methods to add
 * exercises to the workout, getters for the exercises, the total weight of all exercises
 * in the workout, the date of the workout, the total number of sets in the workout and
 * there are a toString method that returns the name of the exercise. Also there are an
 * equals method to check if two workouts are equal and a methods that returns the workouts
 * hashcode which is used in the equals method.
 * </p>
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
     * @throws IllegalArgumentException if the date is null
     */
    public Workout(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
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
     * A method to get the hashcode of the workout.
     *
     * @return the hashcode of the workout
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + exercises.hashCode();
        result = prime * result + date.hashCode();
        return result;
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
        if (!exercises.equals(other.getExercises())) {
            return false;
        }
        if (!getDate().equals(other.getDate())) {
            return false;
        }
        return true;
    }

    /**
     * Returns the total number of sets in the workout.
     *
     * @return the total number of sets in the workout
     */
    private int getTotalSets() {
        int totalSets = 0;
        for (Exercise exercise : exercises) {
            totalSets += exercise.getSets().size();
        }
        return totalSets;
    }

    @Override
    public String toString() {
        return getDate().toString()
                + " Number of exercises: "
                + getExercises().size()
                + " Number of sets: "
                + getTotalSets();
    }
}
