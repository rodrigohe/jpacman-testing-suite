package jpacman.integration.move;

import jpacman.Launcher;
import jpacman.board.Direction;
import jpacman.board.Square;
import jpacman.board.Unit;
import jpacman.game.Game;
import jpacman.level.MapParser;
import jpacman.level.Pellet;
import jpacman.level.Player;
import jpacman.npc.ghost.Navigation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTesting {

    private Game game;
    private Launcher launcher;
    private MapParser mapParser;
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


    /*
    Scenario S2.1: The player consumes
        Given the game has started,
         and  my Pacman is next to a square containing a pellet;
        When  I press an arrow key towards that square;
        Then  my Pacman can move to that square,
         and  I earn the points for the pellet,
         and  the pellet disappears from that square.
    * */
    @Test
    void testPlayerConsumesPellet(){
        player = game.getPlayers().get(0);
        game.move(player, Direction.WEST);
        assertEquals(10, player.getScore());

        // The pellet disappears from "that square"
        Unit pellet = Navigation.findUnit(Pellet.class, player.getSquare());
        assertEquals(null, pellet);
    }

    /*
    Scenario S2.2: The player moves on empty square
        Given the game has started,
         and  my Pacman is next to an empty square;
        When  I press an arrow key towards that square;
        Then  my Pacman can move to that square
         and  my points remain the same.
    * */
    @Test
    void testMoveToEmptySquare(){
        // Getting rid of default map
        launcher.dispose();

        // Loading custom map
        launcher = new Launcher();
        launcher = launcher.withMapFile("/board_emptySquare.txt");
        launcher.launch();
        game = launcher.getGame();
        game.start();

        // Getting Player
        player = game.getPlayers().get(0);

        // Getting Player's Current Score, Initial Square, and Neighbour Square
        int scoreBeforeMov = player.getScore();
        Square sq = player.getSquare();
        Square neighbourSq = sq.getSquareAt(Direction.EAST);

        // Checking neighbour square is empty
        assertTrue((neighbourSq.getOccupants()).isEmpty());

        // Moving to neighbour square
        game.move(player, Direction.EAST);

        // Check if player moved to neighbour square and score remained the same
        assertEquals(neighbourSq, player.getSquare());
        assertEquals(scoreBeforeMov, player.getScore());
    }

    /*
    Scenario S2.3: The move fails
        Given the game has started,
          and my Pacman is next to a cell containing a wall;
        When  I press an arrow key towards that cell;
        Then  the move is not conducted.
    */
    @Test
    void testMoveFails(){
        // Getting rid of default map
        launcher.dispose();

        // Loading custom map
        launcher = new Launcher();
        launcher = launcher.withMapFile("/board_moveFails.txt");
        launcher.launch();
        game = launcher.getGame();
        game.start();

        // Getting Player
        player = game.getPlayers().get(0);

        // Getting Player's Starting Square
        Square startingSq = player.getSquare();

        // Moving towards wall
        game.move(player, Direction.EAST);

        // Check player stayed in the same square
        assertEquals(startingSq, player.getSquare());

    }

    /*
    Scenario S2.4: The player dies
        Given the game has started,
        and  my Pacman is next to a cell containing a ghost;
        When  I press an arrow key towards that square;
        Then  my Pacman dies,
        and  the game is over.
    */
    @Test
    void testPlayerDies(){
        // Getting rid of default map
        launcher.dispose();

        // Loading custom map
        launcher = new Launcher();
        launcher = launcher.withMapFile("/board_playerDies.txt");
        launcher.launch();
        game = launcher.getGame();
        game.start();

        // Getting Player
        player = game.getPlayers().get(0);

        // Colliding with Ghost
        game.move(player, Direction.EAST);

        // Check player is dead
        assertFalse(game.getLevel().isAnyPlayerAlive());

        // Check game is over
        assertFalse(game.isInProgress());
    }

    /*
    Scenario S2.5: Player wins, extends S2.1
        When  I have eaten the last pellet;
        Then  I win the game.
    */
    @Test
    void testPlayerWins(){
        // Getting rid of default map
        launcher.dispose();

        // Loading custom map
        launcher = new Launcher();
        launcher = launcher.withMapFile("/board_playerWins.txt");
        launcher.launch();
        game = launcher.getGame();
        game.start();

        // Getting Player
        player = game.getPlayers().get(0);

        // Colliding with last pellet
        game.move(player, Direction.WEST);

        // Check player earned points for consuming pellet
        assertEquals(10, player.getScore());

        // Check any remaining pellets, 0 pellets = Player wins
        assertEquals(0, game.getLevel().remainingPellets());

        // Check game is over
        assertFalse(game.isInProgress());
    }
}
