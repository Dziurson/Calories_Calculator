package pl.edu.agh.student.calcalc.enums;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.interfaces.IResourced;

public enum VelocityType implements IResourced{
    VELOCITY_IN_MPS(R.string.meters_per_second),
    VELOCITY_IN_KPH(R.string.kilometers_per_hour);

    private final int resourceId;

    VelocityType(int resourceId_) {
        this.resourceId = resourceId_;
    }

    @Override
    public int getResourceId() {
        return resourceId;
    }

}
