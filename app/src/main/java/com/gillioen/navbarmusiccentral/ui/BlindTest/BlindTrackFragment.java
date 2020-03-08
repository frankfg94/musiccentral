package com.gillioen.navbarmusiccentral.ui.BlindTest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.gillioen.navbarmusiccentral.BlindTest.BlindTest;
import com.gillioen.navbarmusiccentral.BlindTest.BlindTestBuilder;
import com.gillioen.navbarmusiccentral.BlindTest.BlindTrack;
import com.gillioen.navbarmusiccentral.MainActivity;
import com.gillioen.navbarmusiccentral.R;

public class BlindTrackFragment extends Fragment {

    private BlindTrackViewModel blindTrackViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.i("BLINDTEST","Creating fragment blindTrack");
        blindTrackViewModel =
                ViewModelProviders.of(this).get(BlindTrackViewModel.class);
        View root = inflater.inflate(R.layout.fragment_blindtrack, container, false);

        new BlindTrack(((MainActivity)getActivity()).musicList.get(3),10,4).playInFragment(this,root);
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}