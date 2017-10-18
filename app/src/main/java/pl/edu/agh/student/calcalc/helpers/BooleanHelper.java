package pl.edu.agh.student.calcalc.helpers;

/**
 * Created by jakub on 18.10.2017.
 */

public class BooleanHelper {
    public static boolean tryParse(String parseString, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(parseString);
        }
        catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
