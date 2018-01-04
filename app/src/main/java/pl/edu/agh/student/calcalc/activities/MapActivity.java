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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.net.Uri;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import pl.edu.agh.student.calcalc.R;
import pl.edu.agh.student.calcalc.controls.CustomScrollView;
import pl.edu.agh.student.calcalc.enums.VelocityUnit;
import pl.edu.agh.student.calcalc.globals.Properties;
import pl.edu.agh.student.calcalc.globals.UserSettings;
import pl.edu.agh.student.calcalc.helpers.ActivityHelper;
import pl.edu.agh.student.calcalc.utilities.FileParser;

import static android.os.Environment.getExternalStorageDirectory;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    NavigationView navSideMenu;
    SupportMapFragment mapGoogleMap;
    CustomScrollView msvActivityContentContainer;
    private GoogleMap googleMap;
    private Button buttonImportFile;
    private static final int ACTION_GET_CONTENT_REQUEST = 200;
    private TextView totalDistanceTextView;
    private TextView totalTimeTextView;
    private TextView averageVelocityTextView;
    private TextView totalCaloriesBurnedTextView;
    private TextView totalDistanceLabelTextView;
    private TextView totalTimeLabelTextView;
    private TextView averageVelocityLabelTextView;
    private TextView totalCaloriesBurnedLabelTextView;
    private SimpleDateFormat dateFormatter;
    private String filename;

    private static final int SHARE_REQUEST_CODE = 102;

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

        totalDistanceTextView = (TextView) findViewById(R.id.total_distance_value);
        totalTimeTextView = (TextView) findViewById(R.id.total_duration_value);
        averageVelocityTextView = (TextView) findViewById(R.id.average_velocity_value);
        totalCaloriesBurnedTextView = (TextView) findViewById(R.id.total_calories_burned_value);
        totalDistanceLabelTextView = (TextView) findViewById(R.id.total_distance_label);
        totalTimeLabelTextView = (TextView) findViewById(R.id.total_duration_label);
        averageVelocityLabelTextView = (TextView) findViewById(R.id.average_velocity_label);
        totalCaloriesBurnedLabelTextView = (TextView) findViewById(R.id.total_calories_burned_label);

        buttonImportFile = (Button) findViewById(R.id.import_file_button);
        navSideMenu = (NavigationView) findViewById(R.id.nav_view);
        navSideMenu.setNavigationItemSelectedListener(this);
        mapGoogleMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_activity_google_map);
        mapGoogleMap.getMapAsync(this);
        msvActivityContentContainer = (CustomScrollView) findViewById(R.id.map_scroll_view);
        msvActivityContentContainer.enableTouchForView(mapGoogleMap.getView());
        dateFormatter = new SimpleDateFormat("HH:mm:ss");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

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
            if(totalDistanceTextView.getText().toString().compareToIgnoreCase("") != 0) {
                filename = getExternalStorageDirectory().getPath() + UserSettings.testDir + ((Long) new Date().getTime()).toString() + ".png";
                View shareImageLayout = ((LayoutInflater) getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.share_image_layout, null);
                String activityType = "";
                switch (UserSettings.activityType) {
                    case WALKING:
                        activityType = getString(R.string.activity_type_walking);
                        break;
                    case RUNNING:
                        activityType = getString(R.string.activity_type_running);
                        break;
                }
                ((TextView) shareImageLayout.findViewById(R.id.share_image_layout_activity_type)).setText(activityType);
                ((TextView) shareImageLayout.findViewById(R.id.share_image_layout_total_distance_value)).setText(totalDistanceTextView.getText());
                ((TextView) shareImageLayout.findViewById(R.id.share_image_layout_average_velocity_value)).setText((averageVelocityTextView.getText().toString().compareToIgnoreCase("") != 0) ? averageVelocityTextView.getText() : "-");
                ((TextView) shareImageLayout.findViewById(R.id.share_image_layout_calories_burned_value)).setText((totalCaloriesBurnedTextView.getText().toString().compareToIgnoreCase("") != 0) ? totalCaloriesBurnedTextView.getText() : "-");
                ((TextView) shareImageLayout.findViewById(R.id.share_image_layout_total_time_value)).setText((totalTimeTextView.getText().toString().compareToIgnoreCase("") != 0) ? totalTimeTextView.getText() : "-");
                shareImageLayout.setDrawingCacheEnabled(true);
                shareImageLayout.buildDrawingCache();
                Bitmap shareImage = Bitmap.createBitmap(800, 400, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(shareImage);
                shareImageLayout.measure(800, 400);
                shareImageLayout.layout(0, 0, 800, 400);
                shareImageLayout.draw(canvas);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(filename);
                    shareImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
                File file = new File(filename);
                try {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    shareIntent.setType("image/png");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(Intent.createChooser(shareIntent, "send"), SHARE_REQUEST_CODE);
                } catch (Exception e) {
                    Toast.makeText(this, getString(R.string.no_sharing_app_found), Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this,getString(R.string.nothing_to_share_no_file),Toast.LENGTH_LONG).show();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showActivitySummary(List<Location> locations) {
        if(googleMap != null) {
            clearTextViews();
            googleMap.clear();
            double totalDistance = 0;
            long totalTime = 0;
            double totalCalories = 0;
            double currentDistance = 0;
            Location prevLocation = null;
            LatLng southWest = null;
            LatLng northEast = null;
            PolylineOptions line = new PolylineOptions();
            LatLng point;
            for(Location location : locations) {
                if(prevLocation != null) {
                    currentDistance = prevLocation.distanceTo(location);
                    totalDistance += currentDistance;
                    totalTime += (location.getTime() - prevLocation.getTime());
                    totalCalories += Math.round(UserSettings.activityType.getCalories(prevLocation, location)*1000000.0)/1000000.0;
                    prevLocation = location;
                }
                else
                    prevLocation = location;
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
            totalDistanceTextView.setText((totalDistance > 1000) ? String.format(Locale.getDefault(),"%.2f %s",totalDistance/1000.0,getString(R.string.kilometers)) : String.format(Locale.getDefault(),"%.2f %s",totalDistance,getString(R.string.meters)));
            totalDistanceLabelTextView.setText(getString(R.string.total_distance));
            if(totalTime != 0) {
                totalTimeTextView.setText(dateFormatter.format(new Date(totalTime)));
                totalTimeLabelTextView.setText(getString(R.string.total_time));
                averageVelocityTextView.setText(String.format(Locale.getDefault(),"%.2f %s",(UserSettings.usedVelocity == VelocityUnit.VELOCITY_IN_MPS) ? totalDistance/(totalTime/1000) : totalDistance/(totalTime/1000)*3.6, UserSettings.usedVelocity.getString(this)));
                averageVelocityLabelTextView.setText(getString(R.string.average_velocity_label));
            }
            if(totalCalories != 0) {
                totalCaloriesBurnedTextView.setText(String.format(Locale.getDefault(),"%.2f %s",totalCalories,getString(R.string.kcal)));
                totalCaloriesBurnedLabelTextView.setText(getString(R.string.calories_burned));
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
                            showActivitySummary(locations);
                        }
                        else if (fileExtension.compareToIgnoreCase("gpx") == 0) {
                            locations = FileParser.parseGpxFile(selectedFile);
                            showActivitySummary(locations);
                        }
                        else {
                            Toast.makeText(this, this.getString(R.string.wrong_file_type_selected), Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (XmlPullParserException e) {
                        Toast.makeText(this, this.getString(R.string.parsing_error), Toast.LENGTH_SHORT).show();
                    }
                    catch (ParseException e) {
                        Toast.makeText(this, this.getString(R.string.parsing_error), Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e) {
                        Toast.makeText(this, this.getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case SHARE_REQUEST_CODE:
                if(filename != null) {
                    File file = new File(filename);
                    if(file.exists() && !file.isDirectory()) {
                        file.delete();
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

    private void clearTextViews(){
        if(totalDistanceTextView != null) {
            totalDistanceTextView.setText("");
        }
        if(totalTimeTextView != null) {
            totalTimeTextView.setText("");
        }
        if(averageVelocityTextView != null) {
            averageVelocityTextView.setText("");
        }
        if(totalCaloriesBurnedTextView != null) {
            totalCaloriesBurnedTextView.setText("");
        }
        if(totalDistanceLabelTextView != null) {
            totalDistanceLabelTextView.setText("");
        }
        if(totalTimeLabelTextView != null) {
            totalTimeLabelTextView.setText("");
        }
        if(averageVelocityTextView != null) {
            averageVelocityLabelTextView.setText("");
        }
        if(totalCaloriesBurnedLabelTextView != null) {
            totalCaloriesBurnedLabelTextView.setText("");
        }
    }
}
