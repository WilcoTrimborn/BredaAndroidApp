package com.example.wilco.breda.dao;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.wilco.breda.applogic.NewPhotoListener;
import com.example.wilco.breda.domain.BredaPhoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Wilco on 25-6-2018.
 */

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class RetrievePhotoAsync extends AsyncTask<String, Void, String> {

    private NewPhotoListener listener;
    private String apiUrlBase;
    private String apiUrlSecond;
    private DatabaseHandler dbHandler;
    private static final String TAG = RetrievePhotoAsync.class.getSimpleName();

    public RetrievePhotoAsync(Context context, NewPhotoListener listener, String apiBaseUrl) {
        this.listener = listener;
        this.apiUrlBase = apiBaseUrl;
        this.dbHandler = new DatabaseHandler(context);
    }

    @Override
    protected String doInBackground(String... params) {
        String response = params[0] + "SPLITSTR";
        if (dbHandler.getPhotosCount(params[0]) < 1) {
            Log.i("RetrievePhotosAsync", "Database does not have entries, retrieving from API...");
            InputStream inputStream = null;
            int responseCode = -1;
            String BredaPhotoUrl = this.apiUrlBase + params[0] + this.apiUrlSecond;
            Log.i("ApiUrlSet", "API url set to: " + BredaPhotoUrl);
            try {
                URL url = new URL(BredaPhotoUrl);
                URLConnection urlConnection = url.openConnection();

                if (!(urlConnection instanceof HttpURLConnection)) {
                    return null;
                }

                HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
                httpConnection.setAllowUserInteraction(false);
                httpConnection.setInstanceFollowRedirects(true);
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();
                responseCode = httpConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpConnection.getInputStream();
                    response += getStringFromInputStream(inputStream);
                } else {
                    Log.e(TAG, "ERROR, Invalid response");
                }
            } catch (MalformedURLException e) {
                Log.e(TAG, "DoInBackground MalformedURLException " + e.getLocalizedMessage());
                return null;
            } catch (IOException e) {
                Log.e(TAG, "DoInBackground IOException " + e.getLocalizedMessage());
            }
        } else {
            Log.i("RetrievePhotoAsync", "Database has 1 or more entries, using database...");
            response += "DATABASE";
        }
        return response;
    }



        protected void onPostExecute(String response) {
            if (response == null || response.equals("")) {
                Log.e(TAG, "onPostExecute() kreeg een lege response");
                return;
            }
            if (response.split("SPLITSTR")[1].equals("DATABASE")) {
                for(BredaPhoto np : dbHandler.getAllPhotos(response.split("SPLITSTR")[0])) {
                    listener.onNewPhotoListener(np);
                    Log.i("NewNasaPhoto", "New photo added from database");
                }
            } else {
                dbHandler.clearTable(response.split("SPLITSTR")[0]);
                response = response.split("SPLITSTR")[1];
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    JSONArray bredaPhotos = jsonObject.getJSONArray("photos");
                    for (int i = 0; i < bredaPhotos.length(); i++) {
                        JSONObject bredaPhoto = bredaPhotos.getJSONObject(i);
                        int id = bredaPhoto.getInt("id");
                        String imgURL = bredaPhoto.getString("URL");
                        JSONObject location = bredaPhoto.getJSONObject("GEOGRAFISCHELIGGING");
                        JSONObject artist = bredaPhoto.getJSONObject("KUNSTENAAR");
                        JSONObject description = bredaPhoto.getJSONObject("OMSCHRIJVING");
                        JSONObject material = bredaPhoto.getJSONObject("MATERIAAL");
                        JSONObject underground = bredaPhoto.getJSONObject("ONDERGROND");
//                        JSONObject photourl = bredaPhoto.getJSONObject("URL");
                        String locationName = location.getString("locationName");
                        String artistName = artist.getString("artistName");
                        String descriptionName = description.getString("descriptionName");
                        String materialName = material.getString("materialName");
                        String undergroundName = underground.getString("undergroundName");
//                        String photourlName = photourl.getString("photourlName");
                        BredaPhoto np = new BredaPhoto(String.valueOf(id), locationName, artistName, descriptionName, materialName, undergroundName, imgURL);
                        listener.onNewPhotoListener(np);
                        Log.i("NewBredaPhoto", "New photo added from API");
                        dbHandler.addPhoto(np);
                        Log.i("NewBredaPhoto", "New photo inserted into DB");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
    }
    private String getStringFromInputStream(InputStream inputStream) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
