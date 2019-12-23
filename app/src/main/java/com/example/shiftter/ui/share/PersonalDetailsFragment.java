package com.example.shiftter.ui.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.shiftter.R;

public class PersonalDetailsFragment extends Fragment {

    private PersonalDetailsViewModel personalDetailsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        personalDetailsViewModel =
                ViewModelProviders.of(this).get(PersonalDetailsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_personal_details, container, false);
        final TextView textView = root.findViewById(R.id.text_share);
        personalDetailsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}