package com.example.calorietrackerapp.restclient;

import com.example.calorietrackerapp.restclient.dao.DAOImpl;
import com.example.calorietrackerapp.restclient.dao.IDAO;
import com.example.calorietrackerapp.restclient.entity.AppUser;
import com.example.calorietrackerapp.restclient.entity.Credential;
import com.example.calorietrackerapp.utils.PasswordHash256;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


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

    public boolean checkEmailExistence(String email) {
        path = "restws.appuser";
        String users = dao.find(path);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        AppUser[] arr = gson.fromJson(users, AppUser[].class);
        for (AppUser user : arr) {
            if (email.equals(user.getEmail())) {
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println(user.getDateOfBirth());
                return false;
            }
        }
        return true;
    }

    public boolean checkUserNameExistence(String userName) {
        path = "restws.credential";
        String credentials = dao.find(path);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Credential[] arr = gson.fromJson(credentials, Credential[].class);
        for (Credential credential : arr) {
            if (userName.equals(credential.getUserName())) {
                return false;
            }
        }
        return true;
    }

    public boolean validateUser(String userName, String password) {
        path = "restws.credential/findByUserName/" + userName;
        String user = dao.find(path);
        String passwordHash = "";
        if (user.length() == 2) {
            return false;
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Credential[] arr = gson.fromJson(user, Credential[].class);
        for (Credential credential : arr) {
            passwordHash = credential.getPasswordHash();
        }
        if (!PasswordHash256.passWordHash(password).equals(passwordHash)) {
            return false;
        }
        return true;
    }

    public String getUserIdByUserName(String userName) {
        path = "restws.credential/findByUserName/" + userName;
        String user = dao.find(path);
        String userId = "";
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Credential[] arr = gson.fromJson(user, Credential[].class);
        for (Credential credential : arr) {
            userId = credential.getUserId().getUserId();
        }
        return userId;
    }

    public String findFirstNameById(String userId) {
        path = "restws.appuser/" + userId;
        String user = dao.find(path);
        String firstName = "";
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        AppUser arr = gson.fromJson(user, AppUser.class);
        firstName = arr.getFirstname();
        return firstName;
    }

}
