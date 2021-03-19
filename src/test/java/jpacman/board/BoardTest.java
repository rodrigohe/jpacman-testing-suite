package jpacman.board;

import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    /*
     * Q: In the Board class, what does the invariant method check?
     * A: It checks if the any of the squares in the board are NULL.
     *    Returns true if none of the squares are null, false otherwise.
     * */
    void OneXOneGridTest(){
        Square[][] grid = new Square[1][1];
        Square basicSq = new BasicSquare();
        grid[0][0] = basicSq;
        Board simpleBoard = new Board(grid);

        assertTrue(simpleBoard.invariant());
    }

    @Test
    /*
    * Q: Write a test method fr the squareAt method. With what exception does your test it fail?
    * A: "Initial grid cannot contain null squares"
    *
    * Q: Remove the option -ea, re-run te test. What happens?
    * A: The test passed.
    * */
    void OneXOneGridNullTest(){
        Square[][] grid = new Square[1][1];
        Square basicSq = new BasicSquare();
        grid[0][0] = null;
        Board simpleBoard = new Board(grid);

        assertNull(simpleBoard.squareAt(0,0));
    }

}