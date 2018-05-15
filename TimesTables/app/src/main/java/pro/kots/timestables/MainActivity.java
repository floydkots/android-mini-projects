package pro.kots.timestables;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView timesTextView;
    SeekBar timesSeekBar;
    ListView timesListView;
    int minimumSeekBarValue = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timesTextView = findViewById(R.id.timesTextView);
        timesSeekBar = findViewById(R.id.timesSeekBar);
        timesListView = findViewById(R.id.timesListView);

        timesSeekBar.setMax(19);
        showTimesNumber(getTimesSeekBarProgress());
        showTimesTable();


        timesSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                showTimesNumber(getTimesSeekBarProgress());
                showTimesTable();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private int getTimesSeekBarProgress() {
        return timesSeekBar.getProgress() + minimumSeekBarValue;
    }

    private void showTimesNumber(int num) {
        timesTextView.setText(String.valueOf(num));
    }

    private void showTimesTable() {
        ArrayList<String> timesTable = getTimesTableArrayList(getTimesSeekBarProgress());
        ArrayAdapter<String> timesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timesTable);
        timesListView.setAdapter(timesArrayAdapter);
    }

    private ArrayList<String> getTimesTableArrayList(int num) {
        ArrayList<String> timesTable = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            int product = i * num;
            String timesString = String.valueOf(num) + " x " + String.valueOf(i) + " = " + String.valueOf(product);
            timesTable.add(timesString);
        }
        return timesTable;
    }
}
