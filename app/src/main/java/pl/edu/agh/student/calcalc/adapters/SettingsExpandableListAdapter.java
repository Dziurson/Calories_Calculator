package pl.edu.agh.student.calcalc.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.containers.Tuple;
import pl.edu.agh.student.calcalc.controls.CustomButton;
import pl.edu.agh.student.calcalc.enums.ExpandableListChildType;
import pl.edu.agh.student.calcalc.enums.ExpandableListGroupType;
import pl.edu.agh.student.calcalc.enums.OutputFileFormat;
import pl.edu.agh.student.calcalc.enums.VelocityType;
import pl.edu.agh.student.calcalc.globals.UserSettings;
import pl.edu.agh.student.calcalc.interfaces.IProperty;

public class SettingsExpandableListAdapter extends CustomExpandableListAdapter {

    LayoutInflater layoutFactory = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    public SettingsExpandableListAdapter(Activity context, List<Tuple<ExpandableListGroupType,List<ExpandableListChildType>>> initializationList) {
        super(context,initializationList);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        switch ((ExpandableListGroupType) getGroup(groupPosition)) {
            case EXPORT_FILE_TYPE:
                convertView = groupInit(ExpandableListGroupType.EXPORT_FILE_TYPE,UserSettings.exportFileFormat);
                break;
            case VELOCITY_UNITS:
                convertView = groupInit(ExpandableListGroupType.VELOCITY_UNITS,UserSettings.usedVelocity);
                break;
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        switch((ExpandableListChildType) getChild(groupPosition, childPosition)) {
            case FILE_TYPE:
                convertView = fileTypeChildInit(convertView,parent);
                break;
            case VELOCITY_UNITS:
                convertView = velocityUnitChildInit(convertView,parent);
                break;
        }
        return convertView;
    }

    private View velocityUnitChildInit(View child, ViewGroup parent) {
        View convertView = layoutFactory.inflate(R.layout.settings_expandable_list_child_file, null);
        final CustomButton metersPerSecondButton = (CustomButton) child.findViewById(R.id.settings_expandable_list_velocity_ms_button);
        final CustomButton kilometersPerHourButton = (CustomButton) child.findViewById(R.id.settings_expandable_list_velocity_kph_button);
        metersPerSecondButton.setFocusable(false);
        kilometersPerHourButton.setFocusable(false);
        final TextView headerTextView = (TextView) parent.findViewById(R.id.settings_group_velocity_value);
        switch(UserSettings.usedVelocity) {
            case VELOCITY_IN_MPS:
                metersPerSecondButton.setButtonSelected(true);
                kilometersPerHourButton.setButtonSelected(false);
                break;
            case VELOCITY_IN_KPH:
                kilometersPerHourButton.setButtonSelected(true);
                metersPerSecondButton.setButtonSelected(false);
                break;
        }
        headerTextView.setText(context.getString(UserSettings.usedVelocity.getStringResourceId()));
        metersPerSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(UserSettings.usedVelocity == VelocityType.VELOCITY_IN_MPS)) {
                    metersPerSecondButton.setButtonSelected(true);
                    kilometersPerHourButton.setButtonSelected(false);
                    UserSettings.usedVelocity = VelocityType.VELOCITY_IN_MPS;
                    headerTextView.setText(context.getString(UserSettings.usedVelocity.getStringResourceId()));
                }
            }
        });
        kilometersPerHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(UserSettings.usedVelocity == VelocityType.VELOCITY_IN_KPH)) {
                    kilometersPerHourButton.setButtonSelected(true);
                    metersPerSecondButton.setButtonSelected(false);
                    UserSettings.usedVelocity = VelocityType.VELOCITY_IN_KPH;
                    headerTextView.setText(context.getString(UserSettings.usedVelocity.getStringResourceId()));
                }
            }
        });
        return convertView;
    }

    private View fileTypeChildInit(View child, View parent) {
        View convertView = layoutFactory.inflate(R.layout.settings_expandable_list_child_file, null);
        final CustomButton gpxFileTypeButton = (CustomButton) child.findViewById(R.id.settings_expandable_list_file_gpx);
        final CustomButton kmlFileTypeButton = (CustomButton) child.findViewById(R.id.settings_expandable_list_file_kml);
        gpxFileTypeButton.setFocusable(false);
        kmlFileTypeButton.setFocusable(false);
        final TextView headerTextView = (TextView) parent.findViewById(R.id.settings_group_file_value);
        switch(UserSettings.exportFileFormat) {
            case KML:
                kmlFileTypeButton.setButtonSelected(true);
                gpxFileTypeButton.setButtonSelected(false);
                break;
            case GPX:
                gpxFileTypeButton.setButtonSelected(true);
                kmlFileTypeButton.setButtonSelected(false);
                break;
        }
        headerTextView.setText(context.getString(UserSettings.exportFileFormat.getStringResourceId()));
        gpxFileTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(UserSettings.exportFileFormat == OutputFileFormat.GPX)) {
                    gpxFileTypeButton.setButtonSelected(true);
                    kmlFileTypeButton.setButtonSelected(false);
                    UserSettings.exportFileFormat = OutputFileFormat.GPX;
                    headerTextView.setText(context.getString(UserSettings.exportFileFormat.getStringResourceId()));
                }
            }
        });
        kmlFileTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(UserSettings.exportFileFormat == OutputFileFormat.KML)) {
                    kmlFileTypeButton.setButtonSelected(true);
                    gpxFileTypeButton.setButtonSelected(false);
                    UserSettings.exportFileFormat = OutputFileFormat.KML;
                    headerTextView.setText(context.getString(UserSettings.exportFileFormat.getStringResourceId()));
                }
            }
        });
        return convertView;
    }

    private View groupInit(ExpandableListGroupType groupType, IProperty propertyToUpdate){
        View convertView = layoutFactory.inflate(groupType.layoutResourceId,null);
        if(groupType.valueResourceId != -1) {
            TextView headerValueTextView = (TextView) convertView.findViewById(groupType.valueResourceId);
            headerValueTextView.setText(context.getString(propertyToUpdate.getStringResourceId()));
        }
        TextView headerKeyTextView = (TextView) convertView.findViewById(groupType.headerResourceId);
        headerKeyTextView.setTypeface(null, Typeface.BOLD);
        headerKeyTextView.setText(context.getString(groupType.stringResourceId));
        return convertView;
    }
}
