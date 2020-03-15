package com.gillioen.navbarmusiccentral.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.gillioen.navbarmusiccentral.R;

public class MyPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}