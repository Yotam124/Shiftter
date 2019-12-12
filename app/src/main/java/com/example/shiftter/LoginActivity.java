package com.example.shiftter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private Button registerBtn;
    private Button loginBtn;
    private EditText userName, password;

    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);

        registerBtn = (Button) findViewById(R.id.registerBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(goToRegister);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(userName.getText().toString(), password.getText().toString());
            }
        });
    }

    private void signIn(final String userName, final String password){
        db = FirebaseDatabase.getInstance().getReference();
        db.child("Users").child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (!userName.isEmpty()) {
                        User login = dataSnapshot.getValue(User.class);
                        if (login.getPassword().equals(password)) {
                            Toast.makeText(LoginActivity.this, "Welcome " + userName, Toast.LENGTH_SHORT).show();
                            //Save to Current user.
                            CurrentUser.setUserName(userName);
                            CurrentUser.setFirstName(login.getFirstName());
                            CurrentUser.setLastName(login.getLastName());
                            //Move to homePage Activity
                            Intent homePage = new Intent(getApplicationContext(), HomePageActivity.class);
                            startActivity(homePage);
                        } else {
                            Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "Username box is empty", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Username isn't registered", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
