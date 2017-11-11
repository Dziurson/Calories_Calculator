package pl.edu.agh.student.calcalc.helpers;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileHelper {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmssSSS");

    public static String getExportFileName() {
        return format.format(new Date());
    }
}
