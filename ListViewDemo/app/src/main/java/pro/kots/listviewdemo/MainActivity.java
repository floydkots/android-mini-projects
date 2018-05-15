package pro.kots.listviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView friendsListView = findViewById(R.id.friendsListView);

        final ArrayList<String> friendsList = new ArrayList<>();
        friendsList.add("Beryl");
        friendsList.add("Clinton");
        friendsList.add("Dorothy");
        friendsList.add("Welline");

        ArrayAdapter<String> friendsListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendsList);
        friendsListView.setAdapter(friendsListAdapter);

        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, friendsList.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
