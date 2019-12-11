package com.example.shiftter;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WorkGroupsActivity extends AppCompatActivity {
    DatabaseReference db;
    private Dialog popup;
    WorkGroup wg;

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_groups);
        popup = new Dialog(this);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bnm_work_groups);

        db = FirebaseDatabase.getInstance().getReference().child("WorkGroups");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_create:
                        ShowPopup(menuItem);
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
                final String groupNameString = groupName.getText().toString();
                popup.dismiss();
                db.child("WorkGroups").child(groupNameString).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            wg = new WorkGroup(CurrentUser.getUserName(),groupNameString,1);
                            db.child(groupNameString).setValue(wg);
                        }else{
                            Toast.makeText(WorkGroupsActivity.this, "Group name already exists", Toast.LENGTH_LONG).show();
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
}
