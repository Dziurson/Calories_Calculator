package pl.edu.agh.student.calcalc.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import pl.edu.agh.student.calcalc.activities.MainActivity;


public class ActivityHelper {
    public static void findOrCreateActivity(Context activityContext, Class<? extends Activity> activityClass){
        Intent intActivityHandler = new Intent(activityContext,activityClass);
        intActivityHandler.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activityContext.startActivity(intActivityHandler);
    }

    public static boolean checkForPermissions(Activity activity, String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
