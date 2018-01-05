package pl.edu.agh.student.calcalc.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import pl.edu.agh.student.calcalc.activities.MainActivity;
import pl.edu.agh.student.calcalc.globals.Properties;
import pl.edu.agh.student.calcalc.globals.UserSettings;


public class ActivityHelper {
    public static void findOrCreateActivity(Context activityContext, Class<? extends Activity> activityClass){
        Intent intActivityHandler = new Intent(activityContext,activityClass);
        intActivityHandler.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activityContext.startActivity(intActivityHandler);
    }

    public static boolean checkForPermissions(Activity activity, String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean savePropertiesState(Activity activity) {
        File state = new File(activity.getFilesDir(), Properties.stateFile);
        if(!state.exists()) {
            try {
                state.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(state);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(StringHelper.concat("velocityUnit",";", UserSettings.usedVelocity.toString()));
            writer.newLine();
            writer.write(StringHelper.concat("exportFileFormat",";",UserSettings.exportFileFormat.toString()));
            writer.newLine();
            writer.write(StringHelper.concat("isMapVisible",";",((Boolean)UserSettings.isMapVisibleOnStartup).toString()));
            writer.newLine();
            writer.write(StringHelper.concat("delayBetweenPoints",";",((Integer)UserSettings.delayBetweenPoints.getValue()).toString()));
            writer.newLine();
            writer.write(StringHelper.concat("userHeight",";",((Integer)UserSettings.userHeight.getValue()).toString()));
            writer.newLine();
            writer.write(StringHelper.concat("userAge",";",((Integer)UserSettings.userAge.getValue()).toString()));
            writer.newLine();
            writer.write(StringHelper.concat("userGender",";",UserSettings.userGender.toString()));
            writer.newLine();
            writer.write(StringHelper.concat("interpolationState",";",UserSettings.interpolationState.toString()));
            writer.newLine();
            writer.write(StringHelper.concat("userWeight",";",((Integer)UserSettings.userWeight.getValue()).toString()));
            writer.newLine();
            writer.write(StringHelper.concat("activityType",";",UserSettings.activityType.toString()));
            writer.close();
            fos.close();
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
