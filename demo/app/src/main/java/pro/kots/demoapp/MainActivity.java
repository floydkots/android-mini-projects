package pro.kots.demoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void nextImage(View view) {
        Log.i("Test", "Button Clicked!");
        ImageView catImageView = (ImageView) findViewById(R.id.catView);
        catImageView.setImageResource(R.drawable.cat2_400);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
