package jpacman.level;

import jpacman.points.DefaultPointCalculator;

class DefaultPlayerInteractionMapTest extends CollisionsTest{
    public DefaultPlayerInteractionMapTest(){
        setCmap(new DefaultPlayerInteractionMap(new DefaultPointCalculator()));
    }
}