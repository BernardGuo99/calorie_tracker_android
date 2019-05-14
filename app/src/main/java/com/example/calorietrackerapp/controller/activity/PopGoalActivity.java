package com.example.calorietrackerapp.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.calorietrackerapp.R;

public class PopGoalActivity extends AppCompatActivity {

    private Button saveButton;
    private EditText goalText;
    private TextInputLayout goalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_goal);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.3));


        goalText = findViewById(R.id.edit_calorie);
        goalLayout = findViewById(R.id.inputLayoutEditCalorie);
        SharedPreferences sharedPref = getSharedPreferences("user_auth", Context.MODE_PRIVATE);


        SharedPreferences sharedPrefGoal = getSharedPreferences("user_goal", Context.MODE_PRIVATE);

        String userId = sharedPref.getString("user_id", "");
        String currentValue = sharedPrefGoal.getString(userId + "Goal", "");
        goalText.setText(currentValue);

//        goalText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (goalText.length() == 0) {
//                    saveButton.setEnabled(false);
//                } else {
//                    saveButton.setEnabled(true);
//                }
//            }
//        });


        saveButton = findViewById(R.id.b_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String calorieGoal = goalText.getText().toString();
                if (calorieGoal.length() == 0) {
                    goalLayout.setError("Enter today's calorie goal");
                    //saveButton.setEnabled(false);
                } else {

                    SharedPreferences sharedPref = getSharedPreferences("user_auth", Context.MODE_PRIVATE);
                    SharedPreferences sharedPrefGoal = getSharedPreferences("user_goal", Context.MODE_PRIVATE);
                    String userId = sharedPref.getString("user_id", "");
                    SharedPreferences.Editor spEditor = sharedPrefGoal.edit();
                    spEditor.putString(userId + "Goal", calorieGoal);
                    spEditor.apply();
                    Intent intent = new Intent(PopGoalActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
}
