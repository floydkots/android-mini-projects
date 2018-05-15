package pro.kots.higherorlower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int randomNumber = generateInt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void displayToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void displayToast(String msg, int length) {
        Toast.makeText(this, msg, length).show();
    }

    private static int generateInt() {
        Random rand = new Random();
        int min = 1;
        int max = 20;
        return rand.nextInt((max - min) + min) + min;
    }

    public void guess(View view) {
        EditText guessEditText = findViewById(R.id.guessEditText);
        int guess = Integer.parseInt(guessEditText.getText().toString()) ;
        if (guess > randomNumber) {
            displayToast("Lower!");
        } else if (guess < randomNumber) {
            displayToast("Higher!");
        } else {
            displayToast("You guessed correctly!");
            randomNumber = generateInt();
            displayToast("Try again...", Toast.LENGTH_LONG);
            guessEditText.setText("");
        }
    }
}
