package com.example.calorietrackerapp.model.dao;

import java.net.HttpURLConnection;

public interface IDAO {
    HttpURLConnection getConnection(String path);

    void createInstance(Object object, String path);

    String find(String path);
}
