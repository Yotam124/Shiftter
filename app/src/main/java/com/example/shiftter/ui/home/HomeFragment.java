package com.example.shiftter.ui.home;

import android.icu.text.DateFormat;
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
import com.example.shiftter.GroupMember;
import com.example.shiftter.R;
import com.example.shiftter.Shift;
import com.example.shiftter.WGToShiftID;
import com.example.shiftter.WorkGroup;
import com.example.shiftter.intoWorkGroupAsManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private DatabaseReference db;
    private String clockIn, clockOut, dateString,hoursForShift;
    private double wage;


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
            Date startDate,endDate;
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
                    try {
                        startDate = format.parse(clockIn);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dateString = formatDate.format(date);
                    Toast.makeText(getActivity(), dateString + clockIn, Toast.LENGTH_LONG).show();

                } else {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    Date time = new Date();
                    clockOut = format.format(time);
                    try {
                        endDate = format.parse(clockOut);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long difference = endDate.getTime() - startDate.getTime();
                    if(difference<0)
                    {

                        Date dateMin = null,dateMax = null;
                        try {
                            dateMin = format.parse("00:00");
                            dateMax = format.parse("24:00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
                    }
                    int days = (int) (difference / (1000*60*60*24));
                    int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                    int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                    if (hours < 10){
                        if (min <10){
                            hoursForShift = "0" + hours + ":0" + min;
                        }else{
                            hoursForShift = "0" + hours + ":" + min;
                        }
                    }else{
                        if (min <10){
                            hoursForShift = hours + ":0" + min;
                        }else{
                            hoursForShift = hours + ":" + min;
                        }
                    }
                    //Toast.makeText(getActivity(), "fck", Toast.LENGTH_LONG).show();
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            GroupMember gm = dataSnapshot.child("WorkGroups").child(CurrentGroup.getGroupID())
                                    .child("ListOfMembers").child(CurrentUser.getUserCodedEmail()).getValue(GroupMember.class);
                            wage = Double.parseDouble(gm.getSalary());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    wage = (wage/60*min) + (wage*hours);
                    Toast.makeText(getActivity(), dateString + clockOut, Toast.LENGTH_LONG).show();
                    pauseChronometer(v);
                    addShift(clockIn, clockOut, dateString,hoursForShift,wage);

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
    public void addShift(String clockIn, String clockOut, String dateString, String hoursForShift,double wage){
        Shift shift = new Shift(CurrentUser.getUserEmail(), dateString, clockIn, clockOut, hoursForShift,wage);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WGToShiftID wgToShiftID = dataSnapshot.child("Members")
                        .child(CurrentUser.getUserCodedEmail())
                        .child(CurrentGroup.getGroupID()).getValue(WGToShiftID.class);
                if(!dataSnapshot.child("Shifts").child(wgToShiftID.getShiftID()).child(dateString).exists()){
                    db.child("Shifts").child(wgToShiftID.getShiftID()).child(dateString).setValue(shift);
                }else if(!dataSnapshot.child("Shifts").child(wgToShiftID.getShiftID()).child(dateString+"NO2").exists()){
                    db.child("Shifts").child(wgToShiftID.getShiftID()).child(dateString+"NO2").setValue(shift);
                }else{
                    Toast.makeText(getActivity(),"To many shifts today", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
