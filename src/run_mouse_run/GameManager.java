package run_mouse_run;

import java.awt.*;
import java.util.ArrayList;


public class GameManager   /// TODO : access GameManager with static Vs references
{
    public static final int CATS_NUMBER = 2;
    public static final int MOUSES_NUMBER = 1;

    private ArrayList<CharacterController> cats;
    private ArrayList<CharacterController> mouses;

    private final LevelGenerator level;
    private final PhysicsEngine physicsEngine;
    private final DrawEngine frame;
    private final Timer timer;

    public GameManager()
    {
        level = new LevelGenerator();
        physicsEngine = new PhysicsEngine();
        frame = new DrawEngine(level.getMap());
        timer = new Timer();
    }

    private void startGame() throws Exception
    {
        // Instantiate mouses (do not use a loop, here we may instantiate mouses from different classes)


        // Instantiate cats (do not use a loop, here we may instantiate cats from different classes)

        // Check instantiated characters number
        //if(cats.size() != CATS_NUMBER || mouses.size() != MOUSES_NUMBER)
           // throw new Exception("Not enough characters instantiated");

        // Display game
        frame.setVisible(true);

        // Start run_mouse_run.Timer
    }

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    GameManager gameManager = new GameManager();
                    gameManager.startGame();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }


    public void stopGame(String result, String characterName)
    {
        switch (result)
        {
            case "Mouse Win":
                break;
            case "Mouse Lose":
                break;
            case "Everybody Lose":
                break;
            case "Cat Win":
                break;

            default:
        }
    }

}
