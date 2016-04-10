package com.build2gether.fx;

import android.widget.TextView;

import com.build2gether.fx.Firebase.FirebaseUtil;
import com.facebook.Profile;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by adityamittal on 4/1/16.
 */
public class testChat{

    private static Firebase root;
//    root.authWithPassword("bobtony@firebase.com", "correcthorsebatterystaple", new Firebase.ValueResultHandler<Map<String, Object>>() {
//        @Override
//        public void onSuccess(Map<String, Object> result) {
//            System.out.println("Successfully created user account with uid: " + result.get("uid"));
//        }
//        @Override
//        public void onError(FirebaseError firebaseError) {
//            // there was an error
//        }
//    });
//    private Firebase root = new Firebase(FirebaseUtil.URL.toString());

    public static ArrayList<String> getContacts() {
        root = new Firebase(FirebaseUtil.MESSENGERUSER.toString() + "/10154664412197388");
        final ArrayList<String> contacts = new ArrayList<String>();
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                System.out.println("List of Contacts: ");
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    System.out.println(postSnapshot.toString());
//                    contacts.add(postSnapshot+"");
//                }
                System.out.println("CONTACTS: " + dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        return contacts;
    }


    public static void getChatRoom(final String contactId) {
        root = new Firebase(FirebaseUtil.MESSENGERUSER.toString() + "/10154664412197388" + "/contacts");
        System.out.println("URL: " + root);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Inside GetChatRoom!");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    System.out.println("Inside GetChatRoom!");
                    System.out.println("DataSnapshot: " + postSnapshot);
                    System.out.println("contactId: " + contactId);
                    if(postSnapshot.equals(contactId)){
                        String contact = postSnapshot.getValue().toString();
                        System.out.println("Contact: " + contact);
                        int index = contact.indexOf(":");
                        String chatRoom = contact.substring(index + 1, contact.length() - 1);
                        System.out.println(chatRoom);
                        getMessageFromChatRoom(chatRoom);
                    }
//                    String contact = dataSnapshot.child("contacts").getValue().toString();
//                    System.out.println("Contact: " + contact);
//                    int index = contact.indexOf(":");
//                    String chatRoom = contact.substring(index + 1, contact.length() - 1);
//                    System.out.println(chatRoom);
//                    getMessageFromChatRoom(chatRoom);
                }

            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public static void getMessageFromChatRoom(String chatRoom) {
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

    public static void main(String args[]) {

        for(int i = 0; i < getContacts().size(); i++) {
            System.out.println("Contacts Gotten" + getContacts().toString());
        }

    }

}
