package pl.edu.agh.student.calcalc.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.enums.ExpandableListChildType;

public class PropertiesExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private List<String> listHeaders;
    private HashMap<String,List<ExpandableListChildType>> childrenMap;

    PropertiesExpandableListAdapter(Activity context, List<String> listHeaders, HashMap<String,List<ExpandableListChildType>> childrenMap) {
        this.childrenMap = childrenMap;
        this.context = context;
        this.listHeaders = listHeaders;
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
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
