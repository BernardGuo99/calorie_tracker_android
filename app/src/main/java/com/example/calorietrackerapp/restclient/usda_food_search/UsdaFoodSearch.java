package com.example.calorietrackerapp.restclient.usda_food_search;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UsdaFoodSearch {
    private static final String API_KEY = "y1lQFO8aGodBG7WOQLn8fVh8iewj1mg5fLF9x0NW";


    public static String searchByFoodName(String keyword) {

        try {
            keyword = URLEncoder.encode(keyword, "utf-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String query_parameter = "";
        //System.out.println(Arrays.toString(params));
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//        if (params != null && values != null) {
//            for (int i = 0; i < params.length; i++) {
//                query_parameter += "&";
//                query_parameter += params[i];
//                query_parameter += "=";
//                query_parameter += values[i];
//            }
//        }
        try {
            url = new URL("https://api.nal.usda.gov/ndb/search/?format=json" + "&q=" + keyword +
                    "&max=25&offset=0&api_key=" + API_KEY);
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

    public static Map<String, String> getFoodValue(String result) {
        Map<String, String> foodMap = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(result).getJSONObject("list");

            if (jsonObject != null) {
                JSONArray itemArray = jsonObject.getJSONArray("item");
                for (int i = 0; i < itemArray.length(); i++) {
                    JSONObject itemObject = itemArray.getJSONObject(i);
                    String foodName = itemObject.getString("name");
                    String ndbNo = itemObject.getString("ndbno");
                    if (foodName.contains("UPC")) {
                        foodName = foodName.substring(0, foodName.lastIndexOf("UPC") - 2);
                    }
                    if (!foodMap.containsKey(foodName)) {
                        foodMap.put(foodName, ndbNo);
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return foodMap;
    }
}
