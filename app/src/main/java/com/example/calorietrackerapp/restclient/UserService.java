package com.example.calorietrackerapp.restclient;

import com.example.calorietrackerapp.restclient.dao.DAOImpl;
import com.example.calorietrackerapp.restclient.dao.IDAO;
import com.example.calorietrackerapp.restclient.entity.AppUser;
import com.example.calorietrackerapp.restclient.entity.Credential;


public class UserService {
    private IDAO dao = new DAOImpl();
    private String path;
    public void createAppUser(AppUser appUser) {
        path = "restws.appuser/";
        dao.createInstance(appUser, path);
    }

    public void createCredential(Credential credential) {
        path = "restws.credential/";
        dao.createInstance(credential, path);
    }

    public Boolean checkEmailExistence(String email){
        return true;
    }


}
