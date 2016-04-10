package com.build2gether.fx;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatPopupWindow;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ImageView;

import com.build2gether.fx.Firebase.FirebaseIO;
import com.build2gether.fx.Firebase.FirebaseUtil;
import com.build2gether.fx.OOP.Inventory;
import com.build2gether.fx.OOP.User;
import com.facebook.login.widget.ProfilePictureView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.*;
import com.squareup.picasso.Request.Builder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * Created by abhilashnair on 2/22/16.
 */

public class ItemOrFavorInfoActivity extends FragmentActivity{


    public static final String EXTRA_LISTNUM = "ListNo";

    PopupWindow offerPopUp;
    LinearLayout layout;
    TextView tv;
    LinearLayout.LayoutParams params;
    View offerPopUpView;



    boolean click = true;

    private static String ownerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favor_item_info);
        Button AskButton = (Button) findViewById(R.id.Ask);


        offerPopUpView = getLayoutInflater().inflate(R.layout.offer_popup, null);
        offerPopUp = new PopupWindow(offerPopUpView, 600, 600);

        TextView text = (TextView) offerPopUpView.findViewById(R.id.test);

        text.setText("We got em");











        HashMap<Inventory, String> inventory = MainPageActivity.inventoryList;
        ArrayList<Inventory> inventoryList = InventoryArrayAdapter.getInventory();


        int listNo = (Integer) getIntent().getExtras().get(EXTRA_LISTNUM);
        Inventory item = inventoryList.get(listNo);
        String userId = inventory.get(item);

        System.out.println("ID: " + userId);
        getLongitudeAndLatitudeByID(userId);
        asyncRetrieveOwnerName(userId);
        ImageView UserImage = (ImageView) findViewById(R.id.proPic);

        Picasso.with(this).load("https://graph.facebook.com/" + userId + "/picture?type=large").into(UserImage);


        ImageView photo = (ImageView) findViewById(R.id.pic);


        Picasso.with(this).load(inventoryList.get(listNo).getImage()).resize(800, 1080).rotate(90).into(photo);

        TextView name = (TextView)findViewById(R.id.title);
        name.setText(inventoryList.get(listNo).getTitle());



        TextView description = (TextView)findViewById(R.id.description);
        description.setText(inventoryList.get(listNo).getDescription());

        CheckBox favors = (CheckBox) findViewById(R.id.favors);
        CheckBox exchanges = (CheckBox) findViewById(R.id.exchanges);
        favors.setEnabled(false);
        exchanges.setEnabled(false);
        if (item.getFavor()) {
            favors.setChecked(true);
        }
        if (item.getExchange()) {
            exchanges.setChecked(true);
        }







    }

    public void onAskButtonClick(View view) {
        if(click) {
            offerPopUp.showAtLocation(offerPopUpView, Gravity.CENTER, 20, 20);
            offerPopUp.setFocusable(true);
            offerPopUp.update(1000, 1000);
            click = false;
        }

    }

    public void onSendOfferClick(View view) {
        EditText offerText = (EditText) view.findViewById(R.id.OfferInput);
        String offer = offerText.getText().toString();
        


        offerPopUp.dismiss();
        click = true;
    }

    public void asyncRetrieveOwnerName(String id) {

        Firebase ref = new Firebase(FirebaseUtil.USERS.toString() + "/" + id);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    ownerName = userSnapshot.child("name").getValue().toString();
                    TextView OwnerName = (TextView) findViewById(R.id.OwnerName);
                    OwnerName.setText(ownerName);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

    }
    public void getLongitudeAndLatitudeByID(String ID) {

        Firebase root = new Firebase(FirebaseUtil.USERS.toString() + "/" + ID);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("There are " + dataSnapshot.getChildrenCount() + "users");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Double latitude = Double.valueOf(postSnapshot.child("latitude").getValue().toString());
                    Double longitude = Double.valueOf(postSnapshot.child("longitude").getValue().toString());

                    SetMapFragment(latitude, longitude);


                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
    private void SetMapFragment(final Double latitude, final Double longitude) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFrag);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                System.out.println("Latitude: " + latitude);
                System.out.println("Longitude: " + longitude);
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title("Marker"));


            }
        });

    }
}