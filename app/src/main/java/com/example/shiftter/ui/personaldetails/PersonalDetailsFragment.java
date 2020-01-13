package com.example.shiftter.ui.personaldetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.shiftter.CurrentUser;
import com.example.shiftter.R;
import com.example.shiftter.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonalDetailsFragment extends Fragment {

    private PersonalDetailsViewModel personalDetailsViewModel;
    private DatabaseReference db;
    private User myUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        personalDetailsViewModel =
                ViewModelProviders.of(this).get(PersonalDetailsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_personal_details, container, false);

        //Start Code


        TextView firstName = root.findViewById(R.id.pd_first_name);
        TextView lastName = root.findViewById(R.id.pd_last_name);
        TextView email = root.findViewById(R.id.pd_email);

        db = FirebaseDatabase.getInstance().getReference();

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myUser = dataSnapshot.child("Users").child(CurrentUser.getUserCodedEmail()).getValue(User.class);

                firstName.setText(myUser.getFirstName());
                lastName.setText(myUser.getLastName());
                email.setText(myUser.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return root;
    }
}