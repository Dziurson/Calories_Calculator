//Copyright 2017 Jakub Dziwura
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.

package pl.edu.agh.student.calcalc.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.commons.io.FilenameUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.controls.CustomScrollView;
import pl.edu.agh.student.calcalc.globals.Properties;
import pl.edu.agh.student.calcalc.helpers.ActivityHelper;
import pl.edu.agh.student.calcalc.utilities.FileParser;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    NavigationView navSideMenu;
    SupportMapFragment mapGoogleMap;
    CustomScrollView msvActivityContentContainer;
    private GoogleMap googleMap;
    private Button buttonImportFile;
    private static final int ACTION_GET_CONTENT_REQUEST = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        buttonImportFile = (Button) findViewById(R.id.import_file_button);
        navSideMenu = (NavigationView) findViewById(R.id.nav_view);
        navSideMenu.setNavigationItemSelectedListener(this);
        mapGoogleMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_activity_google_map);
        mapGoogleMap.getMapAsync(this);
        msvActivityContentContainer = (CustomScrollView) findViewById(R.id.map_scroll_view);
        msvActivityContentContainer.enableTouchForView(mapGoogleMap.getView());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        buttonImportFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityHelper.checkForPermissions(MapActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("file/*");
                        startActivityForResult(intent, ACTION_GET_CONTENT_REQUEST);
                    }
                    catch (Exception e) {
                        Toast.makeText(MapActivity.this,MapActivity.this.getString(R.string.no_file_manager_installed),Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    ActivityCompat.requestPermissions(MapActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Properties.PERMISSION_TO_WRITE_EXTERNAL_STORAGE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        navSideMenu.setCheckedItem(R.id.dmi_map);
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
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
        getMenuInflater().inflate(R.menu.map, menu);
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
        } else if (id == R.id.dmi_properties) {
            ActivityHelper.findOrCreateActivity(this,UserPropertiesActivity.class);
        } else if (id == R.id.dmi_settings) {
            ActivityHelper.findOrCreateActivity(this,SettingsActivity.class);
        } else if (id == R.id.dmi_share) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showPolyline(List<Location> locations) {
        if(googleMap != null) {
            LatLng southWest = null;
            LatLng northEast = null;
            PolylineOptions line = new PolylineOptions();
            LatLng point;
            for(Location location : locations) {
                point = new LatLng(location.getLatitude(),location.getLongitude());
                line.add(point);
                if(southWest != null) {
                    if(point.latitude < southWest.latitude){
                        southWest = new LatLng(point.latitude,southWest.longitude);
                    }
                    if(point.longitude < southWest.longitude){
                        southWest = new LatLng(southWest.latitude,point.longitude);
                    }
                }
                else {
                    southWest = new LatLng(location.getLatitude(),location.getLongitude());
                }
                if(northEast != null) {
                    if(point.latitude > northEast.latitude) {
                        northEast = new LatLng(point.latitude,northEast.longitude);
                    }
                    if(point.longitude > northEast.longitude) {
                        northEast = new LatLng(northEast.latitude,point.longitude);
                    }
                }
                else {
                    northEast = new LatLng(location.getLatitude(),location.getLongitude());
                }
            }
            line.color(getResources().getColor(R.color.colorAccent)).width(5);
            googleMap.addPolyline(line);
            try {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(southWest,northEast),20));
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        switch(requestCode) {
            case ACTION_GET_CONTENT_REQUEST:
                if(resultCode == RESULT_OK) {
                    List<Location> locations;
                    String filepath = resultData.getData().getPath().replaceFirst("/file","");
                    File selectedFile = new File(filepath);
                    String fileExtension = FilenameUtils.getExtension(selectedFile.getName());
                    try {
                        if (fileExtension.compareToIgnoreCase("kml") == 0) {
                            locations = FileParser.parseKmlFile(selectedFile);
                            showPolyline(locations);
                        }
                        else if (fileExtension.compareToIgnoreCase("gpx") == 0) {
                            locations = FileParser.parseGpxFile(selectedFile);
                            showPolyline(locations);
                        }
                        else {
                            Toast.makeText(this, this.getString(R.string.wrong_file_type_selected), Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (XmlPullParserException e) {
                        Toast.makeText(this, this.getString(R.string.parsing_error), Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e) {
                        Toast.makeText(this, this.getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Properties.PERMISSION_TO_WRITE_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,getString(R.string.write_external_storage_permission_granted),Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,getString(R.string.write_external_storage_permission_denied),Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
