package pro.kots.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    CountDownTimer countDownTimer;
    TextView timeTextView;
    TextView equationTextView;
    TextView resultTextView;
    TextView scoreTextView;
    LinearLayout goLayout;
    LinearLayout trainerLayout;
    GridLayout buttonsGrid;
    Button playAgainButton;
    int var1;
    int var2;
    int answer;
    int attemptedQuestions;
    int correctlyAnswered;
    boolean trainingInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goLayout = findViewById(R.id.goLayout);
        trainerLayout = findViewById(R.id.trainerLayout);
        timeTextView = findViewById(R.id.timeTextView);
        resultTextView = findViewById(R.id.resultTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        equationTextView = findViewById(R.id.equationTextView);
        buttonsGrid = findViewById(R.id.buttonsGrid);
        playAgainButton = findViewById(R.id.playAgainButton);

        playAgainButton.setVisibility(View.INVISIBLE);
        goLayout.setVisibility(View.VISIBLE);
        trainerLayout.setVisibility(View.INVISIBLE);


        long millisInFuture = 30 * 1000 + 100;
        long countDownInterval = 1000;
        countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long l) {
                displayTimeLeft( (int) l / 1000);
            }

            @Override
            public void onFinish() {
                displayTimeLeft(0);
                finishTraining();
            }
        };
    }

    private int getRandomInt(int max, int min) {
        return (int) (Math.random() * max + min);
    }

    private String createEquation() {
        int max = 20;
        int min = 1;
        var1 = getRandomInt(max, min);
        var2 = getRandomInt(max, min);
        return getApplicationContext().getString(R.string.equation_text, var1, var2);
    }

    private Set<Integer> createAnswers() {
        answer = var1 + var2;
        HashSet<Integer> answers = new HashSet<>();
        answers.add(answer);
        while (answers.size() < 4) {
            answers.add(getRandomInt(50, answer / 2));
        }
        return answers;
    }

    private void displayAnswers() {
        Set<Integer> answers = createAnswers();
        Integer[] answersArray = answers.toArray(new Integer[answers.size()]);
        final int buttonsCount = buttonsGrid.getChildCount();
        for (int i = 0; i < buttonsCount; i++) {
            Button button = (Button) buttonsGrid.getChildAt(i);
            button.setText(String.valueOf(answersArray[i]));
        }
    }

    private void displayNewEquationAndAnswers() {
        displayNewEquation();
        displayAnswers();
    }

    private void displayNewEquation() {
        equationTextView.setText(createEquation());
    }

    private void startTrainingHelper() {
        resultTextView.setText("");
        goLayout.setVisibility(View.INVISIBLE);
        trainerLayout.setVisibility(View.VISIBLE);
        displayNewEquationAndAnswers();
        startTimer();
        attemptedQuestions = 0;
        correctlyAnswered = 0;
        displayScore();
        trainingInProgress = true;
    }

    public void startTraining(View view) {
        startTrainingHelper();
    }

    private void startTimer() {
        countDownTimer.start();
    }


    private void displayTimeLeft(int seconds) {
        String timeText = getApplicationContext().getString(R.string.time_text, seconds);
        timeTextView.setText(timeText);
    }

    private void displayResult(String result) {
        resultTextView.setText(result);
    }

    private String getScoreString() {
        return getApplicationContext().getString(R.string.score_text, correctlyAnswered, attemptedQuestions);
    }

    private void displayScore() {
        scoreTextView.setText(getScoreString());
    }

    public void checkAnswer(View view) {
        if (trainingInProgress) {
            Button button = (Button) view;
            int tappedAnswer = Integer.parseInt(button.getText().toString());
            if (tappedAnswer == answer) {
                displayResult("Correct!");
                correctlyAnswered += 1;
            } else {
                displayResult("Wrong!");
            }
            attemptedQuestions += 1;
            displayNewEquationAndAnswers();
            displayScore();
        }
    }

    private void finishTraining() {
        String resultText = "Your score: " + getScoreString();
        displayResult(resultText);
        playAgainButton.setVisibility(View.VISIBLE);
        trainingInProgress = false;
    }
}
