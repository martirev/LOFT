package core;

//imports
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class ReadAndWrite {
    
    //Dummy name for the user, the implementation of the user class is made with the assumption that there is only one user.
    private static String dummyName = "dummyName";

    //Private read attributes
    private JSONParser parser = new JSONParser();
    private Collection<User> userClasses = new ArrayList<User>();
    private Collection<Workout> workoutsClasses = new ArrayList<Workout>();
    private Collection<Exercise> exercisesClasses = new ArrayList<Exercise>();
    private Collection<Set> setsClasses = new ArrayList<Set>();
    
    //Private write methods
    private ArrayList<HashMap<String, Object>> setSetsJsonArray(List<Set> list) {
        ArrayList<HashMap<String, Object>> setsArray = new ArrayList<HashMap<String, Object>>();
        int i = 0;
        for (Set set : list) {
            HashMap<String, Object> setJson = new HashMap<String, Object>();
            setJson.put("setNumber", ++i);
            setJson.put("weight", set.getWeight());
            setJson.put("reps", set.getReps());
            setsArray.add(setJson);
        }
        return setsArray;
    }
    private ArrayList<HashMap<String, Object>> setExercisesJsonArray(List<Exercise> list) {
        ArrayList<HashMap<String, Object>>  exercisesArray = new ArrayList<HashMap<String, Object>>();
        for (Exercise exercise : list) {
            HashMap<String, Object> exerciseJson = new HashMap<String, Object>();
            exerciseJson.put("exerciseName", exercise.getName());
            exerciseJson.put("sets", setSetsJsonArray(exercise.getSets()));
            exercisesArray.add(exerciseJson);
        }
        return exercisesArray;
    }
    private HashMap<String, Object> formatWorkoutClassToJson(Workout workout) {
        HashMap<String, Object> workoutJson = new HashMap<String, Object>();
        workoutJson.put("exercises", setExercisesJsonArray(workout.getExercises()));
        workoutJson.put("date", workout.getDate().toString());
        
        return workoutJson;
    }

    //Writing a workout class to the userData file in json format
    public void writeWorkoutToUser(Workout workout) {
        JSONArray existingData = readDataFromFile();
        JSONArray users = new JSONArray();
        
        JSONObject user = new JSONObject();
    
        // Setting up user
        if (existingData == null) {
            user.put("name", dummyName);
            user.put("workouts", new JSONArray());
        } else {
            JSONObject userDummy = (JSONObject) existingData.get(0);
            user = userDummy;
        }
    
        // Adding to user
        HashMap<String, Object> workoutInJSONFormat = this.formatWorkoutClassToJson(workout);
        JSONArray workoutsJsonArray = (JSONArray) user.get("workouts");
        workoutsJsonArray.add(workoutInJSONFormat);
    
        users.add(user); 
    /*  I am not sure how to remove these notifications. However the type safety, the way i see it, should be ok.
     *  The type safty of the casts should be guarranteed in the way we are using the type Object.
    */
    
        try (FileWriter file = new FileWriter("gr2303/src/main/java/core/userData/userData.json")) {    
            file.write(users.toJSONString()); 
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //private read methods
    private ArrayList<User> classReconstructor(JSONArray users) {
        for (Object userObject : users) {
            JSONObject user = (JSONObject) userObject;
            String name = (String) user.get("name");
            
            JSONArray workouts = (JSONArray) user.get("workouts");
            for (Object workoutObject : workouts) {
                JSONObject workout = (JSONObject) workoutObject;
                LocalDate date = LocalDate.parse((String) workout.get("date"));

                JSONArray exercises = (JSONArray) workout.get("exercises");
                for (Object exerciseObject : exercises) {
                    JSONObject exercise = (JSONObject) exerciseObject;
                    String exerciseName = (String) exercise.get("exerciseName");

                    JSONArray sets = (JSONArray) exercise.get("sets");
                    for (Object setObject : sets) {
                        JSONObject set = (JSONObject) setObject;
                        int weight = ((Long) set.get("weight")).intValue();
                        int reps = ((Long) set.get("reps")).intValue();

                        Set newSet = new Set(weight, reps);
                        setsClasses.add(newSet);
                    }
                    Exercise newExercise = new Exercise(exerciseName);
                    for (Set set : setsClasses) {
                        newExercise.addSet(set);
                    }
                    exercisesClasses.add(newExercise);
                }
                Workout newWorkout = new Workout(date);
                for (Exercise exercise : exercisesClasses) {
                    newWorkout.addExercise(exercise);
                }
                workoutsClasses.add(newWorkout);
            }
            User newUser = new User(name);
            for (Workout workout : workoutsClasses) {
                newUser.addWorkout(workout);
            }
            userClasses.add(newUser);
        }
        return (ArrayList<User>) userClasses;
    }

    private JSONArray readDataFromFile() {
        try (FileReader reader = new FileReader("gr2303/src/main/java/core/userData/userData.json")){
            if (reader.ready()) {
                Object obj = parser.parse(reader);
                JSONArray users = (JSONArray) obj;
                return users;
            } else {
                //The file can be empty this is not an error, it just means that there is no data to read.
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    //Returns the user class from the file
    public User returnUserClassFromFile() {
        JSONArray users = readDataFromFile();
        ArrayList<User> user = this.classReconstructor(users);
        User userClass = user.get(0);
        return userClass;
    }

    //quick test
   public static void main(String[] args) {
    //tests for writeDataToFile
        ReadAndWrite readAndWrite = new ReadAndWrite();
        Workout workout1 = new Workout();
        Exercise exercise1 = new Exercise("Bench Press");
        Set set = new Set(100, 10);
        Workout workout2 = new Workout();
        Exercise exercise2 = new Exercise("Squat");
        Set set2 = new Set(200, 10);

        exercise1.addSet(set);
        workout1.addExercise(exercise1);
        exercise2.addSet(set2);
        workout2.addExercise(exercise2);
       
        readAndWrite.writeWorkoutToUser(workout1);
        readAndWrite.writeWorkoutToUser(workout2);
    //tests for readDataFromFile
        ReadAndWrite readAndWrite2 = new ReadAndWrite();
        User user2 = readAndWrite2.returnUserClassFromFile();
        System.out.println(user2.getName());
        System.out.println(user2.getNumberOfWorkouts());
        System.out.println(user2.getWorkouts().get(0).getDate());
        System.out.println(user2.getWorkouts().get(0).getExercises().get(0).getName());

        System.out.println(user2.getWorkouts().get(1).getDate());
        System.out.println(user2.getWorkouts().get(1).getExercises().get(0).getName());

        System.out.println(user2.getWorkouts().get(0).getExercises().get(0).getSets().get(0).getWeight());
    }
}
