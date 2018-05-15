package pro.kots.whatstheweather;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        resultTextView = findViewById(R.id.resultTextView);
    }

    public void findWeather (View view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(cityName.getWindowToken(), 0);
        }
        String city = cityName.getText().toString();

        DownloadTask task = new DownloadTask();
        try {
            String encodedCity = URLEncoder.encode(city, "UTF-8");
            Map<String, String> weatherMap = task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=909ad1da964ffe564dc36e0de90b6dd3").get();
            displayWeather(weatherMap);
        } catch (Exception e) {
            Toast.makeText(this, "Could not find weather", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayWeather(Map<String, String> weatherMap) {
        Iterator iterator = weatherMap.entrySet().iterator();
        StringBuilder message = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            message.append(pair.getKey()).append(": ").append(pair.getValue()).append("\r\n");
        }
        if (!message.toString().isEmpty()) {
            resultTextView.setText(message);
        }
    }
}
