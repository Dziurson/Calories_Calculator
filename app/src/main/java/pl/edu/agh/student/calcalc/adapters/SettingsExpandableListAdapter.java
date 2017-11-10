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
import pl.edu.agh.student.calcalc.interfaces.IResourced;

public class SettingsExpandableListAdapter extends CustomExpandableListAdapter {

    public SettingsExpandableListAdapter(Activity context, List<Tuple<ExpandableListGroupType,List<ExpandableListChildType>>> childrenMap) {
        super(context,childrenMap);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ExpandableListGroupType groupHeader = (ExpandableListGroupType) getGroup(groupPosition);

        switch (groupHeader) {
            case EXPORT_FILE_TYPE:
                convertView = initializeGroup(ExpandableListGroupType.EXPORT_FILE_TYPE,UserSettings.exportFileFormat);
                break;
            case VELOCITY_UNITS:
                convertView = initializeGroup(ExpandableListGroupType.VELOCITY_UNITS,UserSettings.usedVelocity);
                break;
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final ExpandableListChildType childType = (ExpandableListChildType) getChild(groupPosition, childPosition);
        LayoutInflater infalInflater;

        switch(childType) {
            case FILE_TYPE:
                infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_list_child_file_type, null);
                fileTypeSelectorInitialize(convertView,parent);
                break;
            case VELOCITY_UNITS:
                infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_list_child_velocity_units, null);
                VelocityUnitSelectorInitialize(convertView,parent);
                break;
        }
        return convertView;
    }

    private void VelocityUnitSelectorInitialize(View child, ViewGroup parent) {
        final CustomButton msButton = (CustomButton) child.findViewById(R.id.msVelocityTypeButton);
        final CustomButton kphButton = (CustomButton) child.findViewById(R.id.kmhVelocityTypeButton);
        msButton.setFocusable(false);
        kphButton.setFocusable(false);
        final TextView headerValue = (TextView) parent.findViewById(R.id.group_velocity_value);
        switch(UserSettings.usedVelocity) {
            case VELOCITY_IN_MPS:
                msButton.setButtonSelected(true);
                kphButton.setButtonSelected(false);
                break;
            case VELOCITY_IN_KPH:
                kphButton.setButtonSelected(true);
                msButton.setButtonSelected(false);
                break;
        }
        headerValue.setText(context.getString(UserSettings.usedVelocity.getResourceId()));
        msButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(UserSettings.usedVelocity == VelocityType.VELOCITY_IN_MPS)) {
                    msButton.setButtonSelected(true);
                    kphButton.setButtonSelected(false);
                    UserSettings.usedVelocity = VelocityType.VELOCITY_IN_MPS;
                    headerValue.setText(context.getString(UserSettings.usedVelocity.getResourceId()));
                }
            }
        });
        kphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(UserSettings.usedVelocity == VelocityType.VELOCITY_IN_KPH)) {
                    kphButton.setButtonSelected(true);
                    msButton.setButtonSelected(false);
                    UserSettings.usedVelocity = VelocityType.VELOCITY_IN_KPH;
                    headerValue.setText(context.getString(UserSettings.usedVelocity.getResourceId()));
                }
            }
        });
    }

    private void fileTypeSelectorInitialize(View child, View parent) {
        final CustomButton gpxButton = (CustomButton) child.findViewById(R.id.gpxFileTypeButton);
        final CustomButton kmlButton = (CustomButton) child.findViewById(R.id.kmlFileTypeButton);
        gpxButton.setFocusable(false);
        kmlButton.setFocusable(false);
        final TextView headerValue = (TextView) parent.findViewById(R.id.group_file_value);
        switch(UserSettings.exportFileFormat) {
            case KML:
                kmlButton.setButtonSelected(true);
                gpxButton.setButtonSelected(false);
                break;
            case GPX:
                gpxButton.setButtonSelected(true);
                kmlButton.setButtonSelected(false);
                break;
        }
        headerValue.setText(context.getString(UserSettings.exportFileFormat.getResourceId()));
        gpxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(UserSettings.exportFileFormat == OutputFileFormat.GPX)) {
                    gpxButton.setButtonSelected(true);
                    kmlButton.setButtonSelected(false);
                    UserSettings.exportFileFormat = OutputFileFormat.GPX;
                    headerValue.setText(context.getString(UserSettings.exportFileFormat.getResourceId()));
                }
            }
        });
        kmlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(UserSettings.exportFileFormat == OutputFileFormat.KML)) {
                    kmlButton.setButtonSelected(true);
                    gpxButton.setButtonSelected(false);
                    UserSettings.exportFileFormat = OutputFileFormat.KML;
                    headerValue.setText(context.getString(UserSettings.exportFileFormat.getResourceId()));
                }
            }
        });
    }

    private View initializeGroup(ExpandableListGroupType groupType, IResourced globalField){
        LayoutInflater layoutFactory = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View convertView = layoutFactory.inflate(groupType.layoutResourceId,null);

        TextView headerValue = (TextView) convertView.findViewById(groupType.valueResourceId);
        TextView listGroupHeader = (TextView) convertView.findViewById(groupType.headerResourceId);

        headerValue.setText(context.getString(globalField.getResourceId()));
        listGroupHeader.setTypeface(null, Typeface.BOLD);
        listGroupHeader.setText(context.getString(groupType.stringResourceId));

        return convertView;
    }
}
