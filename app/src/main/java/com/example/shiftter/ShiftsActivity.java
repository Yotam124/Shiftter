package com.example.shiftter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ShiftsActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bnv_shifts);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_shifts:
                        Intent shiftActivity = new Intent(getApplicationContext(), ShiftsActivity.class);
                        startActivity(shiftActivity);
                        break;
                    case R.id.navigation_workGroups:
                        Intent WorkgroupActivity = new Intent(getApplicationContext(), WorkGroupsActivity.class);
                        startActivity(WorkgroupActivity);
                        break;
                    case R.id.navigation_homePage:
                        Intent homePageActivity = new Intent(getApplicationContext(), HomePageActivity.class);
                        startActivity(homePageActivity);
                        break;
                }
                return false;
            }
        });
    }

}
