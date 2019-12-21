package com.example.shiftter;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class intoWorkGroupAsManager extends AppCompatActivity {
    DatabaseReference db;
    private FloatingActionButton add, delete;
    private Dialog popup;
    private String memberToAdd, position;
    private Float salary;
    BottomNavigationView bottomNavigationView;

    private ArrayList<String> list = new ArrayList<>();
    Ad_RecyclerView_Manager ad_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_into_work_group_manager);

        TextView title = (TextView) findViewById(R.id.group_title);
        //title.setText(CurrentUser.getCurrentJob());



        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ad_recyclerView = new Ad_RecyclerView_Manager(this, list);
        recyclerView.setAdapter(ad_recyclerView);

        // get db
        db = FirebaseDatabase.getInstance().getReference();

        add = findViewById(R.id.add_user_fab);
        delete = findViewById(R.id.delete_group_fab);

        popup = new Dialog(this);
        //getMembersListOnPageCreate();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ShowAddPopup(v);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ShowDeletePopup(v);
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

// TODO: 12/19/2019 fixing the function "getListOnCreate"
    /*public void getListOnPageCreate(){

        //Fill list
        db.child(CurrentUser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        list.add(ds.getKey());
                    }
                    *//*WorkGroup wg = dataSnapshot.getValue(WorkGroup.class);
                    List<String> items = new ArrayList<>();
                    items = Arrays.asList(wg.getGroupName().split(","));
                    list.addAll(items);*//*
                    ad_recyclerView.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/
// TODO: 12/19/2019 fixing the function "ShowAddPopup" (intoWorkGroupAsManager)
    /*public void ShowAddPopup(View v){
        Button AddBtn;
        final EditText memberName, memberPos, memberSalary;
        popup.setContentView(R.layout.add_member_popup);
        AddBtn = (Button) popup.findViewById(R.id.AddPopupBtn);
        memberName = (EditText) popup.findViewById(R.id.UserToAdd);
        memberPos = (EditText) popup.findViewById(R.id.UserPosition);
        memberSalary = (EditText) popup.findViewById(R.id.UserSalary);
        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberToAdd = memberName.getText().toString();
                position = memberPos.getText().toString();
                salary = Float.parseFloat(memberSalary.getText().toString());
                popup.dismiss();

                //flagGroup = checkExsistInGroup(memberToAdd);
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        WorkGroup groupForUser = dataSnapshot.child("WorkGroups").child(CurrentUser.getEmail()).child(CurrentUser.getCurrentJob()).getValue(WorkGroup.class);

                        if(dataSnapshot.child("WorkGroups")
                                .child(CurrentUser.getEmail())
                                .child(CurrentUser.getCurrentJob())
                                .child("Members").child(memberToAdd).exists()){
                            Toast.makeText(intoWorkGroupAsManager.this,"Member is alredy in the group", Toast.LENGTH_LONG).show();
                        }
                        else {
                            if (!dataSnapshot.child("Users").child(memberToAdd).exists()){
                                Toast.makeText(intoWorkGroupAsManager.this, "User isn't exist", Toast.LENGTH_LONG).show();
                            }
                            else{
                                GroupMember newMember = new GroupMember(memberToAdd, position, salary);
                                db.child("WorkGroups").child(CurrentUser.getEmail())
                                        .child(CurrentUser.getCurrentJob())
                                        .child("Members")
                                        .child(memberToAdd).setValue(newMember);
                                //GroupName g = new GroupName(CurrentUser.getCurrentJob());
                                db.child("Users").child(memberToAdd).child("Groups").child(CurrentUser.getCurrentJob()).setValue(groupForUser);
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        popup.show();

    }

    public void ShowDeletePopup(View v){
        Button deletePopup;
        popup.setContentView(R.layout.delete_group_popup);
        deletePopup = (Button) popup.findViewById(R.id.DeleteGroupPopup);
        deletePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup.dismiss();
                db.child("WorkGroups").child(CurrentUser.getEmail()).child(CurrentUser.getCurrentJob()).removeValue();
                Intent WorkgroupActivity = new Intent(getApplicationContext(), WorkGroupsActivity.class);
                startActivity(WorkgroupActivity);
            }
        });
        popup.show();

    }

    public void getMembersListOnPageCreate(){

        //Fill list
        db.child("WorkGroups").child(CurrentUser.getEmail())
                .child(CurrentUser.getCurrentJob())
                .child("Members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
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
