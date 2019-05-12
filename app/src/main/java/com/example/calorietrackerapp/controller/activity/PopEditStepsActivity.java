package com.example.calorietrackerapp.controller.activity;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.model.room_database.Step;
import com.example.calorietrackerapp.model.room_database.StepDatabase;

public class PopEditStepsActivity extends AppCompatActivity {

    private TextInputLayout editStepsLayout;
    private EditText editStepsEditText;
    private Button updateButton;
    private Button deleteButton;
    StepDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_edit_steps);


        editStepsLayout = findViewById(R.id.inputLayoutEditSteps);
        editStepsEditText = findViewById(R.id.edit_steps_count);
        updateButton = findViewById(R.id.b_edit_steps);
        deleteButton = findViewById(R.id.b_delete_steps);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.5));

        Bundle bundle = getIntent().getExtras();
        String stepsCount = bundle.getString("StepsCount");
        final String stepsId = bundle.getString("StepsId");
        final String userId = bundle.getString("UserId");
        String createTime = bundle.getString("CreateTime");

        editStepsEditText.setText(stepsCount);

        db = Room.databaseBuilder(getApplicationContext(),
                StepDatabase.class, "StepDatabase")
                .fallbackToDestructiveMigration()
                .build();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editStepsEditText.length() == 0) {
                    editStepsLayout.setError("Enter Steps");
                } else if (editStepsEditText.getText().toString().equals("0")) {
                    editStepsLayout.setError("Steps can not be 0");
                } else {
                    UpdateStepsAsyncTask updateStepsAsyncTask = new UpdateStepsAsyncTask();
                    updateStepsAsyncTask.execute(String.valueOf(stepsId), editStepsEditText.getText().toString(), userId);
                    Intent intent = new Intent(PopEditStepsActivity.this, MainActivity.class);
                    intent.putExtra("From", "popStepsAmount");
                    startActivity(intent);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteStepsAsyncTask deleteStepsAsyncTask = new DeleteStepsAsyncTask();
                deleteStepsAsyncTask.execute(String.valueOf(stepsId));
                Intent intent = new Intent(PopEditStepsActivity.this, MainActivity.class);
                intent.putExtra("From", "popStepsAmount");
                startActivity(intent);
            }
        });


    }


    private class UpdateStepsAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Step step = db.stepDao().findByID(Integer.parseInt(params[0]));
            step.setStepCount(Integer.parseInt(params[1]));
            step.setUserId(params[2]);
            db.stepDao().updateSteps(step);
            return null;
        }
    }

    private class DeleteStepsAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Step step = db.stepDao().findByID(Integer.parseInt(params[0]));
            db.stepDao().delete(step);
            return null;
        }
    }
}
