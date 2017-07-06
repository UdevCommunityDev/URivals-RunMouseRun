import java.util.concurrent.ThreadLocalRandom;

public class LevelGenerator
{

    private final int MAP_WIDTH_MIN = 30;
    private final int MAP_WIDTH_MAX = 50;

    private final int MAP_HEIGHT_MIN = 30;
    private final int MAP_HEIGHT_MAX = 50;

    public static int MAP_WIDTH = 0;
    public static int MAP_HEIGHT = 0;

    private static Position MOUSES_INITIAL_POS;
    private static Position CATS_INITIAL_POS;

    private Tile[][] map;

    private final int WALL_PROBABILITY_THRESHOLD = 35;
    private final int POWERUP_VISION_PROBABILITY_THRESHOLD = WALL_PROBABILITY_THRESHOLD + 3;    // 3%
    private final int POWERUP_SPEED_PROBABILITY_THRESHOLD = POWERUP_VISION_PROBABILITY_THRESHOLD + 3; // 3%
    private final int INVISIBLE_ZONE_PROBABILITY_THRESHOLD = POWERUP_SPEED_PROBABILITY_THRESHOLD + 6; // 6%
    private final int MINE_PROBABILITY_THRESHOLD = INVISIBLE_ZONE_PROBABILITY_THRESHOLD + 6;  // 6%
    private final int EMPTY_PATH_PROBABILITY_THRESHOLD = 100;

    // IMPORTANT : MAZE[POS_Y][POS_X]


    public LevelGenerator()
    {
        // nextInt is normally exclusive of the top value,
        // so we add 1 to make it inclusive
        this.MAP_WIDTH = ThreadLocalRandom.current().nextInt(MAP_WIDTH_MIN, MAP_WIDTH_MAX + 1);
        this.MAP_HEIGHT = ThreadLocalRandom.current().nextInt(MAP_HEIGHT_MIN, MAP_HEIGHT_MAX + 1);

        // Generate map
        map = generateRandomMap(MAP_WIDTH, MAP_HEIGHT);

        // Set objects initial position

    }

    public Tile[][] getMap()
    {
        return map;
    }

    public void spawnPowerup()
    {

    }

    public boolean existPath(Position source, Position Destination)
    {
        return false;
    }

    public Tile[][] generateRandomMap(int mapWidth, int mapHeight)
    {
        Tile[][] randomMap = new Tile[MAP_HEIGHT][MAP_WIDTH];
        for(int i = 0; i < MAP_HEIGHT; i++)
        {
            for(int j = 0; j < MAP_WIDTH; j++)
            {
                int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);

                if(randomNum < WALL_PROBABILITY_THRESHOLD)
                {
                    randomMap[i][j] = Tile.WALL;
                }
                else if (randomNum < POWERUP_VISION_PROBABILITY_THRESHOLD)
                {
                    randomMap[i][j] = Tile.EMPTY;  // what's vision ?
                }
                else if (randomNum < POWERUP_SPEED_PROBABILITY_THRESHOLD)
                {
                    randomMap[i][j] = Tile.EMPTY;  // Speed
                }
                else if(randomNum < INVISIBLE_ZONE_PROBABILITY_THRESHOLD)
                {
                    randomMap[i][j] = Tile.EMPTY;  // invisible
                }
                else if(randomNum < MINE_PROBABILITY_THRESHOLD)
                {
                    randomMap[i][j] = Tile.MINE;
                }
                else
                {
                    randomMap[i][j] = Tile.EMPTY;
                }
            }
        }

        return randomMap;
    }

    public Position getEmptyPos()
    {
        while (true)
        {
            // get random position
            int x = ThreadLocalRandom.current().nextInt(0, MAP_WIDTH);
            int y = ThreadLocalRandom.current().nextInt(0, MAP_HEIGHT);

            if (map[y][x] != Tile.WALL)
            {
                return new Position(x, y);
            }
        }
    }

    public void switchTile(int x, int y)
    {
        map[y][x] = map[y][x].next();
    }



}