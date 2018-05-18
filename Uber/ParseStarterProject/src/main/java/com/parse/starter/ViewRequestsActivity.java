package com.parse.starter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ViewRequestsActivity extends AppCompatActivity {
    private ListView requestListView;
    private ArrayList<String> requestsList;
    private ArrayAdapter<String> requestsArrayAdapter;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ArrayList<Double> requestLatitudes;
    private ArrayList<Double> requestLongitudes;
    private ArrayList<String> usernames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        setTitle("Nearby Requests");

        requestListView = (ListView) findViewById(R.id.requestListView);
        requestsList = new ArrayList<>();
        requestLatitudes = new ArrayList<>();
        requestLongitudes = new ArrayList<>();
        usernames = new ArrayList<>();

        requestsArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestsList);
        requestsList.clear();
        requestsList.add("Getting nearby requests");
        requestListView.setAdapter(requestsArrayAdapter);

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Location lastKnowLocation = getLastKnownLocation();
                if (requestLatitudes.size() > i && requestLongitudes.size() > i && usernames.size() > i && lastKnowLocation != null) {
                    Intent intent = new Intent(getApplicationContext(), DriverLocationActivity.class);

                    intent.putExtra(UserType.Extras.REQUEST_LATITUDE, requestLatitudes.get(i));
                    intent.putExtra(UserType.Extras.REQUEST_LONGITUDE, requestLongitudes.get(i));
                    intent.putExtra(UserType.Extras.DRIVER_LATITUDE, lastKnowLocation.getLatitude());
                    intent.putExtra(UserType.Extras.DRIVER_LONGITUDE, lastKnowLocation.getLongitude());
                    intent.putExtra(UserType.Extras.USERNAME, usernames.get(i));

                    startActivity(intent);
                }
            }
        });

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateListView(location);
                ParseGeoPoint geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                ParseUser.getCurrentUser().put(UserType.LOCATION, geoPoint);
                ParseUser.getCurrentUser().saveInBackground();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            requestLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            }
        }
    }

    private Location getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return null;
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnowLocation = getLastKnownLocation();
            if (lastKnowLocation != null) {
                updateListView(lastKnowLocation);
            }
        }
    }

    private void updateListView(@NonNull Location location) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(UserType.Classes.REQUEST);
        final ParseGeoPoint geoPointLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        query.whereNear(UserType.LOCATION, geoPointLocation);
        query.whereDoesNotExist(UserType.DRIVER_USERNAME);
        query.setLimit(10);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    requestsList.clear();
                    requestLatitudes.clear();
                    requestLongitudes.clear();
                    for (ParseObject object : objects) {
                        ParseGeoPoint requestLocation = object.getParseGeoPoint(UserType.LOCATION);
                        if (requestLocation != null) {
                            Double distanceInKilometers = geoPointLocation.distanceInKilometersTo(requestLocation);
                            Double distanceOneDP = (double) Math.round(distanceInKilometers * 10) / 10;
                            requestsList.add(distanceOneDP.toString() + " kilometres");
                            requestLatitudes.add(requestLocation.getLatitude());
                            requestLongitudes.add(requestLocation.getLongitude());
                            usernames.add(object.getString(UserType.USERNAME));
                        }
                    }
                } else {
                    requestsList.add("No active requests nearby");
                }
                requestsArrayAdapter.notifyDataSetChanged();
            }
        });
    }
}
