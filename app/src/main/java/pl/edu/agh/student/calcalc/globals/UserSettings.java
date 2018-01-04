package pl.edu.agh.student.calcalc.globals;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.enums.ActivityType;
import pl.edu.agh.student.calcalc.enums.InterpolationState;
import pl.edu.agh.student.calcalc.enums.UserGender;
import pl.edu.agh.student.calcalc.types.PropertyInteger;
import pl.edu.agh.student.calcalc.enums.OutputFileFormat;
import pl.edu.agh.student.calcalc.enums.VelocityUnit;

public class UserSettings {
    public static VelocityUnit usedVelocity = VelocityUnit.VELOCITY_IN_MPS;
    public static OutputFileFormat exportFileFormat = OutputFileFormat.GPX;
    public static String userFileDirectory = "/CalCalc/data/";
    public static String testDir = "/CalCalc/";
    public static boolean isMapVisibleOnStartup = false;
    public static PropertyInteger delayBetweenPoints = new PropertyInteger(0,R.string.no_points);
    public static PropertyInteger userWeight = new PropertyInteger(50, R.string.zero);
    public static PropertyInteger userHeight = new PropertyInteger(156,R.string.zero);
    public static PropertyInteger userAge = new PropertyInteger(20,R.string.zero);
    public static UserGender userGender = UserGender.GENDER_MALE;
    public static InterpolationState interpolationState = InterpolationState.ENABLED;
    public static ActivityType activityType = ActivityType.WALKING;
}
