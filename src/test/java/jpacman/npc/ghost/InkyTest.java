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

public class InkyTest {
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
     * Inky is placed alongside Blinky, Blinky is placed behind Pac-Man. Both ghosts are behind Pac-Man
     * Tests if Inky will follow Blinky in a basic map.
     */
    @Test
    void testInkyFollowBlinkySameRow(){
        List<String> map = Lists.newArrayList(
                "############",
                "#  IB    P #",
                "############");

        Level level = parser.parseMap(map);
        level.registerPlayer(pacman);
        pacman.setDirection(Direction.WEST);
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(blinky.nextAiMove());
    }


    /*
     * Inky is placed alongside Blinky, Blinky is placed behind Pac-Man. Both ghosts are behind Pac-Man
     * Tests if Inky will follow Blinky in a more complex map.
     */
    @Test
    void testInkyFollowBlinkyDiffRowCol(){
        List<String> map = Lists.newArrayList(
                "############",
                "##P    #####",
                "##        ##",
                "##        ##",
                "##        ##",
                "##   B     #",
                "##   I     #",
                "############");

        Level level = parser.parseMap(map);
        level.registerPlayer(pacman);
        pacman.setDirection(Direction.WEST);
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(blinky.nextAiMove());
    }

    /*
    * Inky is placed in front of Pac-Man while Blinky is placed far behind Pac-Man
    * Tests if Inky will move very far ahead of Pac-Man
    * i.e South since Pac-Man is moving South.
    */
    @Test
    void testInkyFrontBlinkyBehindPacMan(){
        List<String> map = Lists.newArrayList(
                "############",
                "##I    #####",
                "##        ##",
                "##   P    ##",
                "##        ##",
                "##         #",
                "##   B     #",
                "############");

        Level level = parser.parseMap(map);
        level.registerPlayer(pacman);
        pacman.setDirection(Direction.SOUTH);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.of(Direction.SOUTH));
    }

    /*
     * Inky is placed in front of Pac-Man while Blinky is placed far behind Pac-Man; however, Inky is blocked
     * Tests if Inky will return empty direction.
     */
    @Test
    void testInkyBlockedBlinkyBehindPacMan(){
        List<String> map = Lists.newArrayList(
                "############",
                "##I    #####",
                "############",
                "##   P    ##",
                "##        ##",
                "##         #",
                "##   B     #",
                "############");

        Level level = parser.parseMap(map);
        level.registerPlayer(pacman);
        pacman.setDirection(Direction.SOUTH);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.empty());
    }
}
