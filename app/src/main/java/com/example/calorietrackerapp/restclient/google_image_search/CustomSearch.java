package com.example.calorietrackerapp.restclient.google_image_search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.calorietrackerapp.controller.fragment.DailyDietFragment.img;

public class CustomSearch extends AsyncTask<String, String, String> {

    private ArrayList<String> Links = new ArrayList<>();
    private ProgressDialog progressDialog;
    private JSONParser jParser = new JSONParser();
    private JSONObject json;
    public static Activity activity;

    @Override
    protected void onPreExecute() {
        Links.clear();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected String doInBackground(String... args) {
        List<NameValuePair> params = new ArrayList<>();
        json = jParser.makeHttpRequest(createURL(args[0]), "GET", params);
        try {
            JSONArray items = json.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject c = items.getJSONObject(i);
                String link = c.getString("link");
                Links.add(link);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String file_url) {
        progressDialog.cancel();
        Picasso.with(activity).load(Links.get(0)).fit().into(img);
    }

    private String API_KEY = "AIzaSyDFJ27R1zLR2iAZR8RtuEDg1yeb79ixcFY";
    private String IMG_SIZE = "medium";
    private String SEARCH_ENGINE_ID = "011224887862731301805:inqowvialem";
    private String SEARCH_TYPE = "image";
    private String FILE_TYPE = "jpg";

    private String createURL(String url) {
        url = url.replace(" ", "");
        String URL = "https://www.googleapis.com/customsearch/v1?" +
                "key=" + API_KEY +
                "&imgSize=" + IMG_SIZE +
                "&cx=" + SEARCH_ENGINE_ID +
                "&q=" + url +
                "&searchType=" + SEARCH_TYPE +
                "&fileType=" + FILE_TYPE;
        return URL;
    }

}
