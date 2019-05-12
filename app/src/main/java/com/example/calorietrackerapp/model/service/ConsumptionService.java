package com.example.calorietrackerapp.model.service;

import com.example.calorietrackerapp.model.dao.DAOImpl;
import com.example.calorietrackerapp.model.dao.IDAO;
import com.example.calorietrackerapp.model.entity.Consumption;

public class ConsumptionService {
    private IDAO dao = new DAOImpl();
    private String path;

    public void createConsumption(Consumption consumption) {
        path = "restws.consumption/";
        dao.createInstance(consumption, path);
    }
}
