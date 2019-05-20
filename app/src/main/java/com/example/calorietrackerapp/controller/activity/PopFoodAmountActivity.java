package com.example.calorietrackerapp.controller.activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.controller.asynctask.CreateConsumptionAsyncTask;
import com.example.calorietrackerapp.controller.fragment.DailyDietFragment;

public class PopFoodAmountActivity extends AppCompatActivity {

    private Button saveAmountButton;
    private EditText amountText;
    private TextInputLayout amountLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_food_amount);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.3));

        Context context = getApplicationContext();
        CharSequence text = "Added to Your Daily Diet!";
        int duration = Toast.LENGTH_SHORT;
        final Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);


        amountText = findViewById(R.id.edit_ate_amount);
        amountLayout = findViewById(R.id.inputLayoutAteAmount);
        saveAmountButton = findViewById(R.id.b_save_amount);

        saveAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String ateAmount = amountText.getText().toString();


                if (ateAmount.length() == 0) {
                    amountLayout.setError("Enter Amount");
                } else {
                    CreateConsumptionAsyncTask createConsumptionAsyncTask = new CreateConsumptionAsyncTask();

                    SharedPreferences sharedPref = getSharedPreferences("user_auth", Context.MODE_PRIVATE);
                    String userId = sharedPref.getString("user_id", "");

                    Bundle bundle = getIntent().getExtras();
                    String foodName = bundle.getString("Food");
                    createConsumptionAsyncTask.execute(userId, foodName, ateAmount);


//                    FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.content_frame, new DailyDietFragment()).commit();


                    Intent intent = new Intent(PopFoodAmountActivity.this, MainActivity.class);
                    intent.putExtra("From", "popFoodAmount");
                    startActivity(intent);
                    toast.show();
                }
            }
        });
    }
}
