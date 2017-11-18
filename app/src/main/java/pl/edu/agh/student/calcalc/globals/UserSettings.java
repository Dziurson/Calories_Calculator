package pl.edu.agh.student.calcalc.globals;

import pl.edu.agh.student.calcalc.containers.CustomInteger;
import pl.edu.agh.student.calcalc.enums.OutputFileFormat;
import pl.edu.agh.student.calcalc.enums.VelocityUnit;

public class UserSettings {
    public static VelocityUnit usedVelocity = VelocityUnit.VELOCITY_IN_MPS;
    public static OutputFileFormat exportFileFormat = OutputFileFormat.GPX;
    public static String userFileDirectory = "/CalCalc/data/";
    public static CustomInteger delayBetweenPoints = new CustomInteger(1);
}
