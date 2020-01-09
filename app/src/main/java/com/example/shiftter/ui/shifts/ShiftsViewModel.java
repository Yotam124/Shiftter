package com.example.shiftter.ui.shifts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShiftsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ShiftsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("There are no shifts");
    }

    public LiveData<String> getText() {
        return mText;
    }
}