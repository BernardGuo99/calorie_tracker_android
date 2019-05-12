package com.example.calorietrackerapp.controller.asynctask;

import android.os.AsyncTask;

import com.example.calorietrackerapp.restclient.usda_food_search.UsdaFoodSearch;

import java.util.Map;

public class UsdaFoodNutritionAsyncTask extends AsyncTask<String, Void, Map<String, String>> {
    @Override
    protected Map<String, String> doInBackground(String... params) {
        return UsdaFoodSearch.getNutritionValue(UsdaFoodSearch.searchByNdbNo(params[0]));
    }
}
