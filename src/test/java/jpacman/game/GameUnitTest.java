package jpacman.game;

import jpacman.PacmanConfigurationException;
import jpacman.level.Level;
import jpacman.level.Player;
import jpacman.points.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {

    private Player player;
    private Level level;
    private SinglePlayerGame game;

    @BeforeEach
    void setUp(){
        level = mock(Level.class);
        player = mock(Player.class);

        game = new SinglePlayerGame(player, level, mock(PointCalculator.class));
    }

    /*
     * First IF branch is TRUE,
     *      Program should return, game is not is progress.
     *      There should not be any interactions with start() or addObserver().
     *      isInProgress() should return FALSE.
     * */
    @Test
    void gameAlreadyInProgress(){
        when(level.isInProgress()).thenReturn(true);

        game.start();

        verify(level, times(0)).start();
        verify(level, times(0)).addObserver(game);

        assertThat(game.isInProgress()).isFalse();
    }

    /*
     * First IF branch is FALSE, second IF branch is TRUE
     *      Program should skip first if branch and enter the second if branch.
     *      There should be any interactions with start() or addObserver().
     *      isInProgress() should return TRUE.
     * */

    @Test
    void gameNotInProgressPlayerAliveRemainingPellets(){
        when(level.isAnyPlayerAlive()).thenReturn(true);
        when(level.remainingPellets()).thenReturn(10);

        game.start();
        verify(level).start();
        verify(level).addObserver(game);

        assertThat(game.isInProgress()).isTrue();
    }

    /*
    * The following tests are all the "same", the tests should:
    *
    * First and second IF branches are FALSE,
    *      Program should skip both IF branches.
    *      There should not be any interactions with start() or addObserver().
    *      isInProgress() should return FALSE.
    * */
    @Test
    void gameNotInProgressPlayerNotAliveRemainingPellets(){
        when(level.isAnyPlayerAlive()).thenReturn(false);
        when(level.remainingPellets()).thenReturn(10);

        game.start();

        verify(level, times(0)).start();
        verify(level, times(0)).addObserver(game);

        assertThat(game.isInProgress()).isFalse();
    }

    @Test
    void gameNotInProgressPlayerAliveNoRemainingPellets(){
        when(level.isAnyPlayerAlive()).thenReturn(true);
        when(level.remainingPellets()).thenReturn(0);

        game.start();

        verify(level, times(0)).start();
        verify(level, times(0)).addObserver(game);

        assertThat(game.isInProgress()).isFalse();
    }

    @Test
    void gameNotInProgressPlayerNotAliveNoRemainingPellets(){
        when(level.isAnyPlayerAlive()).thenReturn(false);
        when(level.remainingPellets()).thenReturn(0);

        game.start();

        verify(level, times(0)).start();
        verify(level, times(0)).addObserver(game);

        assertThat(game.isInProgress()).isFalse();
    }


}
