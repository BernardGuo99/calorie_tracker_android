package com.example.calorietrackerapp.controller.asynctask;

import android.os.AsyncTask;

import com.example.calorietrackerapp.restclient.service.UserService;

public class CheckEmailExistAsyncTask extends AsyncTask<String, Void, Boolean> {
    UserService userService = new UserService();

    @Override
    protected Boolean doInBackground(String... params) {
        return userService.checkEmailExistence(params[0]);
    }

}
