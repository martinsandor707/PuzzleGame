package puzzlegame.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
