package com.build2gether.fx.Chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.build2gether.fx.Firebase.FirebaseIO;
import com.build2gether.fx.Firebase.FirebaseUtil;
import com.build2gether.fx.R;
import com.facebook.Profile;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;


public class ChatRoomActivity extends AppCompatActivity {





    private EditText mMessage;


    private ListView listView;
    FirebaseIO fbIO = new FirebaseIO();

    // intent string index
    public static final String RECEIVERID = "receiverID";
    public static final String CHATROOMID = "chatroomID";

    //
    private ArrayList<String> messages = new ArrayList<String>();

    // key words for firebase access
    private String receiverID = "";
    private String chatroomID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        receiverID = (String) getIntent().getExtras().get(RECEIVERID);
        chatroomID = (String) getIntent().getExtras().get(CHATROOMID);
        listView = (ListView) findViewById(R.id.Chatlist);//listview

//        TextView meLabel = (TextView) findViewById(R.id.meLbl);
//        TextView companionLabel = (TextView) findViewById(R.id.friendLabel);


        asyncChatMessages(chatroomID);

        //message field
        mMessage = (EditText) findViewById(R.id.message_text);

        final Button post = (Button) findViewById(R.id.messenger_send_button);
        post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String sender = Profile.getCurrentProfile().getId();
                String text = mMessage.getText().toString();
                if(text.equals("")){
                    return;
                }
                mMessage.setText("");
                messages.add(sender + ":" + text);

                fbIO.saveMessage(sender, text, chatroomID);

            }
        });

    }

    public void onSendButtonClick(View view) {
        String sender = Profile.getCurrentProfile().getId();
        String text = mMessage.getText().toString();
        mMessage.setText("");
        messages.add(sender + ":" + text);
//        System.out.println("Chatroom: " + chatroomID);
        fbIO.saveMessage(sender, text, chatroomID);
    }

    /**
     * This function will retrieve messages and with this function and async
     */
    private void asyncChatMessages(String chatroomID) {
        Firebase root = new Firebase(FirebaseUtil.CHATROOM.toString() + "/" + chatroomID + "/message");

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String sender = postSnapshot.child("sender").getValue().toString();
                    String message = postSnapshot.child("text").getValue().toString();

                    messages.add(sender + ":" + message);
                }
//                listView.setAdapter(null);
                populateMessages();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    private void populateMessages() {

        ChatConversationAdapter chatConversationAdapter = new ChatConversationAdapter(ChatRoomActivity.this, messages);
        listView.setAdapter(chatConversationAdapter);


    }
}
