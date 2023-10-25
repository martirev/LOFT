package ui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ui.controllers.LoginScreenController;
import ui.controllers.SceneSwitcher;

/**
 * The main class of the application that extends the JavaFX Application class.
 * It sets up the primary stage and loads the LoginScreen.fxml file with the
 * LoginScreenController as its controller.
 */
public class App extends Application {

    /**
     * Starts a custom JavaFX stage with the specified controller and FXML file.
     *
     * @param stage         the JavaFX stage to start
     * @param fxmlName      the name of the FXML file to load 
     * @param controller    the controller for the FXML file
     * @throws IOException  if the FXML file cannot be loaded
     */
    public static Parent customStart(Stage stage, String fxmlName, SceneSwitcher controller)
            throws IOException {
        stage.setTitle("LØFT");
        
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlName));
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(App.class.getResource("Styles.css").toExternalForm());

        stage.setScene(scene);
        stage.getIcons().add(new Image(App.class.getResourceAsStream("images/weightlifter.png")));

        stage.show();
        return root;
    }

    @Override
    public void start(Stage stage) throws IOException {
        customStart(stage, "LoginScreen.fxml", new LoginScreenController());
    }

    public static void main(String[] args) {
        launch();
    }
}
