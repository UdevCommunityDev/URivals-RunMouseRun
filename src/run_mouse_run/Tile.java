package run_mouse_run;

/**
 *
 * @author Oussama
 */
public enum Tile {

    NOT_DISCOVERED, EMPTY, WALL, CHEESE, POWERUP_VISION, POWERUP_SPEED, INVISIBLE_ZONE, MINE, CAT, MOUSE;

    private static Tile[] vals = values();

    public Tile next()
    {
        return vals[(this.ordinal()+1) % vals.length];
    }
}

