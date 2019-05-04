package com.example.calorietrackerapp.restclient.dao;

import java.net.HttpURLConnection;

public interface IDAO {
    HttpURLConnection getConnection(String path);
    void createInstance(Object object, String path);
}
