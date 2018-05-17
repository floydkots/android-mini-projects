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

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    /*ParseObject score = new ParseObject("Score");
    score.put("username", "rob");
    score.put("score", 86);
    score.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e == null) {
          Log.i("SaveInBackground", "Successful");
        } else {
          Log.i("SaveInBackground", "Failed. Error: " + e.toString());
        }
      }
    });*/

    /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
    query.getInBackground("Dp3QqdNjjN", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {
        if (e == null && object != null) {
          object.put("score", 100);
          object.saveInBackground();

          Log.i("ObjectValue", object.getString("username"));
          Log.i("ObjectValue", Integer.toString(object.getInt("score")));
        }
      }
    });*/

    // Create Tweet class, username and tweet, save on Parse, then query it and update the tweet content
    /*ParseObject tweet = new ParseObject("Tweet");
    tweet.put("username", "Floyd");
    tweet.put("tweet", "Learning to use the parse server");
    tweet.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e == null) {
          Log.i("SaveInBackground", "Successful!");
        } else {
          Log.i("SaveInBackground", "Failed!");
        }
      }
    });*/

    /*ParseQuery<ParseObject> tweetQuery = ParseQuery.getQuery("Tweet");
    tweetQuery.getInBackground("6hvqDgpmo2", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {
        if (e == null && object != null) {
          object.put("tweet", "Learning to use the parse server, and I'm making good progress.");
          object.saveInBackground();

          Log.i("ObjectValue", object.getString("username"));
          Log.i("ObjectValue", object.getString("tweet"));
        }
      }
    });*/

    /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

    query.whereEqualTo("username", "Floyd");
    query.setLimit(1);

    query.whereGreaterThan("score", 200);

    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {
        if (e == null) {
          Log.i("findInBackground", "Retrieved " + objects.size() + " objects");
          if (objects.size() > 0) {
            for (ParseObject object : objects) {
              object.put("score", object.getInt("score") + 50);
              object.saveInBackground();
              Log.i("findInBackgroundResult", object.getString("username") + Integer.toString(object.getInt("score")));
            }
          }
        }
      }
    });*/

    /*ParseUser user = new ParseUser();

    user.setUsername("floydkots");
    user.setPassword("myPass");

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if (e == null) {
          Log.i("Sign Up", "Successful!");
        } else {
          Log.i("Sign Up", "Failed!");
        }
      }
    });*/

    /*ParseUser.logInInBackground("floydkots", "floyd", new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
       if (user != null) {
         Log.i("Login", "Successful!");
       } else {
         Log.i("Login", "Failed! " + e.toString());
       }
      }
    });*/

//    ParseUser.logOut();

    if (ParseUser.getCurrentUser() != null) {
      Log.i("currentUser", "User logged in " + ParseUser.getCurrentUser().getUsername());
    } else {
      Log.i("currentUser", "User not logged in");
    }


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}