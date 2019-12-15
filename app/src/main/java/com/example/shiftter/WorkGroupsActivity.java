package com.example.shiftter;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WorkGroupsActivity extends AppCompatActivity {
    DatabaseReference db;
    private Dialog popup;
    private String groupNameString;
    BottomNavigationView bottomNavigationView;

    //recycle_view_vars
    private ArrayList<String> list = new ArrayList<>();
    Ad_RecyclerView ad_recyclerView;

    private static final String TAG = "WorkGroupsActivity";


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
        db = FirebaseDatabase.getInstance().getReference()
                .child("WorkGroups");

        popup = new Dialog(this);
        getListOnPageCreate();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bnm_work_groups);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_shifts:
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

    public void ShowPopup(MenuItem v){
        Button createPopup;
        final EditText groupName;
        popup.setContentView(R.layout.workgroup_popup);
        createPopup = (Button) popup.findViewById(R.id.createPopup);
        groupName = (EditText) popup.findViewById(R.id.groupName);
        createPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupNameString = groupName.getText().toString();
                popup.dismiss();

               db.child(CurrentUser.getUserName()).child(groupNameString).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            WorkGroup workGroup = new WorkGroup(CurrentUser.getUserName(), groupNameString);
                            db.child(CurrentUser.getUserName()).child(groupNameString).setValue(workGroup);
                        }else {
                            Toast.makeText(WorkGroupsActivity.this,"Be more specific", Toast.LENGTH_LONG).show();
                            //WorkGroup wg = dataSnapshot.getValue(WorkGroup.class);
                            /*String temp1 = wg.getGroupName();
                            String temp2 = temp1 + "," +groupNameString;
                            Map<String, Object> postValues = new HashMap<String, Object>();
                            postValues.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                            postValues.put("groupName", temp2);
                            // TODO : if failed to add to database throw exceptions
                            db.updateChildren(postValues);
                            db.child(CurrentUser.getUserName()).removeValue();*/
                        }
                        getListOnPageCreate();
                        ad_recyclerView.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
        popup.show();

    }

    public void getListOnPageCreate(){

        //Fill list
        db.child(CurrentUser.getUserName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        list.add(ds.getKey());
                    }
                    /*WorkGroup wg = dataSnapshot.getValue(WorkGroup.class);
                    List<String> items = new ArrayList<>();
                    items = Arrays.asList(wg.getGroupName().split(","));
                    list.addAll(items);*/
                    ad_recyclerView.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
