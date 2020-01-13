package com.example.shiftter;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class intoWorkGroupAsManager extends AppCompatActivity {
    private DatabaseReference db;
    private FloatingActionButton addUser, deleteWorkGroup, editManagerDetails;
    private Dialog popup;
    private String memberToAddEmail, position, salary, stManagerSalary,stManagerPosition;

    private WorkGroup workGroup;
    private String groupID;

    private ArrayList<GroupMember> list = new ArrayList<>();
    Ad_RecyclerView_Manager ad_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_into_work_group_manager);

        // get db
        db = FirebaseDatabase.getInstance().getReference();

        TextView title = findViewById(R.id.group_title);
        title.setText(CurrentUser.getCurrentGroup().getGroupName());
        TextView groupDateOfCreation = findViewById(R.id.group_DateOfCreation);
        groupDateOfCreation.setText(CurrentUser.getCurrentGroup().getDateOfCreation());



        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ad_recyclerView = new Ad_RecyclerView_Manager(this, list);
        recyclerView.setAdapter(ad_recyclerView);

        workGroup = CurrentUser.getCurrentGroup();
        groupID = workGroup.getGroupKey();

        addUser = findViewById(R.id.add_user_fab);
        deleteWorkGroup = findViewById(R.id.delete_group_fab);
        editManagerDetails = findViewById(R.id.edit_manager_details);

        popup = new Dialog(this);
        getListOnPageCreate();

        editManagerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button EditBtn;
                final EditText managerSalary,managerPosition;
                popup.setContentView(R.layout.edit_manager_details_popup);
                TextView TextViewManagerCurrentPosition = (TextView) popup.findViewById(R.id.TextViewManagerCurrentPosition);
                TextView TextViewManagerCurrentSalary = (TextView) popup.findViewById(R.id.TextViewManagerCurrentSalary);
                EditBtn = (Button) popup.findViewById(R.id.EditManagerDetailsBtn);
                managerPosition = (EditText) popup.findViewById(R.id.ManagerPosition);
                managerSalary = (EditText) popup.findViewById(R.id.ManagerSalary);
                db.child("WorkGroups").child(groupID).child("ListOfMembers").child(CurrentUser.getUserCodedEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GroupMember gm = dataSnapshot.getValue(GroupMember.class);
                        TextViewManagerCurrentPosition.setText("Position: " + gm.getPosition());
                        TextViewManagerCurrentSalary.setText("Salary: " + gm.getSalary());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                EditBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stManagerPosition = managerPosition.getText().toString().trim();
                        stManagerSalary = managerSalary.getText().toString().trim();
                        if (TextUtils.isEmpty(stManagerPosition)){
                            managerPosition.setError("Please enter position");
                            managerPosition.requestFocus();
                        }else if (TextUtils.isEmpty(stManagerSalary)){
                            managerSalary.setError("Please enter salary");
                            managerSalary.requestFocus();
                        }else {
                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Functions.DeleteGroupMember(CurrentUser.getCurrentGroup(),CurrentUser.getUserCodedEmail());
                                    Functions.AddGroupMember(groupID,CurrentUser.getUser(),stManagerPosition,stManagerSalary);
                                    popup.dismiss();
                                    Toast.makeText(intoWorkGroupAsManager.this, "Manager data changed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                });
                popup.show();
            }
        });


        //Add User Button
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button AddBtn;
                final EditText memberEmail, memberPos, memberSalary;
                popup.setContentView(R.layout.add_member_popup);
                AddBtn = (Button) popup.findViewById(R.id.AddPopupBtn);
                memberEmail = (EditText) popup.findViewById(R.id.UserToAdd);
                memberPos = (EditText) popup.findViewById(R.id.UserPosition);
                memberSalary = (EditText) popup.findViewById(R.id.UserSalary);
                AddBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        memberToAddEmail = memberEmail.getText().toString().trim();
                        position = memberPos.getText().toString().trim();
                        salary = memberSalary.getText().toString().trim();
                        if (TextUtils.isEmpty(memberToAddEmail)) {
                            memberEmail.setError("Please enter a Email");
                            memberEmail.requestFocus();
                        } else if (TextUtils.isEmpty(position)) {
                            memberPos.setError("Please enter a position");
                            memberPos.requestFocus();
                        } else if (TextUtils.isEmpty(salary)) {
                            memberSalary.setError("Please enter a position");
                            memberSalary.requestFocus();
                        } else {
                            String codedEmail = Functions.encodeUserEmail(memberToAddEmail);
                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("Users").child(codedEmail).exists()) {
                                        User user = dataSnapshot.child("Users").child(codedEmail).getValue(User.class);
                                        Functions.AddGroupMember(groupID, user, position, salary);

                                        popup.dismiss();
                                        Toast.makeText(intoWorkGroupAsManager.this, "Member Added Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(intoWorkGroupAsManager.this, "The User Does Not Exist ", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                });
                popup.show();
            }
        });

        // TODO: 12/25/2019 Delete WorkGroup Button
        //Delete Group Button
        deleteWorkGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button deletePopup;
                popup.setContentView(R.layout.delete_group_popup);
                deletePopup = (Button) popup.findViewById(R.id.DeleteGroupPopup);
                deletePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.child("WorkGroups")
                                                        .child(workGroup.getGroupKey())
                                                        .child("ListOfMembers").getChildren()){

                                    Functions.DeleteGroupMember(workGroup, ds.getKey());
                                }
                                db.child("WorkGroups").child(CurrentUser.getCurrentGroup().getGroupKey()).removeValue();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        popup.dismiss();

                        Intent groupFragment = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(groupFragment);
                    }
                });
                popup.show();
            }
        });
    }

    // TODO: 12/19/2019 fixing the function "getListOnCreate"
    public void getListOnPageCreate() {
        //Fill list
        db.child("WorkGroups").child(groupID).child("ListOfMembers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String memberEmail = Functions.decodeUserEmail(ds.getKey());
                        if (!memberEmail.equals(workGroup.getManagerEmail())){
                            list.add(ds.getValue(GroupMember.class));
                            //list.add(memberEmail);
                        }
                    }
                    ad_recyclerView.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


// TODO: 12/19/2019 fixing the function "ShowAddPopup" (intoWorkGroupAsManager)

    /*public void ShowDeletePopup(View v){
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
                        list.addUser(ds.getKey());
                    }
                    ad_recyclerView.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
}
