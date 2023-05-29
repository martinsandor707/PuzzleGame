package model;

/**
 * Enum class used to keep track of which cells should be drawn and which shouldn't.
 */
public enum CellState {

    /**
     * These cells should NOT be drawn.
     */
    INVALID,

    /**
     * These cells will be drawn.
     */
    VALID
}
