package com.parse.starter;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DriverLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Intent intent;
    private RelativeLayout mapRelativeLayout;

    private Double driverLat;
    private Double driverLon;
    private Double requestLat;
    private Double requestLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        intent = getIntent();
        mapRelativeLayout = (RelativeLayout) findViewById(R.id.mapRelativeLayout);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.i("mMap", "Ready");
        mapRelativeLayout
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        driverLat = intent.getDoubleExtra(UserType.Extras.DRIVER_LATITUDE, 0);
                        driverLon = intent.getDoubleExtra(UserType.Extras.DRIVER_LONGITUDE, 0);
                        requestLat = intent.getDoubleExtra(UserType.Extras.REQUEST_LATITUDE, 0);
                        requestLon = intent.getDoubleExtra(UserType.Extras.REQUEST_LONGITUDE, 0);

                        LatLng driverPosition = new LatLng(driverLat, driverLon);
                        LatLng requestPosition = new LatLng(requestLat, requestLon);

                        Marker driverMarker = mMap.addMarker(new MarkerOptions()
                                .position(driverPosition)
                                .title("Your Location")
                        );
                        Marker requestMarker = mMap.addMarker(new MarkerOptions()
                                .position(requestPosition)
                                .title("Request Location")
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
                    }
                });
    }

    public void acceptRequest(View view) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(UserType.Classes.REQUEST);
        query.whereEqualTo(UserType.USERNAME, intent.getStringExtra(UserType.Extras.USERNAME));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject object : objects) {
                        object.put(UserType.DRIVER_USERNAME, ParseUser.getCurrentUser().getUsername());
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    goToDirectionsIntent();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void goToDirectionsIntent() {
        String rawUrl = "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f";
        String mapsUrl = String.format(
                Locale.getDefault(),
                rawUrl,
                driverLat,
                driverLon,
                requestLat,
                requestLon
        );
        Uri mapsUri = Uri.parse(mapsUrl);
        Intent directionsIntent = new Intent(Intent.ACTION_VIEW, mapsUri);
        startActivity(directionsIntent);
    }
}
