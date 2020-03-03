package com.gillioen.navbarmusiccentral.Preferences;


import android.preference.PreferenceActivity;

import com.gillioen.navbarmusiccentral.R;

import java.util.List;

public class MyPreferenceActivity extends PreferenceActivity {

    @Override
    public void onBuildHeaders(List<Header> target)
    {
        loadHeadersFromResource(R.xml.headers_preference, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName)
    {
        return MyPreferenceFragment.class.getName().equals(fragmentName);
    }
}
