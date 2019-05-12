package com.example.calorietrackerapp.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.controller.asynctask.CheckFoodExistenceAsyncTask;
import com.example.calorietrackerapp.controller.asynctask.CreateConsumptionAsyncTask;
import com.example.calorietrackerapp.controller.asynctask.CreateFoodAsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PopAddNewFoodActivity extends AppCompatActivity {

    private Spinner newFoodCategorySpinner;
    private EditText newAmountEditText;
    private Button addAmountButton;
    private TextInputLayout newAmountLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_add_new_food);

        Context context = getApplicationContext();
        CharSequence text = "Added to Your Daily Diet!";
        int duration = Toast.LENGTH_SHORT;
        final Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);


        newFoodCategorySpinner = findViewById(R.id.spinner_new_food_category);
        newAmountEditText = findViewById(R.id.edit_new_ate_amount);
        addAmountButton = findViewById(R.id.b_new_save_amount);
        newAmountLayout = findViewById(R.id.inputLayoutNewAteAmount);

        List<String> categoryList = new ArrayList<>();
        categoryList.add("Fruit");
        categoryList.add("Meat");
        categoryList.add("Vegetable");
        categoryList.add("Bread");
        categoryList.add("Drink");
        categoryList.add("Seafood");
        categoryList.add("Other");
        final ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newFoodCategorySpinner.setAdapter(categorySpinnerAdapter);

        final Bundle bundle = getIntent().getExtras();
        int categoryPosition = bundle.getInt("Category") - 1;


        newFoodCategorySpinner.setSelection(categoryPosition);

        addAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newAmountEditText.length() == 0) {
                    newAmountLayout.setError("Enter Amount");
                } else {
                    String foodName = bundle.getString("Name");

                    boolean pass = true;
                    try {
                        pass = new CheckFoodExistenceAsyncTask().execute(foodName).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (pass) {
                        String foodServing = bundle.getString("Serving");
                        double foodCalorie = bundle.getDouble("Calorie");
                        int foodFat = bundle.getInt("Fat");
                        CreateFoodAsyncTask createFoodAsyncTask = new CreateFoodAsyncTask();
                        createFoodAsyncTask.execute(foodName,
                                newFoodCategorySpinner.getSelectedItem().toString(),
                                foodServing,
                                String.valueOf(foodFat), String.valueOf(foodCalorie));
                    }


                    SharedPreferences sharedPref = getSharedPreferences("user_auth", Context.MODE_PRIVATE);
                    String userId = sharedPref.getString("user_id", "");

                    String foodQuantity = newAmountEditText.getText().toString();

                    CreateConsumptionAsyncTask createConsumptionAsyncTask = new CreateConsumptionAsyncTask();
                    createConsumptionAsyncTask.execute(userId, foodName, foodQuantity);

                    Intent intent = new Intent(PopAddNewFoodActivity.this, MainActivity.class);
                    intent.putExtra("From", "popFoodAmount");
                    startActivity(intent);
                    toast.show();
                }
            }
        });


    }
}
