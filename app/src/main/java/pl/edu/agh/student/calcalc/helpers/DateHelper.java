package pl.edu.agh.student.calcalc.helpers;

import java.util.Date;

/**
 * Created by jakub on 18.10.2017.
 */

public class DateHelper {
    public static long getInterval(Date toDate, Date fromDate) {
        return toDate.getTime() - fromDate.getTime();
    }

    public static long getIntervalWithCurrentDate(Date date) {
        return new Date().getTime() - date.getTime();
    }
}
