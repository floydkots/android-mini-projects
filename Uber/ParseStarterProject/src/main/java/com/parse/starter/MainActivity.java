/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    Switch userTypeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userTypeSwitch = (Switch) findViewById(R.id.userTypeSwitch);

        hideActionBar();

        if (userIsLoggedIn()) {
            redirectToActivity();
        } else {
            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        Log.i("Info", "Anonymous login successful");
                    } else {
                        Log.i("Info", "Anonymous login failed");
                    }
                }
            });
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void getStarted(View view) {
        ParseUser.getCurrentUser().put(UserType.RIDER_OR_DRIVER, riderOrDriver());
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i("Info", "Redirecting as " + riderOrDriver());
                redirectToActivity();
            }
        });

    }

    private void hideActionBar() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private String riderOrDriver() {
        return userTypeSwitch.isChecked() ? UserType.DRIVER : UserType.RIDER;
    }

    private boolean userIsLoggedIn() {
        return !(ParseUser.getCurrentUser() == null);
    }

    private void redirectToActivity() {
        Intent intent;
        if (ParseUser.getCurrentUser().getString(UserType.RIDER_OR_DRIVER).equals(UserType.RIDER)) {
            intent = new Intent(getApplicationContext(), RiderActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), ViewRequestsActivity.class);
        }
        startActivity(intent);
    }

}