package puzzlegame.model;

import jaxb.JAXBHelper;
import org.tinylog.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * The model of the main game.<br>
 * Contains methods for accessing the individual cells in the GridPane after initialization
 */
public class PuzzleGameModel {

        public static final int BOARD_SIZE=10;
        private final Cell[][] board = new Cell[2][BOARD_SIZE];

        /**I only need this for ease of XML conversion */
        private BoardWrapper XmlBoard=new BoardWrapper();

        /**
         * Initializes the {@link #board}, storing if a given cell is valid or not.<br>
         * It currently sets up the board according to the last save, or if none is found, then the Star
         */
        public PuzzleGameModel() {
                File lastSave=new File("LastSave.xml");
                if (lastSave.exists()){
                        loadFromXml(lastSave.getPath());
                }else{
                        loadFromXml("StartingBoard.xml");
                }
                Logger.info("The board was successfully initialized");
        }

        public void saveToXml(String filePath) {
                XmlBoard.setBoard(new ArrayList<Cell>());
                Logger.debug("XmlBoard cleared");
                for (int row=0;row<2;row++){
                        for (int column=0; column<BOARD_SIZE; column++){
                                XmlBoard.getBoard().add(board[row][column]);
                        }
                }
                Logger.info("XmlBoard filled with current board");
                try{
                        JAXBHelper.toXML(XmlBoard,new FileOutputStream(filePath));
                        Logger.info("Starting position saved into "+filePath);
                } catch(Exception e){
                        Logger.info(e,"Writing to XML didn't work!");
                }
        }

        public void loadFromXml(String filePath){
                try {
                        XmlBoard=JAXBHelper.fromXML(BoardWrapper.class,new FileInputStream(filePath));
                        Logger.info("The size of the starting list is: "+XmlBoard.getBoard().size());

                        for (int row=0; row<2; row++){       //The first row of the board is mostly invalid
                                for (int column=0; column<BOARD_SIZE; column++){
                                        if(XmlBoard.getBoard().get(row*10+column).getState()==CellState.INVALID){
                                                board[row][column]=new Cell();
                                                Logger.debug("Invalid cell successfully added");
                                        }
                                        else{
                                                board[row][column]=new Cell(XmlBoard.getBoard().get(row*10+column).getValue());
                                                Logger.debug("Valid cell added:\n STATE="+board[row][column].getState()+"\t VALUE=" +board[row][column].getValue());
                                        }

                                }
                        }
                        Logger.debug("Board is:\n" +toString());
                } catch (Exception e) {
                        Logger.info(e,"Loading from XML didn't work!");
                }
        }

        /**
         * Determines if the puzzle has been solved.
         * The puzzle has been solved if every single piece is located in the bottom row and are in ascending order.
         * @return Whether the player won.
         */
        public boolean isEndState(){
                boolean endState=true;

                for (int i=0; i<BOARD_SIZE-1;i++){
                        if (!(board[1][i].getValue()==(i+1))) {
                                endState=false;
                                break;
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
         * @throws IllegalMoveException if the move doesn't follow the game rules
         */
        public void move(int thisRow, int thisColumn, int thatRow, int thatColumn) throws IllegalMoveException{
                if (isLegal(thisRow,thisColumn,thatRow,thatColumn) && board[thatRow][thatColumn].getValue()==0 &&
                board[thisRow][thisColumn].getValue()>0){
                        board[thatRow][thatColumn].setValue(board[thisRow][thisColumn].getValue());
                        board[thisRow][thisColumn].setValue(0);
                }
                else {
                        throw new IllegalMoveException("You can't move from "+thisRow+" "+thisColumn+" to " +thatRow+" "+thatColumn);
                }
        }

        /**
         * Decides whether moving from one cell to another is legal.
         * @param i Starting row
         * @param j Starting column
         * @param m Destination row
         * @param n Destination column
         * @return True if the move obeys the rules of the game, false otherwise
         */
        public boolean isLegal(int i, int j, int m, int n){
                return  (i==0 || i==1) && (m==0 || m==1) &&
                        (j>=0 && j<BOARD_SIZE) && (n>=0 && n<BOARD_SIZE) &&
                        ((abs(i-m)==1 && abs(j-n)==0) || (abs(i-m)==0 && abs(j-n)==1)) &&
                        board[m][n].getState()==CellState.VALID;
        }

        /**
         * @return A String representation of the current board state, where invalid states are denoted by X, empty cells
         * are 0 and occupied cells are their respective value
         */
        @Override
        public String toString(){
                StringBuilder builder=new StringBuilder();

                for (int row=0; row<2; row++){
                        for (int column=0; column<BOARD_SIZE;column++){
                                if (board[row][column].getState()==CellState.INVALID)
                                        builder.append("X ");
                                else
                                        builder.append(board[row][column].getValue()+" ");
                        }
                        builder.append("\n");
                }
                return builder.toString();
        }

        /** @return The {@link Cell} object on the {@link #board} specified by the coordinates*/
        public Cell getCell(int i, int j){
                return board[i][j];
        }

}
