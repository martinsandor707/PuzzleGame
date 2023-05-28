package puzzlegame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import puzzlegame.model.CellState;
import puzzlegame.model.IllegalMoveException;
import puzzlegame.model.PuzzleGameModel;

import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;

public class PuzzleGameController {

    private final PuzzleGameModel model=new PuzzleGameModel();
    private StackPane selectedCell;
    private int selectedRow, selectedColumn;
    @FXML
    private GridPane board;

    /** I hate having to use this, but accessing any values on the GridPane is extremely bothersome*/
    private final StackPane[][] gridPaneArray=new StackPane[2][10];

    /**
     * In the starting state the GridPane is filled up with StackPanes in accordance with the {@code model}.
     * All cells are stored, but {@code INVALID} cells don't get drawn.
     */

    @FXML
    public void initialize(){

        for (int row=0; row<board.getRowCount(); row++){
            for (int column=0; column<board.getColumnCount(); column++){
                StackPane cell;
                if (model.getCell(row,column).getState()== CellState.INVALID){
                    cell=new StackPane();
                }
                else{
                    cell=drawValidCell(row,column);
                }
                cell.setPrefSize(120,120);
                board.add(cell, column, row);
                gridPaneArray[row][column]=cell;
            }
        }
    }

    private StackPane drawValidCell(int i, int j){
        StackPane cell=new StackPane();
        cell.getStyleClass().add("cell");

        if(model.getCell(i,j).getValue()>0){
            Circle piece=new Circle(50);
            piece.setFill(Color.GREEN);

            Label value=new Label(String.valueOf(model.getCell(i, j).getValue()));
            value.setTextFill(Color.WHITE);
            value.setAlignment(Pos.CENTER);

            cell.getChildren().add(piece);
            cell.getChildren().add(value);
            cell.setOnMouseClicked(this::occupiedCellClicked);
        }
        else{
            cell.setOnMouseClicked(this::emptyCellClicked);
        }

        return cell;
    }

    @FXML
    public void emptyCellClicked(MouseEvent event){
        if (selectedCell!=null){
            try{
                StackPane destinationCell=(StackPane)event.getSource();
                int destinationRow= GridPane.getRowIndex(destinationCell);
                int destinationColumn= GridPane.getColumnIndex(destinationCell);

                model.move(selectedRow,selectedColumn,destinationRow,destinationColumn);
                Logger.debug("The player did a legal move. The current state of the board should be:\n" +model+"\n");

                turnCellIntoOccupied(destinationCell,model.getCell(destinationRow,destinationColumn).getValue());
                turnCellIntoEmpty(selectedCell);

                selectedCell.getStyleClass().remove("selectedCell");
                selectedCell.getStyleClass().add("cell");
                selectedCell=null;
                unhighlightAllCells();

                if (model.isEndState()){
                    Logger.info("The player won, hooray!");
                    File lastSave=new File("LastSave.xml");
                    lastSave.delete();
                    showVictoryPopUp();
                }


            } catch(IllegalMoveException e){
                Logger.info( "The player attempted to do an illegal move: "+e.getMessage()+"\n");
            }
        }

    }

    private void showVictoryPopUp() {
        final Stage victoryPopUp = new Stage();
        victoryPopUp.initModality(Modality.APPLICATION_MODAL);
        victoryPopUp.setTitle("Hooray, you won!!");

        Label victoryMessage= new Label("You won, great job!\n " +
                "Feel free to reset everything and poke around more, maybe you can find some bugs that escaped my attention :P\n"+
                "Alternatively you can click the button on the right and get back to the main menu");
        victoryMessage.setAlignment(Pos.CENTER);
        victoryMessage.setTextAlignment(TextAlignment.CENTER);

        Button playAgain=new Button("Play again!");
        playAgain.setOnAction(e ->{
            victoryPopUp.close();
            resetGame(e);
        });

        Button quit=new Button("Back to the main menu");
        quit.setOnAction(e ->{
            victoryPopUp.close();
            backToMainMenu(e);
        });

        VBox Vlayout= new VBox(10);
        HBox Hlayout= new HBox(10);

        Hlayout.getChildren().addAll(playAgain,quit);
        Hlayout.setAlignment(Pos.CENTER);

        //elements added
        Vlayout.getChildren().addAll(victoryMessage,Hlayout);

        Vlayout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(Vlayout, 600, 450);

        victoryPopUp.setScene(scene1);
        victoryPopUp.show();
    }

    private void turnCellIntoEmpty(StackPane cell){
        cell.getChildren().remove(0,2);
        cell.setOnMouseClicked(this::emptyCellClicked);
    }

