package com.example.shiftter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button registerBtn;
   // private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerBtn = (Button) findViewById(R.id.registerBtn);
      //  loginBtn = (Button) findViewById(R.id.loginBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in1 = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(in1);
            }
        });
    }
}
