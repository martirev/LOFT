package ui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ui.controllers.LoginScreenController;

/**
 * The main class of the application that extends the JavaFX Application class.
 * It sets up the primary stage and loads the LoginScreen.fxml file with the
 * LoginScreenController as its controller.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("LØFT");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
        fxmlLoader.setController(new LoginScreenController());
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/weightlifter.png")));

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
