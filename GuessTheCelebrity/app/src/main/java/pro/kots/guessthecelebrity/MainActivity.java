package pro.kots.guessthecelebrity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    Map<String, String> celebs;
    List<String> celebNames;
    List<String> celebURLs;
    ImageView imageView;
    String randomCelebName;
    GridLayout buttonsGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        buttonsGrid = findViewById(R.id.buttonsGrid);

        setCelebsNamesAndUrls();
        newChallenge();
    }

    private String fetchArticle() {
        DownloadTask task = new DownloadTask();
        try {
            return task.execute("http://www.posh24.se/kandisar").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setCelebsNamesAndUrls() {
        String article = fetchArticle();
        celebs = new CelebNameImageFetcher(article).getNamesAndImages();
        celebNames = new ArrayList<>(celebs.keySet());
        celebURLs = new ArrayList<>(celebs.values());
        /*logCelebNamesUrls();*/
    }

    private void logCelebNamesUrls() {
        for (Object o : celebs.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            Log.i("Name", pair.getKey().toString());
            Log.i("Image", pair.getValue().toString());
        }
    }

    private int getRandomInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    private String getRandomCelebName () {
        int index = getRandomInt(celebNames.size());
        return celebNames.get(index);
    }

    private Bitmap fetchImage (String url) {
        ImageDownloader imageDownloader = new ImageDownloader();
        try {
            return imageDownloader.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void displayCelebImageAndChoices (String name) {
        Bitmap celebImage = fetchImage(celebs.get(name));
        imageView.setImageBitmap(celebImage);
        HashSet<String> choices = new HashSet<>();
        choices.add(name);
        while (choices.size() < 4) {
            choices.add(getRandomCelebName()) ;
        }
        String[] choicesArray = choices.toArray(new String[choices.size()]);
        for (int i = 0; i < buttonsGrid.getChildCount(); i++) {
            Button button = (Button) buttonsGrid.getChildAt(i);
            button.setText(choicesArray[i]);
        }
    }

    private void newChallenge() {
        randomCelebName = getRandomCelebName();
        displayCelebImageAndChoices(randomCelebName);
    }

    private void showResult(String result) {
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
    }

    public void celebChosen(View view) {
        Button button = (Button) view;
        if (button.getText().toString().equals(randomCelebName)) {
            showResult("Correct!");
        } else {
            showResult("Wrong! " + "It's " + randomCelebName);
        }
        newChallenge();
    }
}
