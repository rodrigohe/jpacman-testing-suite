package jpacman.game;

import jpacman.Launcher;
import jpacman.board.Direction;
import jpacman.board.Square;
import jpacman.board.Unit;
import jpacman.level.Pellet;
import jpacman.level.Player;
import jpacman.npc.ghost.Navigation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class stateTransitionsTest {
    private Game game;
    private Launcher launcher;
    private Player player;

    @BeforeEach
    public void setUp(){
        launcher = new Launcher();
        launcher.launch();
        game = launcher.getGame();
        game.start();
    }

    @AfterEach  // Makes sure each test starts with a fresh launcher
    void tearDown(){
        launcher.dispose();
    }

    @Test
    void testStartButtonPressed(){
        game.start();
        assertTrue(game.isInProgress());
    }

    @Test
    void testPauseButtonPressed(){
        game.start();
        assertTrue(game.isInProgress());

        game.stop();
        assertFalse(game.isInProgress());
    }

    @Test
    void testPlayerEatsPellet(){
        // Get total num of pellets before Player eats one
        int startingNumPellets = game.getLevel().remainingPellets();

        // Get player, move, and consume one pellet
        player = game.getPlayers().get(0);
        game.move(player, Direction.WEST);
        // Player received points for consuming pellet
        assertEquals(10, player.getScore());

        // Check if pellet disappeared from square
        Unit pellet = Navigation.findUnit(Pellet.class, player.getSquare());
        assertEquals(null, pellet);

        // Check Number of pellets decreased by one
        assertEquals(startingNumPellets - 1, game.getLevel().remainingPellets());

        // Check Game is still in progress
        assertTrue(game.isInProgress());
    }

    @Test
    void testPressArrowKey(){
        // Get Player, its current square and neighbour square
        player = game.getPlayers().get(0);
        Square sq = player.getSquare();
        Square neighbourSq = sq.getSquareAt(Direction.EAST);

        // Moving to neighbour square
        game.move(player, Direction.EAST);

        // Check if player moved to neighbour square
        assertEquals(neighbourSq, player.getSquare());

        // Check Game is still in progress
        assertTrue(game.isInProgress());
    }

    @Test
    void testPlayerEatsLastPellet(){
        // Getting rid of default map
        launcher.dispose();

        // Load custom map
        launcher = new Launcher();
        launcher = launcher.withMapFile("/board_playerWins.txt");
        launcher.launch();
        game = launcher.getGame();
        game.start();

        // Get Player and collide with last pellet
        player = game.getPlayers().get(0);
        game.move(player, Direction.WEST);

        // Check if player earned points for consuming pellet
        assertEquals(10, player.getScore());

        // Check any remaining pellets, 0 pellets = Player wins
        assertEquals(0, game.getLevel().remainingPellets());

        // Check if game is over
        assertFalse(game.isInProgress());
    }

    @Test
    void testPlayerCollidesGhost(){
        // Getting rid of default map
        launcher.dispose();

        // Load custom map
        launcher = new Launcher();
        launcher = launcher.withMapFile("/board_playerDies.txt");
        launcher.launch();
        game = launcher.getGame();
        game.start();

        // Get Player and collide with Ghost
        player = game.getPlayers().get(0);
        game.move(player, Direction.EAST);

        // Check if player is dead
        assertFalse(game.getLevel().isAnyPlayerAlive());

        // Check if game is over
        assertFalse(game.isInProgress());
    }


}
