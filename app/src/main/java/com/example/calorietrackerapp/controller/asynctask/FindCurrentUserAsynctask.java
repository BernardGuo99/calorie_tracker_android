package com.example.calorietrackerapp.controller.asynctask;


import android.os.AsyncTask;

import com.example.calorietrackerapp.controller.my_interface.InterfaceForResult;
import com.example.calorietrackerapp.restclient.service.UserService;

public class FindCurrentUserAsynctask extends AsyncTask<String, Void, String> {
    UserService userService = new UserService();
    private InterfaceForResult myInterface;

    public FindCurrentUserAsynctask(InterfaceForResult myInterface) {
        this.myInterface = myInterface;
    }

    @Override
    protected String doInBackground(String... params) {
        return userService.findFirstNameById(params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        myInterface.done(s);
    }
}
