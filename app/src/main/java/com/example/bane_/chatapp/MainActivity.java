package com.example.bane_.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText userName, passWord;
    Button login;
    TextView register;
    ProgressDialog pd;
    FirebaseAuth fireBaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = (EditText) findViewById(R.id.username);
        passWord = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginButton);
        register = (TextView) findViewById(R.id.register);

        fireBaseAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

    }

    public void userLogin() {
        String email = userName.getText().toString().trim();
        String pass = passWord.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your e-mail adress", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        pd.setMessage("Loging in...");
        pd.show();

        fireBaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            finish();
                            startActivity(new Intent(getApplicationContext(), Users.class));
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if (view == login) {
            userLogin();
        }
        if (view == register) {
            finish();
            startActivity(new Intent(this, Register.class));
        }
    }
}
