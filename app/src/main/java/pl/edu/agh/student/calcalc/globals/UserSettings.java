package pl.edu.agh.student.calcalc.globals;

import pl.edu.agh.student.calcalc.enums.UserGender;
import pl.edu.agh.student.calcalc.types.PropertyInteger;
import pl.edu.agh.student.calcalc.enums.OutputFileFormat;
import pl.edu.agh.student.calcalc.enums.VelocityUnit;

public class UserSettings {
    public static VelocityUnit usedVelocity = VelocityUnit.VELOCITY_IN_MPS;
    public static OutputFileFormat exportFileFormat = OutputFileFormat.GPX;
    public static String userFileDirectory = "/CalCalc/data/";
    public static boolean isMapVisibleOnStartup = false;
    public static PropertyInteger delayBetweenPoints = new PropertyInteger(1);
    public static PropertyInteger userWeight = new PropertyInteger(50);
    public static PropertyInteger userHeight = new PropertyInteger(156);
    public static PropertyInteger userAge = new PropertyInteger(20);
    public static UserGender userGender = UserGender.GENDER_MALE;
}
