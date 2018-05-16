package pro.kots.databasedemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            SQLiteDatabase myDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);

//            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS newUsers (name VARCHAR, age INT(3), id INT PRIMARY KEY)");
//            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (name VARCHAR, age INT(3))");
//            myDatabase.execSQL("INSERT INTO newUsers (name, age) VALUES ('Rob', 34)");
//            myDatabase.execSQL("INSERT INTO newUsers (name, age) VALUES ('Floyd', 24)");
//            myDatabase.execSQL("INSERT INTO newUsers (name, age) VALUES ('Tommy', 4)");
//            myDatabase.execSQL("INSERT INTO newUsers (name, age) VALUES ('Beta', 18)");
//            myDatabase.execSQL("INSERT INTO newUsers (name, age) VALUES ('Florence', 16)");

            myDatabase.execSQL("DELETE FROM newUsers WHERE id = 1");

//            myDatabase.execSQL("UPDATE users SET age = 2 WHERE name = 'Tommy'");

            Cursor c = myDatabase.rawQuery("SELECT * FROM newUsers", null);
            int nameIndex = c.getColumnIndex("name");
            int ageIndex = c.getColumnIndex("age");
            int idIndex = c.getColumnIndex("id");

            c.moveToFirst();
            while (c != null) {
                Log.i("UserResults - name", c.getString(nameIndex));
                Log.i("UserResults - age", Integer.toString(c.getInt(ageIndex)));
                Log.i("UserResults - id", Integer.toString(c.getInt(idIndex)));

                c.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*try {
            SQLiteDatabase eventsDb = this.openOrCreateDatabase("Events", MODE_PRIVATE, null);
            eventsDb.execSQL("CREATE TABLE IF NOT EXISTS events (name VARCHAR, year INT(4))");
            eventsDb.execSQL("INSERT INTO events (name, year) VALUES ('Kenya Independence', 1963)");
            eventsDb.execSQL("INSERT INTO events (name, year) VALUES ('Floyd Birthday', 1993)");

            Cursor eventsCursor = eventsDb.rawQuery("SELECT * FROM events", null);
            int nameIndex = eventsCursor.getColumnIndex("name");
            int yearIndex = eventsCursor.getColumnIndex("year");

            eventsCursor.moveToFirst();
            while (eventsCursor != null) {
                Log.i("name", eventsCursor.getString(nameIndex));
                Log.i("year", Integer.toString(eventsCursor.getInt(yearIndex)));

                eventsCursor.moveToNext();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
