import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.IllegalMoveException;
import model.PuzzleGameModel;

import static model.PuzzleGameModel.BOARD_SIZE;

public class PuzzleGameModelTest {

    PuzzleGameModel model;

    @BeforeEach
    public void setup(){
        model=new PuzzleGameModel();
        model.loadFromXml("src/main/resources/StartingBoard.xml");
        System.out.println("----- The winning state is below -----\n"+model);
    }

    @Test
    public void endStateShouldBeFalse(){
        Assertions.assertFalse(model.isEndState(),"Test failed! End state should be false but was true instead!");
    }

    @Test
    public void endStateShouldBeTrue(){
        getWinningState();
        Assertions.assertTrue(model.isEndState(), "Test failed! End state should be true but was false instead!");
    }

    public void getWinningState(){
        for (int j=0; j<BOARD_SIZE-1; j++){
            model.getCell(1,j).setValue(j+1);
        }
        model.getCell(1,9).setValue(0);
    }

    @Test
    public void moveShouldThrowIllegalMoveException(){
        Assertions.assertThrows(IllegalMoveException.class,() -> model.move(0,0,3,3),
                "Test Failed! Move didn't follow the rules!");
    }

    @Test
    public void moveShouldChangeBoardStateCorrectly(){
        model.move(1,3,0,3);
        Assertions.assertAll(
                "Checking if move() changes both the starting and desired cells.",
                () -> Assertions.assertEquals(4,model.getCell(0,3).getValue(), "Test failed! Destination should have changed!"),
                () -> Assertions.assertEquals(0,model.getCell(1,3).getValue(), "Test failed! Starting position should have changed!"));
    }

}
