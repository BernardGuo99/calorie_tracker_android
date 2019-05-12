package com.example.calorietrackerapp.controller.asynctask;

import android.os.AsyncTask;

import com.example.calorietrackerapp.model.service.FoodService;

public class CheckFoodExistenceAsyncTask extends AsyncTask<String, Void, Boolean> {
    FoodService service = new FoodService();

    @Override
    protected Boolean doInBackground(String... params) {
        return service.checkFoodExistence(params[0]);
    }
}