    private void turnCellIntoOccupied(StackPane cell, int number){
        Circle piece=new Circle(50);
        piece.setFill(Color.GREEN);

        Label value=new Label(String.valueOf(number));

        value.setTextFill(Color.WHITE);
        value.setAlignment(Pos.CENTER);

        cell.getChildren().add(piece);
        cell.getChildren().add(value);
        cell.setOnMouseClicked(this::occupiedCellClicked);
    }


    @FXML
    public void occupiedCellClicked(MouseEvent event){
        if (selectedCell!=null){    //Reset the opacity of the previously selected cell
            selectedCell.getStyleClass().remove("selectedCell");
            selectedCell.getStyleClass().add("cell");
        }
        selectedCell=(StackPane)event.getSource();

        selectedCell.getStyleClass().remove("cell");
        selectedCell.getStyleClass().add("selectedCell");

        selectedRow= GridPane.getRowIndex(selectedCell);
        selectedColumn= GridPane.getColumnIndex(selectedCell);

        highlightNeighbouringCells();

        Logger.info("Currently selected cell: {} {}  VALUE: {}",
                selectedRow, selectedColumn, model.getCell(selectedRow,selectedColumn));
    }

    /**
     * This method first resets every {@code Cell} to default style, then checks every neighbour of the selected cell to see
     * if they are valid and unoccupied. If both of these are true, then the neighbouring cell gets the "{@code possibleCellMove}" styling<br><br>
     *
     * REASONING: Using many if-else clauses is bad practice, but it should be much faster than traversing the entire GridPane again
     * just to highlight the cells. I could shorten it by putting it in a try-catch clause instead, but if the selected cell
     * is on an edge, then execution will stop before highlighting other neighbours which are still on the {@code GridPane}.
     */
    private void highlightNeighbouringCells(){

        unhighlightAllCells();

        if (selectedRow==0){
            gridPaneArray[1][selectedColumn].getStyleClass().remove("cell");
            gridPaneArray[1][selectedColumn].getStyleClass().add("possibleCellMove");
            Logger.info("Highlighting bottom neighbour");
        }
        else{
            if (selectedColumn>0 && model.getCell(selectedRow,selectedColumn-1).getValue()==0){
                gridPaneArray[selectedRow][selectedColumn-1].getStyleClass().remove("cell");
                gridPaneArray[selectedRow][selectedColumn-1].getStyleClass().add("possibleCellMove");
                Logger.info("Highlighting left neighbour");
            }
            if (selectedColumn<9 && model.getCell(selectedRow,selectedColumn+1).getValue()==0){
                gridPaneArray[selectedRow][selectedColumn+1].getStyleClass().remove("cell");
                gridPaneArray[selectedRow][selectedColumn+1].getStyleClass().add("possibleCellMove");
                Logger.info("Highlighting right neighbour");
            }
            if (model.getCell(0,selectedColumn).getState()==CellState.VALID && model.getCell(0,selectedColumn).getValue()==0){
                gridPaneArray[0][selectedColumn].getStyleClass().remove("cell");
                gridPaneArray[0][selectedColumn].getStyleClass().add("possibleCellMove");
                Logger.info("Highlighting top neighbour");
            }
        }

    }

    private void unhighlightAllCells(){
        for (int row=0; row<gridPaneArray.length; row++){
            for (int column=0; column<gridPaneArray[row].length; column++){

                if (model.getCell(row,column).getState()==CellState.VALID && gridPaneArray[row][column].getStyleClass().contains("possibleCellMove")){
                    gridPaneArray[row][column].getStyleClass().remove("possibleCellMove");
                    gridPaneArray[row][column].getStyleClass().add("cell");

                }
            }
        }
    }

    public void saveCurrentState(ActionEvent actionEvent) {
        model.saveToXml("LastSave.xml");
    }

    public void resetGame(ActionEvent actionEvent) {
        model.loadFromXml("StartingBoard.xml");
        board.getChildren().remove(0,20);
        selectedCell=null;
        for (int row=0; row<board.getRowCount(); row++) {
            for (int column = 0; column < board.getColumnCount(); column++) {
                StackPane cell;
                if (model.getCell(row, column).getState() == CellState.INVALID) {
                    cell = new StackPane();
                } else {
                    cell = drawValidCell(row, column);
                }
                cell.setPrefSize(120, 120);
                board.add(cell, column, row);
                gridPaneArray[row][column] = cell;
            }
        }
    }

    public void backToMainMenu(ActionEvent actionEvent) {
        try{
            Stage stage=(Stage) board.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/mainmenu/mainMenu.fxml"));
            stage.setTitle("Main menu");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch(IOException e){
            Logger.debug("mainMenu.fxml doesn't exist!");
        }
    }
}
