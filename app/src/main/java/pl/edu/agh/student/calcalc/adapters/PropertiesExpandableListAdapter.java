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
import pl.edu.agh.student.calcalc.types.Tuple;
import pl.edu.agh.student.calcalc.enums.ExpandableListViewChild;
import pl.edu.agh.student.calcalc.enums.ExpandableListViewGroup;

public class PropertiesExpandableListAdapter extends CustomExpandableListAdapter {

    public PropertiesExpandableListAdapter(FragmentActivity context, List<Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>>> childrenMap) {
        super(context,childrenMap);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String header = (String) getGroup(groupPosition);

        if(convertView == null) {
            LayoutInflater layoutFactory = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutFactory.inflate(R.layout.settings_expandable_list_group_file,null);
        }
        TextView listGroupHeader = (TextView) convertView.findViewById(R.id.settings_group_file_header);
        listGroupHeader.setTypeface(null, Typeface.BOLD);
        listGroupHeader.setText(header);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ExpandableListViewChild childType = (ExpandableListViewChild) getChild(groupPosition, childPosition);
        return convertView;
    }
}
