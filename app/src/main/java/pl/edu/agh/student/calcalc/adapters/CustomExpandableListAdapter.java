package pl.edu.agh.student.calcalc.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.HashMap;
import java.util.List;

import pl.edu.agh.student.calcalc.enums.ExpandableListChildType;

public abstract class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    protected Activity context;
    protected List<String> listHeaders;
    protected HashMap<String,List<ExpandableListChildType>> childrenMap;

    public CustomExpandableListAdapter(Activity context, List<String> listHeaders, HashMap<String,List<ExpandableListChildType>> childrenMap) {
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
    public abstract View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);

    @Override
    public abstract View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent);

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
