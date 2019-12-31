package com.example.shiftter.ui.personaldetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PersonalDetailsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PersonalDetailsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is share fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}