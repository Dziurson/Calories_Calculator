package pl.edu.agh.student.calcalc.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.adapters.UserPropertiesExpandableListAdapter;
import pl.edu.agh.student.calcalc.controls.CustomExpandableListView;
import pl.edu.agh.student.calcalc.enums.ExpandableListViewChild;
import pl.edu.agh.student.calcalc.enums.ExpandableListViewGroup;
import pl.edu.agh.student.calcalc.helpers.ActivityHelper;
import pl.edu.agh.student.calcalc.types.Tuple;

public class UserPropertiesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navSideMenu;
    UserPropertiesExpandableListAdapter listAdapter;
    CustomExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navSideMenu = (NavigationView) findViewById(R.id.nav_view);
        navSideMenu.setNavigationItemSelectedListener(this);

        MenuItem item = navSideMenu.getMenu().getItem(4);
        item.setVisible(false);
        prepareExpandableList();

        prepareExpandableList();
    }

    @Override
    protected void onResume() {
        navSideMenu.setCheckedItem(R.id.dmi_properties);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.properties, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dmi_home) {
            ActivityHelper.findOrCreateActivity(this,MainActivity.class);
        } else if (id == R.id.dmi_map) {
            ActivityHelper.findOrCreateActivity(this,MapActivity.class);
        } else if (id == R.id.dmi_settings) {
            ActivityHelper.findOrCreateActivity(this,SettingsActivity.class);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareExpandableList() {
        expListView = (CustomExpandableListView) findViewById(R.id.user_properties_activity_expandable_list);

        List<Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>>> listMap = new ArrayList<>();

        List<ExpandableListViewChild> userWeightChildren = new ArrayList<>();
        userWeightChildren.add(ExpandableListViewChild.USER_WEIGHT);
        Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>> userWeightEntry = new Tuple<>(ExpandableListViewGroup.USER_WEIGHT, userWeightChildren);
        listMap.add(userWeightEntry);

        List<ExpandableListViewChild> userHeightChildren = new ArrayList<>();
        userHeightChildren.add(ExpandableListViewChild.USER_HEIGHT);
        Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>> userHeightEntry = new Tuple<>(ExpandableListViewGroup.USER_HEIGHT, userHeightChildren);
        listMap.add(userHeightEntry);

        List<ExpandableListViewChild> userAgeChildren = new ArrayList<>();
        userAgeChildren.add(ExpandableListViewChild.USER_AGE);
        Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>> userAgeEntry = new Tuple<>(ExpandableListViewGroup.USER_AGE, userAgeChildren);
        listMap.add(userAgeEntry);

        List<ExpandableListViewChild> userGenderChildren = new ArrayList<>();
        userGenderChildren.add(ExpandableListViewChild.USER_GENDER);
        Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>> userGenderEntry = new Tuple<>(ExpandableListViewGroup.USER_GENDER, userGenderChildren);
        listMap.add(userGenderEntry);

        List<ExpandableListViewChild> activityTypeChidren = new ArrayList<>();
        activityTypeChidren.add(ExpandableListViewChild.ACTIVITY_TYPE);
        Tuple<ExpandableListViewGroup,List<ExpandableListViewChild>> activityTypeEntry = new Tuple<>(ExpandableListViewGroup.ACTIVITY_TYPE, activityTypeChidren);
        listMap.add(activityTypeEntry);

        listAdapter = new UserPropertiesExpandableListAdapter(this, listMap);
        expListView.setAdapter(listAdapter);
    }
}
