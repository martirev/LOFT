package core;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class UserAdapter extends TypeAdapter<User> {

    /**
     * Converts a JSON format to a set class.
     * 
     * @param in JsonReader
     * @return Set class
     * @throws IOException
     */
    private Set setReader(JsonReader in) throws IOException {
        int reps = 0;
        int weight = 0;

        in.beginObject();
        while (in.hasNext()) {
            String setProperty = in.nextName();
            if (setProperty.equals("reps")) {
                reps = in.nextInt();
            } else if (setProperty.equals("weight")) {
                weight = in.nextInt();
            } else {
                in.skipValue();
            }
        }
        in.endObject();

        return new Set(reps, weight);
    }

    /**
     * Converts a JSON format to a exercise class.
     * 
     * @param in JsonReader
     * @return Exercise class
     * @throws IOException
     */
    private Exercise exerciseReader(JsonReader in) throws IOException {
        Exercise exercise = null;

        in.beginObject();
        while (in.hasNext()) {
            String exerciseProperty = in.nextName();
            if (exerciseProperty.equals("exerciseName")) {
                exercise = new Exercise(in.nextString());
            } else if (exerciseProperty.equals("sets")) {
                in.beginArray();
                List<Set> sets = new ArrayList<Set>();
                while (in.hasNext()) {
                    sets.add(setReader(in));
                }
                in.endArray();
                for (Set set : sets) {
                    exercise.addSet(set);
                }
            } else {
                in.skipValue();
            }
        }
        in.endObject();

        return exercise;
    }

    /**
     * Converts a JSON format to a workout class.
     * 
     * @param in JsonReader
     * @return Workout class
     * @throws IOException
     */
    private Workout workoutReader(JsonReader in) throws IOException {
        Workout workout = null;

        in.beginObject();
        while (in.hasNext()) {
            String workoutProperty = in.nextName();
            if (workoutProperty.equals("date")) {
                workout = new Workout(LocalDate.parse(in.nextString()));
            } else if (workoutProperty.equals("exercises")) {
                in.beginArray();
                List<Exercise> exercises = new ArrayList<Exercise>();
                while (in.hasNext()) {
                    exercises.add(exerciseReader(in));
                }
                in.endArray();
                for (Exercise exercise : exercises) {
                    workout.addExercise(exercise);
                }
            } else {
                in.skipValue();
            }
        }
        in.endObject();
        return workout;
    }

    /**
     * Converts a JSON format to a set class.
     * 
     * @param in JsonReader
     * @return Set class
     * @throws IOException
     */
    @Override
    public User read(JsonReader in) throws IOException {
        User user = new User();

        in.beginObject();

        while (in.hasNext()) {
            String name = in.nextName();
            if (name.equals("users")) {
                in.beginArray();
                while (in.hasNext()) {
                    in.beginObject();
                    while (in.hasNext()) {
                        String userProperty = in.nextName();
                        if (userProperty.equals("workouts")) {
                            in.beginArray();
                            List<Workout> workouts = new ArrayList<Workout>();
                            while (in.hasNext()) {
                                workouts.add(workoutReader(in));
                            }
                            in.endArray();
                            for (Workout workout : workouts) {
                                user.addWorkout(workout);
                            }
                        } else {
                            in.skipValue();
                        }
                    }
                    in.endObject();
                }
                in.endArray();
            } else {
                in.skipValue();
            }
        }
        in.endObject();
        return user;
    }

    /**
     * Converts a set class to a JSON format.
     * 
     * @param out  JsonWriter
     * @param sets Set class
     * @throws IOException
     */
    private void setWriter(JsonWriter out, Set sets) throws IOException {
        out.beginObject();
        out.name("reps").value(sets.getReps());
        out.name("weight").value(sets.getWeight());
        out.endObject();
    }

    /**
     * Converts an exercise class to a JSON format.
     * 
     * @param out      JsonWriter
     * @param exercise Exercise class
     * @throws IOException
     */
    private void exerciseWriter(JsonWriter out, Exercise exercise) throws IOException {
        out.beginObject();
        out.name("exerciseName").value(exercise.getName());
        out.name("sets").beginArray();

        List<Set> sets = exercise.getSets();
        for (Set set : sets) {
            setWriter(out, set);
        }

        out.endArray();
        out.endObject();
    }

    /**
     * Converts a workout class to a JSON format.
     * 
     * @param out     JsonWriter
     * @param workout Workout class
     * @throws IOException
     */
    private void workoutWriter(JsonWriter out, Workout workout) throws IOException {
        out.beginObject();
        out.name("date").value(workout.getDate().toString());
        out.name("exercises").beginArray();

        List<Exercise> exercises = workout.getExercises();
        for (Exercise exercise : exercises) {
            exerciseWriter(out, exercise);
        }

        out.endArray();
        out.endObject();
    }

    /**
     * Converts a user class to a JSON format.
     * 
     * @param out   JsonWriter
     * @param value User class
     * @throws IOException
     */
    @Override
    public void write(JsonWriter out, User value) throws IOException {
        out.beginObject();
        out.name("users").beginArray();
        out.beginObject();
        out.name("name").value(value.getName());
        out.name("workouts").beginArray();

        List<Workout> workouts = value.getWorkouts();
        for (Workout workout : workouts) {
            workoutWriter(out, workout);
        }

        out.endArray();
        out.endObject();
        out.endArray();
        out.endObject();
    }
}
