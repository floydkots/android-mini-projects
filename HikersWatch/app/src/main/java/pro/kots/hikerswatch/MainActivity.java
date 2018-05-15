package pro.kots.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location", location.toString());
                updateLocationInfo(location);
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
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0 , 0, locationListener);

            if (lastKnownLocation != null) {
                updateLocationInfo(lastKnownLocation);
            }
        }
    }

    public void updateLocationInfo(Location location) {
        Log.i("LocationInfo", location.toString());
        TextView latTextView = findViewById(R.id.latTextView);
        TextView lonTextView = findViewById(R.id.lonTextView);
        TextView altTextView = findViewById(R.id.altTextView);
        TextView accTextView = findViewById(R.id.accTextView);

        latTextView.setText(getString(R.string.latText, location.getLatitude()));
        lonTextView.setText(getString(R.string.lonText, location.getLongitude()));
        altTextView.setText(getString(R.string.altText, location.getAltitude()));
        accTextView.setText(getString(R.string.accText, location.getAccuracy()));

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {

            StringBuilder address = new StringBuilder("Could not find address");
            List<Address> addressesList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addressesList != null && addressesList.size() > 0) {
                Log.i("Place Info", addressesList.get(0).toString());
                address = new StringBuilder("Address: \n");
                if (addressesList.get(0).getSubThoroughfare() != null) {
                        address.append(addressesList.get(0).getSubThoroughfare()).append(" ");
                }

                if (addressesList.get(0).getThoroughfare() != null) {
                        address.append(addressesList.get(0).getThoroughfare()).append("\n");
                }

                if (addressesList.get(0).getLocality() != null) {
                        address.append(addressesList.get(0).getLocality()).append("\n");
                }

                if (addressesList.get(0).getPostalCode() != null) {
                        address.append(addressesList.get(0).getPostalCode()).append("\n");
                }

                if (addressesList.get(0).getCountryName() != null) {
                        address.append(addressesList.get(0).getCountryName()).append("\n");
                }
            }

            TextView addressTextView = findViewById(R.id.addressTextView);
            addressTextView.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }
}
