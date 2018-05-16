package pro.kots.multipleactivitiesdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    TextView friendTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        friendTextView = findViewById(R.id.friendTextView);

        Intent intent = getIntent();
        String friend = intent.getStringExtra("friend");
        String greeting = "Hello \n" + friend + "!";
        friendTextView.setText(greeting);
    }

    public void toMainActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
