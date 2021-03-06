package com.gillioen.navbarmusiccentral.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gillioen.navbarmusiccentral.AudioTrack;
import com.gillioen.navbarmusiccentral.MainActivity;
import com.gillioen.navbarmusiccentral.MusicAdapter;
import com.gillioen.navbarmusiccentral.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment {

    @Nullable
    public ArrayList<AudioTrack> ar = null;
    private boolean ascendingSort = true;

    @Nullable
    RecyclerView rv = null;

    @Nullable
    public MusicAdapter musicAdapter;
    private static boolean init = false;

    @Override
    public void onStart() {

        super.onStart();
        MainActivity ma = (MainActivity) (getActivity());
        rv = getView().findViewById(R.id.recyclerView);
        if(!init)
        {
            ma.setOnMusicListLoaded(list ->
                    {
                        displayMusicList();
                        init = true;
                    }
            );
        }
        else if(rv != null)
        {
            displayMusicList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Reloading the recycler view
        displayMusicList();
        Log.d("STATE","onResume()");
    }

    static int i = 0;
    void displayMusicList()
    {
        Log.d("AUDIOTRACK","display Music List" + (++i));
        MainActivity ma = (MainActivity) getActivity();
        musicAdapter = new MusicAdapter(ma.musicList, ma.localPlayer, ma.deezerPlayer, ma.isSpotifyPremium, ma.spotifyPlayer, getContext());
        rv.setAdapter(musicAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_home, container, false);

        EditText searchBar =  root.findViewById(R.id.search_bar_fragment_home);
        Spinner sp = root.findViewById(R.id.spinner);
        Button but = root.findViewById(R.id.sortTracksModeButton);
        MainActivity act = ((MainActivity)getActivity());
        setListeners(searchBar,sp,but,act);
        return root;
    }

    private void setListeners(EditText searchBar, Spinner sp, Button but, MainActivity act)
    {
        but.setOnClickListener(v -> {
            if(ar == null || ar.size() == 0)
            {
                ar = act.musicList;
                musicAdapter.filterList(ar);
            }
            if(ascendingSort == true)
            {
                but.setText("Croissant");
                setAscendingSort(false);
            }
            else
            {
                but.setText("Décroissant");
                setAscendingSort(true);
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(@NonNull Editable s) {
                filterRecyclerView(s.toString());
            }
        });

        // Setting the different comparator classes
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NonNull AdapterView<?> parent, View view, int position, long id) {
                ar = act.musicList;
                Comparator<AudioTrack> sortingAlgorithm = new AudioTrackAlphabetComparator();
                switch (position) {
                    case 0:
                        Toast.makeText(parent.getContext(), "Tri par Titre", Toast.LENGTH_SHORT).show();
                        sortingAlgorithm = new AudioTrackAlphabetComparator();
                        break;
                    case 1:
                        Toast.makeText(parent.getContext(), "Tri par Artiste", Toast.LENGTH_SHORT).show();
                        sortingAlgorithm = new AudioTrackArtistComparator();
                        break;
                    case 2:
                        Toast.makeText(parent.getContext(), "Tri par Application", Toast.LENGTH_SHORT).show();
                        sortingAlgorithm = new AudioTrackAPIComparator();
                        break;
                    case 3 :
                        Toast.makeText(parent.getContext(), "Tri par Date", Toast.LENGTH_SHORT).show();
                        sortingAlgorithm = new AudioTrackDateComparator();
                        break;
                }
                Collections.sort(ar,sortingAlgorithm);
                if(musicAdapter!=null)
                    musicAdapter.filterList(ar);
                else
                    Log.d("NULL","NULL");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    /**
     * Filter by author or music name
     */
    private void filterRecyclerView(@NonNull String text){
        ArrayList<AudioTrack> filteredList = new ArrayList();
        MainActivity ma = ((MainActivity)getActivity());
        for(AudioTrack t : ma.musicList)
        {
            if(t.getTitle().toLowerCase().contains(text.toLowerCase()) || t.getArtist().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(t);
                Log.d("SEARCH","FOUND : " + t);
            }
        }

        // Display the filteredList in the recycler view
        if(musicAdapter != null)
            musicAdapter.filterList(filteredList);
    }

    public void setAscendingSort(boolean ascendingSort) {

            if(ar != null && ar.size() > 0)
            {
                Collections.reverse(ar);
                musicAdapter.filterList(ar);
                this.ascendingSort = ascendingSort;
            }
    }



}