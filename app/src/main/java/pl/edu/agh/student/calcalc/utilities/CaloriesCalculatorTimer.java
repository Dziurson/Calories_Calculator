package pl.edu.agh.student.calcalc.utilities;

import android.support.annotation.NonNull;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jakub on 14.10.2017.
 */

public class CaloriesCalculatorTimer {

    private boolean isStarted;
    private boolean isPaused;
    private Date startDate;
    private Date pausedDate;
    private Timer timer;
    private TimerTask timertask;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    private final TextView textViewToUpdate;


    public CaloriesCalculatorTimer(@NonNull final TextView textViewToUpdate) {
        this.textViewToUpdate = textViewToUpdate;
        isStarted = false;
    }

    public void start() {
        timer = new Timer();
        timertask = new TimerTask() {
            @Override
            public void run() {
                if (!isPaused) {
                    textViewToUpdate.setText(timeFormat.format(new Date(new Date().getTime() - startDate.getTime())));
                }
            }
        };
        startDate = new Date();
        timer.scheduleAtFixedRate(timertask,startDate,10);
        isStarted = true;
        isPaused = false;
    }

    public void stop() {
        timertask.cancel();
        timer.cancel();
        timer.purge();
        isStarted = false;
    }

    public void resume() {
        startDate = new Date(startDate.getTime() + new Date().getTime() - pausedDate.getTime());
        isPaused = false;
    }

    public void pause() {
        isPaused = true;
        pausedDate = new Date();
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
