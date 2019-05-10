package com.example.calorietrackerapp.controller.asynctask;

import android.os.AsyncTask;

import com.example.calorietrackerapp.restclient.entity.Food;
import com.example.calorietrackerapp.restclient.service.FoodService;


public class QueryFoodByNameAsynctask extends AsyncTask<String, Void, Food> {
    FoodService service = new FoodService();

    @Override
    protected Food doInBackground(String... params) {
        return service.getFoodByName(params[0]);
    }
}
