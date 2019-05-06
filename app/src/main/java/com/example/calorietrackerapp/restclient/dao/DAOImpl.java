package com.example.calorietrackerapp.restclient.dao;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;

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


    public void createInstance(Object object, String path) {
        HttpURLConnection conn = this.getConnection(path);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String stringJson = gson.toJson(object);
//        Timestamp ts = new Timestamp(new Date().getTime());
//        System.out.println(ts);
//        System.out.println(new Date());
//        System.out.println("########################################################################");
//        System.out.println(stringJson);
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        try {
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(stringJson.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(stringJson);
            out.close();
            Log.i("error", new Integer(conn.getResponseCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

    @Override
    public String find(String path) {
        String textResult = "";
        HttpURLConnection conn = this.getConnection(path);
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            Scanner inStream = new Scanner(conn.getInputStream());
            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        //Object[] arr = new Gson().fromJson(textResult, Object[].class);
        return textResult;
    }

}
