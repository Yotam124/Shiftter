package com.example.shiftter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private Button registerBtn;
    private Button loginBtn;
    private EditText emailId, password;

    FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();


        emailId = (EditText) findViewById(R.id.email);
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

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser authUser = auth.getCurrentUser();
                if (authUser != null){
                    Toast.makeText(LoginActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailId.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    emailId.setError("Please enter a Email");
                    emailId.requestFocus();
                }else if (TextUtils.isEmpty((pwd))){
                    password.setError("Please enter a Password");
                    password.requestFocus();
                }else if (pwd.length() < 6){
                    password.setError("Password must be at least 6 letters");
                }else{
                    auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                CurrentUser.setUserID(auth.getCurrentUser().getUid());
                                Intent homePage = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(homePage);
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Error, Please Login Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

}