package com.example.calorietrackerapp.model.google_custom_search;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class TextSearch {
    private static final String API_KEY = "AIzaSyCmXvXxNTg7-1n5n-8s4Sf95qThIYULQsM";
    private static final String SEARCH_ID_cx = "001228395695091314793:shn7wfnn4vm";

    public static String search(String keyword, String[] params, String[] values) {
        keyword = keyword.replace(" ", "+");
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";
        String query_parameter = "";
        //System.out.println(Arrays.toString(params));
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        if (params != null && values != null) {
            for (int i = 0; i < params.length; i++) {
                query_parameter += "&";
                query_parameter += params[i];
                query_parameter += "=";
                query_parameter += values[i];
            }
        }
        try {
            url = new URL("https://www.googleapis.com/customsearch/v1?key=" +
                    API_KEY + "&cx=" + SEARCH_ID_cx + "&q=" + keyword + query_parameter);
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

    public static String getSnippet(String result) {
        String snippet = null;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if (jsonArray != null && jsonArray.length() > 0) {
                snippet = jsonArray.getJSONObject(0).getString("snippet");
                if (snippet.contains(("..."))) {
                    snippet = snippet.substring(0, snippet.lastIndexOf("..."));
                    if (snippet.contains(".")) {
                        snippet = snippet.substring(0, snippet.lastIndexOf(".") + 1).replace("\n", "");
                    } else {
                        snippet = snippet.substring(0, snippet.lastIndexOf(",")).replace("\n", "") + ".";
                    }
                } else {
                    snippet = snippet.replace("\n", "");
                }
                System.out.println(snippet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            snippet = "NO INFO FOUND";
        }
        return snippet;
    }
}
