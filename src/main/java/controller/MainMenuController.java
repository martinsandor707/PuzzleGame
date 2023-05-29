package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * The controller of the main menu.
 */
public class MainMenuController {

    @FXML
    private AnchorPane anchorPane;

    /**
     * Changing the scene to the game after button press.
     *
     * @param actionEvent the event invoking the method
     */
    public void changeToPuzzleGame(ActionEvent actionEvent) {
        try{
            Stage stage=(Stage) anchorPane.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/puzzlegame/game.fxml"));
            stage.setTitle("Good ol' puzzle");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch(IOException e){
            Logger.debug("game.fxml doesn't exist!");
        }
    }

    /**
     * Changing the scene to the "about" page after button press.
     *
     * @param actionEvent the event invoking the method
     */
    public void changeToAbout(ActionEvent actionEvent) {
        try{
            Stage stage=(Stage) anchorPane.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/about/about.fxml"));
            stage.setTitle("Who? What? Where?");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch(IOException e){
            Logger.debug("about.fxml doesn't exist!");
        }

    }

    /**
     * Exits the application.
     * @param actionEvent the event invoking the method
     */
    public void quit(ActionEvent actionEvent) {
        Stage stage=(Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
}
