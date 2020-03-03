package com.gillioen.navbarmusiccentral;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DeezerDownloaderTask extends AsyncTask<String, Integer, String> {

    private final String token;
    public DeezerDownloaderTask(String token)
    {
        this.token = token;
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        return sb.toString();
    }

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
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                 s = readStream(in);
                Log.i("JFL", s);
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