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
     * Sets up the JavaFX system properties to support headless mode.
     */
    public static void supportHeadless() {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
            System.setProperty("headless.geometry", "1366x768-32");
        }
    }

    /**
     * Starts a custom JavaFX stage with the specified controller and FXML file.
     *
     * @param stage      the JavaFX stage to start
     * @param fxmlName   the name of the FXML file to load
     * @param controller the controller for the FXML file
     * @throws IOException if the FXML file cannot be loaded
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
