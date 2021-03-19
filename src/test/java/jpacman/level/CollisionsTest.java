package jpacman.level;

import jpacman.npc.Ghost;
import jpacman.points.DefaultPointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public abstract class CollisionsTest {

    private Player player = mock(Player.class);
    private Ghost ghost = mock(Ghost.class);
    private Pellet pellet = mock(Pellet.class);
    private CollisionMap cmap;

    public void setCmap(CollisionMap cmap){
        this.cmap = cmap;
    }

    @Test
    public void playerCollidesGhost(){
        cmap.collide(player, ghost);

        verify(player).setAlive(false);
        verify(player).setKiller(ghost);

        verifyZeroInteractions(ghost);
        verifyZeroInteractions(pellet);
    }

    @Test
    public void ghostCollidesPlayer(){
        cmap.collide(ghost, player);

        verify(player).setKiller(ghost);
        verify(player).setAlive(false);

        verifyZeroInteractions(ghost);
        verifyZeroInteractions(pellet);
    }

    @Test
    public void playerCollidesPellet(){
        cmap.collide(player, pellet);

        verify(player).addPoints(pellet.getValue());
        verify(pellet).leaveSquare();

        verifyZeroInteractions(ghost);

    }

    @Test
    public void ghostCollidesPellet(){
        cmap.collide(ghost, pellet);

        verifyZeroInteractions(ghost);
        verifyZeroInteractions(pellet);
        verifyZeroInteractions(player);
    }

    @Test
    public void ghostCollidesGhost(){
        cmap.collide(ghost, ghost);

        verifyZeroInteractions(ghost);
        verifyZeroInteractions(pellet);
        verifyZeroInteractions(player);
    }

    @Test
    public void playerCollidesPlayer(){
        cmap.collide(player, player);

        verifyZeroInteractions(ghost);
        verifyZeroInteractions(pellet);
        verifyZeroInteractions(player);

    }
//    @Test
//    void playerCollidesBerry(){
//        /*
//        * There aren't any berries in this game :/
//        * */
//    }
//
//    @Test
//    void playerCollidesBlueGhost(){
//        /*
//         * Need berries to work
//         * */
//    }


}
