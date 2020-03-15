package com.gillioen.navbarmusiccentral;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MusicDownloaderTask extends AsyncTask<String, Integer, String> {

    private final String token;

    public MusicDownloaderTask(String token)
    {
        this.token = token;
    }

    @NonNull
    private String readStream(@NonNull InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        return sb.toString();
    }

    @NonNull
    @Override
    protected String doInBackground(String... params) {

        String urlToFetch = params[0];
        String s = "";
        URL url = null;
        try {
            url = new URL(urlToFetch);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.setRequestProperty("Authorization","Bearer " + token);
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                 s = readStream(in);
                Log.d("JFL", s);
            } finally {
                urlConnection.disconnect();
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    protected void onPreExecute(){

    }

    protected void onPostExecute(){

    }

    protected void onProgressUpdate(){

    }

}