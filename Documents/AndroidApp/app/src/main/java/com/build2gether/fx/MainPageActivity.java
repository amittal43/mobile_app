package com.build2gether.fx;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.build2gether.fx.Chat.ContactListActivity;
import com.build2gether.fx.Firebase.FirebaseIO;
import com.build2gether.fx.Firebase.FirebaseUtil;
import com.build2gether.fx.OOP.Inventory;
import com.build2gether.fx.OOP.User;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class MainPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static HashMap<Inventory, String> inventoryList = new HashMap<Inventory, String>();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private User user;
    private ListView listView;
    //    private static HashMap<Inventory, String> items;
    private Set<Inventory> set;
    private ArrayList<Inventory> listOfItems;
    private InventoryArrayAdapter listAdapter;
    private AdapterView.OnItemClickListener itemClickListener;
    private String latitiude;
    private String longitude;

    private Toolbar toolbar;
    static int num = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debuggg", "in on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        //list view

        listView = (ListView) findViewById(R.id.ItemFavListView);

        asyncRetrieveInventory();

        //Tina's stuff
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToPostScr();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void goToPostScr() {
        startActivity(new Intent(this, PostActivity.class));
    }

    @Override
    public void onResume() {
        Log.d("debuggg", "in on resume");
        super.onResume();
    }

    @Override
    public void onStart() {
        Log.d("debuggg", "in on start");
        super.onStart();
    }

    @Override
    public void onRestart() {
        Log.d("debuggg", "in on resume");
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
            super.onBackPressed();
            //startActivity(new Intent(this, MainPageActivity.class));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        user = new User(Profile.getCurrentProfile());
        TextView userName = (TextView) findViewById(R.id.userName);
        userName.setText("Welcome " + user.getName());

        ProfilePictureView proPic = (ProfilePictureView) findViewById(R.id.proPic);
        proPic.setProfileId(user.getId());

        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.chat_window) {
            Intent intent = new Intent(this, ContactListActivity.class);
            startActivity(intent);
        } else if (id == R.id.signout) {
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("finish", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
            startActivity(intent);
            finish();
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void asyncRetrieveInventory() {
        Log.d("debuggg", "in retrieve Inventory");
        Firebase ref = new Firebase(FirebaseUtil.INVENTORY.toString());
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                inventoryList.clear();
                Log.d("debuggg", "in on DataChange");
                for (DataSnapshot ownerID : dataSnapshot.getChildren()) {
                    Log.d("debuggg", "in on DataChange 1");
                    String userId = ownerID.getKey();
                    for (DataSnapshot ItemID : ownerID.getChildren()) {
                        Log.d("debuggg", "in on DataChange 2");

                        String title = ItemID.child("title").getValue().toString();
                        String date = ItemID.child("date").getValue().toString();

                        String description = ItemID.child("description").getValue().toString();
                        boolean exchange = (boolean) ItemID.child("exchange").getValue();
                        boolean isFavor = (boolean) ItemID.child("favor").getValue();
                        String itemURL = ItemID.child("image").getValue().toString();

                        Inventory item = new Inventory(title, description, exchange, isFavor, itemURL, date);

                        inventoryList.put(item, userId);

                    }

                }

                populateHomePage();

                Log.d("debuggg", "in after retrieve inventory");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

    private void populateHomePage() {


        System.out.println("Size of Inventory: " + inventoryList.size());


        Log.d("debuggg", "in on create 1");
        set = inventoryList.keySet();
        listOfItems = new ArrayList<Inventory>();
        for (Inventory i : set) {
            listOfItems.add(i);
            Log.d("HashMap", i.getTitle());
        }
        Log.d("debuggg", "in on create 2");

        listAdapter = new InventoryArrayAdapter(this, listOfItems);

        EditText searchbar = (EditText) findViewById(R.id.search_edit_frame);

        searchbar.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                listAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });







        //assign list view to adapter
        listView.setAdapter(listAdapter);

        //Click Listener
        itemClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView,
                                    View itemView, int position, long id) {
                //Do something when an item is clicked
                Intent intent = new Intent(MainPageActivity.this, ItemOrFavorInfoActivity.class);//detail view of Item or Favor
                intent.putExtra(ItemOrFavorInfoActivity.EXTRA_LISTNUM, (int) id);//add id of list item and send it to list

                startActivity(intent);

            }
        };

        //assign item click listener
        listView.setOnItemClickListener(itemClickListener);
        Log.d("debuggg", "in on create 3");

    }


}