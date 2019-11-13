package com.example.shiftter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
  //  private String userId = "";
    private Button createBtn;
    private EditText firstName, lastName, password, password2 , userName;
    String fn, ln, us, p, p2;

    /*FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db = database.getReference("Users");*/

    User user;
    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);


        db = FirebaseDatabase.getInstance().getReference().child("Users");


        createBtn = (Button) findViewById(R.id.createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUser();
            }
        });
    }
    public void AddUser(){
        fn = firstName.getText().toString();
        ln = lastName.getText().toString();
        us = userName.getText().toString();
        p = password.getText().toString();
        p2 = password2.getText().toString();

        if (TextUtils.isEmpty(us) && TextUtils.isEmpty(p)){
            Toast.makeText(this, "Please enter a Username and Password", Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty((us))){
            Toast.makeText(this, "Please enter a Username", Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(p)){
            Toast.makeText(this, "Please enter a Password", Toast.LENGTH_LONG).show();
        }else if (!p.equals(p2)){
            Toast.makeText(this, "Your passwords do not match", Toast.LENGTH_LONG).show();
        }else{
            user = new User(fn,ln,us,p);
            db.child(us).setValue(user);
            Toast.makeText(RegisterActivity.this, "User created sucessfull", Toast.LENGTH_LONG).show();

            Intent backToMain = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(backToMain);
        }

    }
}
