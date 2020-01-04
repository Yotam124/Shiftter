package com.example.shiftter.ui.groups;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftter.Ad_RecyclerView;
import com.example.shiftter.CurrentUser;
import com.example.shiftter.Functions;
import com.example.shiftter.R;
import com.example.shiftter.WorkGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupsFragment extends Fragment {

    private GroupsViewModel groupsViewModel;

    DatabaseReference db, dbForMembers;
    FirebaseAuth auth;

    private Dialog popup;
    private String groupNameString;
    FloatingActionButton addFab;

    //recycle_view_vars
    private ArrayList<String> list = new ArrayList<>();
    Ad_RecyclerView ad_recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        groupsViewModel =
                ViewModelProviders.of(this).get(GroupsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_groups, container, false);


        //start Code

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ad_recyclerView = new Ad_RecyclerView(getActivity(), list);
        recyclerView.setAdapter(ad_recyclerView);

        // get db
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        popup = new Dialog(getActivity());
        getListOnPageCreate();

        addFab = root.findViewById(R.id.fab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopup(v);
            }
        });


        return root;
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
                String groupID = db.push().getKey();
                //Date of group creation
                Date date = new Date();
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                String stringDate = dt.format(date);
                //Create WorkGroup and sate to db
                String codedEmail = CurrentUser.getUserCodedEmail();
                WorkGroup workGroup = new WorkGroup(groupID, groupNameString, CurrentUser.getUserEmail(), 0, stringDate);

                //Open WorkGroup
                db.child("WorkGroups").child(groupID).setValue(workGroup);

                //Adding manager as member.
                Functions.AddGroupMember(groupID, CurrentUser.getUser(), "Manager", "0");

                Toast.makeText(getActivity(), "WorkGroup Created Successfully", Toast.LENGTH_SHORT).show();
                // TODO: 12/23/2019 add increment func to numOfMembers in a group
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
                if (dataSnapshot.child("Members").child(CurrentUser.getUserCodedEmail()).exists()) {
                    list.clear();
                    for (DataSnapshot ds : dataSnapshot.child("Members").child(CurrentUser.getUserCodedEmail()).getChildren()){
                        String groupID = ds.getKey();
                        WorkGroup workGroup = dataSnapshot.child("WorkGroups").child(groupID).getValue(WorkGroup.class);
                        list.add(workGroup.getGroupName());
                    }
                    ad_recyclerView.notifyDataSetChanged();
                }else{
                    Toast.makeText(getActivity(), "There is no WorkGroups.",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}