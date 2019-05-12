package com.example.calorietrackerapp.model.usda_food_search;

import org.json.JSONArray;
import org.json.JSONException;
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
        return textResult;
    }


    public static String searchByNdbNo(String ndbNo) {

        try {
            ndbNo = URLEncoder.encode(ndbNo, "utf-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        //String query_parameter = "";
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


            url = new URL("https://api.nal.usda.gov/ndb/V2/reports?ndbno=" + ndbNo +
                    "&type=b&format=json&api_key=" + API_KEY);
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
        return textResult;
    }

    public static Map<String, String> getNutritionValue(String result) {
        Map<String, String> nutritionMap = new HashMap<>();
        String energy = "0";
        String serving = "No Info";
        String fat = "0";
        String protein = "0";
        try {
            JSONArray jsonArray = new JSONObject(result).getJSONArray("foods");
            if (jsonArray != null) {
                JSONArray nutrientsArray = jsonArray.getJSONObject(0).getJSONObject("food").getJSONArray("nutrients");
                String nutrientId = "";
                for (int i = 0; i < nutrientsArray.length(); i++) {
                    JSONObject nutrientObject = nutrientsArray.getJSONObject(i);
                    nutrientId = nutrientObject.getString("nutrient_id");
                    switch (nutrientId) {
                        case "208":
                            energy = nutrientObject.getString("value");
                            serving = nutrientObject.getJSONArray("measures").getJSONObject(0).getString("label");

                            break;
                        case "203":
                            protein = nutrientObject.getString("value");
                            serving = nutrientObject.getJSONArray("measures").getJSONObject(0).getString("label");

                            break;
                        case "204":
                            fat = nutrientObject.getString("value");
                            serving = nutrientObject.getJSONArray("measures").getJSONObject(0).getString("label");
                            break;
                        default:
                            break;
                    }
                    nutritionMap.put("Energy", energy);
                    nutritionMap.put("Serving", serving);
                    nutritionMap.put("Protein", protein);
                    nutritionMap.put("Fat", fat);


                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nutritionMap;
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
