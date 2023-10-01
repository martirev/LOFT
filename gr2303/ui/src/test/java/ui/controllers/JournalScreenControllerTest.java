package ui.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import core.Exercise;
import core.ReadAndWrite;
import core.Workout;
import core.Set;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.App;

public class JournalScreenControllerTest extends ApplicationTest {

    private JournalScreenController controller;

    private static String testFileLocation = System.getProperty("user.home") + System.getProperty("file.separator")
            + "testUserData.json";

    private Parent root;

    private static Workout workout1;
    private static Workout workout2;

    @Override
    public void start(Stage stage) throws IOException {
        SceneSwitcher.setFileLocation(testFileLocation);

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("JournalScreen.fxml"));
        controller = new JournalScreenController(testFileLocation);
        fxmlLoader.setController(controller);
        root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @AfterAll
    public static void tearDown() {
        deleteTestfile();
    }

    @BeforeAll
    public static void setUp() {
        deleteTestfile();

        workout1 = new Workout(LocalDate.of(2023, 10, 1));
        workout2 = new Workout(LocalDate.of(2019, 1, 1));
        Exercise exercise1 = new Exercise("Bench Press");
        Exercise exercise2 = new Exercise("Squats");
        Exercise exercise3 = new Exercise("Shoulder Press");
        Exercise exercise4 = new Exercise("Lateral Raises");

        Set benchSet1 = new Set(10, 150);
        Set benchSet2 = new Set(8, 130);
        Set benchSet3 = new Set(6, 110);
        Set benchSet4 = new Set(4, 90);

        Set squatSet1 = new Set(10, 200);
        Set squatSet2 = new Set(8, 180);
        Set squatSet3 = new Set(6, 160);
        Set squatSet4 = new Set(4, 140);

        exercise1.addSet(benchSet1);
        exercise1.addSet(benchSet2);
        exercise1.addSet(benchSet3);
        exercise1.addSet(benchSet4);

        exercise2.addSet(squatSet1);
        exercise2.addSet(squatSet2);
        exercise2.addSet(squatSet3);
        exercise2.addSet(squatSet4);

        workout1.addExercise(exercise1);
        workout1.addExercise(exercise2);

        Set shoulderPress1 = new Set(4, 130);
        Set shoulderPress2 = new Set(4, 120);

        Set lateralRaises1 = new Set(5, 170);
        Set lateralRaises2 = new Set(5, 180);

        exercise3.addSet(shoulderPress1);
        exercise3.addSet(shoulderPress2);

        exercise4.addSet(lateralRaises1);
        exercise4.addSet(lateralRaises2);

        workout2.addExercise(exercise3);
        workout2.addExercise(exercise4);

        ReadAndWrite readAndWrite = new ReadAndWrite(testFileLocation);

        readAndWrite.writeWorkoutToUser(workout1);
        readAndWrite.writeWorkoutToUser(workout2);
    }

    @Test
    public void testJournalLength() {
        ListView<Workout> listView = lookup("#exercisesListView").query();
        assertEquals(2, listView.getItems().size());
    }

    @Test
    public void testJournalOrder() {
        ListView<Workout> listView = lookup("#exercisesListView").query();
        assertTrue(listView.getItems().get(0).getDate().toString().equals("2023-10-01"));
        assertTrue(listView.getItems().get(1).getDate().toString().equals("2019-01-01"));
    }

    @Test
    public void testJournalExerciseLength() {
        clickOn(workout1.toString());
        ListView<TextArea> workoutListView = lookup("#workoutListView").query();
        assertEquals(2, workoutListView.getItems().size());
    }

    @Test
    public void testJournalExerciseOrder() {
        clickOn(workout1.toString());
        ListView<TextArea> workoutListView = lookup("#workoutListView").query();
        assertTrue("Bench Press".equals(workoutListView.getItems().get(0).getText().split("\n")[0]));
        assertTrue("Squats".equals(workoutListView.getItems().get(1).getText().split("\n")[0]));
        clickOn(workout2.toString());
        assertTrue("Shoulder Press".equals(workoutListView.getItems().get(0).getText().split("\n")[0]));
        assertTrue("Lateral Raises".equals(workoutListView.getItems().get(1).getText().split("\n")[0]));
    }

    @Test
    public void testReturn() {
        clickOn("Return");
        ObservableList<Node> rootChildren = root.getChildrenUnmodifiable();
        assertTrue(rootChildren.size() == 1);
        assertTrue(rootChildren.get(0).getId().equals("baseAnchor"));
        assertTrue(((Parent) rootChildren.get(0)).getChildrenUnmodifiable().stream()
                .anyMatch(x -> x instanceof Text && ((Text) x).getText().equals("LØ")));
        assertTrue(((Parent) rootChildren.get(0)).getChildrenUnmodifiable().stream()
                .anyMatch(x -> x instanceof Text && ((Text) x).getText().equals("FT")));
    }

    private static void deleteTestfile() {
        try {
            if ((new File(testFileLocation)).exists()) {
                Files.delete(Path.of(testFileLocation));
            }
        } catch (IOException e) {
            System.err.println("Error deleting file");
        }
    }
}
