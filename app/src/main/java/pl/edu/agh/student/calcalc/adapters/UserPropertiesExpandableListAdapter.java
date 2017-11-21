package pl.edu.agh.student.calcalc.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.controls.CustomSeekBar;
import pl.edu.agh.student.calcalc.globals.UserSettings;
import pl.edu.agh.student.calcalc.interfaces.IPropertyWithResource;
import pl.edu.agh.student.calcalc.types.Tuple;
import pl.edu.agh.student.calcalc.enums.ExpandableListViewChild;
import pl.edu.agh.student.calcalc.enums.ExpandableListViewGroup;

public class UserPropertiesExpandableListAdapter extends CustomExpandableListAdapter {

    LayoutInflater layoutFactory = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    private final int heightOffset = 100;
    private final int weightOffset = 30;

    public UserPropertiesExpandableListAdapter(FragmentActivity context, List<Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>>> childrenMap) {
        super(context,childrenMap);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        switch ((ExpandableListViewGroup) getGroup(groupPosition)) {
            case USER_WEIGHT:
                convertView = groupInit(ExpandableListViewGroup.USER_WEIGHT, UserSettings.userWeight);
                break;
            case USER_HEIGHT:
                convertView = groupInit(ExpandableListViewGroup.USER_HEIGHT, UserSettings.userHeight);
                break;
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        switch((ExpandableListViewChild) getChild(groupPosition, childPosition)) {
            case USER_WEIGHT:
                convertView = userWeightChildInit(parent);
                break;
            case USER_HEIGHT:
                convertView = userHeightChildInit(parent);
        }
        return convertView;
    }

    private View userWeightChildInit(ViewGroup parent) {
        View convertView = layoutFactory.inflate(R.layout.user_properties_expandable_list_child_user_weight, null);
        final SeekBar userWeightSeekBar = (CustomSeekBar) convertView.findViewById(R.id.user_properties_expandable_list_user_weight);
        final TextView headerValueTextView = (TextView) parent.findViewById(R.id.user_properties_group_user_weight_value);
        userWeightSeekBar.setProgress(UserSettings.userWeight.getValue()-weightOffset);
        userWeightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                UserSettings.userWeight.setValue(progress+weightOffset);
                headerValueTextView.setText(UserSettings.userWeight.getString(context).concat(" ").concat(context.getString(R.string.kilogram_unit)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return convertView;
    }

    private View groupInit(ExpandableListViewGroup groupType, IPropertyWithResource propertyToUpdate){
        View convertView = layoutFactory.inflate(groupType.layoutResourceId,null);
        if(groupType.valueResourceId != -1) {
            TextView headerValueTextView = (TextView) convertView.findViewById(groupType.valueResourceId);
            switch(groupType){
                case USER_WEIGHT:
                    headerValueTextView.setText(propertyToUpdate.getString(context).concat(" ").concat(context.getString(R.string.kilogram_unit)));
                    break;
                case USER_HEIGHT:
                    headerValueTextView.setText(propertyToUpdate.getString(context).concat(" ").concat(context.getString(R.string.centimeter_unit)));
                    break;
                default:
                    headerValueTextView.setText(propertyToUpdate.getString(context));
                    break;
            }
        }
        TextView headerKeyTextView = (TextView) convertView.findViewById(groupType.headerResourceId);
        headerKeyTextView.setTypeface(null, Typeface.BOLD);
        headerKeyTextView.setText(context.getString(groupType.stringResourceId));
        return convertView;
    }

    private View userHeightChildInit(ViewGroup parent) {
        View convertView = layoutFactory.inflate(R.layout.user_properties_expandable_list_child_user_height, null);
        final SeekBar userHeightSeekBar = (CustomSeekBar) convertView.findViewById(R.id.user_properties_expandable_list_user_height);
        final TextView headerValueTextView = (TextView) parent.findViewById(R.id.user_properties_group_user_height_value);
        userHeightSeekBar.setProgress(UserSettings.userHeight.getValue()-heightOffset);
        userHeightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                UserSettings.userHeight.setValue(progress+heightOffset);
                headerValueTextView.setText(UserSettings.userHeight.getString(context).concat(" ").concat(context.getString(R.string.centimeter_unit)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return convertView;
    }
}