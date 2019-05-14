package com.example.calorietrackerapp.model.service;

import com.example.calorietrackerapp.model.dao.DAOImpl;
import com.example.calorietrackerapp.model.dao.IDAO;
import com.example.calorietrackerapp.model.entity.Report;

public class ReportService {

    private IDAO dao = new DAOImpl();
    private String path;

    public void createReport(Report report) {
        path = "restws.report/";
        dao.createInstance(report, path);
    }

}
