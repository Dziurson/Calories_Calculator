package pl.edu.agh.student.calcalc.enums;

import android.content.Context;
import android.support.annotation.NonNull;

import pl.edu.agh.student.calcalc.R;

public enum ExpandableListGroupType {
    VELOCITY_UNITS(R.layout.expandable_list_group_velocity, R.id.group_velocity_header, R.id.group_velocity_value, R.string.velocity_label),
    EXPORT_FILE_TYPE(R.layout.expandable_list_group_file, R.id.group_file_header, R.id.group_file_value, R.string.dmi_import_file_label);

    public final int stringResourceId;
    public final int headerResourceId;
    public final int layoutResourceId;
    public final int valueResourceId;

    ExpandableListGroupType(int layoutResourceId_, int headerResourceId_, int valueResourceId_, int stringResourceId_) {
        this.layoutResourceId = layoutResourceId_;
        this.headerResourceId = headerResourceId_;
        this.valueResourceId = valueResourceId_;
        this.stringResourceId = stringResourceId_;
    }
}
