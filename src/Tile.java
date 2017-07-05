
/**
 *
 * @author Oussama
 */
public enum Tile {
    
    EMPTY,
    WALL,
    MINE,
    CHEESE,
    INVISIBLE_ZONE,
    POWERUP_VISION,
    POWERUP_SPEED;
    
	private static Tile[] vals = values();
    
	public Tile next()
    {
        return vals[(this.ordinal()+1) % vals.length];
    }
}
