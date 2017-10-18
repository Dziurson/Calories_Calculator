package pl.edu.agh.student.calcalc.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jakub on 15.10.2017.
 */

public class ActivityHelper {
    public static void findOrCreateActivity(Context activityContext, Class<? extends Activity> activityClass){
        Intent intActivityHandler = new Intent(activityContext,activityClass);
        intActivityHandler.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activityContext.startActivity(intActivityHandler);
    }
}
