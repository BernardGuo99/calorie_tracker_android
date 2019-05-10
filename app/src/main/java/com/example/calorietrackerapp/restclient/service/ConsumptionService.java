package com.example.calorietrackerapp.restclient.service;

import com.example.calorietrackerapp.restclient.dao.DAOImpl;
import com.example.calorietrackerapp.restclient.dao.IDAO;
import com.example.calorietrackerapp.restclient.entity.Consumption;

public class ConsumptionService {
    private IDAO dao = new DAOImpl();
    private String path;

    public void createConsumption(Consumption consumption) {
        path = "restws.consumption/";
        dao.createInstance(consumption, path);
    }
}
