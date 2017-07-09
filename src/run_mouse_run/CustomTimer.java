package run_mouse_run;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;


public class CustomTimer
{
    private Time startTime;
    private Time currentTime = new Time(0); // In milliseconds
    private final int GAME_SPEED = 1000; // In milliseconds
    public static final int POWERUP_DURABILITY = 10000; // In milliseconds
    private final long TIME_LIMIT = 180000; // In milliseconds
    private Timer timer;
    private TimerTask task;


    public CustomTimer()
    {
        startTime = Time.valueOf(LocalTime.now());

        task = new TimerTask()
        {
            @Override
            public void run()
            {
                currentTime.setTime(Time.valueOf(LocalTime.now()).getTime() - startTime.getTime());

                if (currentTime.getTime() > TIME_LIMIT)
                {
                    GameManager.stopGame("Everybody Lose", "");
                }
            }

        };
        timer = new Timer();

    }

    public void startTimer()
    {
        timer.scheduleAtFixedRate(task, 0, GAME_SPEED);
    }
    public void stopTimer()
    {

    }
}
