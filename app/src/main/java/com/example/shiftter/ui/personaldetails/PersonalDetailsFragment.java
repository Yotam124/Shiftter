package com.example.shiftter.ui.personaldetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.shiftter.R;

public class PersonalDetailsFragment extends Fragment {

    private PersonalDetailsViewModel personalDetailsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        personalDetailsViewModel =
                ViewModelProviders.of(this).get(PersonalDetailsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_personal_details, container, false);

        //Start Code




        return root;
    }
}