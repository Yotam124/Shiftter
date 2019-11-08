package com.example.shiftter;

import android.os.Bundle;
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
    private EditText firstName, lastName, password, password2 , userName, email;
    String fn, ln, p, p2, us, em;

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
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);
        userName = (EditText) findViewById(R.id.userName);
        email = (EditText) findViewById(R.id.email);

        db = FirebaseDatabase.getInstance().getReference().child("Users");


        createBtn = (Button) findViewById(R.id.createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fn = firstName.getText().toString();
                ln = lastName.getText().toString();
                p = password.getText().toString();
                p2 = password2.getText().toString();
                us = userName.getText().toString();
                em = email.getText().toString();

                user = new User(fn,ln,us,p,em);
                db.push().setValue(user);
                Toast.makeText(RegisterActivity.this, "data inserted sucessfull", Toast.LENGTH_LONG).show();
                /*User user = new User(fn,ln,p,us,em);
                db.child("Users").child(us).setValue(user);*/

            }
        });
    }
}
