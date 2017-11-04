package pl.edu.agh.student.calcalc.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.enums.ExpandableListChildType;
import pl.edu.agh.student.calcalc.enums.OutputFileFormat;
import pl.edu.agh.student.calcalc.globals.UserSettings;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private List<String> listHeaders;
    private HashMap<String,List<ExpandableListChildType>> childrenMap;

    public CustomExpandableListAdapter(Activity context, List<String> listHeaders, HashMap<String,List<ExpandableListChildType>> childrenMap) {
        this.context = context;
        this.listHeaders = listHeaders;
        this.childrenMap = childrenMap;
    }

    @Override
    public int getGroupCount() {
        return listHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childrenMap.get(listHeaders.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listHeaders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childrenMap.get(listHeaders.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String header = (String) getGroup(groupPosition);
        if(convertView == null) {
            LayoutInflater layoutFactory = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutFactory.inflate(R.layout.expandable_list_group,null);
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
                fleTypeSelectorInitialize(convertView);
                break;
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private void fleTypeSelectorInitialize(View view) {
        RadioButton gpxButton = (RadioButton) view.findViewById(R.id.gpxFileTypeButton);
        RadioButton kmlButton = (RadioButton) view.findViewById(R.id.kmlFileTypeButton);
        switch(UserSettings.exportFileFormat) {
            case KML:
                kmlButton.setChecked(true);
                gpxButton.setChecked(false);
                break;
            case GPX:
                gpxButton.setChecked(true);
                kmlButton.setChecked(false);
                break;
        }
        gpxButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    UserSettings.exportFileFormat = OutputFileFormat.GPX;
                }
            }
        });
        kmlButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    UserSettings.exportFileFormat = OutputFileFormat.KML;
                }
            }
        });
    }
}
