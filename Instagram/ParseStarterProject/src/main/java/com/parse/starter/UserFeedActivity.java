package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    String activeUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        Intent intent = getIntent();
        activeUsername = intent.getStringExtra("username");
        setTitle(activeUsername + "'s Feed" );

        fetchAndDisplayUsersImages();
    }

    private ImageView createImageView(Bitmap bitmap) {
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        imageView.setImageBitmap(bitmap);
        return imageView;
    }

    private void displayImageBitmap(Bitmap bitmap) {
        linearLayout.addView(createImageView(bitmap));
    }

    private void fetchAndDisplayUsersImages() {
        ParseQuery<ParseObject> query = new ParseQuery<>("Image");
        query.whereEqualTo("username", activeUsername);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            ParseFile file = (ParseFile) object.get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null && data != null) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        displayImageBitmap(bitmap);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}
