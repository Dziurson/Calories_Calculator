package pl.edu.agh.student.calcalc.enums;

import pl.edu.agh.student.calcalc.R;

public enum VelocityType {
    VELOCITY_IN_MPS(R.string.meters_per_second) {
        @Override
        public String toString() {
            return "VELOCITY_IN_MPS";
        }
    },
    VELOCITY_IN_KPH(R.string.kilometers_per_hour) {
        @Override
        public String toString() {
            return "VELOCITY_IN_KPH";
        }
    };

    private final int resourceId;

    VelocityType(int resourceId_) {
        this.resourceId = resourceId_;
    }

    public int getStringResourceId() {
        return this.resourceId;
    }

}
