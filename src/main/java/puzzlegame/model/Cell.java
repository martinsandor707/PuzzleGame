package puzzlegame.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/** Class used to show if a cell is valid, and if so, what piece occupies it */
public class Cell {
    CellState state;
    int value;

    public Cell(){
        state=CellState.INVALID;
    }
    public Cell(int value){
        state=CellState.VALID;
        this.value=value;
    }

    @Override
    public String toString() {
        if (state==CellState.INVALID)
            return "INVALID";
        else
            return String.valueOf(value);
    }
}
