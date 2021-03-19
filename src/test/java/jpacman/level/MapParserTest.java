package jpacman.level;

import jpacman.PacmanConfigurationException;
import jpacman.board.Board;
import jpacman.board.BoardFactory;
import jpacman.board.Square;
import jpacman.npc.Ghost;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class MapParserTest {
    private MapParser mapParser;
    private LevelFactory levelFac;
    private BoardFactory boardFac;


    @BeforeEach
    void setUp() {
        levelFac = mock(LevelFactory.class);
        boardFac = mock(BoardFactory.class);
        mapParser = new MapParser(levelFac, boardFac);
    }

    /*
    * ==================================================================
    *                       Good Weather Test Cases
    * ==================================================================
    * */

    @Test
    void testParseEmptySquare(){
        List<String> map = Lists.newArrayList(" ");
        mapParser.parseMap(map);

        verify(boardFac).createGround();
        verify(levelFac).createLevel(any(), anyList(), anyList());
    }

    @Test
    void testParseWall(){
        List<String> map = Lists.newArrayList("#");
        mapParser.parseMap(map);

        verify(boardFac).createWall();
        verify(levelFac).createLevel(any(), anyList(), anyList());
    }

    @Test
    void testParsePellet(){
        Square square = mock(Square.class);
        when(boardFac.createGround()).thenReturn(square);   // Giving a sudo-functionality

        Pellet pellet = mock(Pellet.class);
        when(levelFac.createPellet()).thenReturn(pellet);

        List<String> map = Lists.newArrayList(".");

        mapParser.parseMap(map);
        verify(boardFac).createGround();
        verify(levelFac).createPellet();
        verify(pellet).occupy(square);
        verify(levelFac).createLevel(any(), anyList(), anyList());
    }

    @Test
    void testParseGhost(){
        Square square = mock(Square.class);
        when(boardFac.createGround()).thenReturn(square);

        Ghost ghost = mock(Ghost.class);
        when(levelFac.createGhost()).thenReturn(ghost);

        List<String> map = Lists.newArrayList("G");

        mapParser.parseMap(map);

        verify(boardFac).createGround();
        verify(levelFac).createGhost();
        verify(ghost).occupy(square);
        verify(levelFac).createLevel(any(), anyList(), anyList());

    }

    @Test
    void testParsePlayer(){
        Board board = mock(Board.class);
        Square square = mock(Square.class);
        Ghost ghost = mock(Ghost.class);
        CollisionMap collisions = mock(CollisionMap.class);

        Level level = new Level(board, Lists.newArrayList(ghost), Lists.newArrayList(square), collisions);

        Player pacman = mock(Player.class);

        level.registerPlayer(pacman);

        List<String> map = Lists.newArrayList("P");

        mapParser.parseMap(map);

        verify(boardFac).createGround();
        verify(pacman).occupy(square);
        verify(levelFac).createLevel(any(), anyList(), anyList());
    }

    /*
     * ==================================================================
     *                       Bad Weather Test Cases
     * ==================================================================
     * */

    @Test
    void parseNullMap(){
        List<String> map = null;

        assertThatThrownBy(() -> mapParser.parseMap(map)).isInstanceOf(PacmanConfigurationException.class)
                .hasMessageContaining("Input text cannot be null.");
    }

    @Test
    void parseEmptyMap(){
        List<String> map = Lists.newArrayList();

        assertThatThrownBy(() -> mapParser.parseMap(map)).isInstanceOf(PacmanConfigurationException.class)
                .hasMessageContaining("Input text must consist of at least 1 row");
    }

    @Test
    void parseMapWithEmptyString(){
        List<String> map = Lists.newArrayList("");

        assertThatThrownBy(() -> mapParser.parseMap(map)).isInstanceOf(PacmanConfigurationException.class).
                hasMessageContaining("Input text lines cannot be empty");
    }

    @Test
    void parseMapWithBadFormat(){
        List<String> map = Lists.newArrayList("#####",
                "####",
                "#####");

        assertThatThrownBy(() -> mapParser.parseMap(map)).isInstanceOf(PacmanConfigurationException.class)
                .hasMessageContaining("Input text lines are not of equal width");
    }

    @Test
    void parseNonExistingMapFile(){
        String mapName = "file that is not here";
        assertThatThrownBy(() -> {
            mapParser.parseMap("file that is not here");
        }).isInstanceOf(PacmanConfigurationException.class)
                .hasMessageContaining("Could not get resource for: " + mapName);
    }
}