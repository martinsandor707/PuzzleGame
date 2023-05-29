package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main invokes this class to initialize the GUI.
 */
public class PuzzleGameApplication extends Application {

    /**
     *  Sets up the main menu.
     * @param stage The stage that is automatically given by JavaFX
     * @throws IOException if the fxml file doesn't exist
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/mainmenu/mainMenu.fxml"));
        stage.setTitle("main.Main Menu");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
