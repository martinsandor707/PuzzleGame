package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * The controller of the "about" page. Very basic.
 */
public class AboutController {

    @FXML
    private Label label;

    /**
     * A simple initialization method used to set the contents of the label.
     */
    @FXML
    public void initialize(){
        label.setText("Thank you for trying out my little puzzle game!\n " +
                "I had to create this project in 4 days instead of working\non it for 2 weeks or so like I was supposed to, much to my annoyance\n"+
                "Despite this, I think it turned out pretty well, I hope my teachers will think so too.");
        label.setAlignment(Pos.CENTER);
    }

    /**
     * Changing the scene back to the main menu after button press.
     *
     * @param actionEvent the event invoking the method
     */
    public void changeToMain(ActionEvent actionEvent) {
        try{
            Stage stage=(Stage) label.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/mainmenu/mainmenu.fxml"));
            stage.setTitle("Main Menu");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch(IOException e){
            Logger.debug("main.fxml doesn't exist!");
        }
    }
}
