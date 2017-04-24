package com.example.bane_.chatapp;

import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Chat extends AppCompatActivity {

    EditText message;
    ImageView sendButton;
    LinearLayout space;
    String otherPerson, otherPersonUid;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    TextView text;
    ScrollView scrollView;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        message = (EditText) findViewById(R.id.messageArea);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        space = (LinearLayout) findViewById(R.id.layout1);
        text = (TextView) findViewById(R.id.text);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 1000);

        firebaseAuth = FirebaseAuth.getInstance();

        otherPersonUid = (String) getIntent().getSerializableExtra("uid");

        user = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        otherPerson = (String) getIntent().getSerializableExtra("otherPerson");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!message.getText().toString().equals("")) {
                    //  i = getIntent();
                    MessageInfo info = new MessageInfo();
                    long temp = System.currentTimeMillis();
                    String string = Long.toString(temp);

                    info.setText(message.getText().toString());
                    info.setTimestamp(string);
                    info.setFromId(user.getUid());
                    info.setTold(otherPersonUid);

                    String myID = databaseReference.child("messages").push().getKey();

                    message.setText("");

                    databaseReference.child("messages").child(myID).setValue(info);
                    databaseReference.child("user-messages").child(user.getUid()).child(otherPersonUid).child(myID).setValue(1);
                    databaseReference.child("user-messages").child(otherPersonUid).child(user.getUid()).child(myID).setValue(1);

                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 1000);

                }
            }
        });

        databaseReference.child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                MessageInfo newInfo = dataSnapshot.getValue(MessageInfo.class);

                Toast.makeText(Chat.this, ""+otherPersonUid+"   "+otherPerson, Toast.LENGTH_SHORT).show();
                if (newInfo.getFromId().equals(user.getUid()) && newInfo.getTold().equals(otherPersonUid)) {
                    setMessageBox("You: \n", newInfo.getText(), 1);
                } else if (newInfo.getFromId().equals(otherPersonUid) && newInfo.getTold().equals(user.getUid())) {
                    setMessageBox(otherPerson + ": \n", newInfo.getText(), 2);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setMessageBox(String sender, String message, int type) {
        LinearLayout space = (LinearLayout) findViewById(R.id.layout1);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        TextView textView = new TextView(Chat.this);
        textView.setTextSize(17);
        textView.setText(sender + message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 100, 30);
        textView.setLayoutParams(lp);

        if (type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1);
        } else {
            textView.setBackgroundResource(R.drawable.rounded_corner2);
            lp.setMargins(100, 0, 0, 30);
        }

        space.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }

}
