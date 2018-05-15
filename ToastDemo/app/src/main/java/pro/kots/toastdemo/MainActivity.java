package pro.kots.toastdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickMe(View view) {
        EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        Toast.makeText(this, "Hi there, " + nameEditText.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}
