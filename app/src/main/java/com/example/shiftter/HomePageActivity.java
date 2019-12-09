package com.example.shiftter;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;

public class HomePageActivity extends AppCompatActivity {

    DatabaseReference db;

    private ImageButton fingerPrintBtn;
    BottomNavigationView bottomNavigationView;

    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        fingerPrintBtn = (ImageButton) findViewById(R.id.fingerPrintBtn);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        chronometer = findViewById(R.id.chronometer);


        fingerPrintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running){
                    startChronometer(v);
                }else{
                    pauseChronometer(v);
                }
                Toast.makeText(HomePageActivity.this,""+CurrentUser.getUserName(), Toast.LENGTH_LONG).show();
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_shifts:
                        Intent shiftsActivity = new Intent(getApplicationContext(), ShiftsActivity.class);
                        startActivity(shiftsActivity);
                    case R.id.navigation_workGroups:

                    case R.id.navigation_search:

                }
                return false;
            }
        });
    }




    //Chronometer functions
    public void startChronometer(View v){
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }
    public void pauseChronometer(View v){
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
        }
    }









    //Top Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.personalDetails_item:
                // do your code
                return true;
            case R.id.logout_item:
                // do your code
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
