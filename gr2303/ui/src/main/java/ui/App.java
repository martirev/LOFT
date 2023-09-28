package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.controllers.HomeScreenController;

import java.io.IOException;

/**
 * JavaFX App
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
