package com.example.calorietrackerapp.restclient.service;

import com.example.calorietrackerapp.restclient.dao.DAOImpl;
import com.example.calorietrackerapp.restclient.dao.IDAO;
import com.example.calorietrackerapp.restclient.entity.Food;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FoodService {
    private IDAO dao = new DAOImpl();
    private String path;

    public List<String> getFoodNameByCategory(String category) {
        path = "restws.food/findByCategory/" + category;
        String items = dao.find(path);
        List<String> foodList = new ArrayList<>();
        foodList.add("Select Your Food");
        Gson gson = new Gson();
        Food[] arr = gson.fromJson(items, Food[].class);
        for (Food food : arr) {
            foodList.add(food.getFoodName());
        }
        return foodList;
    }
}
