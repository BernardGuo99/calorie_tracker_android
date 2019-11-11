package com.example.calorietrackerapp.model.google_custom_search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.calorietrackerapp.R;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ImageSearch extends AsyncTask<String, String, String> {

    private ArrayList<String> Links = new ArrayList<>();
    private ProgressDialog progressDialog;
    private JSONParser jParser = new JSONParser();
    private JSONObject json;
    public static Activity activity;
    public ImageView img;

    public ImageSearch(ImageView img) {
        this.img = img;
    }

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
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println(args[0]);
        json = jParser.makeHttpRequest(createURL(args[0]), "GET", params);
        System.out.println(json);
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
        try {
            Picasso.with(activity).load(Links.get(0)).into(img);
        } catch (Exception e) {
            e.printStackTrace();
            img.setImageResource(R.mipmap.no_image_found);
        }

    }

    private String API_KEY = Resources.getSystem().getString(R.string.google_api_key);
    private String IMG_SIZE = "medium";
    private String SEARCH_ENGINE_ID = Resources.getSystem().getString(R.string.google_search_engine_id);
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
