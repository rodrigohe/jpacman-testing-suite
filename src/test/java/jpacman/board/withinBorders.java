package jpacman.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;


public class withinBorders {

    private Board board;

    @BeforeEach
    void setUp(){
        Square[][] grid = new Square[5][5];

        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                grid[i][j] = new BasicSquare();
            }
        }
        board = new Board(grid);
    }

    @ParameterizedTest(name = "x = {0}, y = {1}, truthVal = {2}")
    @CsvSource({
            "2,2, true",    // Middle
            "0,0, true",    // Bottom Left Corner (BLC)
            "4,4, true",    // Top Left Corner (TLC)
            "4,5, false",   // 1 Unit above of the TLC
            "0,-1, false",  // 1 Unit below BLC
            "8,8, false"    // Outside of Borders
    })

    void parameterizedTest(int x, int y, boolean truthVal){
        assertThat(board.withinBorders(x, y)).isEqualTo(truthVal);
    }
}
