package com.gillioen.navbarmusiccentral.ui.BlindTest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.gillioen.navbarmusiccentral.blindTest.BlindTest;
import com.gillioen.navbarmusiccentral.blindTest.BlindTestBuilder;
import com.gillioen.navbarmusiccentral.MainActivity;
import com.gillioen.navbarmusiccentral.NotificationGenerator;
import com.gillioen.navbarmusiccentral.R;

public class BlindTrackFragment extends Fragment {

    private BlindTrackViewModel blindTrackViewModel;
    BlindTest bt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("BLINDTEST","Creating fragment blindTrack");
        blindTrackViewModel =
                ViewModelProviders.of(this).get(BlindTrackViewModel.class);
        View root = inflater.inflate(R.layout.fragment_blindtrack, container, false);
        MainActivity ma = (MainActivity)getActivity();
        BlindTest configBt = ma.createdBlindTest;
        if(configBt != null)
        {
            bt = new BlindTestBuilder(ma.musicList)
                    .setTitle("Blindtest configuré")
                    .setGameTrackCount(configBt.getGameTrackCount())
                    .setRevealDurSeconds(configBt.getTrackRevealDurationSec())
                    .setTrackPlayDurationSec(configBt.getTrackPlayDurationSec())
                    .setChoiceCountForEachTrack(configBt.getChoiceCountForEachTrack())
                    .build();
            ma.createdBlindTest = null;
        }
        else {
            // if bug or not configured blindtest, we start one with default settings
            bt = new BlindTestBuilder(ma.musicList)
                    .setTitle(BlindTest.defaultTitle)
                    .setGameTrackCount(BlindTest.defaultGameTrackCount)
                    .setRevealDurSeconds(BlindTest.defaultTrackRevealDurationSec)
                    .setTrackPlayDurationSec(BlindTest.defaultTrackPlayDurationSec)
                    .setChoiceCountForEachTrack(BlindTest.defaultChoiceCountForEachTrack)
                    .build();
        }

        Log.i("BLINDTEST",bt.toString());
        NotificationGenerator.setEnabled(false);
        bt.startGame(this,root);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        NotificationGenerator.setEnabled(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}