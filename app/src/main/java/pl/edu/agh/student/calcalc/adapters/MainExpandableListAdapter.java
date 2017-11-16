package pl.edu.agh.student.calcalc.adapters;

import android.content.Context;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.commands.LocationCommand;
import pl.edu.agh.student.calcalc.containers.Tuple;
import pl.edu.agh.student.calcalc.enums.ExpandableListChildType;
import pl.edu.agh.student.calcalc.enums.ExpandableListGroupType;
import pl.edu.agh.student.calcalc.globals.UserSettings;
import pl.edu.agh.student.calcalc.helpers.LocationHelper;
import pl.edu.agh.student.calcalc.listeners.ApplicationLocationListener;
import pl.edu.agh.student.calcalc.utilities.Timer;

public class MainExpandableListAdapter extends CustomExpandableListAdapter implements LocationCommand, OnMapReadyCallback{

    private Timer durationTimer;
    private ApplicationLocationListener locationListener;
    private TextView longitudeTextView;
    private TextView latitudeTextView;
    private TextView altitudeTextView;
    private TextView velocityTextView;
    private LayoutInflater layoutFactory = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    private View mapView;
    private GoogleMap googleMap;

     public MainExpandableListAdapter(FragmentActivity context, List<Tuple<ExpandableListGroupType, List<ExpandableListChildType>>> initializationList, HashMap<String,Object> extras) {
        super(context, initializationList);
        durationTimer = (Timer) extras.get("timer");
        locationListener = ApplicationLocationListener.getInstance();
        locationListener.addOnLocationChangedCommand(this);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return initializeGroup((ExpandableListGroupType) getGroup(groupPosition));
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        switch((ExpandableListChildType) getChild(groupPosition, childPosition)) {
            case TIME:
                convertView = layoutFactory.inflate(R.layout.main_expandable_list_child_time, null);
                durationTimer.setTextViewToUpdate((TextView)convertView.findViewById(R.id.main_expandable_list_timer));
                break;
            case LOCATION:
                convertView = layoutFactory.inflate(R.layout.main_expandable_list_child_location, null);
                longitudeTextView = (TextView) convertView.findViewById(R.id.main_expandable_list_longitude);
                latitudeTextView = (TextView) convertView.findViewById(R.id.main_expandable_list_latitude);
                break;
            case ALTITUDE:
                convertView = layoutFactory.inflate(R.layout.main_expandable_list_child_altitude, null);
                altitudeTextView = (TextView) convertView.findViewById(R.id.main_expandable_list_altitude);
                break;
            case VELOCITY:
                convertView = layoutFactory.inflate(R.layout.main_expandable_list_child_velocity, null);
                velocityTextView = (TextView) convertView.findViewById(R.id.main_expandable_list_velocity);
                break;
            case MAP:
                if (mapView == null) {
                    mapView = layoutFactory.inflate(R.layout.main_expandable_list_chid_map, null);
                    initializeMap();
                }
                convertView = mapView;
                break;
        }
        return convertView;
    }

    @Override
    public void onLocationChanged(Location location) {
        Tuple<String, String> formattedLocationTuple = LocationHelper.format(location);
        if(latitudeTextView != null && longitudeTextView != null) {
            latitudeTextView.setText(formattedLocationTuple.first);
            longitudeTextView.setText(formattedLocationTuple.second);
        }
        if(altitudeTextView != null) {
            altitudeTextView.setText(String.format(Locale.getDefault(),"%d %s",(int)location.getAltitude(),context.getString(R.string.m_a_s_l)));
        }
        if(velocityTextView != null) {
            velocityTextView.setText(String.format(Locale.getDefault(),"%d %s",(int)location.getExtras().getDouble(UserSettings.usedVelocity.toString()),context.getString(UserSettings.usedVelocity.getStringResourceId())));
        }
        if(googleMap != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("TEST")); //TODO: REPLACE TEST WITH CALORIES FROM START
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16)); //TODO: ADD ZOOM CHANGE DUE TO SPEED
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private View initializeGroup(ExpandableListGroupType groupType){
        View convertView = layoutFactory.inflate(groupType.layoutResourceId,null);
        TextView header = (TextView) convertView.findViewById(groupType.headerResourceId);
        header.setText(groupType.stringResourceId);
        return convertView;
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) context.getSupportFragmentManager().findFragmentById(R.id.main_expandable_list_map);
        mapFragment.getMapAsync(this);
    }
}
