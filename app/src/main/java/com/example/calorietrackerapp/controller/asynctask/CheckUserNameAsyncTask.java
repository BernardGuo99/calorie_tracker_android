package com.example.calorietrackerapp.controller.asynctask;

import android.os.AsyncTask;

import com.example.calorietrackerapp.model.service.UserService;

public class CheckUserNameAsyncTask extends AsyncTask<String, Void, Boolean> {
    UserService userService = new UserService();

    @Override
    protected Boolean doInBackground(String... params) {
        return userService.checkUserNameExistence(params[0]);
    }
}
