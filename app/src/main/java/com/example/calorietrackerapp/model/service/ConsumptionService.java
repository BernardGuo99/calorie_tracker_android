package com.example.calorietrackerapp.model.service;

import com.example.calorietrackerapp.model.dao.DAOImpl;
import com.example.calorietrackerapp.model.dao.IDAO;
import com.example.calorietrackerapp.model.entity.Consumption;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsumptionService {
    private IDAO dao = new DAOImpl();
    private String path;

    public void createConsumption(Consumption consumption) {
        path = "restws.consumption/";
        dao.createInstance(consumption, path);
    }

    public String dailyCaloriesConsumed(String userId, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        path = "restws.consumption/dailyCaloriesConsumed/" + userId + "/" + dateString;
        return dao.find(path);
    }
}
