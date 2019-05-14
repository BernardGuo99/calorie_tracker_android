package com.example.calorietrackerapp.controller.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.controller.activity.MainActivity;
import com.example.calorietrackerapp.controller.activity.PopEditStepsActivity;
import com.example.calorietrackerapp.model.room_database.Step;
import com.example.calorietrackerapp.model.room_database.StepDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class StepsFragment extends Fragment {

    private View vMySteps;
    private EditText enterStepsEditText;
    private TextInputLayout enterStepsLayout;
    private Button addStepsButton;
    private TextView stepsTotalTextView;


    List<HashMap<String, String>> stepsListArray;
    SimpleAdapter myListAdapter;
    ListView stepsList;
    String[] colHEAD = new String[]{"StepsCount", "CreatedTime"};
    int[] dataCell = new int[]{R.id.tv_steps_count, R.id.tv_created_time};
    StepDatabase db = null;

    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMySteps = inflater.inflate(R.layout.fragment_steps, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("My Steps");

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                StepDatabase.class, "StepDatabase")
                .fallbackToDestructiveMigration()
                .build();

        enterStepsEditText = vMySteps.findViewById(R.id.et_enter_steps);
        enterStepsLayout = vMySteps.findViewById(R.id.inputLayoutStepsCount);
        addStepsButton = vMySteps.findViewById(R.id.b_add_steps);
        stepsTotalTextView = vMySteps.findViewById(R.id.tv_total_steps);


        stepsList = vMySteps.findViewById(R.id.steps_list);
        stepsListArray = new ArrayList<>();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("user_auth", Context.MODE_PRIVATE);
        userId = sharedPref.getString("user_id", null);

        ReadAllStepsAsyncTask readAllStepsAsyncTask = new ReadAllStepsAsyncTask();
        readAllStepsAsyncTask.execute(userId);


        addStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stepsEntered = enterStepsEditText.getText().toString();
                if (stepsEntered.length() == 0) {
                    enterStepsLayout.setError("Enter Your Steps");
                } else {


                    AddStepsAsyncTask addStepsAsyncTask = new AddStepsAsyncTask();
                    addStepsAsyncTask.execute(userId, stepsEntered);

                    ReadAllStepsAsyncTask readAllStepsAsyncTask = new ReadAllStepsAsyncTask();
                    readAllStepsAsyncTask.execute(userId);

                    enterStepsEditText.setText("");
                    enterStepsEditText.clearFocus();


                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
                }
            }
        });

        stepsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> stepsItem = (HashMap<String, String>) stepsList.getItemAtPosition(position);
                if (!stepsItem.get("StepsCount").equals("STEPS")) {
                    Intent intent = new Intent(getActivity(), PopEditStepsActivity.class);
                    intent.putExtra("StepsCount", stepsItem.get("StepsCount"));
                    intent.putExtra("StepsId", stepsItem.get("StepsId"));
                    intent.putExtra("UserId", stepsItem.get("UserId"));
                    intent.putExtra("CreateTime", stepsItem.get("CreateTime"));
                    startActivity(intent);
                }


            }
        });


        return vMySteps;
    }


    private class AddStepsAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm MMMM dd, yyyy");
            String dateString = sdf.format(date);
            Step step = new Step(params[0], Integer.parseInt(params[1]), dateString);
            db.stepDao().insert(step);
            return null;
        }
    }

    private class ReadAllStepsAsyncTask extends AsyncTask<String, Void, List<Step>> {

        @Override
        protected List<Step> doInBackground(String... params) {
            return db.stepDao().findByUserId(params[0]);
        }

        @Override
        protected void onPostExecute(List<Step> steps) {
            stepsListArray.clear();
            Integer totalSteps = 0;

            HashMap<String, String> map0 = new HashMap<>();
            map0.put("StepsCount", "STEPS");
            map0.put("CreatedTime", "ADDED TIME");
            addMap(map0);
            for (Step step : steps) {
                HashMap<String, String> map = new HashMap<>();
                totalSteps += step.getStepCount();

                map.put("StepsCount", String.valueOf(step.getStepCount()));
                map.put("CreatedTime", step.getCreatedTime());
                map.put("StepsId", String.valueOf(step.getUid()));
                map.put("UserId", step.getUserId());
                map.put("CreateTime", step.getCreatedTime());
                addMap(map);
            }
            stepsTotalTextView.setText("Steps Today: " + totalSteps);
        }
    }

    protected void addMap(HashMap map) {
        stepsListArray.add(map);
        myListAdapter = new SimpleAdapter(getActivity(), stepsListArray, R.layout.steps_list_view, colHEAD, dataCell);
        stepsList.setAdapter(myListAdapter);
    }
}
