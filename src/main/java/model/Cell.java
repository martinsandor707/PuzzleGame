package model;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Class used to show if a cell is valid, and if so, what piece occupies it.
 */
@XmlRootElement(name = "cell")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter

public class Cell {
    @XmlAttribute
    CellState state;
    @XmlValue
    int value;

    /**
     * Default constructor creating an invalid cell.
     */
    public Cell(){
        state=CellState.INVALID;
    }

    /**
     * Constructor creating a valid cell.
     * @param value the value of the cell
     */
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
