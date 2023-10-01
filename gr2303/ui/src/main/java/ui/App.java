package ui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.controllers.HomeScreenController;

/**
 * The main class of the application that extends the JavaFX Application class.
 * It sets up the primary stage and loads the HomeScreen.fxml file with the
 * HomeScreenController as its controller.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Light Weight");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomeScreen.fxml"));
        fxmlLoader.setController(new HomeScreenController());

        stage.setScene(new Scene(fxmlLoader.load()));

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
