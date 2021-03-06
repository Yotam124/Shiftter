package com.example.shiftter.ui.shifts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftter.Ad_RecyclerView_Shifts;
import com.example.shiftter.CurrentGroup;
import com.example.shiftter.CurrentUser;
import com.example.shiftter.Functions;
import com.example.shiftter.R;
import com.example.shiftter.Shift;
import com.example.shiftter.WGToShiftID;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShiftsFragment extends Fragment {

    private ShiftsViewModel shiftsViewModel;
    DatabaseReference db;

    //recycle_view_vars
    private ArrayList<Shift> list = new ArrayList<>();
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> spinnerDataList;


    Ad_RecyclerView_Shifts ad_recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shiftsViewModel =
                ViewModelProviders.of(this).get(ShiftsViewModel.class);

        //As manager
        if(CurrentUser.getUserEmail().equals(CurrentGroup.getGroupManagerID())){
            View root = inflater.inflate(R.layout.fragment_shifts_mamager, container, false);

            TextView groupName = root.findViewById(R.id.group_title_shifts_manager);
            groupName.setText(CurrentGroup.getGroupMame());


            db = FirebaseDatabase.getInstance().getReference();
            spinner = root.findViewById(R.id.shift_spinner);
            spinnerDataList = new ArrayList<>();
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerDataList);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                   String membersShifts = parent.getItemAtPosition(position).toString();
                   getOnCreateAsManager(membersShifts);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            retrieveDataForSpinner();

            RecyclerView recyclerView = root.findViewById(R.id.recyclerView_Shifts);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            ad_recyclerView = new Ad_RecyclerView_Shifts(getActivity(), list);
            recyclerView.setAdapter(ad_recyclerView);

            //View List on recycleView
            getListOnPageCreate();

            return root;

        }else { //As Member
            View root = inflater.inflate(R.layout.fragment_shifts, container, false);

            //start Code
            TextView groupName = root.findViewById(R.id.group_title_shifts);
            groupName.setText(CurrentGroup.getGroupMame());

            db = FirebaseDatabase.getInstance().getReference();


            RecyclerView recyclerView = root.findViewById(R.id.recyclerView_Shifts);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            ad_recyclerView = new Ad_RecyclerView_Shifts(getActivity(), list);
            recyclerView.setAdapter(ad_recyclerView);

            //View List on recycleView
            getListOnPageCreate();

            return root;
        }
    }
    public void getListOnPageCreate(){

        //Fill list
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Members").child(CurrentUser.getUserCodedEmail()).exists()) {
                    list.clear();
                    WGToShiftID wgToShiftID = dataSnapshot.child("Members").child(CurrentUser.getUserCodedEmail()).child(CurrentGroup.getGroupID()).getValue(WGToShiftID.class);
                    for (DataSnapshot ds : dataSnapshot.child("Shifts").child(wgToShiftID.getShiftID()).getChildren()) {
                        Shift shift = ds.getValue(Shift.class);
                        list.add(shift);
                    }

                    ad_recyclerView.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "There are no shifts.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void getOnCreateAsManager(String membersShifts){
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String codedEmail = Functions.encodeUserEmail(membersShifts);
                if(CurrentUser.getUserEmail().equals(CurrentGroup.getGroupManagerID())) {
                    if (dataSnapshot.child("Members").child(codedEmail).exists()) {
                        list.clear();
                        WGToShiftID wgToShiftID = dataSnapshot.child("Members").child(codedEmail).child(CurrentGroup.getGroupID()).getValue(WGToShiftID.class);
                        for (DataSnapshot ds : dataSnapshot.child("Shifts").child(wgToShiftID.getShiftID()).getChildren()) {
                            Shift shift = ds.getValue(Shift.class);
                            list.add(shift);
                        }

                        ad_recyclerView.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), "There are no shifts.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void retrieveDataForSpinner(){

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("WorkGroups").child(CurrentGroup.getGroupID()).exists()) {
                    for (DataSnapshot ds : dataSnapshot.child("WorkGroups").child(CurrentGroup.getGroupID()).child("ListOfMembers").getChildren()){
                        String member = ds.getKey();
                        spinnerDataList.add(Functions.decodeUserEmail(member));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}