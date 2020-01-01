package com.example.shiftter.ui.home;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.shiftter.CurrentGroup;
import com.example.shiftter.CurrentUser;
import com.example.shiftter.R;
import com.example.shiftter.Shift;
import com.example.shiftter.WGToShiftID;
import com.example.shiftter.WorkGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    DatabaseReference db;
    private String clockIn, clockOut, dateString;


    private ImageButton fingerPrintBtn;

    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> spinnerDataList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseDatabase.getInstance().getReference();

        fingerPrintBtn = (ImageButton) root.findViewById(R.id.fingerPrintBtn);

        chronometer = root.findViewById(R.id.chronometer);
        spinner = root.findViewById(R.id.spinner);
        spinnerDataList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerDataList);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = parent.getItemAtPosition(position).toString();
                updateCurrGroup(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // TODO: Fixing the function (un //)
        retrieveDataForSpinner();

        fingerPrintBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (!running) {
                    startChronometer(v);
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
                    Date time = new Date();
                    Date date = new Date();
                    clockIn = format.format(time);
                    dateString = formatDate.format(date);
                    Toast.makeText(getActivity(), dateString + clockIn, Toast.LENGTH_LONG).show();
                } else {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    Date time = new Date();
                    clockOut = format.format(time);
                    Toast.makeText(getActivity(), dateString + clockOut, Toast.LENGTH_LONG).show();
                    pauseChronometer(v);
                    addShift(clockIn, clockOut, dateString);
                }
            }
        });
        return root;
    }


    // TODO: 12/19/2019 Fixing the function after the new database (retrieveDataForSpinner).
    public void retrieveDataForSpinner(){

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Members").child(CurrentUser.getUserCodedEmail()).exists()) {
                    for (DataSnapshot ds : dataSnapshot.child("Members").child(CurrentUser.getUserCodedEmail()).getChildren()){
                        String groupID = ds.getKey();
                        WorkGroup workGroup = dataSnapshot.child("WorkGroups").child(groupID).getValue(WorkGroup.class);
                        spinnerDataList.add(workGroup.getGroupName());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    //Chronometer functions
    public void startChronometer(View v){
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }
    public void pauseChronometer(View v){
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
        }
    }


    public void updateCurrGroup(String groupNameSelected){
        db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.child("Members").child(CurrentUser.getUserCodedEmail()).getChildren()){
                            WorkGroup workGroup = dataSnapshot.child("WorkGroups").child(ds.getKey()).getValue(WorkGroup.class);
                            if (workGroup.getGroupName().equals(groupNameSelected)){
                                CurrentGroup.setGroupID(workGroup.getGroupKey());
                                CurrentGroup.setGroupMame(groupNameSelected);
                                CurrentGroup.setGroupManagerID(workGroup.getManagerEmail());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }




    // TODO: 12/19/2019 Fixing the function after the new database (addShifts).
    public void addShift(String clockIn, String clockOut, String dateString){
        Shift shift = new Shift(CurrentUser.getUserEmail(), dateString, clockIn, clockOut);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WGToShiftID wgToShiftID = dataSnapshot.child("Members")
                        .child(CurrentUser.getUserCodedEmail())
                        .child(CurrentGroup.getGroupID()).getValue(WGToShiftID.class);
                db.child("Shifts").child(wgToShiftID.getShiftID()).child(dateString).setValue(shift);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}