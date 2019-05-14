package com.example.calorietrackerapp.model.service;

import com.example.calorietrackerapp.model.dao.DAOImpl;
import com.example.calorietrackerapp.model.dao.IDAO;
import com.example.calorietrackerapp.model.entity.Food;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class FoodService {
    private IDAO dao = new DAOImpl();
    private String path;

    public void createFood(Food food) {
        path = "restws.food/";
        dao.createInstance(food, path);
    }

    public boolean checkFoodExistence(String foodName) {
        path = "restws.food/";
        String foods = dao.find(path);
        Gson gson = new Gson();
        Food[] arr = gson.fromJson(foods, Food[].class);
        for (Food food : arr) {
            if (food.getFoodName().equals(foodName)) {
                return true;
            }
        }
        return false;
    }


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

    public Food getFoodByName(String foodName) {
        try {
            path = "restws.food/findByFoodName/" + URLEncoder.encode(foodName, "utf-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(path);
        String items = dao.find(path);
        List<Food> foodList = new ArrayList<>();
        Gson gson = new Gson();
        Food[] arr = gson.fromJson(items, Food[].class);
        for (Food food : arr) {
            foodList.add(food);
        }
        return foodList.get(0);
    }
}
