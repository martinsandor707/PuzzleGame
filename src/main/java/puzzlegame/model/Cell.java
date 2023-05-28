package puzzlegame.model;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement(name = "cell")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
/** Class used to show if a cell is valid, and if so, what piece occupies it */
public class Cell {
    @XmlAttribute
    CellState state;
    @XmlValue
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
