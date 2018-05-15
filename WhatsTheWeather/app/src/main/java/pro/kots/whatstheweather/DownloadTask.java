package pro.kots.whatstheweather;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DownloadTask extends AsyncTask<String, Void, Map<String, String>> {

    @Override
    protected Map<String, String> doInBackground(String... urls) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urls[0]);
            HttpURLConnection urlConnection = null;
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            int data = reader.read();
            while (data != -1) {
                result.append((char) data);
                data = reader.read();
            }
            return processJSON(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, String> processJSON (String s) {
        Map<String, String> weatherMap = new HashMap<>();
        String weatherContent = "";
        try {
            JSONObject jsonObject = new JSONObject(s);
            weatherContent = jsonObject.getString("weather");
            Log.i("Weather content", weatherContent);
            JSONArray arr = new JSONArray(weatherContent);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jsonPart = arr.getJSONObject(i);
                String main = jsonPart.getString("main");
                String description = jsonPart.getString("description");
                weatherMap.put(main, description);
            }
            return weatherMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
