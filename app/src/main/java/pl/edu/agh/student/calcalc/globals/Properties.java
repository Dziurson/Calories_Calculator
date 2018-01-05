package pl.edu.agh.student.calcalc.globals;

import android.app.Activity;

import static android.os.Environment.getExternalStorageDirectory;

public class Properties {
    public static Activity mainActivity = null;
    public static final int PERMISSION_TO_WRITE_EXTERNAL_STORAGE = 100;
    public static final int PERMISSION_TO_ACCESS_FINE_LOCATION = 102;
    public static final String stateFile = "applicationState";
}
