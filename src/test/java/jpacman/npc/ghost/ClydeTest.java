package jpacman.npc.ghost;

import jpacman.board.BoardFactory;
import jpacman.board.Direction;
import jpacman.level.Level;
import jpacman.level.LevelFactory;
import jpacman.level.Player;
import jpacman.level.PlayerFactory;
import jpacman.points.DefaultPointCalculator;
import jpacman.points.PointCalculator;
import jpacman.sprite.PacManSprites;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ClydeTest {
    private GhostMapParser parser;
    private Player pacman;

    @BeforeEach
    void setUp(){
        PacManSprites sprites = new PacManSprites();
        PlayerFactory playerFac = new PlayerFactory(sprites);
        PointCalculator pointCalc = new DefaultPointCalculator();

        BoardFactory boardFac = new BoardFactory(sprites);
        GhostFactory ghostFac = new GhostFactory(sprites);
        LevelFactory levelFac = new LevelFactory(sprites, ghostFac, pointCalc);

        pacman = playerFac.createPacMan();
        parser = new GhostMapParser(levelFac, boardFac, ghostFac);
    }

    /*
    * Clyde has two AIs:
    *       1. "Clyde is Shy." Clyde will run TOWARDS Pac-Man
    *          if the number of spaces between Clyde and Pac-Man >= 8
    *       2. "Clyde is not Shy." Clyde will run AWAY from Pac-Man
    *          if the number of spaces between Clyde and Pac-Man < 8
    */

    /*
    * Clyde and Pac-Man are on the same row, 6 spaces apart.
    * Tests if Clyde can find the path towards Pac-Man and run away from it.
    */
    @Test
    void testClydeShySameRow(){
        List<String> map = Lists.newArrayList(
                "############",
                "#P      C###",
                "############");

        Level level = parser.parseMap(map);
        level.registerPlayer(pacman);
        pacman.setDirection(Direction.EAST);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(clyde.nextAiMove()).isEqualTo(Optional.of(Direction.EAST));
    }

    /*
     * Clyde and Pac-Man are on different rows and columns, 10 spaces apart.
     * Tests if Clyde can find the path towards Pac-Man and run towards it.
     */
    @Test
    void testClydeNotShyDiffRowCol(){
        List<String> map = Lists.newArrayList(
                "############",
                "#P##########",
                "# ##########",
                "#  #########",
                "## #########",
                "##      C###",
                "############");

        Level level = parser.parseMap(map);
        level.registerPlayer(pacman);
        pacman.setDirection(Direction.SOUTH);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(clyde.nextAiMove()).isEqualTo(Optional.of(Direction.WEST));
    }


    /*
     * Clyde and Pac-Man are on different rows and columns, 11 spaces apart.
     * The path between Pac-Man and Clyde is blocked.
     * Tests if Clyde knows that the path is indeed blocked.
     */
    @Test
    void testBlockedPath(){
        List<String> map = Lists.newArrayList(
                "############",
                "#P##########",
                "# ##########",
                "############",
                "############",
                "# ##########",
                "# ##########",
                "#      C####",
                "############");

        Level level = parser.parseMap(map);
        level.registerPlayer(pacman);
        pacman.setDirection(Direction.SOUTH);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(clyde.nextAiMove()).isEqualTo(Optional.empty());
    }

    /*
     * Pac-Man is not on the board.
     * Tests if Clyde knows that there is no path/target.
     */
    @Test
    void testNoPacMan(){
        List<String> map = Lists.newArrayList(
                "############",
                "#      C####",
                "############");
        Level level = parser.parseMap(map);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(clyde.nextAiMove()).isEqualTo(Optional.empty());
    }
}
