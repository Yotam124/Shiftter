package com.example.shiftter;

import android.app.Dialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        db = FirebaseDatabase.getInstance().getReference().child("WorkGroups").child(CurrentUser.getUserName());
        popup = new Dialog(this);
        getListOnPageCreate();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bnm_work_groups);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_create:
                        ShowPopup(menuItem);
                        break;
                    case R.id.navigation_delete:
                        db.child(groupNameString).removeValue();
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

               db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            WorkGroup workGroup = new WorkGroup(CurrentUser.getUserName(), groupNameString);
                            db.setValue(workGroup);
                        }else {
                            WorkGroup wg = dataSnapshot.getValue(WorkGroup.class);
                            String temp1 = wg.getGroupName();
                            String temp2 = temp1 + "," +groupNameString;
                            Map<String, Object> postValues = new HashMap<String, Object>();
                            postValues.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                            postValues.put("groupName", temp2);
                            // TODO : if failed to add to database throw exceptions
                            db.updateChildren(postValues);
                            db.child(CurrentUser.getUserName()).removeValue();
                        }
                        list.add(groupNameString);
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
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    WorkGroup wg = dataSnapshot.getValue(WorkGroup.class);
                    list.clear();
                    List<String> items = new ArrayList<>();
                    items = Arrays.asList(wg.getGroupName().split(","));
                    list.addAll(items);
                    ad_recyclerView.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
