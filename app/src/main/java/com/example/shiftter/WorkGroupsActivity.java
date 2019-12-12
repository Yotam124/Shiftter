package com.example.shiftter;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorkGroupsActivity extends AppCompatActivity {
    DatabaseReference db;
    private Dialog popup;
    private String groupNameString;
    BottomNavigationView bottomNavigationView;
    //private ArrayList<String> myDataset;

    private static final String TAG = "WorkGroupsActivity";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();


    //private RecyclerView recyclerView;
   // private RecyclerView.Adapter mAdapter;
   // private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_groups);
        db = FirebaseDatabase.getInstance().getReference().child("WorkGroups").child(CurrentUser.getUserName());



        popup = new Dialog(this);

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
                            groupNameString = groupNameString + "," + dataSnapshot.getValue();
                            postValues.put("groupName", temp2);
                            db.updateChildren(postValues);
                            db.child(CurrentUser.getUserName()).removeValue();
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
