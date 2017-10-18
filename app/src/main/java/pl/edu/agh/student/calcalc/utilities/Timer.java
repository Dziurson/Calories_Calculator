package pl.edu.agh.student.calcalc.utilities;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.TimerTask;

import pl.edu.agh.student.calcalc.helpers.DateHelper;

/**
 * Created by jakub on 14.10.2017.
 */

public class Timer {

    private boolean isStarted;
    private Activity context;
    private boolean isPaused;
    private Date startDate;
    private Date pausedDate;
    private java.util.Timer timer;
    private TimerTask timertask;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    private final TextView textViewToUpdate;


    public Timer(@NonNull final TextView textViewToUpdate, @NonNull Activity context) {
        this.context = context;
        this.textViewToUpdate = textViewToUpdate;
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        isStarted = false;
    }

    public void start() {
        timer = new java.util.Timer();
        isStarted = true;
        isPaused = false;
        timertask = new TimerTask() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!isPaused)
                            textViewToUpdate.setText(timeFormat.format(new Date(DateHelper.getIntervalWithCurrentDate(startDate))));
                    }
                });
            }
        };
        startDate = new Date();
        timer.scheduleAtFixedRate(timertask,startDate,10);
    }

    public void stop() {
        timertask.cancel();
        timer.cancel();
        timer.purge();
        isStarted = false;
    }

    public void resume() {
        startDate = new Date(startDate.getTime() + DateHelper.getIntervalWithCurrentDate(pausedDate));
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
