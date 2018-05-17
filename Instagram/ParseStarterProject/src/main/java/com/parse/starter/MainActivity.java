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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    boolean signUpModeActive = true;
    Button signupButton;
    TextView changeSignupModeTextView;
    EditText usernameEditText;
    EditText passwordEditText;
    RelativeLayout backgroundRelativeLayout;
    ImageView logoImageView;
    Intent userListIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Instagram");

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        signupButton = (Button) findViewById(R.id.signupButton);
        changeSignupModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);
        backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);
        userListIntent = new Intent(getApplicationContext(), UserListActivity.class);

        if (changeSignupModeTextView != null) {
            changeSignupModeTextView.setOnClickListener(this);
        }

        passwordEditText.setOnKeyListener(this);
        backgroundRelativeLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);

        if (userLoggedIn()) showUserList();

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }


    private boolean userLoggedIn() {
        return ParseUser.getCurrentUser() != null;
    }

    public void signUp(View view) {

        if (usernameEditText != null && passwordEditText != null) {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "A username and password are required", Toast.LENGTH_SHORT).show();
            } else {
                if (signUpModeActive) {
                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(password);

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.i("Signup", "Successful!");
                                showUserList();
                            } else {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null) {
                                Log.i("Signup", "Login successful!");
                                showUserList();
                            } else {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.changeSignupModeTextView) {
            if (signUpModeActive) {
                signUpModeActive = false;
                signupButton.setText(getString(R.string.login_button_text));
                changeSignupModeTextView.setText(getString(R.string.login_mode_text));
            } else {
                signUpModeActive = true;
                signupButton.setText(getString(R.string.signup_button_text));
                changeSignupModeTextView.setText(R.string.signup_mode_text);
            }

        } else if (view.getId() == R.id.backgroundRelativeLayout || view.getId() == R.id.logoImageView) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            signUp(view);
        }
        return false;
    }

    public void showUserList() {
        startActivity(userListIntent);
    }
}