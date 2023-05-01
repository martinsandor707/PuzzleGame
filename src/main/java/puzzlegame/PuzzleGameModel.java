package puzzlegame;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

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
                for (int i=0; i<BOARD_SIZE; i++){       //The first row of the GridPane is mostly invalid
                        if (i>EXTRA_CELL_INTERVAL && (i+1)%EXTRA_CELL_INTERVAL==0)
                                board[0][i]=new ReadOnlyObjectWrapper<Cell>(Cell.VALID); //except for some free cells at set intervals
                        else board[0][i]=new ReadOnlyObjectWrapper<Cell>(Cell.INVALID);
                }
                for (int i=0; i<BOARD_SIZE; i++){
                        board[1][i]=new ReadOnlyObjectWrapper<Cell>(Cell.VALID);
                }
        }

        /**
         * Returns whether the specified cell is valid or not
         * @param i The row of the given cell
         * @param j The column of the given cell
         * @return The enum of the {@link Cell}
         */
        public ReadOnlyObjectProperty<Cell> CellProperty(int i, int j) {
                return board[i][j].getReadOnlyProperty();
        }

}
