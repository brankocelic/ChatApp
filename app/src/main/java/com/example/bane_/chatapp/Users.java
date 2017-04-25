package com.example.bane_.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Users extends AppCompatActivity {
    ListView usersList;
    TextView noUsersList;
    ArrayList<String> list_of_rooms = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    FirebaseAuth fireBaseAuth;
    String userID;
    ProgressDialog pd;
    ArrayList<String> uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersList = (ListView) findViewById(R.id.usersList);
        noUsersList = (TextView) findViewById(R.id.noUsersText);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list_of_rooms);
        fireBaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fireBaseAuth.getCurrentUser();
        userID = user.getUid();

        usersList.setAdapter(arrayAdapter);
        pd = new ProgressDialog(Users.this);
        uid = new ArrayList<>();

        pd.setMessage("Loading...");
        pd.show();

        DatabaseReference users = root.child("users");
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String rFace = ds.getKey();// uid.add(rFace);
                    if (!(rFace.compareTo(userID) == 0)) {
                        UsersOfFirebase usersOfFirebase = new UsersOfFirebase();
                        usersOfFirebase.setName(ds.child("name").getValue().toString());
                        list_of_rooms.add(usersOfFirebase.getName());
                        uid.add(rFace);
                        //  }
                    }
                    if (list_of_rooms.size() == 0) {
                        pd.dismiss();
                        noUsersList.setText("No Messages");
                    } else {
                        pd.dismiss();
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, //it is always the same;
                                    int position, long id) {

                Intent chat = new Intent(Users.this, Chat.class);
                chat.putExtra("uid", uid.get(position));
                //  Intent intent = new Intent(MainActivity.this, Chat.class); //creates a new intent
                startActivity(chat);

            }
        });
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, //it is always the same;
                                    int position, long id) {

                Intent chat = new Intent(Users.this, Chat.class);
                String selectedFromList = (usersList.getItemAtPosition(position).toString());
                chat.putExtra("uid", uid.get(position));
                chat.putExtra("otherPerson", selectedFromList);
                startActivity(chat);

            }
        });
    }

}

