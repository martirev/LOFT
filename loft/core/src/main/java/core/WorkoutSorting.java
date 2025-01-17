package core;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The WorkoutSorting class provides methods to sort and manipulate a list of
 * workouts. It contaions one constructor that lets you specify a list of
 * workouts.
 * <p>
 * The class contains two fields, one list of workouts and a HashMap of all
 * exercises with key being the name of the exercise and value being a list of
 * all exercises with that name.
 * </p>
 * <p>
 * The class provides methods to sort workouts by date, get a HashMap of all
 * exercises with the same name, get a list of all exercises with the same name,
 * get the weight of the personal record (PR) in the workouts of an exercise
 * with a given name, and search for exercises with a given name, and more
 * described below.
 * </p>
 */
public class WorkoutSorting {

    private List<Workout> workouts = new ArrayList<Workout>();
    private HashMap<String, List<Exercise>> sameExercises = new HashMap<String, List<Exercise>>();

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
                if (sameExercises.containsKey(name)) {
                    sameExercises.get(name).add(exercise);
                } else {
                    sameExercises.put(name, new ArrayList<Exercise>(Arrays.asList(exercise)));
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
    public HashMap<String, List<Exercise>> getSameExercises() {
        return new HashMap<>(sameExercises);
    }

    /**
     * The method returns a list of all exercises with the same name. Uses the
     * HashMap created in the constructor.
     *
     * @param name the name of the exercise to get all exercises with the same name.
     * @return a list of all exercises with the same name.
     */
    public List<Exercise> getSameExercises(String name) {
        if (!sameExercises.containsKey(name)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(sameExercises.get(name));
    }

    /**
     * The method returns a list of all exercises with the same name. This takes an
     * exercise as an argument and uses the {@link #getSameExercises(String name)}
     * method to get all exercises with the same name.
     *
     * @param exercise the exercise to get all exercises with the same name.
     * @return a list of all exercises with the same name.
     */
    public List<Exercise> getSameExercises(Exercise exercise) {
        return getSameExercises(exercise.getName());
    }

    /**
     * Returns the personal record (PR) for a specific exercise on a given date.
     * The PR is the highest weight lifted for the type of exercise on the given
     * date.
     *
     * @param exercise the exercise type to get the PR for, only the exercise name
     *                 matters (e.g. "Squat")
     * @param date     the date to get the PR for
     * @return the PR for the exercise on the given date, or 0 if no PR was found
     */
    public int getPrOnDay(Exercise exercise, LocalDate date) {
        int max = 0;
        for (Workout workout : workouts) {
            if (workout.getDate().equals(date)) {
                int val = workout.getExercises().stream()
                        .filter(tempExercise -> tempExercise.getName().equals(exercise.getName()))
                        .mapToInt(Exercise::getLocalPr).max()
                        .orElse(0);
                if (val > max) {
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
        if (!sameExercises.containsKey(name)) {
            return 0;
        }
        return sameExercises.get(name).stream().mapToInt(Exercise::getLocalPr).max().orElse(0);
    }

    /**
     * The method returns a Collection of workout with the same name.
     *
     * @param name the name of the exercises to search for
     * @return A Collection of workout with the same name
     */
    public Collection<String> searchForExercises(String name) {
        return sameExercises.keySet().stream()
                .filter(n -> n.toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the total weight lifted on a specific day.
     *
     * @param date the date to filter the workouts by
     * @return the total weight lifted on the specified day
     */
    public int getTotalWeightOnDay(LocalDate date) {
        return workouts.stream()
                .filter(w -> w.getDate().equals(date))
                .mapToInt(Workout::getTotalWeight)
                .sum();
    }

    /**
     * Returns a list of unique dates from the workouts in chronological order.
     *
     * @return a list of LocalDate objects representing unique dates from the
     *         workouts in ascending order.
     */
    public List<LocalDate> getUniqueDates() {
        return workouts.stream()
                .map(Workout::getDate)
                .distinct()
                .sorted((d1, d2) -> d1.compareTo(d2))
                .collect(Collectors.toList());
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

    /**
     * Returns the heaviest total weight lifted in a set of a specific exercise.
     *
     * @param name the name of the exercise to get the heaviest weight lifted in a
     *             set
     * @return the heaviest weight lifted in a set of the exercise with the given
     *         name.
     */
    public int getHeaviestLiftedSet(String name) {
        if (!sameExercises.containsKey(name)) {
            return 0;
        }
        return sameExercises.get(name).stream()
                .mapToInt(Exercise::getHeaviestLiftedSet)
                .max().orElse(0);
    }

    /**
     * Returns the total weight ever lifted for a specific exercise.
     *
     * @param name the name of the exercise to get the total weight for
     * @return the total weight ever lifted for the exercise with the given name
     */
    public int getTotalWeightEver(String name) {
        if (!sameExercises.containsKey(name)) {
            return 0;
        }
        return sameExercises.get(name).stream()
                .mapToInt(Exercise::getTotalWeight)
                .sum();
    }

    /**
     * Returns a string with the format for the highscore of a specific exercise.
     *
     * @param name the name of the exercise to get the format for
     * @return a string with the format for the highscore of the exercise with the
     */
    public String getFormatForHighscore(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\tPersonal record:\n\t" + getExercisesPr(name) + " kg\n");
        sb.append("\n\tHighest weight in a set:\n\t" + getHeaviestLiftedSet(name) + " kg\n");
        sb.append("\n\tDays since last exercise:\n\t" + daysSinceExercise(name) + "\n");
        sb.append("\n\tTotal reps ever:\n\t" + getTotalReps(name) + "\n");
        sb.append("\n\tTotal weight ever:\n\t" + getTotalWeightEver(name) + " kg");
        return sb.toString();
    }

    /**
     * Returns the number of days since the last exercise with the given name was
     * performed.
     *
     * @param name the name of the exercise to check for
     * @return the number of days since the last exercise with the given name was
     *         performed
     * @throws IllegalArgumentException if no exercise with the given name exists
     */
    public int daysSinceExercise(String name) {
        if (sameExercises.containsKey(name)) {
            for (Workout workout : getMostRecentWorkouts()) {
                for (Exercise exercise : workout.getExercises()) {
                    if (exercise.getName().equals(name)) {
                        LocalDate workoutDate = workout.getDate();
                        LocalDate currentDate = LocalDate.now();
                        return (int) ChronoUnit.DAYS.between(workoutDate, currentDate);
                    }
                }
            }
        }
        throw new IllegalArgumentException("No exercise with the given name was found");
    }

    /**
     * Returns the total number of reps performed for a specific exercise.
     *
     * @param name the name of the exercise to filter the workouts by
     * @return the total number of reps performed for the specified exercise
     */
    public int getTotalReps(String name) {
        if (!sameExercises.containsKey(name)) {
            return 0;
        }
        return sameExercises.get(name).stream()
                .mapToInt(e -> e.getSets().stream().mapToInt(Set::getReps).sum())
                .sum();
    }

    /**
     * Returns a list of unique exercises from the most recent workouts.
     *
     * @return a list of Exercise objects representing unique exercises
     */
    public List<Exercise> getAllUniqueExerciseNames() {
        return getMostRecentWorkouts().stream()
                .flatMap(workout -> workout.getExercises().stream())
                .map(exercise -> exercise.getName())
                .distinct()
                .map(name -> new Exercise(name))
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of all exercises sorted by PR.
     *
     * @return a list of all exercises sorted by PR
     */
    public List<Exercise> getAllUniqueExerciseNamesSortedByPr() {
        return getAllUniqueExerciseNames().stream()
                .sorted((e1, e2) -> getExercisesPr(e2.getName()) - getExercisesPr(e1.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the total weight lifted in all workouts.
     *
     * @return the total weight lifted in all workouts
     */
    public int getTotalWeightLifted() {
        return getWeightPerDay().values().stream().mapToInt(Integer::intValue).sum();

    }
}
