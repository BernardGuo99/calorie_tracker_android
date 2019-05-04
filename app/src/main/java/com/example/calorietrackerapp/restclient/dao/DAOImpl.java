package com.example.calorietrackerapp.restclient.dao;

import android.util.Log;

import com.google.gson.Gson;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class DAOImpl implements IDAO {
    private static final String BASE_URL = "http://10.0.2.2:8080/CalorieTracker-webservice/webresources/";

    public HttpURLConnection getConnection(String path) {
        final String methodPath = path;
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void createInstance(Object object, String path){
        HttpURLConnection conn = this.getConnection(path);
        Gson gson =new Gson();
        String stringJson=gson.toJson(object);
        System.out.println(new Date());
        System.out.println("########################################################################");
        System.out.println(stringJson);
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        try {
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(stringJson.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringJson);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

    public boolean checkExistence(String s, String path){
return true;
    }
}
