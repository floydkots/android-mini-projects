package pro.kots.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private double convertDollarsToKsh(Double dollars) {
        return dollars * 100.55;
    }

    public void convert(View view) {
        EditText currencyEditText = (EditText) findViewById(R.id.currencyEditText);
        Double converted = convertDollarsToKsh(Double.parseDouble(currencyEditText.getText().toString()));
        Toast.makeText(this, "$" + String.format(Locale.ENGLISH,"%.2f", converted), Toast.LENGTH_LONG).show();
    }
}
