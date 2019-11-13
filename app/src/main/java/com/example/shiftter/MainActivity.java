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
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button registerBtn;
    private Button loginBtn;
    private EditText userName, password;

    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        private void signIn(final String username, final String password){
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(username).exists()) {
                        if (!username.isEmpty()) {
                            User login = dataSnapshot.child(username).getValue(User.class);
                            if (login.getPassword().equals(password)) {
                                Toast.makeText(MainActivity.this, "Welcome " + username, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Username box is empty", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        Toast.makeText(MainActivity.this, "Username isn't registered", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
}
