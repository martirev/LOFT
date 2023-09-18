package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class WorkoutSorting {

    private List<Workout> workouts = new ArrayList<Workout>();
    private HashMap<String, List<Exercise>> sameExersices = new HashMap<String, List<Exercise>>();

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

    public List<Workout> getMostRecentWorkouts() {
        return workouts.stream()
                .sorted((w1, w2) -> w2.getDate().compareTo(w1.getDate()))
                .collect(Collectors.toList());
    }

    public HashMap<String, List<Exercise>> getSameExersices() {
        return new HashMap<>(sameExersices);
    }

    public List<Exercise> getSameExersices(String name) {
        if (!sameExersices.containsKey(name)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(sameExersices.get(name));
    }

    public List<Exercise> getSameExersices(Exercise exercise) {
        return getSameExersices(exercise.getName());
    }

    public int getExercisesPr(String name) {
        return sameExersices.get(name).stream().mapToInt(e -> e.getLocalPr()).max().getAsInt();
    }

    public Collection<String> searchForExercises(String name) {
        return sameExersices.keySet().stream()
                .filter(n -> n.toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}
