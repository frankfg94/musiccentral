package com.gillioen.navbarmusiccentral.ui.BlindTest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.gillioen.navbarmusiccentral.blindTest.BlindTest;
import com.gillioen.navbarmusiccentral.blindTest.BlindTestBuilder;
import com.gillioen.navbarmusiccentral.MainActivity;
import com.gillioen.navbarmusiccentral.NotificationGenerator;
import com.gillioen.navbarmusiccentral.R;

public class BlindTestConfigFragment extends Fragment {

    private BlindTrackViewModel blindTrackViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        MainActivity ma = (MainActivity)(getActivity());
        BlindTestBuilder builder = new BlindTestBuilder(ma.musicList);
        Log.i("BLINDTEST","Creating fragment blindTrack");
        blindTrackViewModel = ViewModelProviders.of(this).get(BlindTrackViewModel.class);
        View root = inflater.inflate(R.layout.fragment_blindtest_config, container, false);

        // Fetching controls
        NumberPicker numberPickerListen = root.findViewById(R.id.numberPickerListenDuration);
        NumberPicker numberPickerReveal = root.findViewById(R.id.numberPickerRevealDuration);
        NumberPicker numberPickerTrackCount = root.findViewById(R.id.numberPickerTrackCount);
        RadioGroup choicesRadioGroup = root.findViewById(R.id.possibleChoicesRadioGroup);
        Button startBlindTestButton = root.findViewById(R.id.startBlindTestButton);

        setPickersLimits(numberPickerListen,numberPickerReveal,numberPickerTrackCount);

        // Default values
        numberPickerListen.setValue(BlindTest.defaultTrackPlayDurationSec);
        numberPickerReveal.setValue(BlindTest.defaultTrackRevealDurationSec);
        numberPickerTrackCount.setValue(BlindTest.defaultGameTrackCount);
        choicesRadioGroup.getChildAt(BlindTest.defaultChoiceCountForEachTrack);

        setListeners( startBlindTestButton,choicesRadioGroup, builder,root);

        return root;
    }


    private void setPickersLimits(@NonNull NumberPicker numberPickerListen, @NonNull NumberPicker numberPickerReveal, @NonNull NumberPicker numberPickerTrackCount) {
        MainActivity ma = (MainActivity) getActivity();

        numberPickerListen.setMinValue(3);
        numberPickerListen.setMaxValue(30);
        numberPickerReveal.setMinValue(3);
        numberPickerReveal.setMaxValue(10);
        numberPickerTrackCount.setMinValue(1);
        numberPickerTrackCount.setMaxValue(ma.musicList.size());
    }

    // The values will be automatically adjusted by the getters & setters
    private void setListeners(@NonNull Button startButton, @NonNull RadioGroup radioGroup, @NonNull BlindTestBuilder builder, @NonNull View v) {


        startButton.setOnClickListener(e -> {

            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = v.findViewById(selectedId);
            int choiceCount = Integer.parseInt(String.valueOf(radioButton.getText()));
            builder.setChoiceCountForEachTrack(choiceCount);

            // passing the blindtest to the activity
            MainActivity ma = (MainActivity) BlindTestConfigFragment.this.getActivity();
            ma.createdBlindTest = builder.build();

            // Switching from the config fragment to the game fragment
            BlindTrackFragment blindTestFragment = new BlindTrackFragment();
            BlindTestConfigFragment.this.getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.blindtest_frame, blindTestFragment, "findThisFragment")
                    .addToBackStack(null)
                    .commit();

        });


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