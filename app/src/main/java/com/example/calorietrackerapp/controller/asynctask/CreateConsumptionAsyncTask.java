package com.example.calorietrackerapp.controller.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.calorietrackerapp.restclient.entity.AppUser;
import com.example.calorietrackerapp.restclient.entity.Consumption;
import com.example.calorietrackerapp.restclient.entity.Food;
import com.example.calorietrackerapp.restclient.service.ConsumptionService;
import com.example.calorietrackerapp.restclient.service.FoodService;

import java.util.Date;
import java.util.UUID;

public class CreateConsumptionAsyncTask extends AsyncTask<String, Void, Void> {

    FoodService foodService = new FoodService();
    ConsumptionService consumptionService = new ConsumptionService();

    @Override
    protected Void doInBackground(String... params) {
        Consumption consumption = new Consumption(UUID.randomUUID().toString());
        consumption.setUserId(new AppUser(params[0]));
        consumption.setFoodId(foodService.getFoodByName(params[1]));
        consumption.setConsumptionDate(new Date());
        consumption.setQuantity(Integer.parseInt(params[2]));
        consumptionService.createConsumption(consumption);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
