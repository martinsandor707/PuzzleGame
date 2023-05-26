package puzzlegame;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;
import puzzlegame.model.CellState;
import puzzlegame.model.IllegalMoveException;
import puzzlegame.model.PuzzleGameModel;

public class PuzzleGameController {

    private final PuzzleGameModel model=new PuzzleGameModel();
    private StackPane selectedCell=null;
    private int selectedRow, selectedColumn;
    @FXML
    private GridPane board;

    /** I hate having to use this, but accessing any values on the GridPane is extremely bothersome*/
    private final StackPane[][] gridPaneArray=new StackPane[board.getRowCount()][board.getColumnCount()];

    @FXML
    public void initialize(){
        for (int row=0; row<board.getRowCount(); row++){
            for (int column=0; column<board.getColumnCount(); column++){
                StackPane cell;
                if (model.getCell(row,column).getState()== CellState.INVALID){
                    cell=new StackPane();
                    cell.setOpacity(1);             //We just draw an invisible cell to fill up the GridPane
                }
                else{
                    cell=drawValidCell(row,column);
                }
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
            piece.getStyleClass().add("piece");

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
                int destinationRow= board.getRowIndex(destinationCell);
                int destinationColumn=board.getColumnIndex(destinationCell);

                model.move(selectedRow,selectedColumn,destinationRow,destinationColumn);

                StackPane dummyCell=selectedCell;
                gridPaneArray[selectedRow][selectedColumn]=gridPaneArray[destinationRow][destinationColumn];
                gridPaneArray[destinationRow][destinationColumn]=dummyCell;
            } catch(IllegalMoveException e){
                //TODO: Insert logging here
            }
        }

    }

    @FXML
    public void occupiedCellClicked(MouseEvent event){
        if (selectedCell!=null){    //Reset the opacity of the previously selected cell
            selectedCell.setOpacity(0.5);
        }
        selectedCell=(StackPane)event.getSource();
        selectedCell.setOpacity(0);

        selectedRow= board.getRowIndex(selectedCell);
        selectedColumn=board.getColumnIndex(selectedCell);

        highlightNeighbouringCells();

        //TODO: Insert logging here

    }

    /**
     * This method first resets every cell to default style, then checks every neighbour of the selected cell to see
     * if they are valid and unoccupied. If both of these are true, then the neighbouring cell gets the "possible move" styling
     */
    private void highlightNeighbouringCells(){
        for (int row=0; row<gridPaneArray.length; row++){
            for (int column=0; column<gridPaneArray[row].length; column++){

                if (model.getCell(row,column).getState()==CellState.VALID){
                    gridPaneArray[row][column].getStyleClass().remove(0);
                    gridPaneArray[row][column].getStyleClass().add("cell");
                }
            }
        }
        if (selectedRow==0){
            gridPaneArray[1][selectedColumn].getStyleClass().remove(0);
            gridPaneArray[1][selectedColumn].getStyleClass().add("possibleCellMove");
        }
        else{
            if (selectedColumn>0 && model.getCell(selectedRow,selectedColumn-1).getValue()==0){
                gridPaneArray[selectedRow][selectedColumn-1].getStyleClass().remove(0);
                gridPaneArray[selectedRow][selectedColumn-1].getStyleClass().add("possibleCellMove");
            }
            if (selectedColumn<9 && model.getCell(selectedRow,selectedColumn+1).getValue()==0){
                gridPaneArray[selectedRow][selectedColumn+1].getStyleClass().remove(0);
                gridPaneArray[selectedRow][selectedColumn+1].getStyleClass().add("possibleCellMove");
            }
            if (model.getCell(0,selectedColumn).getState()==CellState.VALID && model.getCell(0,selectedColumn).getValue()==0){
                gridPaneArray[0][selectedColumn].getStyleClass().remove(0);
                gridPaneArray[0][selectedColumn].getStyleClass().add("possibleCellMove");
            }
        }

    }

}
