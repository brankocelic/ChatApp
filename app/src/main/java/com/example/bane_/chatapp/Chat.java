package com.example.bane_.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Chat extends AppCompatActivity {

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference messsages, userMessages1, userMessages2, readUser, readMessages;
    FirebaseAuth fireBaseAuth;
    String userID;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        messsages = FirebaseDatabase.getInstance().getReference("messages");
        uid = (String) getIntent().getSerializableExtra("uid");

        fireBaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fireBaseAuth.getCurrentUser();
        userID = user.getUid();
        userMessages1 = FirebaseDatabase.getInstance().getReference("user-messages").child(userID).child(uid);
        userMessages2 = FirebaseDatabase.getInstance().getReference("user-messages").child(uid).child(userID);
        readUser = FirebaseDatabase.getInstance().getReference().child("user-messages");

        readUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue().toString().equals(userID)) {
                        for (DataSnapshot ds1 : ds.getChildren()) {
                            if (ds1.getValue().toString().equals(uid)){
                                Toast.makeText(Chat.this, ""+ds.getChildren(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessages();
            }
        });
    }

    public void addMessages() {
        MessageInfo ms = new MessageInfo(userID, messageArea.getText().toString(), uid);

        String id = messsages.push().getKey();
        messsages.child(id).setValue(ms);
        userMessages1.child(id).setValue("1");
        userMessages2.child(id).setValue("1");
    }

    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);

        if (type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1);
        } else {
            textView.setBackgroundResource(R.drawable.rounded_corner2);
        }

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
