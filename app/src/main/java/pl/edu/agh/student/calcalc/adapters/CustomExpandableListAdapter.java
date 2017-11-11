package pl.edu.agh.student.calcalc.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

import pl.edu.agh.student.calcalc.containers.Tuple;
import pl.edu.agh.student.calcalc.enums.ExpandableListChildType;
import pl.edu.agh.student.calcalc.enums.ExpandableListGroupType;

public abstract class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    protected Activity context;
    private List<Tuple<ExpandableListGroupType,List<ExpandableListChildType>>> initializationList;

    public CustomExpandableListAdapter(Activity context, List<Tuple<ExpandableListGroupType,List<ExpandableListChildType>>> initializationList) {
        this.initializationList = initializationList;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return initializationList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return initializationList.get(groupPosition).second.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return initializationList.get(groupPosition).first;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return initializationList.get(groupPosition).second.get(childPosition);
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
