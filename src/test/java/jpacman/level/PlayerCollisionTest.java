package jpacman.level;

import jpacman.points.DefaultPointCalculator;

public class PlayerCollisionTest extends CollisionsTest{
    public PlayerCollisionTest(){
        setCmap(new PlayerCollisions(new DefaultPointCalculator()));
    }
}
