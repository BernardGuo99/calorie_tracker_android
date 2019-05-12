package com.example.calorietrackerapp.model.entity;

public class Food {
    private String foodId;
    private String foodName;
    private String category;
    private Double calorieAmount;
    private String serviceUnit;
    private Double fat;

    public Food(String foodId) {
        this.foodId = foodId;
    }

    public Food() {
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getCalorieAmount() {
        return calorieAmount;
    }

    public void setCalorieAmount(Double calorieAmount) {
        this.calorieAmount = calorieAmount;
    }

    public String getServiceUnit() {
        return serviceUnit;
    }

    public void setServiceUnit(String serviceUnit) {
        this.serviceUnit = serviceUnit;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }
}
