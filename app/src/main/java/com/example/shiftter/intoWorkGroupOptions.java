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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class intoWorkGroupOptions extends AppCompatActivity {
    DatabaseReference db, db2;
    private FloatingActionButton add, delete;
    private Dialog popup;
    private String memberToAdd, position;
    private Float salary;
    BottomNavigationView bottomNavigationView;

    private ArrayList<String> list = new ArrayList<>();
    Ad_RecyclerView ad_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_into_work_group_options);


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ad_recyclerView = new Ad_RecyclerView(this, list);
        recyclerView.setAdapter(ad_recyclerView);

        // get db
        db = FirebaseDatabase.getInstance().getReference();

        add = findViewById(R.id.add_user_fab);
        delete = findViewById(R.id.delete_group_fab);

        popup = new Dialog(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAddPopup(v);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowDeletePopup(v);
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

    public void ShowAddPopup(View v){
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
                        if(dataSnapshot.child("WorkGroups")
                                .child(CurrentUser.getUserName())
                                .child(CurrentUser.getCurrentJob())
                                .child("Members").child(memberToAdd).exists()){
                            Toast.makeText(intoWorkGroupOptions.this,"Member is alredy in the group", Toast.LENGTH_LONG).show();
                        }
                        else {
                            if (!dataSnapshot.child("Users").child(memberToAdd).exists()){
                                Toast.makeText(intoWorkGroupOptions.this, "User isn't exist", Toast.LENGTH_LONG).show();
                            }
                            else{
                                GroupMember newMember = new GroupMember(memberToAdd, position, salary);
                                db.child("WorkGroups").child(CurrentUser.getUserName()).child(CurrentUser.getCurrentJob()).child("Members").child(memberToAdd).setValue(newMember);
                                GroupName g = new GroupName(CurrentUser.getCurrentJob());
                                db.child("Users").child(memberToAdd).child("Groups").child(g.groupName).setValue(g);
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
                db.child(CurrentUser.getUserName()).child(CurrentUser.getCurrentJob()).removeValue();
                Intent WorkgroupActivity = new Intent(getApplicationContext(), WorkGroupsActivity.class);
                startActivity(WorkgroupActivity);
            }
        });
        popup.show();

    }
}
