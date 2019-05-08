package com.example.calorietrackerapp.controller.asynctask;

import android.os.AsyncTask;

import com.example.calorietrackerapp.restclient.service.FoodService;

import java.util.List;

public class QueryFoodByCategoryAsyncTask extends AsyncTask<String, Void, List> {
    FoodService service = new FoodService();

    @Override
    protected List doInBackground(String... params) {
        return service.getFoodNameByCategory(params[0]);
    }
}
