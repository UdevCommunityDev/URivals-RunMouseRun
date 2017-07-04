
/**
 *
 * @author Oussama
 */
public enum Tile {
    
    EMPTY,
    WALL,
    MINE,
    CHEESE;
    
	private static Tile[] vals = values();
    
	public Tile next()
    {
        return vals[(this.ordinal()+1) % vals.length];
    }
}
