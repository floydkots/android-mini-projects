package pro.kots.multipleactivitiesdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView friendsListView;
    ArrayList<String> friendsList;
    ArrayAdapter<String> friendsArrayAdapter;
    Intent secondActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        friendsListView = findViewById(R.id.friendsListView);
        secondActivityIntent = new Intent(getApplicationContext(), SecondActivity.class);

        friendsList = new ArrayList<>();
        friendsList.add("Beryl");
        friendsList.add("Clinton");
        friendsList.add("Dd");
        friendsList.add("Dorothy");
        friendsList.add("David");

        friendsArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendsList);
        friendsListView.setAdapter(friendsArrayAdapter);

        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String chosenFriend = friendsListView.getItemAtPosition(i).toString();
                secondActivityIntent.putExtra("friend", chosenFriend);
                startActivity(secondActivityIntent);
            }
        });

    }

    public void toSecondActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
        intent.putExtra("username", "rob");
        startActivity(intent);
    }

}
