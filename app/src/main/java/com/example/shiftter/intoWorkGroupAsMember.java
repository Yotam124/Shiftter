package com.example.shiftter;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class intoWorkGroupAsMember extends AppCompatActivity {

    private FloatingActionButton exitGroupFab;
    private DatabaseReference db;

    private WorkGroup wgForManagerName;
    private GroupMember gmForDetails;

    private Dialog popup;
    private FloatingActionButton leaveFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_into_work_group_as_member);

        db = FirebaseDatabase.getInstance().getReference();

        popup = new Dialog(this);

        leaveFab = findViewById(R.id.exit_group_fab);

        TextView title = findViewById(R.id.group_title);
        title.setText(CurrentGroup.getGroupMame());

        TextView managerName = findViewById(R.id.manager_name);
        TextView memberSalary = findViewById(R.id.member_salary);
        TextView memberPosition = findViewById(R.id.member_position);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                wgForManagerName = dataSnapshot.child("WorkGroups").child(CurrentUser.getCurrentGroup().getGroupKey()).getValue(WorkGroup.class);
                managerName.setText(wgForManagerName.getManagerEmail());

                gmForDetails = dataSnapshot.child("WorkGroups").child(CurrentUser.getCurrentGroup().getGroupKey())
                        .child("ListOfMembers").child(CurrentUser.getUserCodedEmail()).getValue(GroupMember.class);
                memberPosition.setText(gmForDetails.getPosition());
                memberSalary.setText(gmForDetails.getSalary());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        leaveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button deletePopup;
                popup.setContentView(R.layout.delete_group_popup);
                deletePopup = (Button) popup.findViewById(R.id.DeleteGroupPopup);
                deletePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Functions.DeleteGroupMember(CurrentUser.getCurrentGroup(),CurrentUser.getUserCodedEmail());
                        popup.dismiss();

                        Intent groupFragment = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(groupFragment);
                    }
                });
                popup.show();
            }
        });
    }
}
