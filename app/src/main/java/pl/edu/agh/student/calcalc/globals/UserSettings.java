package pl.edu.agh.student.calcalc.globals;

import pl.edu.agh.student.calcalc.enums.OutputFileFormat;
import pl.edu.agh.student.calcalc.enums.VelocityType;

public class UserSettings {
    public static VelocityType usedVelocity = VelocityType.VELOCITY_IN_MPS;
    public static OutputFileFormat exportFileFormat = OutputFileFormat.GPX;
    public static String userFileDirectory = "/CalCalc/data/";
}
