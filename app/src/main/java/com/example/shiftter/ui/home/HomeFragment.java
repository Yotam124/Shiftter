package com.example.shiftter.ui.home;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.shiftter.CurrentUser;
import com.example.shiftter.R;
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
    private String clockIn, clockOut;


    private ImageButton fingerPrintBtn;

    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    private Spinner spinner;
    private ValueEventListener listener;
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


        // TODO: Fixing the function (un //)
        retrieveDataForSpinner();

        fingerPrintBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (!running) {
                    startChronometer(v);
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    clockIn = format.format(date);
                    Toast.makeText(getActivity(), clockIn, Toast.LENGTH_LONG).show();
                } else {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    clockOut = format.format(date);
                    Toast.makeText(getActivity(), clockOut, Toast.LENGTH_LONG).show();
                    pauseChronometer(v);
                }
            }
        });
        return root;
    }

    // TODO: 12/19/2019 Fixing the function after the new database (retrieveDataForSpinner).
    public void retrieveDataForSpinner(){

        db.child("Members").child(CurrentUser.getUserCodedEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        WorkGroup workGroup = ds.getValue(WorkGroup.class);
                        String groupName = workGroup.getGroupName();
                        spinnerDataList.add(groupName);
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


    // TODO: 12/19/2019 Fixing the function after the new database (addShifts).
    /*public void addShift(String clockIn, String clockOut){
        String userName = CurrentUser.getEmail();
        db = FirebaseDatabase.getInstance().getReference();
        db.child("Users").child("userName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

}