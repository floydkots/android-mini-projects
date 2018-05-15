package pro.kots.jsondemo;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
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
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String weatherContent = "";
        try {
            JSONObject jsonObject = new JSONObject(s);
            weatherContent = jsonObject.getString("weather");
            Log.i("Weather content", weatherContent);
            JSONArray arr = new JSONArray(weatherContent);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jsonPart = arr.getJSONObject(i);
                Log.i("Main", jsonPart.getString("main"));
                Log.i("Description", jsonPart.getString("description"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
