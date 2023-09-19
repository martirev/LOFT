package ui.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import core.Exercise;
import core.ReadAndWrite;
import core.Set;
import core.User;
import core.Workout;
import core.WorkoutSorting;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public class WorkoutScreenController extends SceneSwitcher {

    @FXML
    private ListView<String> dropdownMenu;

    @FXML
    private ListView<TextArea> workoutListView;

    @FXML
    private TextField searchBar, setField;

    @FXML
    private GridPane grid;

    @FXML
    private AnchorPane exerciseSettings;

    @FXML
    private Button editButton, addButton;

    private Exercise selectedExercise;

    private Workout workout;

    private ReadAndWrite readAndWrite;

    public WorkoutScreenController() {
        readAndWrite = new ReadAndWrite();
    }

    public WorkoutScreenController(String location) {
        readAndWrite = new ReadAndWrite(location);
    }

    public Workout getWorkout() {
        return workout;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = readAndWrite.returnUserClassFromFile();

        WorkoutSorting workoutSorting = new WorkoutSorting(user.getWorkouts());

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            searchUpdated(workoutSorting, newValue);
        });
        searchUpdated(workoutSorting, "");

        dropdownMenu.getSelectionModel().selectedItemProperty().addListener(dropdownSelectHandler());

        setField.setTextFormatter(getNumberFormatter());
        setField.textProperty().addListener((observable, oldValue, newValue) -> {
            setFieldUpdated(newValue);
        });
        grid.setGridLinesVisible(true);

        workout = new Workout();
    }

    /**
     * Creates a ChangeListener that enables the edit button if the dropdown menu
     * has a valid item selected
     * 
     * @return a ChangeListener that enables the edit button if the dropdown menu
     *         has a valid item selected
     */
    private ChangeListener<String> dropdownSelectHandler() {
        return new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (newValue == null || (newValue.equals("Add as new") && searchBar.getText().isEmpty())) {
                    editButton.setDisable(true);
                    return;
                }

                editButton.setDisable(false);
            }
        };
    }

    /**
     * Updates the dropdown menu with the search results
     * 
     * @param workoutSorting the WorkoutSorting object to search with
     * @param newValue       the new value of the search bar
     */
    private void searchUpdated(WorkoutSorting workoutSorting, String newValue) {
        if (newValue == null || newValue.isEmpty()) {
            editButton.setDisable(true);
        }
        String dummy = "Add as new";

        List<String> result = new ArrayList<String>(Arrays.asList(dummy));
        result.addAll(workoutSorting.searchForExercises(newValue));
        dropdownMenu.getItems().setAll(result);
    }

    /**
     * Updates the grid with the new number of sets when the set inputfield is
     * updated
     * 
     * @param newValue the new value of the set inputfield
     */
    private void setFieldUpdated(String newValue) {
        if (newValue == null || newValue.isEmpty()) {
            newValue = "0";
        }

        if (newValue.length() > 2 || Integer.parseInt(newValue) > 18) {
            newValue = "18";
        }

        grid.getRowConstraints().clear();
        grid.getChildren().clear();
        int numSets = Integer.parseInt(newValue);
        for (int i = 0; i < numSets; i++) {
            Text infoField = new Text("Set " + (i + 1) + ":");
            grid.add(infoField, 0, i);

            TextField weightField = new TextField();
            weightField.setTextFormatter(getNumberFormatter());
            weightField.setPromptText("Weight");
            grid.add(weightField, 1, i);

            TextField repsField = new TextField();
            repsField.setTextFormatter(getNumberFormatter());
            repsField.setPromptText("Reps");
            grid.add(repsField, 2, i);
        }

        for (int i = 0; i < numSets; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / 18);
            grid.getRowConstraints().add(rowConst);
        }

    }

    /**
     * Makes the program go back to the home screen
     */
    @FXML
    private void handleReturnPress() {
        insertPane("HomeScreen.fxml");
    }

    /**
     * Adds the workout to the user and goes back to the home screen
     */
    @FXML
    private void handleFinishPress() {
        readAndWrite.writeWorkoutToUser(workout);
        insertPane("HomeScreen.fxml");
    }

    /**
     * Creates a TextFormatter that only allows numbers to be entered
     * 
     * @return a TextFormatter that only allows numbers to be entered
     */
    private TextFormatter<String> getNumberFormatter() {
        return new TextFormatter<>(change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        });
    }

    /**
     * Handles the edit button being pressed. If user selects "Add as new", there
     * will be created a new exercise with the name from the search bar. Else, the
     * selected exercise will be edited.
     */
    @FXML
    private void handleEditButton() {
        String selectedExercisename = dropdownMenu.getSelectionModel().getSelectedItem();

        if (selectedExercisename == null) {
            return;
        }

        selectedExercise = new Exercise(selectedExercisename);
        exerciseSettings.setVisible(true);

        if (selectedExercise.getName().equals("Add as new")) {
            selectedExercise = new Exercise(searchBar.getText());
        }

        searchBar.setText(selectedExercise.getName());
    }

    /**
     * Handles the add button being pressed. Adds the exercise to the workout and
     * formats the exercise so that it can be shown in the workout listview.
     */
    @FXML
    private void handleAddButton() {
        List<Set> sets = new ArrayList<Set>();
        for (int i = 0; i < grid.getChildren().size(); i += 3) {
            TextField weightField = (TextField) grid.getChildren().get(i + 1);
            TextField repsField = (TextField) grid.getChildren().get(i + 2);

            if (weightField.getText().isEmpty() || repsField.getText().isEmpty()) {
                return;
            }

            int weight = Integer.parseInt(weightField.getText());
            int reps = Integer.parseInt(repsField.getText());
            sets.add(new Set(reps, weight));
        }
        sets.stream().forEach(selectedExercise::addSet);
        workout.addExercise(selectedExercise);

        TextArea elementArea = new TextArea();
        StringBuilder sb = new StringBuilder();
        sb.append(selectedExercise);
        for (int i = 0; i < sets.size(); i++) {
            final Set set = sets.get(i);
            sb.append("\n\tSet " + (i + 1) + ": ");
            sb.append("Weight: " + set.getWeight() + " Reps: " + set.getReps());
        }

        elementArea.setPrefWidth(workoutListView.getWidth() - 40);
        elementArea.setPrefRowCount(1 + sets.size());
        elementArea.setEditable(false);
        elementArea.setText(sb.toString());

        workoutListView.getItems().add(elementArea);

        selectedExercise = null;
        setField.setText("");
        exerciseSettings.setVisible(false);
        searchBar.setText("");
    }
}
