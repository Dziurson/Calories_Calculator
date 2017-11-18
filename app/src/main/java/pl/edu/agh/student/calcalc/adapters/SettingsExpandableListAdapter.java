package pl.edu.agh.student.calcalc.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.containers.Tuple;
import pl.edu.agh.student.calcalc.controls.CustomButton;
import pl.edu.agh.student.calcalc.enums.ExpandableListViewChild;
import pl.edu.agh.student.calcalc.enums.ExpandableListViewGroup;
import pl.edu.agh.student.calcalc.enums.OutputFileFormat;
import pl.edu.agh.student.calcalc.enums.VelocityUnit;
import pl.edu.agh.student.calcalc.globals.UserSettings;
import pl.edu.agh.student.calcalc.interfaces.IPropertyWithResource;

public class SettingsExpandableListAdapter extends CustomExpandableListAdapter {

    LayoutInflater layoutFactory = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    public SettingsExpandableListAdapter(FragmentActivity context, List<Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>>> initializationList) {
        super(context,initializationList);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        switch ((ExpandableListViewGroup) getGroup(groupPosition)) {
            case EXPORT_FILE_TYPE:
                convertView = groupInit(ExpandableListViewGroup.EXPORT_FILE_TYPE,UserSettings.exportFileFormat);
                break;
            case VELOCITY_UNITS:
                convertView = groupInit(ExpandableListViewGroup.VELOCITY_UNITS,UserSettings.usedVelocity);
                break;
            case MAP_POINTS:
                convertView = groupInit(ExpandableListViewGroup.MAP_POINTS,UserSettings.delayBetweenPoints);
                break;
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        switch((ExpandableListViewChild) getChild(groupPosition, childPosition)) {
            case FILE_TYPE:
                convertView = fileTypeChildInit(parent);
                break;
            case VELOCITY_UNITS:
                convertView = velocityUnitChildInit(parent);
                break;
            case MAP_POINTS:
                convertView = layoutFactory.inflate(R.layout.settings_expandable_list_child_map_points, null);
        }
        return convertView;
    }

    private View velocityUnitChildInit(ViewGroup parent) {
        View convertView = layoutFactory.inflate(R.layout.settings_expandable_list_child_velocity, null);
        final CustomButton metersPerSecondButton = (CustomButton) convertView.findViewById(R.id.settings_expandable_list_velocity_ms_button);
        final CustomButton kilometersPerHourButton = (CustomButton) convertView.findViewById(R.id.settings_expandable_list_velocity_kph_button);
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
        headerTextView.setText(UserSettings.usedVelocity.getString(context));
        metersPerSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(UserSettings.usedVelocity == VelocityUnit.VELOCITY_IN_MPS)) {
                    metersPerSecondButton.setButtonSelected(true);
                    kilometersPerHourButton.setButtonSelected(false);
                    UserSettings.usedVelocity = VelocityUnit.VELOCITY_IN_MPS;
                    headerTextView.setText(UserSettings.usedVelocity.getString(context));
                }
            }
        });
        kilometersPerHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(UserSettings.usedVelocity == VelocityUnit.VELOCITY_IN_KPH)) {
                    kilometersPerHourButton.setButtonSelected(true);
                    metersPerSecondButton.setButtonSelected(false);
                    UserSettings.usedVelocity = VelocityUnit.VELOCITY_IN_KPH;
                    headerTextView.setText(UserSettings.usedVelocity.getString(context));
                }
            }
        });
        return convertView;
    }

    private View fileTypeChildInit(ViewGroup parent) {
        View convertView = layoutFactory.inflate(R.layout.settings_expandable_list_child_file, null);
        final CustomButton gpxFileTypeButton = (CustomButton) convertView.findViewById(R.id.settings_expandable_list_file_gpx);
        final CustomButton kmlFileTypeButton = (CustomButton) convertView.findViewById(R.id.settings_expandable_list_file_kml);
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
        headerTextView.setText(UserSettings.exportFileFormat.getString(context));
        gpxFileTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(UserSettings.exportFileFormat == OutputFileFormat.GPX)) {
                    gpxFileTypeButton.setButtonSelected(true);
                    kmlFileTypeButton.setButtonSelected(false);
                    UserSettings.exportFileFormat = OutputFileFormat.GPX;
                    headerTextView.setText(UserSettings.exportFileFormat.getString(context));
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
                    headerTextView.setText(UserSettings.exportFileFormat.getString(context));
                }
            }
        });
        return convertView;
    }

    private View groupInit(ExpandableListViewGroup groupType, IPropertyWithResource propertyToUpdate){
        View convertView = layoutFactory.inflate(groupType.layoutResourceId,null);
        if(groupType.valueResourceId != -1) {
            TextView headerValueTextView = (TextView) convertView.findViewById(groupType.valueResourceId);
            headerValueTextView.setText(propertyToUpdate.getString(context));
        }
        TextView headerKeyTextView = (TextView) convertView.findViewById(groupType.headerResourceId);
        headerKeyTextView.setTypeface(null, Typeface.BOLD);
        headerKeyTextView.setText(context.getString(groupType.stringResourceId));
        return convertView;
    }
}
