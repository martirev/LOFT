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
import java.util.*;

public class ReadAndWrite {

    //write attributes
    private JSONObject user = new JSONObject();
    private JSONObject workoutJson = new JSONObject();
    private JSONObject exercisesJson = new JSONObject();
    private JSONObject setsJson = new JSONObject();

    private JSONArray users = new JSONArray();
    private JSONArray setsJsonArray = new JSONArray();
    private JSONArray exercisesJsonArray = new JSONArray();
    private JSONArray workoutsJsonArray = new JSONArray();
    
    private String dummyName = "dummyName";

    //read attributes
    private JSONParser parser = new JSONParser();
    
    //write methods
    private void setSetsJsonArray(List<Set> list) {
        int i = 0;
        for (Set set : list) {
            setsJson.put("setNumber", i++);
            setsJson.put("weight", set.getWeight());
            setsJson.put("reps", set.getReps());
            setsJsonArray.add(setsJson);
        }
    }

    private void setExercisesJsonArray(List<Exercise> list) {
        for (Exercise exercise : list) {
            exercisesJson.put("exerciseName", exercise.getName());
            exercisesJson.put("sets", setsJsonArray);
            exercisesJson.put("totalWeight", exercise.getTotalWeight());
            exercisesJson.put("localPr", exercise.getLocalPr());
            exercisesJsonArray.add(exercisesJson);
        }
    }

    private void setWorkoutsJsonArray(Workout... workouts) {
        int i = 0;
        for (Workout workout : workouts) {
            workoutJson.put("workoutNumber", i++);
            workoutJson.put("exercises", exercisesJsonArray);
            workoutJson.put("totalWeight", workout.getTotalWeight());
            workoutJson.put("date", workout.getDate().toString());
            workoutsJsonArray.add(workoutJson);
        }
    }

    //read methods
    private Object parseUsersObject(JSONObject user) {
        JSONArray workoutObject = (JSONArray) user.get("workouts");
        JSONArray exerciseObject = (JSONArray) workoutObject.get("exercises");
        JSONArray setObject = (JSONArray) exerciseObject.get("sets");

        Set set = new Set((int) setObject.get("weight"), (int) setObject.get("reps"));
        
        Exercise exercise = new Exercise((String) exerciseObject.get("exerciseName"));
        exercise.addSet(set);

        Workout workout = new Workout();
        workout.addExercise(exercise);

        User userToGet = new User((String) user.get("name"));
        userToGet.addWorkout(workout);

        return userToGet;
    }

    //write method to use
    public void writeDataToFile(Workout... workouts) {
        user.put("name", dummyName);
        for (Workout workout : workouts) {
            for (Exercise exercise : workout.getExercises()) {
                setSetsJsonArray(exercise.getSets());
            }
            setExercisesJsonArray(workout.getExercises());
            setWorkoutsJsonArray(workout);   
        }
        user.put("workouts", workoutsJsonArray);
        users.add(user);
        try (FileWriter file = new FileWriter("gr2303/src/main/java/core/userData/userData.json")) {    
            file.write(users.toJSONString()); 
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //read method to use
    public Object readDataFromFile(String name) {
        try (FileReader reader = new FileReader("gr2303/src/main/java/core/userData/userData.json")) {
            
            Object obj = parser.parse(reader);
            JSONArray users = (JSONArray) obj;

            for (Object userObj : users) {
                JSONObject user = (JSONObject) userObj;

                if (user.get("name").equals(name)) {
                    return parseUsersObject(user);
                }
            }
        }
        catch (FileNotFoundException e) {
                e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    //quick test
   public static void main(String[] args) {
    //tests for writeDataToFile
    /*
        ReadAndWrite readAndWrite = new ReadAndWrite();
        Workout workout = new Workout();
        Exercise exercise = new Exercise("Bench Press");
        Set set = new Set(100, 10);

        exercise.addSet(set);
        workout.addExercise(exercise);
       
        readAndWrite.writeDataToFile(workout);
     */
    //tests for readDataFromFile
        ReadAndWrite readAndWrite = new ReadAndWrite();
        User user = (User) readAndWrite.readDataFromFile("dummyName");
        System.out.println(user.getName());
        System.out.println(user.getNumberOfWorkouts());
        System.out.println(user.getWorkouts());
    }
}
