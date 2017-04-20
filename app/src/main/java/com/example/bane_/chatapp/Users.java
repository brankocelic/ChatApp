package com.example.bane_.chatapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    ArrayAdapter<String> arrayAdapter;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    FirebaseAuth fireBaseAuth;
    String userID;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersList = (ListView) findViewById(R.id.usersList);
        noUsersList = (TextView) findViewById(R.id.noUsersText);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list_of_rooms);
        fireBaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user=fireBaseAuth.getCurrentUser();
        userID=user.getUid();

        usersList.setAdapter(arrayAdapter);
        pd = new ProgressDialog(Users.this);

        pd.setMessage("Loading...");
        pd.show();

        DatabaseReference users = root.child("users");
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();
               // Toast.makeText(Users.this, ""+userID, Toast.LENGTH_SHORT).show();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String rFace=ds.getValue().toString();
                    Toast.makeText(Users.this, ""+rFace, Toast.LENGTH_SHORT).show();
                    if (!(rFace.compareTo(userID)==0)) {
                        UsersOfFirebase usersOfFirebase = new UsersOfFirebase();
                        usersOfFirebase.setName(ds.child("name").getValue().toString());
                        set.add(usersOfFirebase.getName());
                    }
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                if (list_of_rooms.size() == 0) {
                    pd.dismiss();
                    noUsersList.setText("No Messages");
                } else {
                    pd.dismiss();
                    arrayAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}
