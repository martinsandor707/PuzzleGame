package model;

/**
 * Exception class used to denote if the player tries to move in a way that's not allowed by the rules of the game.
 */
public class IllegalMoveException extends RuntimeException{

    /**
     * Constructor.
     * @param message Whatever you want to see,
     */
    public IllegalMoveException(String message) {
        super(message);
    }
}
