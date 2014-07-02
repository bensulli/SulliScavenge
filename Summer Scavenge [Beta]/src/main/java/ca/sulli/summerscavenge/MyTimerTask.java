package ca.sulli.summerscavenge;

import android.util.Log;

import java.util.TimerTask;

/**
 * Created by Sullivan on 7/1/2014.
 */
public class MyTimerTask extends TimerTask {

    public GameState updatedGameState = new GameState();

    @Override
    public void run()
    {
        updatedGameState = Interface.UpdateState();

        if(MainActivity.gameState!=updatedGameState)
        {
            MainActivity.gameState = updatedGameState;
            MainActivity.Refresh();
        }
        else
        {
            Log.i(null, "Nothing to update");
        }
    }

}
