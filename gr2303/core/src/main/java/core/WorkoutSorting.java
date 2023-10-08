package core;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The WorkoutSorting class provides methods to sort and manipulate a list of
 * workouts. It includes methods to sort workouts by date, get a HashMap of all
 * exercises with the same name, get a list of all exercises with the same name,
 * get the weight of the personal record (PR) in the workouts of an exercise
 * with a given name, and search for exercises with a given name.
 */
public class WorkoutSorting {

    private List<Workout> workouts = new ArrayList<Workout>();
    private HashMap<String, List<Exercise>> sameExersices = new HashMap<String, List<Exercise>>();

    /**
     * Constructor for a WorkoutSorting that lets you specify a list of workouts. It
     * also creates a HashMap of all exercises with key being the name of the
     * exercise and value being a list of all exercises with that name.
     *
     * @param workouts a list of workouts to sort
     */
    public WorkoutSorting(List<Workout> workouts) {
        this.workouts = new ArrayList<>(workouts);
        for (Workout workout : workouts) {
            for (Exercise exercise : workout.getExercises()) {
                String name = exercise.getName();
                if (sameExersices.containsKey(name)) {
                    sameExersices.get(name).add(exercise);
                } else {
                    sameExersices.put(name, new ArrayList<Exercise>(Arrays.asList(exercise)));
                }
            }
        }
    }

    /**
     * The method sorts all the workouts by date with a stream and compares them
     * based on the date.
     *
     * @return a list of all workouts sorted by date
     */
    public List<Workout> getMostRecentWorkouts() {
        return workouts.stream()
                .sorted((w1, w2) -> w2.getDate().compareTo(w1.getDate()))
                .collect(Collectors.toList());
    }

    /**
     * The method returns the HashMap created in the constructor.
     *
     * @return A HashMap of all exercises with key being the name of the exercise
     *         and value being a list of all exercises with that name.
     */
    public HashMap<String, List<Exercise>> getSameExersices() {
        return new HashMap<>(sameExersices);
    }

    /**
     * The method returns a list of all exercises with the same name. Uses the
     * HashMap created in the constructor.
     *
     * @param name the name of the exercise to get all exercises with the same name.
     * @return a list of all exercises with the same name.
     */
    public List<Exercise> getSameExersices(String name) {
        if (!sameExersices.containsKey(name)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(sameExersices.get(name));
    }

    /**
     * The method returns a list of all exercises with the same name. Thsi takes an
     * exercise as an argument and uses the getSameExersices(String name) method to
     * get all exercises with the same name.
     *
     * @param exercise the exercise to get all exercises with the same name.
     * @return a list of all exercises with the same name.
     */
    public List<Exercise> getSameExersices(Exercise exercise) {
        return getSameExersices(exercise.getName());
    }

    public int getPrOnDay(Exercise exercise, LocalDate date) {
        int max = 0;
        for(Workout workout : workouts) {
            if(workout.getDate().equals(date)) {
                int val = workout.getExercises().stream().filter(tempExercise -> tempExercise.getName()
                .equals(exercise.getName())).mapToInt(tempExercise -> tempExercise.getLocalPr()).max().orElse(0);
                if(val > max ) {
                    max = val;
                }
            }
        }
        return max;
    }

    
    
          

    /**
     * The method returns the weight of the pr in the workouts of the exercise with
     * the given name. Uses the HashMap created in the constructor.
     *
     * @param name the name of the exercise to get the pr of the workouts of.
     * @return the weight of the pr in the workouts of the exercise with the given
     *         name.
     */
    public int getExercisesPr(String name) {
        return sameExersices.get(name).stream().mapToInt(e -> e.getLocalPr()).max().getAsInt();
    }

    /**
     * The method returns a Collection of workout with the same name.
     *
     * @param name the name of the exercises to search for
     * @return A Collection of workout with the same name
     */
    public Collection<String> searchForExercises(String name) {
        return sameExersices.keySet().stream()
                .filter(n -> n.toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the total weight lifted on a specific day.
     *
     * @param date the date to filter the workouts by
     * @return the total weight lifted on the specified day
     */
    private int getTotalWeightOnDay(LocalDate date) {
        return workouts.stream()
                .filter(w -> w.getDate().equals(date))
                .mapToInt(Workout::getTotalWeight)
                .sum();
    }

    /**
     * Returns a collection of unique dates from the workouts.
     *
     * @return a collection of LocalDate objects representing unique dates from the
     *         workouts.
     */
    private Collection<LocalDate> getUniqueDates() {
        return workouts.stream()
                .map(Workout::getDate)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a map of the total weight lifted per day, where the keys are the
     * unique dates of the workouts and the values are the total weight lifted on
     * each day.
     *
     * @return a map of the total weight lifted per day
     */
    public Map<LocalDate, Integer> getWeightPerDay() {
        return getUniqueDates().stream().map(d -> Map.entry(d, getTotalWeightOnDay(d)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
