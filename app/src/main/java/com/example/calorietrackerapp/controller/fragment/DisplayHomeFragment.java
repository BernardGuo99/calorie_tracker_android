package com.example.calorietrackerapp.controller.fragment;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.example.calorietrackerapp.model.room_database.Step;
import com.example.calorietrackerapp.model.room_database.StepDatabase;
import com.example.calorietrackerapp.model.service.ConsumptionService;
import com.example.calorietrackerapp.model.service.FoodService;
import com.example.calorietrackerapp.model.service.UserService;

import java.util.Date;
import java.util.List;

public class DisplayHomeFragment extends Fragment {

    private View vHomepage;
    private LinearLayout goalLayout;
    private TextView goalValueText;
    private TextView calorieLeftTextView;
    private TextView calorieConsumedTextView;
    private TextView calorieBurnedTextView;
    private TextView calorieNetTextView;
    private TextView stepsTextView;
    StepDatabase db = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vHomepage = inflater.inflate(R.layout.fragment_main, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Calorie Tracker");
        TextView tv = vHomepage.findViewById(R.id.tv);
        calorieLeftTextView = vHomepage.findViewById(R.id.tv_calorie_left);
        calorieConsumedTextView = vHomepage.findViewById(R.id.tv_calorie_consumed);
        calorieBurnedTextView = vHomepage.findViewById(R.id.tv_calorie_burned);
        calorieNetTextView = vHomepage.findViewById(R.id.tv_calorie_net);
        stepsTextView = vHomepage.findViewById(R.id.tv_daily_total_steps);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                StepDatabase.class, "StepDatabase")
                .fallbackToDestructiveMigration()
                .build();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("user_auth", Context.MODE_PRIVATE);
        String userName = sharedPref.getString("username", "");
        String userId = sharedPref.getString("user_id", null);

        tv.setText("Welcome to Calorie Tracker, " + userName + "!");


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

        ReadTotalStepsAsyncTask readTotalStepsAsyncTask = new ReadTotalStepsAsyncTask();
        readTotalStepsAsyncTask.execute(userId);

        SetCalorieNumberAsyncTask setCalorieNumberAsyncTask = new SetCalorieNumberAsyncTask();
        setCalorieNumberAsyncTask.execute(userId);

        return vHomepage;
    }

    private class ReadTotalStepsAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            List<Step> steps = db.stepDao().findByUserId(params[0]);
            Integer totalSteps = 0;
            for (Step step : steps) {
                totalSteps += step.getStepCount();
            }
            return totalSteps;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer < 2) {
                stepsTextView.setText(integer + " Step");
            } else {
                stepsTextView.setText(integer + " Steps");
            }
        }
    }

    private class SetCalorieNumberAsyncTask extends AsyncTask<String, Void, String[]> {

        ConsumptionService consumptionService = new ConsumptionService();
        UserService userService = new UserService();

        @Override
        protected String[] doInBackground(String... params) {
            List<Step> steps = db.stepDao().findByUserId(params[0]);
            Integer totalSteps = 0;
            for (Step step : steps) {
                totalSteps += step.getStepCount();
            }


            long consumedNumber = Math.round(Double.parseDouble(consumptionService.dailyCaloriesConsumed(params[0], new Date())));
            double burnedAtRest = Double.parseDouble(userService.getCalorieBurnedAtRest(params[0]));
            double totalBurnedBySteps = Double.parseDouble(userService.getUserCaloriePerStep(params[0])) * totalSteps;
            long totalBurnedNumber = Math.round(burnedAtRest + totalBurnedBySteps);
            long netNumber = consumedNumber - totalBurnedNumber;

            String left = "";

            SharedPreferences sharedPref = getActivity().getSharedPreferences("user_auth", Context.MODE_PRIVATE);
            String goal = sharedPref.getString(params[0] + "Goal", "");

            if (goal.length() != 0) {
                left = String.valueOf(Long.parseLong(goal) - netNumber);
            } else {
                left = "Set Your Calorie Goal At the Bottom";
            }
            return new String[]{String.valueOf(consumedNumber),
                    String.valueOf(totalBurnedNumber),
                    String.valueOf(netNumber),
                    left,
                    String.valueOf(totalSteps)};

        }

        @Override
        protected void onPostExecute(String[] s) {
            calorieConsumedTextView.setText(s[0]);
            calorieBurnedTextView.setText(s[1]);
            calorieNetTextView.setText(s[2]);
            if (!s[3].contains("-")) {
                calorieLeftTextView.setText(s[3] + " calories left");
            } else {
                String reverse = s[3].substring(1);
                calorieLeftTextView.setText(reverse + " calories over");
            }


            if (Integer.parseInt(s[4]) < 2) {
                stepsTextView.setText(s[4] + " Step");
            } else {
                stepsTextView.setText(s[4] + " Steps");
            }
        }
    }


}
