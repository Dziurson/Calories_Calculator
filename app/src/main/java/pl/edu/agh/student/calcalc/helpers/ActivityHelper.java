package pl.edu.agh.student.calcalc.helpers;

import android.content.Context;
import android.content.Intent;

/**
 * Created by jakub on 15.10.2017.
 */

public class ActivityHelper {
    public static void bringActivityToFront(Context activityContext, Class<?> activityClass){
        Intent intActivityHandler = new Intent(activityContext,activityClass);
        intActivityHandler.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activityContext.startActivity(intActivityHandler);
    }
}
