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
import java.util.ArrayList;
import java.util.List;

public class ReadAndWrite {
    
    private static String dummyName = "dummyName";

    //Private read attributes
    private JSONParser parser = new JSONParser();

    private Collection<User> userClasses = new ArrayList<User>();
    private Collection<Workout> workoutsClasses = new ArrayList<Workout>();
    private Collection<Exercise> exercisesClasses = new ArrayList<Exercise>();
    private Collection<Set> setsClasses = new ArrayList<Set>();
    
    //write methods
    private JSONArray setSetsJsonArray(List<Set> list) {
        JSONArray setsArray = new JSONArray();
        int i = 0;
        for (Set set : list) {
            JSONObject setJson = new JSONObject();
            setJson.put("setNumber", ++i);
            setJson.put("weight", set.getWeight());
            setJson.put("reps", set.getReps());
            setsArray.add(setJson);
        }
        return setsArray;
    }

    private JSONArray setExercisesJsonArray(List<Exercise> list) {
        JSONArray exercisesArray = new JSONArray();
        for (Exercise exercise : list) {
            JSONObject exerciseJson = new JSONObject();
            exerciseJson.put("exerciseName", exercise.getName());
            exerciseJson.put("sets", setSetsJsonArray(exercise.getSets()));
            exerciseJson.put("totalWeight", exercise.getTotalWeight());
            exerciseJson.put("localPr", exercise.getLocalPr());
            exercisesArray.add(exerciseJson);
        }
        return exercisesArray;
    }

    private JSONObject formatWorkoutClassToJson(Workout workout) {
        JSONObject workoutJson = new JSONObject();
        workoutJson.put("exercises", setExercisesJsonArray(workout.getExercises()));
        workoutJson.put("totalWeight", workout.getTotalWeight());
        workoutJson.put("date", workout.getDate().toString());
        
        return workoutJson;
    }

    //Writes the workout to the file, the code is made with the assumption that there is only one user,
    //but it can be easily changed to support multiple users.
    public void writeDataToFile(Workout workout) {
        JSONArray exsistingData = readDataFromFile();
        JSONArray users = new JSONArray();
        
        JSONObject user = new JSONObject();

        //Setting up user
        if (exsistingData == null) {
            user.put("name", dummyName);
            user.put("workouts", new JSONArray());
        } else {
            JSONObject userDummy = (JSONObject) exsistingData.get(0);
            user = userDummy;
        }

        //Adding to user
        JSONObject workoutInJSONFormat = this.formatWorkoutClassToJson(workout);
        JSONArray workoutsJsonArray = (JSONArray) user.get("workouts");
        workoutsJsonArray.add(workoutInJSONFormat);

        users.add(user);

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
                    int exerciseLocalPr = ((Long) exercise.get("localPr")).intValue();
                    int exerciseTotalWeight = ((Long) exercise.get("totalWeight")).intValue();

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
            Object obj = parser.parse(reader);
            JSONArray users = (JSONArray) obj;
            return users;
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
       
        readAndWrite.writeDataToFile(workout1);
        readAndWrite.writeDataToFile(workout2);
    //tests for readDataFromFile
        ReadAndWrite readAndWrite2 = new ReadAndWrite();
        User user2 = readAndWrite2.returnUserClassFromFile();
        System.out.println(user2.getName());
        System.out.println(user2.getNumberOfWorkouts());
        System.out.println(user2.getWorkouts().get(0).getTotalWeight());
        System.out.println(user2.getWorkouts().get(0).getDate());
        System.out.println(user2.getWorkouts().get(0).getExercises().get(0).getName());

        System.out.println(user2.getWorkouts().get(1).getTotalWeight());
        System.out.println(user2.getWorkouts().get(1).getDate());
        System.out.println(user2.getWorkouts().get(1).getExercises().get(0).getName());

        System.out.println(user2.getWorkouts().get(0).getExercises().get(0).getSets().get(0).getWeight());
    }
}
