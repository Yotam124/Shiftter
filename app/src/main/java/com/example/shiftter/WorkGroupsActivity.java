package com.example.shiftter;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WorkGroupsActivity extends AppCompatActivity {
    DatabaseReference db, dbForMembers;
    FirebaseAuth auth;

    private Dialog popup;
    private String groupNameString;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton addFab;

    //recycle_view_vars
    private ArrayList<String> list = new ArrayList<>();
    Ad_RecyclerView ad_recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_groups);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ad_recyclerView = new Ad_RecyclerView(this, list);
        recyclerView.setAdapter(ad_recyclerView);

        // get db
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        popup = new Dialog(this);
        //getListOnPageCreate();

        addFab = findViewById(R.id.fab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);
            }
        });


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bnm_work_groups);

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

    public void ShowPopup(View v){
        Button createPopup;
        final EditText groupName;
        popup.setContentView(R.layout.workgroup_popup);
        createPopup = (Button) popup.findViewById(R.id.createPopup);
        groupName = (EditText) popup.findViewById(R.id.groupName);
        createPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                groupNameString = groupName.getText().toString().trim();
                String groupKey = db.push().getKey();

                WorkGroup workGroup = new WorkGroup(auth.getCurrentUser().getUid(), groupNameString, 0);
                db.child("WorkGroups").child(groupKey).setValue(workGroup);
                db.child("Users").child(CurrentUser.getUserID()).child("Groups").setValue(new PK(groupKey));

                ad_recyclerView.notifyDataSetChanged();

            }
        });
        popup.show();

    }

    public void getListOnPageCreate(){

        //Fill list
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(CurrentUser.getUserID()).exists()) {
                    list.clear();
                    for (DataSnapshot ds : dataSnapshot.child("Users").child(CurrentUser.getUserID()).child("Groups").getChildren()){
                        PK pk = ds.getValue(PK.class);
                        WorkGroup workGroup = dataSnapshot.child("WorkGroups").child(pk.getPk()).getValue(WorkGroup.class);
                        list.add(workGroup.getGroupName());
                    }
                    ad_recyclerView.notifyDataSetChanged();
                }
                /*else{
                    dbForMembers.child("Users").child(CurrentUser.getUserID()).child("Groups").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                list.clear();
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    list.add(ds.getKey());
                                }
                                ad_recyclerView.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
