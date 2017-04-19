package com.example.bane_.chatapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersList = (ListView) findViewById(R.id.usersList);
        noUsersList=(TextView)findViewById(R.id.noUsersText);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,list_of_rooms);

        usersList.setAdapter(arrayAdapter);
        pd=new ProgressDialog(Users.this);
        pd.setMessage("Loading...");
        pd.show();

        DatabaseReference users = root.child("users");
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set =new HashSet<String>();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    set.add(ds.getRef().child("name").getKey());
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                if(list_of_rooms.size() == 0) noUsersList.setText("No Messages");
                else { pd.dismiss(); arrayAdapter.notifyDataSetChanged(); }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


}
