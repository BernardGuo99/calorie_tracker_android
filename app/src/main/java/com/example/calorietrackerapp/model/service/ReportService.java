package com.example.calorietrackerapp.model.service;

import com.example.calorietrackerapp.model.dao.DAOImpl;
import com.example.calorietrackerapp.model.dao.IDAO;
import com.example.calorietrackerapp.model.entity.Report;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ReportService {

    private IDAO dao = new DAOImpl();
    private String path;

    public void createReport(Report report) {
        path = "restws.report/";
        dao.createInstance(report, path);
    }

    public List<String[]> getReportsByDateRange(String userId, Date startDate, Date endDate) {
        List<String[]> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        path = "restws.report/findUserReportDateRange/" + userId + "/" + sdf.format(startDate) + "/" + sdf.format(endDate);
        String result = dao.find(path);
        System.out.println(result);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Report[] arr = gson.fromJson(result, Report[].class);


        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i =0;i<jsonArray.length();i++){
                if (jsonArray.getJSONObject(i).isNull("caloriesConsumed")){
                    list.add(new String[]{"0", "0"});
                }else{
                    list.add(new String[]{jsonArray.getJSONObject(i).getString("caloriesConsumed"),jsonArray.getJSONObject(i).getString("caloriesBurned")});
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        System.out.println(Arrays.toString(arr));
//        System.out.println(arr.length);
//        for (Report report : arr) {
//            if (report == null) {
//                list.add(new String[]{"0", "0"});
//            } else {
//                list.add(new String[]{String.valueOf(report.getCaloriesConsumed()), String.valueOf(report.getCaloriesBurned())});
//            }
//        }

        return list;
    }


    public float[] findCaloriesByUserAndDate(String userId, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        path = "restws.report/queryCaloriesInOneDay/" + userId + "/" + dateString;
        float[] calories = new float[3];
        String result = dao.find(path);
        try {
            JSONObject jsonObject = new JSONArray(result).getJSONObject(0);

            calories[0] = Float.parseFloat(jsonObject.getString("caloriesConsumed"));
            calories[1] = Float.parseFloat(jsonObject.getString("caloriesBurned"));
            calories[2] = Float.parseFloat(jsonObject.getString("remainingCalorie"));

        } catch (JSONException e) {
            e.printStackTrace();
            calories = null;
        }


        return calories;
    }

}
