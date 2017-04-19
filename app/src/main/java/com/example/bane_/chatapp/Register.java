package com.example.bane_.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText userName, passWord, editUser;
    Button register;
    TextView login;
    ProgressDialog pd;
    FirebaseAuth fireBaseAuth;
    DatabaseReference databaseReference;
    boolean uslov = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = (EditText) findViewById(R.id.username);
        passWord = (EditText) findViewById(R.id.password);
        editUser = (EditText) findViewById(R.id.editUser);
        register = (Button) findViewById(R.id.registerButton);
        login = (TextView) findViewById(R.id.login);

        fireBaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        pd = new ProgressDialog(this);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    public void userRegister() {
        final String email = userName.getText().toString().trim();
        String pass = passWord.getText().toString().trim();
        final String user = editUser.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your e-mail address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(user)) {
            Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show();
            return;
        }

        pd.setMessage("Registering please wait...");
        pd.show();
        fireBaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     //   pd.dismiss();
                        if (!task.isSuccessful()) {
                            pd.dismiss();
                            Toast.makeText(Register.this, "Could not register, please try again!", Toast.LENGTH_SHORT).show();
                            uslov = true;
                        }
                    }
                });
        if (!uslov) {
            fireBaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference users = databaseReference.child("users");
                                DatabaseReference theUser = users.child(fireBaseAuth.getCurrentUser().getUid());
                                theUser.child("email").setValue(fireBaseAuth.getCurrentUser().getEmail());
                                theUser.child("name").setValue(user);
                                pd.dismiss();
                                Toast.makeText(Register.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(Register.this, "sdasadsa", Toast.LENGTH_SHORT).show();
                        }

                    });
        }
    }


    @Override
    public void onClick(View v) {
        if (v == register) {
            userRegister();
        }
        if (v == login) {
            finish();
            startActivity(new Intent(Register.this, MainActivity.class));
        }
    }
}
