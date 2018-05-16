package pro.kots.memorableplaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ListView placesListView;
    private static ArrayList<String> placesList;
    private static ArrayAdapter<String> placesListAdapter;

    protected static Map<String, Location> namedLocations;
    private static SharedPreferences sharedPreferences;

    Intent mapIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placesListView = findViewById(R.id.placesListView);
        placesList = new ArrayList<>();
        namedLocations = new HashMap<>();

        placesListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, placesList);
        placesListView.setAdapter(placesListAdapter);
        setPlacesAndNamedLocations();

        sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);


        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String address = placesListView.getItemAtPosition(i).toString();
                showAddressOnMap(address);
            }
        });

        mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
    }

    private void setPlacesAndNamedLocations() {
        ArrayList<String> places = new ArrayList<>();
        ArrayList<String> lats = new ArrayList<>();
        ArrayList<String> lons = new ArrayList<>();
        try {
            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places", ObjectSerializer.serialize(new ArrayList<String>())));
            lats = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats", ObjectSerializer.serialize(new ArrayList<String>())));
            lons = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lons", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (places.size() > 0 && lats.size() > 0 && lons.size() > 0) {
            if (places.size() == lats.size() && lats.size() == lons.size()) {
                for (String place : places) {
                    addPlace(place);
                }
                namedLocations = namedLocationsFromArrayLists(places, lats, lons);
                Log.i("sharedPreferences", "Retrieval successful!");
            }
        }
    }

    private void showAddressOnMap(String address) {
        mapIntent.putExtra("address", address);
        startActivity(mapIntent);
    }

    public void addNewPlace(View view) {
        startActivity(mapIntent);
    }

    private static void addPlace(String place) {
        placesList.add(place);
        placesListAdapter.notifyDataSetChanged();
    }

    protected static void addNamedLocation(String address, Location location) {
        int counter = 1;
        while (namedLocations.containsKey(address)) {
            address += counter++;
        }
        MainActivity.namedLocations.put(address, location);
        addPlace(address);
        try {
            ArrayList<ArrayList<String>> arrayLists = arrayListsFromNamedLocations();
            sharedPreferences.edit().putString("places", ObjectSerializer.serialize(arrayLists.get(0))).apply();
            sharedPreferences.edit().putString("lats", ObjectSerializer.serialize(arrayLists.get(1))).apply();
            sharedPreferences.edit().putString("lons", ObjectSerializer.serialize(arrayLists.get(2))).apply();
            Log.i("sharedPreferences", "Save successful!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<ArrayList<String>> arrayListsFromNamedLocations() {
        ArrayList<String> places = new ArrayList<>();
        ArrayList<String> lats = new ArrayList<>();
        ArrayList<String> lons = new ArrayList<>();

        for (Object o : namedLocations.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            places.add(pair.getKey().toString());
            lats.add(Double.toString(((Location) pair.getValue()).getLatitude()));
            lons.add(Double.toString(((Location) pair.getValue()).getLongitude()));
        }
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
        arrayLists.add(places);
        arrayLists.add(lats);
        arrayLists.add(lons);
        return arrayLists;
    }

    private static Map<String, Location> namedLocationsFromArrayLists(ArrayList<String> places, ArrayList<String> lats, ArrayList<String> lons) {
        Map<String, Location> namedLocations = new HashMap<>();
        if (places.size() == lats.size() && lats.size() == lons.size()) {
            for (int i = 0; i < places.size(); i++) {
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(Double.parseDouble(lats.get(i)));
                location.setLongitude(Double.parseDouble(lons.get(i)));
                namedLocations.put(places.get(i), location);
            }
            return namedLocations;
        } else {
            return null;
        }
    }

    protected static Location getNamedLocation(String address) {
        return namedLocations.get(address);
    }
}
