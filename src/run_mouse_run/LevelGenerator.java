package run_mouse_run;

import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class LevelGenerator
{
    private final int MAP_WIDTH_MIN = 30;
    private final int MAP_WIDTH_MAX = 70;

    private final int MAP_HEIGHT_MIN = 30;
    private final int MAP_HEIGHT_MAX = 50;

    public static int MAP_WIDTH = 0;
    public static int MAP_HEIGHT = 0;

    public static Position MOUSES_INITIAL_POS;
    public static Position CATS_INITIAL_POS;
    private final static int INITIAL_MINIMUM_DISTANCE = 15; // minimum eloignés de 15 cases

    private Map map;

    private final int WALL_PROBABILITY_THRESHOLD = 35;
    private final int POWERUP_VISION_PROBABILITY_THRESHOLD = WALL_PROBABILITY_THRESHOLD + 1;    // 1%
    private final int POWERUP_SPEED_PROBABILITY_THRESHOLD = POWERUP_VISION_PROBABILITY_THRESHOLD + 1; // 1%
    private final int INVISIBLE_ZONE_PROBABILITY_THRESHOLD = POWERUP_SPEED_PROBABILITY_THRESHOLD + 1; // 1%
    private final int MINE_PROBABILITY_THRESHOLD = INVISIBLE_ZONE_PROBABILITY_THRESHOLD + 1;  // 1%
    private final int EMPTY_PATH_PROBABILITY_THRESHOLD = 100;

    // IMPORTANT : MAZE[POS_Y][POS_X]

    private PathFinder pathFinder;

    public LevelGenerator()
    {
        pathFinder = new PathFinder();

        // nextInt is normally exclusive of the top value,
        // so we add 1 to make it inclusive
        this.MAP_WIDTH = ThreadLocalRandom.current().nextInt(MAP_WIDTH_MIN, MAP_WIDTH_MAX + 1);
        this.MAP_HEIGHT = ThreadLocalRandom.current().nextInt(MAP_HEIGHT_MIN, MAP_HEIGHT_MAX + 1);

        // Generate map
        map = generateRandomMap(MAP_WIDTH, MAP_HEIGHT);

        // Set objects initial position
        setInitialPosition();

    }

    public Map getMap()
    {
        return map;
    }

    public void setMap(int width, int height)
    {
        map = generateRandomMap(width, height);
        setInitialPosition();
    }
    public void spawnVisionPowerup()
    {

    }

    public void spawnSpeedPowerup()
    {

    }

    /***
     * Set MOUSE, CATS and Cheese initial positions and checks if correct
     * correct position : Path exists, space consists 70% of empty Tiles
     * ... else regenerate a new map
     */
    private void setInitialPosition()
    {
        /* Set Mouse initial pos*/
        do
        {
            MOUSES_INITIAL_POS = getEmptyPos();
            CATS_INITIAL_POS = getEmptyPos();
        }
        while( !isPathOkay(MOUSES_INITIAL_POS, CATS_INITIAL_POS, INITIAL_MINIMUM_DISTANCE) // repeat until path exists
                );  // And mouses and cats are far enough

        /*Add Mouse and cats to the map */
        map.setTile(MOUSES_INITIAL_POS.getPosX(), MOUSES_INITIAL_POS.getPosY(), Tile.MOUSE);
        map.setTile(CATS_INITIAL_POS.getPosX(), CATS_INITIAL_POS.getPosY(), Tile.CAT);


        /*Spawn cheese*/
        for (int i = 0; i < GameManager.gameManager.CAT_NUMBER+1; i++)
        {
            Position cheesePos;
            do{
                cheesePos = getEmptyPos();
            } while (!isPathOkay(MOUSES_INITIAL_POS, cheesePos, INITIAL_MINIMUM_DISTANCE) ||
            map.getTile(cheesePos.getPosX(), cheesePos.getPosY()) == Tile.CHEESE);

            // path exists, we add the cheese to the map
            map.setTile(cheesePos.getPosX(), cheesePos.getPosY(), Tile.CHEESE);
        }

        int cellCount = getEmptyCellCount(MOUSES_INITIAL_POS, new ArrayList<Position>());

        if(cellCount < MAP_HEIGHT*MAP_WIDTH*WALL_PROBABILITY_THRESHOLD/100*2/3) // 2/3 /// TODO : test values
        {
            // Generate map
            map = generateRandomMap(MAP_WIDTH, MAP_HEIGHT);
            // Set objects initial position
            setInitialPosition();
        }
        else
        {
            System.out.println("OK");
        }
    }

    /**
     * Checks if path exists, and distance is far enough ( >= INITIAL_MINIMUM_DISTANCE )
     */
    private boolean isPathOkay(Position src, Position dst, int dist)
    {
        ArrayList<Position> path = pathFinder.getShortestPath(map, src, dst);

        if(path.isEmpty() || path.size() < dist)
            return false;
        else
            return true;
    }

    /**
     * compute path using run_mouse_run.PathFinder
     * @return true if path exists
     */
    public boolean existPath(Position source, Position destination)
    {
        if(Position.comparePosition(source, destination))
            return true;

        if(pathFinder.getShortestPath(map, source, destination).isEmpty())
            return false;
        else
            return true;
    }

    /**
    * Generates a map random using Probability constants
     * @param mapHeight
     * @param mapWidth
     * @return map (Map) , the generated map ( doesn't modify this class' map )
    */
    private Map generateRandomMap(int mapWidth, int mapHeight)
    {
        Map randomMap = new Map("Level Map", MAP_WIDTH, MAP_HEIGHT, Tile.EMPTY);

        for(int i = 0; i < MAP_HEIGHT; i++)
        {
            for(int j = 0; j < MAP_WIDTH; j++)
            {
                int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);

                if(randomNum < WALL_PROBABILITY_THRESHOLD)
                {
                    randomMap.setTile(j, i, Tile.WALL);
                }
                else if (randomNum < POWERUP_VISION_PROBABILITY_THRESHOLD)
                {
                    randomMap.setTile(j, i, Tile.POWERUP_VISION);
                }
                else if (randomNum < POWERUP_SPEED_PROBABILITY_THRESHOLD)
                {
                    randomMap.setTile(j, i, Tile.POWERUP_SPEED);
                }
                else if(randomNum < INVISIBLE_ZONE_PROBABILITY_THRESHOLD)
                {
                    randomMap.setTile(j, i, Tile.INVISIBLE_ZONE);
                }
                else if(randomNum < MINE_PROBABILITY_THRESHOLD)
                {
                    randomMap.setTile(j, i, Tile.MINE);
                }
                else
                {
                    randomMap.setTile(j, i, Tile.EMPTY);
                }
            }
        }

        return randomMap;
    }

    /**
     * Counts the number of reachable cells from a given start point
     * @param start any position in given space to count
     * @param visited an empty ArrayList to keep track of visited nodes when visiting
     * @return the number of tiles in that space
     */
    public int getEmptyCellCount(Position start, ArrayList<Position> visited)
    {
        boolean isOutOfBound = (start.getPosX() >= MAP_WIDTH || start.getPosX() < 0
                                || start.getPosY() >= MAP_HEIGHT || start.getPosY() < 0);

        boolean isWall = isOutOfBound || (map.getTile(start.getPosX(), start.getPosY()) == Tile.WALL);

        boolean isVisited = false;
        for(Position p : visited)
            if(Position.comparePosition(p,start))
                isVisited = true;

        if(isOutOfBound || isWall || isVisited)
        {
            return 0;
        }
        else
        {
            visited.add(start);
            return 1 +
                    getEmptyCellCount(new Position(start.getPosX()+1, start.getPosY()), visited)
                    + getEmptyCellCount(new Position(start.getPosX()-1, start.getPosY()), visited)
                    + getEmptyCellCount(new Position(start.getPosX(), start.getPosY()+1), visited)
                    + getEmptyCellCount(new Position(start.getPosX(), start.getPosY()-1), visited);
        }


    }

    /**
     * keep generating random x, y coordinates until getting an empty position
     * @return an Empty run_mouse_run.Position
     */
    public Position getEmptyPos()
    {
        while (true)
        {
            // get random position
            int x = ThreadLocalRandom.current().nextInt(0, MAP_WIDTH);
            int y = ThreadLocalRandom.current().nextInt(0, MAP_HEIGHT);

            if (map.getTile(x, y) == Tile.EMPTY)
            {
                return new Position(x, y);
            }
        }
    }

    /**
     * Get a random position far enough from every mouse/cat
     * @return Position : a valid respawn position
     */
    public Position getValidRespawnPosition()
    {
        int minDist = INITIAL_MINIMUM_DISTANCE;

        while (minDist > 0)
        {
            int tests = 0;
            /*Try 3 times */
            while (tests < 3)
            {
                Position randomPos = getEmptyPos();
                boolean okay = true;

                // Check MINIMUM_DISTANCE from mouses
                for (Mouse m : GameManager.gameManager.getMouses())
                {
                    if (!isPathOkay(randomPos, m.getPosition(), minDist))
                        okay = false;
                }

                // Check MINIMUM_DISTANCE from cats
                for (Cat c : GameManager.gameManager.getCats())
                {
                    if (!isPathOkay(randomPos, c.getPosition(), minDist))
                        okay = false;
                }

                if (okay)
                    return randomPos;

                tests++;
            }
            /*If not found, reduce minimal distance*/
            minDist--;
        }

        return getEmptyPos(); // if none, ..
    }

    public final Map getViewedMap(Position position, int viewDistance)
    {
        Map viewMap = new Map("", viewDistance +1 , viewDistance+1, Tile.EMPTY);

        Position startPoint = new Position(position.getPosX(), position.getPosY());

        startPoint.setPosX((startPoint.getPosX() - viewDistance >=0)?startPoint.getPosX() - viewDistance:0);
        startPoint.setPosY((startPoint.getPosX() - viewDistance >=0)?startPoint.getPosY() - viewDistance:0);

        for (int i = 0; i < viewDistance && startPoint.getPosX() + i < LevelGenerator.MAP_WIDTH; i++)
            for (int j = 0; j < viewDistance && startPoint.getPosY() + j < LevelGenerator.MAP_HEIGHT; j++)
            {
                viewMap.setTile(i, j, (map.getTile(i, j) != Tile.MINE)? map.getTile(i, j): Tile.EMPTY);
            }

        return viewMap;
    }
}