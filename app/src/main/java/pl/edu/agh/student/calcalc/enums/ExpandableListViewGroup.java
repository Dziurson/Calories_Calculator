package pl.edu.agh.student.calcalc.enums;

import pl.edu.agh.student.calcalc.R;

public enum ExpandableListViewGroup {
    VELOCITY_UNITS(R.layout.settings_expandable_list_group_velocity, R.id.settings_group_velocity_header, R.id.settings_group_velocity_value, R.string.velocity_label),
    EXPORT_FILE_TYPE(R.layout.settings_expandable_list_group_file, R.id.settings_group_file_header, R.id.settings_group_file_value, R.string.output_file_type),
    TIME(R.layout.main_expandable_list_group_time,R.id.main_group_time_header,-1,R.string.timer_label),
    LOCATION(R.layout.main_expandable_list_group_location,R.id.main_group_location_header,-1,R.string.location_label),
    ALTITUDE(R.layout.main_expandable_list_group_altitude,R.id.main_group_altitude_header,-1,R.string.altitude_label),
    VELOCITY(R.layout.main_expandable_list_group_velocity,R.id.main_group_velocity_header,-1,R.string.velocity_label),
    MAP_POINTS(R.layout.settings_expandable_list_group_map_points,R.id.settings_group_map_points_header,R.id.settings_group_map_points_value,R.string.map_points);

    public final int stringResourceId;
    public final int headerResourceId;
    public final int layoutResourceId;
    public final int valueResourceId;

    ExpandableListViewGroup(int layoutResourceId_, int headerResourceId_, int valueResourceId_, int stringResourceId_) {
        this.layoutResourceId = layoutResourceId_;
        this.headerResourceId = headerResourceId_;
        this.valueResourceId = valueResourceId_;
        this.stringResourceId = stringResourceId_;
    }
}