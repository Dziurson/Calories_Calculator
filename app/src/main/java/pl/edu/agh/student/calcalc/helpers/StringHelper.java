package pl.edu.agh.student.calcalc.helpers;

public class StringHelper {
    public static String concat(String... parts) {
        StringBuilder builder = new StringBuilder();
        for(String part : parts) {
            builder.append(part);
        }
        return builder.toString();
    }
}
