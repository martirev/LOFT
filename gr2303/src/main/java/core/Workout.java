package core;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Workout {

    private List<Exercise> exercises = new ArrayList<Exercise>();
    private int totalWeight = 0;
    private LocalDate date;


    public Workout() {
        this.date = LocalDate.now();
    }

    public Workout(LocalDate date) {
        this.date = date;
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
        totalWeight += exercise.getTotalWeight();
    }


    public List<Exercise> getExercises() {
        return new ArrayList<Exercise>(exercises);
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public LocalDate getDate() {
        return date;
    }

    

}
