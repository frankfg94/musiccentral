package com.gillioen.navbarmusiccentral;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class AudioTrackListAdapter extends ArrayAdapter<AudioTrack> {

    private final List<AudioTrack> tracks;
    private int resourceLayout;
    private Context mContext;

    public AudioTrackListAdapter(Context context, int resource, List<AudioTrack> tracks) {
        super(context, resource, tracks);
        this.resourceLayout = resource;
        this.tracks = tracks;
        this.mContext = context;
        Picasso.with(context).setLoggingEnabled(true);
    }

    @Override
    public int getCount() {
        return  tracks.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, parent,false);
        }

        AudioTrack trackToDisplay = getItem(position);

        if (trackToDisplay != null) {

            if(trackToDisplay.imgPath != null)
            {
                // Online loading
                    ImageView tt1 = (ImageView) v.findViewById(R.id.imageView);
                    Picasso.with(mContext)
                            .load(trackToDisplay.imgPath)
                            .fit()
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(tt1);
            }
            Log.i("IMG","Loading image with picasso : " +  trackToDisplay.imgPath);

            TextView titleTv = v.findViewById(R.id.tviewTitle);
            TextView descriptionTv = v.findViewById(R.id.tviewAuthor);
            ImageView imV = v.findViewById(R.id.imageView);
            ImageView apiIconImg = v.findViewById(R.id.apiIcon);

            titleTv.setText(tracks.get(position).title);
            descriptionTv.setText(tracks.get(position).artist);
            //imV.setImageResource(tracks.get(position).imgPath);

            if(trackToDisplay.api == ApiType.Spotify )
                apiIconImg.setImageResource(R.drawable.spotify);
            else if (trackToDisplay.api == ApiType.Deezer)
                apiIconImg.setImageResource(R.drawable.deezer);
            else
                apiIconImg.setImageResource(0);
        }

        return v;
    }

}