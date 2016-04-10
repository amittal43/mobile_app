package com.build2gether.fx.Firebase;

import android.annotation.SuppressLint;
import android.provider.ContactsContract;
import android.util.Log;

import com.build2gether.fx.OOP.Inventory;
import com.build2gether.fx.OOP.Message;
import com.build2gether.fx.OOP.Notification;
import com.build2gether.fx.OOP.User;
import com.facebook.AccessToken;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.facebook.Profile;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by abhilashnair on 3/7/16.
*/
public class FirebaseIO {

    private Firebase root;
    private static ArrayList<String> usersID = new ArrayList<String>();
    public HashMap<Inventory, String> inventoryList = new HashMap<Inventory, String>();

    public FirebaseIO() {

        this.root = new Firebase(FirebaseUtil.URL.toString());

    }

    public void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            root.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {

                @SuppressLint("LongLogTag")
                @Override
                public void onAuthenticated(AuthData authData) {
                    //The facebook user is authenticated with the firebase app
                    Log.v("Authenticated successfully with payload:", authData.toString());
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    //there was an error
                    Log.e("Login Failed!", firebaseError.toString());
                }
            });
        } else {

            root.unauth();

        }
    }

    public void saveUser(String id, double latitude, double longitude, String Name) {

        root = new Firebase(FirebaseUtil.USERS.toString());
        Firebase childOfroot = root.child(Profile.getCurrentProfile().getId());
        Firebase childofChild = childOfroot.child("profile");
        childofChild.child("latitude").setValue(latitude);
        childofChild.child("longitude").setValue(longitude);
        childofChild.child("name").setValue(Profile.getCurrentProfile().getName());
        childofChild.child("picture url").setValue("http://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?type=large");

    }

    public void saveInventory(String title, String description, boolean isFavor, boolean exchange, String ItemURL) {
        //ItemOrFavorInfo itemOrFavorInfo = new ItemOrFavorInfo(title, description,isFavor,exchange, ItemURL);
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        System.out.println("Current Date: " + ft.format(dNow));
        Inventory item = new Inventory(title, description, isFavor, exchange, ItemURL, ft.format(dNow));
        root = new Firebase(FirebaseUtil.INVENTORY.toString());
        Firebase childOfRoot = root.child(Profile.getCurrentProfile().getId());
        System.out.println("Inventory Root: " + childOfRoot);
        childOfRoot.push().setValue(item);

    }

    public void saveOffer(String offer, String myId, String theirId, String itemId, String status) {
        Log.d("notification", "trying to save offer");
        root = new Firebase(FirebaseUtil.NOTIFICATION.toString());
        Firebase childOfRoot = root.child(myId);
        Notification notification = new Notification(offer, theirId, itemId, status);
        childOfRoot.push().setValue(notification);
    }

    public void saveMessage(String sender, String text, String chatroomID) {
        root = new Firebase(FirebaseUtil.CHATROOM.toString() + "/" + chatroomID + "/message");
        Message newMessage = new Message(sender, text);
        root.push().setValue(newMessage);
    }

    public ArrayList<String> getUsers() {
        root = new Firebase(FirebaseUtil.USERS.toString());
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("There are " + dataSnapshot.getChildrenCount() + "users");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String ID = postSnapshot.getKey();
                    for (DataSnapshot another : postSnapshot.getChildren()) {
                        usersID.add(another.child("name").getValue().toString());
                    }
                    postSnapshot.getValue();
                    //System.out.println(ID);
                    usersID.add(ID);
                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        return usersID;
    }

    public String getUsersbyID(String ID) {
        root = new Firebase(FirebaseUtil.USERS.toString());
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("There are " + dataSnapshot.getChildrenCount() + "users");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    System.out.println(postSnapshot.getValue().toString());
                    for (DataSnapshot another : postSnapshot.getChildren()) {
                        usersID.add(postSnapshot.getKey());
                        System.out.println("POST" + postSnapshot.getKey());
                    }
                    //System.out.println(ID);


                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        return ID;
    }

    public String getLatitudeByID(String ID) {
        final String latitude = "";
        root = new Firebase(FirebaseUtil.USERS.toString() + "/" + ID);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("There are " + dataSnapshot.getChildrenCount() + "users");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    latitude.concat(postSnapshot.child("latitude").getValue().toString());
                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        return latitude;
    }

    public String getLongitudeByID(String ID) {
        final String longitude = "";
        root = new Firebase(FirebaseUtil.USERS.toString() + "/" + ID);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("There are " + dataSnapshot.getChildrenCount() + "users");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    longitude.concat(postSnapshot.child("latitude").getValue().toString());
                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        return longitude;
    }

    public void getChatRoom() {
        root = new Firebase(FirebaseUtil.MESSENGERUSER.toString() + "/" + Profile.getCurrentProfile().getId());
        System.out.println("URL: " + root);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String contact = dataSnapshot.child("contacts").getValue().toString();
                System.out.println("Contact: " + contact);
                int index = contact.indexOf(":");
                String chatRoom = contact.substring(index + 1, contact.length() - 1);
                System.out.println(chatRoom);
                getMessageFromChatRoom(chatRoom);

            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void getMessageFromChatRoom(String chatRoom) {
        root = new Firebase(FirebaseUtil.CHATROOM.toString() + "/" + chatRoom + "/message");
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.child("sender").getValue().equals(Profile.getCurrentProfile().getId())) {
                        System.out.println(Profile.getCurrentProfile().getId() + " says " + postSnapshot.child("text").getValue());
                    } else {
                        System.out.println("Another user says " + postSnapshot.child("text").getValue());
                    }

                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public ArrayList<String> getContacts() {
        root = new Firebase(FirebaseUtil.MESSENGERUSER.toString());
        System.out.println("URL :" + root);
        final ArrayList<String> contacts = new ArrayList<String>();
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                System.out.println("List of Contacts: ");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    System.out.println(postSnapshot.toString());
                    contacts.add(postSnapshot + "");
                }
                System.out.println(dataSnapshot.getValue() + "");

                System.out.println("CONTACTS: " + dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        return contacts;
    }

    public HashMap<Inventory, String> retrieveInventory() {
        Log.d("database", "in retrieve inventory");
        getUsers();
        Firebase ownerRoot = new Firebase(FirebaseUtil.INVENTORY.toString());
        //inventoryList.clear();
        ownerRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot ownerID : dataSnapshot.getChildren()) {

                    System.out.println(ownerID.toString());
                    for (DataSnapshot ItemID : ownerID.getChildren()) {

                        String userId = ownerID.getKey();
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
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        Log.d("lengthyyy", String.valueOf(inventoryList.size()));
        return inventoryList;
    }


}