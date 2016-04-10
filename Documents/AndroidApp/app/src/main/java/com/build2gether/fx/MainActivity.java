package com.build2gether.fx;

import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;


public class MainActivity extends AppCompatActivity {

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private FBFragment fbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        AccessToken curr = AccessToken.getCurrentAccessToken();
        addFB(curr);


        //transaction.add(R.id.main_layout, login_and_signup, "Login_and_Signup");
        //transaction.commit();
    }

    private void addFB(AccessToken curr) {
        if(curr == null) {
            fbLogin = new FBFragment();
            //googleLogin = new GoogleFragment();

            manager = getSupportFragmentManager();
            transaction = manager.beginTransaction();
            //transaction.replace(R.id.main_layout, fbLogin, "FacebookLogin");
            transaction.add(R.id.main_layout, fbLogin, "FacebookLogin");
            //transaction.add(R.id.main_layout, googleLogin, "GoogleLogin");
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            startActivity(new Intent(this, MainPageActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //updateWithToken(AccessToken.getCurrentAccessToken());
    }

    @Override
    public void onRestart() {
        super.onRestart();
        AccessToken curr = AccessToken.getCurrentAccessToken();
        addFB(curr);
    }

    @Override
    public void onStart() {
        super.onStart();
        //updateWithToken(AccessToken.getCurrentAccessToken());
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}