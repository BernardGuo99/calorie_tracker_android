package com.example.calorietrackerapp.model.google_custom_search;

import android.content.res.Resources;

import com.example.calorietrackerapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NearbySearch {
    private static final String API_KEY = Resources.getSystem().getString(R.string.google_api_key);
    private static final String TYPE = "park";


    public static String search(String latitude, String langitude) {

        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";

        //System.out.println(Arrays.toString(params));
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");


        try {
            url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                    latitude + "," + langitude + "&radius=5000&type=" + TYPE + "&key=" + API_KEY);
            System.out.println(url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        //System.out.println(textResult);
        return textResult;
    }

    public static List<String[]> getAllLocations(String result) {
        //double[] location = new double[2];


        List<String[]> locations = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject location = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                    String latitude = location.getString("lat");
                    String langitude = location.getString("lng");
                    String name = jsonArray.getJSONObject(i).getString("name");
                    locations.add(new String[]{latitude, langitude, name});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
        System.out.println("******************************************************");
        System.out.println(locations);
        return locations;
    }

}
