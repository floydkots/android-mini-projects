package com.parse.starter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class RiderActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button callUberButton;
    private TextView infoTextView;
    private Boolean driverActive;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Boolean requestActive = false;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        callUberButton = (Button) findViewById(R.id.callUberButton);
        infoTextView = (TextView) findViewById(R.id.infoTextView);
        driverActive = false;
        handler = new Handler();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(UserType.Classes.REQUEST);
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    requestActive = true;
                    callUberButton.setText(getString(R.string.cancel_uber));

                    checkForUpdates();
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateMap(location);
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

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnowLocation != null) {
                updateMap(lastKnowLocation);
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

    private void updateMap(Location location) {
        if (driverActive == false) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
            mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
        }

    }

    public void callUber(View view) {
        Log.i("Info", "Call Uber");

        if (requestActive) {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(UserType.Classes.REQUEST);
            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null && objects.size() > 0) {
                        for (ParseObject object : objects) {
                            object.deleteInBackground();
                        }
                        requestActive = false;
                        callUberButton.setText(getString(R.string.call_uber));
                    }
                }
            });

        } else {
            Location lastKnownLocation = getLastKnownLocation();
            if (lastKnownLocation != null) {
                ParseObject request = new ParseObject(UserType.Classes.REQUEST);
                ParseGeoPoint parseGeoPoint = new ParseGeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                request.put("username", ParseUser.getCurrentUser().getUsername());
                request.put("location", parseGeoPoint);
                request.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            callUberButton.setText(getString(R.string.cancel_uber));
                            requestActive = true;

                            checkForUpdates();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Could not find location. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void logout(View view) {
        ParseUser.logOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void checkForUpdates() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(UserType.Classes.REQUEST);
        query.whereEqualTo(UserType.USERNAME, ParseUser.getCurrentUser().getUsername());
        query.whereExists(UserType.DRIVER_USERNAME);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {

                    driverActive = true;

                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo(UserType.USERNAME, objects.get(0).getString(UserType.DRIVER_USERNAME));
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (e == null && objects.size() > 0) {
                                ParseGeoPoint driverLocation = objects.get(0).getParseGeoPoint(UserType.LOCATION);
                                Location lastKnowLocation = getLastKnownLocation();
                                if (lastKnowLocation != null) {
                                    ParseGeoPoint userLocation = new ParseGeoPoint(lastKnowLocation.getLatitude(), lastKnowLocation.getLongitude());
                                    Double distanceInKilometers = driverLocation.distanceInKilometersTo(userLocation);

                                    if (distanceInKilometers < 0.01) {
                                        String msg = "Your driver is here!";
                                        infoTextView.setText(msg);

                                        ParseQuery<ParseObject> query = ParseQuery.getQuery(UserType.Classes.REQUEST);
                                        query.whereEqualTo(UserType.USERNAME, ParseUser.getCurrentUser().getUsername());
                                        query.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> objects, ParseException e) {
                                                if (e == null) {
                                                    for (ParseObject object : objects) {
                                                        object.deleteInBackground();
                                                    }
                                                }
                                            }
                                        });

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                infoTextView.setText("");
                                                callUberButton.setVisibility(View.VISIBLE);
                                                callUberButton.setText(getString(R.string.call_uber));
                                                requestActive = false;
                                                driverActive = false;
                                            }
                                        }, 5000);

                                    } else {

                                        Double distanceOneDP = (double) Math.round(distanceInKilometers * 10) / 10;

                                        infoTextView.setText(getString(R.string.driver_distance_info, distanceOneDP));

                                        Double driverLat = driverLocation.getLatitude();
                                        Double driverLon = driverLocation.getLongitude();
                                        Double requestLat = userLocation.getLatitude();
                                        Double requestLon = userLocation.getLongitude();

                                        LatLng driverPosition = new LatLng(driverLat, driverLon);
                                        LatLng requestPosition = new LatLng(requestLat, requestLon);

                                        mMap.clear();

                                        Marker driverMarker = mMap.addMarker(new MarkerOptions()
                                                .position(driverPosition)
                                                .title("Driver Location")
                                        );
                                        Marker requestMarker = mMap.addMarker(new MarkerOptions()
                                                .position(requestPosition)
                                                .title("Your Location")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                        );

                                        ArrayList<Marker> markers = new ArrayList<>();
                                        markers.add(driverMarker);
                                        markers.add(requestMarker);

                                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                        for (Marker marker : markers) {
                                            builder.include(marker.getPosition());
                                        }
                                        LatLngBounds bounds = builder.build();

                                        int padding = 100; // offset from edges of the map in pixels
                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                                        mMap.animateCamera(cameraUpdate);

                                        callUberButton.setVisibility(View.INVISIBLE);

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                checkForUpdates();
                                            }
                                        }, 2000);
                                    }

                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
