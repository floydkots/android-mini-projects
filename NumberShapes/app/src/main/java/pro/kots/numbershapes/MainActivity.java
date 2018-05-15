package pro.kots.numbershapes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void testNumber(View view) {
        EditText usersNumber = findViewById(R.id.usersNumber);

        try {
            int number = Integer.parseInt(usersNumber.getText().toString());

            Number myNumber = new Number(number);

            String msg = String.valueOf(myNumber.number);

            if (myNumber.isSquare()) {
                if (myNumber.isTriangular()) {
                    msg += " is both triangular and square!";
                } else {
                    msg += " is square but not triangular!";
                }
            } else if (myNumber.isTriangular()) {
                msg += " is triangular but not square!";
            } else {
                msg += " is neither triangular nor square!";
            }
            makeToast(msg);
        } catch (NumberFormatException nfe) {
            makeToast("Enter a number!");
        }
    }
}
