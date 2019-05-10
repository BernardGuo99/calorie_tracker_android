package com.example.calorietrackerapp.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.controller.activity.MainActivity;
import com.example.calorietrackerapp.controller.activity.PopGoalActivity;
import com.example.calorietrackerapp.controller.asynctask.FindCurrentUserAsynctask;
import com.example.calorietrackerapp.controller.my_interface.InterfaceForResult;

public class DisplayHomeFragment extends Fragment implements InterfaceForResult {

    private View vHomepage;
    private LinearLayout goalLayout;
    private TextView goalValueText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vHomepage = inflater.inflate(R.layout.fragment_main, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Calorie Tracker");
        TextView tv = vHomepage.findViewById(R.id.tv);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("user_auth", Context.MODE_PRIVATE);
        String userId = sharedPref.getString("user_id", null);
        FindCurrentUserAsynctask findCurrentUserAsynctask = new FindCurrentUserAsynctask(this);
        findCurrentUserAsynctask.execute(userId);


        TextClock textClock = vHomepage.findViewById(R.id.textClock);
        textClock.setFormat12Hour("HH:mm:ss MMM d, yyyy");

        goalValueText = vHomepage.findViewById(R.id.goal_value);
        String setGoal = sharedPref.getString(userId + "Goal", "");
        if (setGoal.length() != 0) {
            goalValueText.setText(setGoal + " Cal");
        }


        goalLayout = vHomepage.findViewById(R.id.set_goal);
        goalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopGoalActivity.class);
                startActivity(intent);
            }
        });
        return vHomepage;
    }

    @Override
    public void done(Object result) {
        TextView tv = vHomepage.findViewById(R.id.tv);
        tv.setText("Welcome to Calorie Tracker, " + result.toString() + "!");
    }
}
