package com.build2gether.fx.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.build2gether.fx.Firebase.FirebaseUtil;
import com.build2gether.fx.R;
import com.facebook.Profile;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * @version 1.0.1
 * @author Abi
 */
public class ContactListActivity extends AppCompatActivity {

    private ArrayList<String> contacts = new ArrayList<String>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        listView = (ListView) findViewById(R.id.contactListView);
        //setup the whole view
        asyncContacts();
    }

    /**
     * This function will retreive contacts and with this function sets up the whole view
     */
    private void asyncContacts() {
        Firebase root = new Firebase(FirebaseUtil.MESSENGERUSER.toString() + "/" + Profile.getCurrentProfile().getId());

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot another : postSnapshot.getChildren()) {
                        contacts.add(another.getKey().toString() + "," + another.getValue().toString());

                    }
                }

                populateContactList();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    /**
     * This function populates contact list view
     */
    private void populateContactList() {
        ContactListActivityAdapter chatAdapter = new ContactListActivityAdapter(ContactListActivity.this, contacts);
        listView.setAdapter(chatAdapter);

        AdapterView.OnItemClickListener chatroomClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ContactListActivity.this, ChatRoomActivity.class);

                int index = (int) id; //selected contact index

                String receiverID = contacts.get(index).split(",")[0];
                String chatroomID = contacts.get(index).split(",")[1].split(":")[1];

                System.out.println(receiverID + " testing " + chatroomID);

                intent.putExtra(ChatRoomActivity.RECEIVERID, receiverID);
                intent.putExtra(ChatRoomActivity.CHATROOMID, chatroomID);


                startActivity(intent);
            }
        };

        listView.setOnItemClickListener(chatroomClickListener);
    }
}
