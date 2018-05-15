package pro.kots.animations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView bart = findViewById(R.id.bart);
        bart.setTranslationX(-1000f);
        bart.setTranslationY(-1000f);
    }

    public void fade(View view) {
        ImageView bart = findViewById(R.id.bart);
        bart.animate()
                .translationXBy(1000f)
                .translationYBy(1000f)
                .rotationBy(3600)
                .setDuration(1000);
        /*ImageView homer = findViewById(R.id.homer);

        if (bart.getAlpha() > homer.getAlpha()) {
            bart.animate().alpha(0f).setDuration(500);
            homer.animate().alpha(1f).setDuration(1000);
        } else {
            bart.animate().alpha(1f).setDuration(1000);
            homer.animate().alpha(0f).setDuration(500);
        }*/
    }
}
