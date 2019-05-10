package com.example.calorietrackerapp.restclient.entity;

import java.util.Date;

public class Consumption {
    private String consumptionId;
    private Date consumptionDate;
    private Integer quantity;
    private AppUser userId;
    private Food foodId;

    public Consumption(String consumptionId) {
        this.consumptionId = consumptionId;
    }

    public Consumption() {
    }

    public String getConsumptionId() {
        return consumptionId;
    }

    public void setConsumptionId(String consumptionId) {
        this.consumptionId = consumptionId;
    }

    public Date getConsumptionDate() {
        return consumptionDate;
    }

    public void setConsumptionDate(Date consumptionDate) {
        this.consumptionDate = consumptionDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public AppUser getUserId() {
        return userId;
    }

    public void setUserId(AppUser userId) {
        this.userId = userId;
    }

    public Food getFoodId() {
        return foodId;
    }

    public void setFoodId(Food foodId) {
        this.foodId = foodId;
    }
}
