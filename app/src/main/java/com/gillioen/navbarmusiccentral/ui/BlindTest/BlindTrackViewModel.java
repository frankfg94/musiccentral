package com.gillioen.navbarmusiccentral.ui.BlindTest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BlindTrackViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BlindTrackViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is BlindTrack fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}