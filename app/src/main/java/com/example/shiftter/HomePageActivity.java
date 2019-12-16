package com.example.shiftter;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class HomePageActivity extends AppCompatActivity {

    DatabaseReference db,db2;
    private String clockIn, clockOut;


    private ImageButton fingerPrintBtn;
    BottomNavigationView bottomNavigationView;

    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    private Spinner spinner;
    private ValueEventListener listener;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> spinnerDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        db = FirebaseDatabase.getInstance().getReference().child("WorkGroups");
        db2 = FirebaseDatabase.getInstance().getReference();


        fingerPrintBtn = (ImageButton) findViewById(R.id.fingerPrintBtn);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        chronometer = findViewById(R.id.chronometer);
        spinner = findViewById(R.id.spinner);
        spinnerDataList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(HomePageActivity.this, android.R.layout.simple_spinner_dropdown_item,spinnerDataList);
        spinner.setAdapter(adapter);
        retrieveDataForSpinner();

        fingerPrintBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (!running){
                    startChronometer(v);
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    clockIn = format.format(date);
                    Toast.makeText(HomePageActivity.this,clockIn, Toast.LENGTH_LONG).show();
                }else{
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    clockOut = format.format(date);
                    Toast.makeText(HomePageActivity.this,clockOut, Toast.LENGTH_LONG).show();
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
                        break;
                    case R.id.navigation_workGroups:
                        Intent WorkgroupActivity = new Intent(getApplicationContext(), WorkGroupsActivity.class);
                        startActivity(WorkgroupActivity);
                        break;
                    case R.id.navigation_homePage:
                        break;

                }
                return false;
            }
        });
    }

    public void retrieveDataForSpinner(){

        db2.child("Users").child(CurrentUser.getUserName()).child("Groups").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        spinnerDataList.add(ds.getKey());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addShift(String clockIn, String clockOut){
        String userName = CurrentUser.getUserName();
        db = FirebaseDatabase.getInstance().getReference();
        db.child("Users").child("userName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}