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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private Button createBtn;
    private EditText firstName, lastName, password, password2 , emailId;
    String fn, ln, email, pwd, p2;

    User user;

    DatabaseReference db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        emailId =  (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference().child("Users");

        createBtn = (Button) findViewById(R.id.createBtn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fn = firstName.getText().toString().trim();
                ln = lastName.getText().toString().trim();
                email = emailId.getText().toString().trim();
                pwd = password.getText().toString().trim();
                p2 = password2.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailId.setError("Please enter a Email");
                    emailId.requestFocus();
                }else if (TextUtils.isEmpty((pwd))){
                    password.setError("Please enter a Password");
                    password.requestFocus();
                }else if (pwd.length() < 6){
                    password.setError("Password must be at least 6 letters");
                }else if (!pwd.equals(p2)){
                    password2.setError("Your passwords do not match");
                    password2.requestFocus();
                }else{
                    String codedEmail = encodeUserEmail(email);
                    auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                FirebaseUser firebaseUser = auth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(fn +" "+ln).build();
                                firebaseUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    //firebaseUser DisplayName Update Succeed.
                                                }else{
                                                    //firebaseUser DisplayName Update Failed.
                                                }
                                            }
                                        });


                                db.child(codedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()){
                                            user = new User(fn, ln, email, pwd, auth.getUid());
                                            db.child(codedEmail).setValue(user);
                                            Intent backToMain = new Intent(getApplicationContext(), LoginActivity.class);
                                            startActivity(backToMain);

                                            Toast.makeText(RegisterActivity.this, "User created sucessfull", Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }else{
                                Toast.makeText(RegisterActivity.this,"SignUp Unsuccessful, Please Try Again. ", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
}