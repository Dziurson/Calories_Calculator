package pl.edu.agh.student.calcalc.enums;

import pl.edu.agh.student.calcalc.R;

public enum ExpandableListViewGroup {
    VELOCITY_UNITS(R.layout.settings_expandable_list_group_velocity, R.id.settings_group_velocity_header, R.id.settings_group_velocity_value, R.string.velocity_label),
    EXPORT_FILE_TYPE(R.layout.settings_expandable_list_group_file, R.id.settings_group_file_header, R.id.settings_group_file_value, R.string.output_file_type),
    MAP_POINTS(R.layout.settings_expandable_list_group_map_points,R.id.settings_group_map_points_header,R.id.settings_group_map_points_value,R.string.map_points),
    USER_WEIGHT(R.layout.user_properties_expandable_list_group_user_weight,R.id.user_properties_group_user_weight_header,R.id.user_properties_group_user_weight_value,R.string.user_weight),
    USER_HEIGHT(R.layout.user_properties_expandable_list_group_user_height,R.id.user_properties_group_user_height_header,R.id.user_properties_group_user_height_value,R.string.user_height),
    USER_AGE(R.layout.user_properties_expandable_list_group_user_age,R.id.user_properties_group_user_age_header,R.id.user_properties_group_user_age_value,R.string.user_age),
    USER_GENDER(R.layout.user_properties_expandable_list_group_user_gender,R.id.user_properties_group_user_gender_header,R.id.user_properties_group_user_gender_value,R.string.user_gender),
    INTERPOLATION(R.layout.settings_expandable_list_group_interpolation_enabled,R.id.settings_group_interpolation_enabled_header,R.id.settings_group_interpolation_enabled_value,R.string.interpolation_label),
    ACTIVITY_TYPE(R.layout.user_properties_expandable_list_group_activity_type,R.id.user_properties_group_activity_type_header,R.id.user_properties_group_activity_type_value,R.string.activity_type_header);

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
