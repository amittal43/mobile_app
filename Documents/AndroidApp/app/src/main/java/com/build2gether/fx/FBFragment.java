package com.build2gether.fx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.build2gether.fx.Firebase.FirebaseIO;
import com.build2gether.fx.Location.GPSTracker;
import com.build2gether.fx.OOP.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;



import java.util.Arrays;

public class FBFragment extends Fragment {

    private CallbackManager mCallbackManager;
    private boolean signedIn = false;
    private Profile profile;
    private User user;

    public FBFragment() {}

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            if (loginResult.getAccessToken() != null) {
                AccessToken accessToken = loginResult.getAccessToken();
                profile = Profile.getCurrentProfile();
                if (profile != null) {
                    user = new User(profile);
                }
                
                signedIn = true;
                updateUI(true);
                FirebaseIO firebaseIO = new FirebaseIO();
                firebaseIO.onFacebookAccessTokenChange(accessToken);
                Log.d("location", "in nav");
                GPSTracker gps = new GPSTracker(getContext());
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                firebaseIO.saveUser(profile.getId(), latitude, longitude, profile.getName());
            }
        }

        @Override
        public void onCancel() {
            // App code
        }

        @Override
        public void onError(FacebookException exception) {
            // App code
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        updateUI(signedIn);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(signedIn);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
    }

    private void updateUI(boolean signedIn) {
        this.signedIn = signedIn;
        if(signedIn) {
            this.getView().findViewById(R.id.login_button).setVisibility(View.GONE);
            startActivity(new Intent(getActivity(), MainPageActivity.class));
        } else {
            this.getView().findViewById(R.id.login_button).setVisibility(View.VISIBLE);
        }
    }

    private void setupFBLogin(View view) {
        LoginButton fbLogin = (LoginButton) view.findViewById(R.id.login_button);
        String[] array = {"email", "user_birthday"};
        fbLogin.setReadPermissions(Arrays.asList(array));
        fbLogin.setFragment(this);
        fbLogin.registerCallback(mCallbackManager, mCallback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fb, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupFBLogin(view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}