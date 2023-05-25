package puzzlegame.model;

import javafx.beans.property.ReadOnlyObjectWrapper;

import static java.lang.Math.abs;

/**
 * The model of the main game.<br>
 * Contains methods for accessing the individual cells in the GridPane after initialization
 */
public class PuzzleGameModel {
        public static final int BOARD_SIZE=10;
        public static final int EXTRA_CELL_INTERVAL=2;

        private final ReadOnlyObjectWrapper<Cell>[][] board = new ReadOnlyObjectWrapper[2][BOARD_SIZE];

        /**
         * Initializes the {@link #board}, storing if a given cell is valid or not.<br>
         * It currently sets up the board according to the picture provided in the project's README.md, but the variables
         * {@link #BOARD_SIZE} and {@link #EXTRA_CELL_INTERVAL} give some extra customizability.
         * In the future it would probably be best to get the value of {@link #BOARD_SIZE} directly from the GridPane Object
         */
        public PuzzleGameModel() {
                for (int i=0; i<BOARD_SIZE; i++){       //The first row of the board is mostly invalid
                        if (i>EXTRA_CELL_INTERVAL && (i+1)%EXTRA_CELL_INTERVAL==0 && i!=BOARD_SIZE-1)
                                board[0][i]=new ReadOnlyObjectWrapper<>(new Cell(0)); //except for some free cells at set intervals
                        else board[0][i]=new ReadOnlyObjectWrapper<>(new Cell());
                }
                board[1][0]=new ReadOnlyObjectWrapper<>(new Cell(0));       //We need to move the "1" piece here
                for (int i=1; i<BOARD_SIZE-1; i++){
                        board[1][i]=new ReadOnlyObjectWrapper<>(new Cell(i+1));
                }
                board[1][BOARD_SIZE-1]=new ReadOnlyObjectWrapper<>(new Cell(1));    //The "1" piece starts here
        }

        /**
         * Determines if the puzzle has been solved.
         * The puzzle has been solved if every single piece is located in the bottom row and are in ascending order.
         * @return Whether the player won.
         */
        public boolean isEndState(){
                boolean endState=true;

                for (int i=0; i<BOARD_SIZE-1;i++){
                        if (!(board[1][i].get().getValue()==(i+1))) {
                                endState=false;
                        }
                }

                return endState;
        }

        /**
         * Moves a piece on the board from its current position to the one specified by the parameters.
         * A move is legal if the piece would move only a single cell in a cardinal direction (in other words moving diagonally is not allowed).<br>
         * Additionally, the cell in the desired position has to be empty (have a value of zero),
         * and the current cell has to be occupied (have a value >0 )
         * @param thisRow The parameters of the piece's current position
         * @param thisColumn .
         * @param thatRow The parameters of the desired position
         * @param thatColumn .
         * @throws IllegalMoveException Custom exception for the game's rules
         */
        public void move(int thisRow, int thisColumn, int thatRow, int thatColumn) throws IllegalMoveException{
                if (isLegal(thisRow,thisColumn,thatRow,thatColumn) && board[thatRow][thatColumn].get().getValue()==0 &&
                board[thisRow][thisColumn].get().getValue()>0){
                        board[thatRow][thatColumn].get().setValue(board[thisRow][thisColumn].get().getValue());
                        board[thisRow][thisColumn].get().setValue(0);
                }
                else {
                        throw new IllegalMoveException("You can't move from "+thisRow+" "+thisColumn+" to " +thatRow+" "+thatColumn);
                }
        }

        private boolean isLegal(int i, int j, int m, int n){
                return  (i==0 || i==1) && (m==0 || m==1) &&
                        (j>=0 && j<BOARD_SIZE) && (n>=0 && n<BOARD_SIZE) &&
                        ((abs(i-m)==1 && abs(j-n)==0) || (abs(i-m)==0 && abs(j-n)==1));
        }

        @Override
        public String toString(){
                StringBuilder builder=new StringBuilder();

                for (int row=0; row<2; row++){
                        for (int column=0; column<BOARD_SIZE;column++){
                                if (board[row][column].get().getState()==CellState.INVALID)
                                        builder.append("X ");
                                else
                                        builder.append(board[row][column].get().getValue()+" ");
                        }
                        builder.append("\n");
                }
                return builder.toString();
        }

        /** @return The {@link Cell} object on the {@link #board} specified by the coordinates*/
        public Cell getCell(int i, int j){
                return board[i][j].get();
        }

}
