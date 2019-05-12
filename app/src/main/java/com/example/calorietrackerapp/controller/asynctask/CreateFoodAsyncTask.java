package com.example.calorietrackerapp.controller.asynctask;

import android.os.AsyncTask;

import com.example.calorietrackerapp.model.entity.Food;
import com.example.calorietrackerapp.model.service.FoodService;

import java.util.UUID;

public class CreateFoodAsyncTask extends AsyncTask<String, Void, Void> {
    private FoodService service = new FoodService();


    @Override
    protected Void doInBackground(String... params) {
        Food food = new Food(UUID.randomUUID().toString());
        food.setFoodName(params[0]);
        food.setCategory(params[1]);
        food.setServiceUnit(params[2]);
        food.setFat(Double.parseDouble(params[3]));
        food.setCalorieAmount(Double.parseDouble(params[4]));
        service.createFood(food);
        return null;
    }
}
