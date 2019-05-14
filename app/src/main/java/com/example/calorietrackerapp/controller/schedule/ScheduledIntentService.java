package com.example.calorietrackerapp.controller.schedule;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.calorietrackerapp.model.entity.AppUser;
import com.example.calorietrackerapp.model.entity.Report;
import com.example.calorietrackerapp.model.room_database.Step;
import com.example.calorietrackerapp.model.room_database.StepDatabase;
import com.example.calorietrackerapp.model.service.ConsumptionService;
import com.example.calorietrackerapp.model.service.ReportService;
import com.example.calorietrackerapp.model.service.UserService;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScheduledIntentService extends IntentService {
    StepDatabase db = null;


    public ScheduledIntentService() {
        super("ScheduledIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        counter++;
//        Date currentTime = Calendar.getInstance().getTime();
//        String strTime = currentTime.toString();
//
//        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
//                "service", Context.MODE_PRIVATE);
//        SharedPreferences.Editor spEditor = sharedPreferences.edit();
//        spEditor.putString("service", " " + counter + " " + strTime);
//        spEditor.apply();
//
//        Log.i("message ", "The number of runs: " + counter + " times" + " " + strTime);
        db = Room.databaseBuilder(getApplicationContext(),
                StepDatabase.class, "StepDatabase")
                .fallbackToDestructiveMigration()
                .build();

//        System.out.println("((((((((((((((((((((((((((((((((((((((((((((*************************************");

        Log.i("message ", "Report created!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        GenerateDailyReportAsyncTask generateDailyReportAsyncTask = new GenerateDailyReportAsyncTask();
        generateDailyReportAsyncTask.execute();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private class GenerateDailyReportAsyncTask extends AsyncTask<Void, Void, Void> {

        ConsumptionService consumptionService = new ConsumptionService();
        UserService userService = new UserService();
        ReportService reportService = new ReportService();

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences sharedPref = getSharedPreferences("user_auth", Context.MODE_PRIVATE);
            SharedPreferences sharedPrefGoal = getSharedPreferences("user_goal", Context.MODE_PRIVATE);


//            String userId = sharedPref.getString("user_id", null);
//            String goal = sharedPrefGoal.getString(userId + "Goal", "");

            // Get out all users' goals regardless of the current logged-in user
            SharedPreferences.Editor spEditor = sharedPrefGoal.edit();
            Map<String, ?> allUsersGoals = sharedPrefGoal.getAll();
            Iterator iter = allUsersGoals.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = entry.getKey().toString();
                String goal = entry.getValue().toString();

                // Get this user's ID
                String singleUserId = key.substring(0, key.indexOf("Goal"));
                // Clear this user's daily goal
                spEditor.putString(singleUserId + "Goal", "");
                spEditor.apply();

                // Get this user's total steps
                List<Step> steps = db.stepDao().findByUserId(singleUserId);
                Integer totalSteps = 0;
                for (Step step : steps) {
                    totalSteps += step.getStepCount();
                }
                // Delete this user's all steps today
                db.stepDao().deleteByUserId(singleUserId);
                double consumed = Double.parseDouble(consumptionService.dailyCaloriesConsumed(singleUserId, new Date()));
                double burnedAtRest = Double.parseDouble(userService.getCalorieBurnedAtRest(singleUserId));
                double totalBurnedBySteps = Double.parseDouble(userService.getUserCaloriePerStep(singleUserId)) * totalSteps;
                double totalBurnedNumber = burnedAtRest + totalBurnedBySteps;

                Report report = new Report(UUID.randomUUID().toString());
                report.setUserId(new AppUser(singleUserId));
                if (goal.length() != 0) {
                    report.setCalorieGoal(Double.parseDouble(goal));
                } else {
                    report.setCalorieGoal(0.0);
                }


                report.setCaloriesBurned(totalBurnedNumber);
                report.setCaloriesConsumed(consumed);
                report.setReportDate(new Date());
                report.setStepsTaken(totalSteps);

                reportService.createReport(report);
            }
            return null;
        }
    }
}