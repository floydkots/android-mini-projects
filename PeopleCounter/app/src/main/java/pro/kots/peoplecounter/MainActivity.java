package pro.kots.peoplecounter;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private TextView countTextView;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enables Always-on
        setAmbientEnabled();

        counter = 0;
        countTextView = findViewById(R.id.countTextView);
        updateTextView();
    }

    public void plusOne(View view) {
        counter++;
        updateTextView();
    }

    public void reset(View view) {
        counter = 0;
        updateTextView();
    }

    private void updateTextView() {
        countTextView.setText(getString(R.string.count, counter));
    }
}
