package jpacman.integration.suspension;

import jpacman.Launcher;
import jpacman.game.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SuspensionTests {

    private Game game;
    private Launcher launcher;

    @BeforeEach
    public void setUp(){
        launcher = new Launcher();
        launcher.launch();
        game = launcher.getGame();
        game.start();
    }

    @AfterEach
    void tearDown(){
        launcher.dispose();
    }

    /*
    Scenario S4.1: Suspend the game.
        Given the game has started;
        When  the player clicks the "Stop" button;
        Then  all moves from ghosts and the player are suspended.
    */
    @Test
    void testSuspendGame(){
        assertTrue(game.isInProgress());
        game.stop();
        assertFalse(game.isInProgress());
    }

    /*
    Scenario S4.2: Restart the game.
        Given the game is suspended;
        When  the player hits the "Start" button;
        Then  the game is resumed.
    */
    @Test
    void testRestartGame(){
        assertTrue(game.isInProgress());
        game.stop();
        assertFalse(game.isInProgress());
        game.start();
        assertTrue(game.isInProgress());
    }
}
