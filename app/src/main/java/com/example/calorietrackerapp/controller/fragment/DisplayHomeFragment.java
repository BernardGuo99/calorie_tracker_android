package com.example.calorietrackerapp.controller.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.controller.activity.MainActivity;
import com.example.calorietrackerapp.restclient.UserService;

public class DisplayHomeFragment extends Fragment {

    private View vHomepage;
    //private TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vHomepage = inflater.inflate(R.layout.fragment_main, container, false);
        TextView tv = vHomepage.findViewById(R.id.tv);
        tv.setText("cnm");
        FindCurrentUserAsynctask findCurrentUserAsynctask = new FindCurrentUserAsynctask();
        findCurrentUserAsynctask.execute();
        return vHomepage;
    }


    private class FindCurrentUserAsynctask extends AsyncTask<String, Void, String> {
        UserService userService = new UserService();

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("user_auth", Context.MODE_PRIVATE);
            String userId = sharedPref.getString("user_id", null);
            System.out.println(userId);
            return userService.findFirstNameById(userId);
        }

        @Override
        protected void onPostExecute(String s) {
            TextView tv = vHomepage.findViewById(R.id.tv);
            tv.setText(s);
        }
    }
}
