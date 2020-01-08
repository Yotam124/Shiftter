package com.example.shiftter.ui.shifts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shiftter.Ad_RecyclerView_Shifts;
import com.example.shiftter.CurrentGroup;
import com.example.shiftter.CurrentUser;
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
    Ad_RecyclerView_Shifts ad_recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shiftsViewModel =
                ViewModelProviders.of(this).get(ShiftsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_shifts, container, false);

        //start Code

        db = FirebaseDatabase.getInstance().getReference();


        RecyclerView recyclerView = root.findViewById(R.id.recyclerView_Shifts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ad_recyclerView = new Ad_RecyclerView_Shifts(getActivity(), list);
        recyclerView.setAdapter(ad_recyclerView);

        //View List on recycleView
        getListOnPageCreate();

        return root;
    }



    public void getListOnPageCreate(){

        //Fill list
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Members").child(CurrentUser.getUserCodedEmail()).exists()) {
                    list.clear();
                    WGToShiftID wgToShiftID = dataSnapshot.child("Members").child(CurrentUser.getUserCodedEmail()).child(CurrentGroup.getGroupID()).getValue(WGToShiftID.class);

                    for (DataSnapshot ds : dataSnapshot.child("Shifts").child(wgToShiftID.getShiftID()).getChildren()){
                        Shift shift = ds.getValue(Shift.class);
                        list.add(shift);

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