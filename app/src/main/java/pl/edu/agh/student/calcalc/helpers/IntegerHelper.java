package pl.edu.agh.student.calcalc.helpers;

/**
 * Created by jakub on 18.10.2017.
 */

public class IntegerHelper {
    public static int tryParse(String parseString, int defaultValue) {
        try {
            return Integer.parseInt(parseString);
        }
        catch (NumberFormatException ex) {
            return defaultValue;
        }

    }
}
