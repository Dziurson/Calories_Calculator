package pl.edu.agh.student.calcalc.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.enums.ExpandableListChildType;
import pl.edu.agh.student.calcalc.enums.OutputFileFormat;
import pl.edu.agh.student.calcalc.globals.UserSettings;

public class SettingsExpandableListAdapter extends CustomExpandableListAdapter {

    public SettingsExpandableListAdapter(Activity context, List<String> listHeaders, HashMap<String,List<ExpandableListChildType>> childrenMap) {
        super(context,listHeaders,childrenMap);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String header = (String) getGroup(groupPosition);

        if(convertView == null) {
            LayoutInflater layoutFactory = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutFactory.inflate(R.layout.expandable_list_group,null);
        }

        if(groupPosition == 0) {
            final TextView headerValue = (TextView) convertView.findViewById(R.id.expandableListValue);
            headerValue.setText(UserSettings.exportFileFormat.getExtensionWithDot());
        }

        TextView listGroupHeader = (TextView) convertView.findViewById(R.id.expandableListHeader);
        listGroupHeader.setTypeface(null, Typeface.BOLD);
        listGroupHeader.setText(header);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final ExpandableListChildType childType = (ExpandableListChildType) getChild(groupPosition, childPosition);

        switch(childType) {
            case FILE_TYPE:
                if (convertView == null) {
                    LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = infalInflater.inflate(R.layout.expandable_list_file_type_selector, null);
                }
                fileTypeSelectorInitialize(convertView,parent);
                break;
        }
        return convertView;
    }

    private void fileTypeSelectorInitialize(View child, View parent) {
        final Button gpxButton = (Button) child.findViewById(R.id.gpxFileTypeButton);
        final Button kmlButton = (Button) child.findViewById(R.id.kmlFileTypeButton);
        gpxButton.setFocusable(false);
        kmlButton.setFocusable(false);
        final TextView headerValue = (TextView) parent.findViewById(R.id.expandableListValue);
        switch(UserSettings.exportFileFormat) {
            case KML:
                kmlButton.setBackgroundResource(R.color.colorLightBlue);
                kmlButton.setTextColor(Color.WHITE);
                gpxButton.setBackgroundResource(R.color.colorLightGrey);
                gpxButton.setTextColor(Color.BLACK);
                break;
            case GPX:
                gpxButton.setBackgroundResource(R.color.colorLightBlue);
                gpxButton.setTextColor(Color.WHITE);
                kmlButton.setBackgroundResource(R.color.colorLightGrey);
                kmlButton.setTextColor(Color.BLACK);
                break;
        }
        headerValue.setText(UserSettings.exportFileFormat.getExtensionWithDot());
        gpxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(UserSettings.exportFileFormat == OutputFileFormat.GPX)) {
                    gpxButton.setBackgroundResource(R.color.colorLightBlue);
                    gpxButton.setTextColor(Color.WHITE);
                    kmlButton.setBackgroundResource(R.color.colorLightGrey);
                    kmlButton.setTextColor(Color.BLACK);
                    UserSettings.exportFileFormat = OutputFileFormat.GPX;
                    headerValue.setText(UserSettings.exportFileFormat.getExtensionWithDot());
                }
            }
        });
        kmlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(UserSettings.exportFileFormat == OutputFileFormat.KML)) {
                    kmlButton.setBackgroundResource(R.color.colorLightBlue);
                    kmlButton.setTextColor(Color.WHITE);
                    gpxButton.setBackgroundResource(R.color.colorLightGrey);
                    gpxButton.setTextColor(Color.BLACK);
                    UserSettings.exportFileFormat = OutputFileFormat.KML;
                    headerValue.setText(UserSettings.exportFileFormat.getExtensionWithDot());
                }
            }
        });
    }
}
