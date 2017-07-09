package run_mouse_run;

/**
 * Created by Oussama on 09/07/2017.
 */
public class Map
{
    private Tile[][] map;

    public String name;
    public int height;
    public int width;

    public Map(String name, int width, int height)
    {
        this.name = name;
        map = new Tile[height][width];
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                map[i][j] = Tile.EMPTY;
            }
        }

        this.height = height;
        this.width = width;
    }

    public Tile getTile(int x, int y)
    {
        return map[y][x];
    }

    public void setTile(int x, int y, Tile tile)
    {
        map[y][x] = tile;
    }

    /**
     *
     * @return
     */
    public Position[] getCheesePos()
    {
        return null;
    }

    public void switchTile(int x, int y)
    {
        map[y][x] = map[y][x].next();
    }


}
