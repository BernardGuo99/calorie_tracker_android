package com.example.calorietrackerapp.controller.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.controller.activity.AddNewFoodActivity;
import com.example.calorietrackerapp.controller.activity.AuthUserActivity;
import com.example.calorietrackerapp.controller.activity.LoginActivity;
import com.example.calorietrackerapp.controller.activity.MainActivity;
import com.example.calorietrackerapp.controller.activity.PopFoodAmountActivity;
import com.example.calorietrackerapp.controller.asynctask.QueryFoodByCategoryAsyncTask;
import com.example.calorietrackerapp.controller.asynctask.QueryFoodByNameAsynctask;
import com.example.calorietrackerapp.controller.asynctask.TextSearchAsyncTask;
import com.example.calorietrackerapp.restclient.entity.Food;
import com.example.calorietrackerapp.restclient.google_custom_search.ImageSearch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DailyDietFragment extends Fragment {
    private View vMyDailyDiet;
    private Spinner foodCategorySpinner;
    private Spinner itemsSpinner;
    public static ImageView img;
    private TextView imageTextView;
    private TextView fatTextView;
    private TextView servingTextView;
    private TextView foodDetailsTextView;
    private Button addToDailyButton;
    private Button addFoodButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMyDailyDiet = inflater.inflate(R.layout.daily_diet_fragment, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("My Daily Diet");

        foodCategorySpinner = vMyDailyDiet.findViewById(R.id.spinner_food_category);

        List<String> categoryList = new ArrayList<>();
        categoryList.add("Select a Food Category");
        categoryList.add("Fruit");
        categoryList.add("Meat");
        categoryList.add("Vegetable");
        categoryList.add("Bread");
        categoryList.add("Drink");
        categoryList.add("Seafood");
        final ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryList);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodCategorySpinner.setAdapter(categorySpinnerAdapter);
        itemsSpinner = vMyDailyDiet.findViewById(R.id.spinner_food_items);
        img = vMyDailyDiet.findViewById(R.id.imageFood);
        imageTextView = vMyDailyDiet.findViewById(R.id.foodImageViewText);
        fatTextView = vMyDailyDiet.findViewById(R.id.tv_fat);
        servingTextView = vMyDailyDiet.findViewById(R.id.tv_serving);
        foodDetailsTextView = vMyDailyDiet.findViewById(R.id.tv_food_details);
        addToDailyButton = vMyDailyDiet.findViewById(R.id.b_addToDaily);
        addFoodButton = vMyDailyDiet.findViewById(R.id.b_addFood);

        addToDailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopFoodAmountActivity.class);
                intent.putExtra("Food", itemsSpinner.getSelectedItem().toString());
                startActivity(intent);

            }
        });


        foodCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                img.setImageResource(R.mipmap.food_image_default);
                foodDetailsTextView.setText("Select Your Food Now");
                String categorySelected = foodCategorySpinner.getSelectedItem().toString();
                TextView selected = (TextView) parent.getChildAt(0);
                selected.setTypeface(Typeface.DEFAULT_BOLD);
                selected.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                selected.setPadding(0, 0, 0, 0);
                if (categorySelected.equals("Select a Food Category")) {
                    itemsSpinner.setVisibility(View.GONE);
                } else {
                    itemsSpinner.setVisibility(View.VISIBLE);
                    List<String> foodList = new ArrayList<>();
                    try {
                        foodList = new QueryFoodByCategoryAsyncTask().execute(categorySelected).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (foodList.size() > 1) {
                        final ArrayAdapter<String> foodSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, foodList);
                        foodSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        itemsSpinner.setAdapter(foodSpinnerAdapter);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        itemsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categorySelected = foodCategorySpinner.getSelectedItem().toString();
                TextView selected = (TextView) parent.getChildAt(0);
                selected.setTypeface(Typeface.DEFAULT_BOLD);
                selected.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                selected.setPadding(0, 0, 0, 0);


                String foodSelected = itemsSpinner.getSelectedItem().toString();
                String searchString = categorySelected + ": " + foodSelected;
                if (!searchString.contains("Select Your Food")) {
                    foodDetailsTextView.setVisibility(View.VISIBLE);
                    fatTextView.setVisibility(View.VISIBLE);
                    servingTextView.setVisibility(View.VISIBLE);
                    addToDailyButton.setVisibility(View.VISIBLE);
                    addFoodButton.setVisibility(View.GONE);

                    String foodDetailsResult = "";
                    try {
                        foodDetailsResult = new TextSearchAsyncTask().execute(foodSelected).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    foodDetailsTextView.setText(foodDetailsResult);

                    try {
                        Food foodItem = new QueryFoodByNameAsynctask().execute(foodSelected).get();
                        imageTextView.setText(foodSelected + "        " + foodItem.getCalorieAmount() + " Cal");

                        SpannableStringBuilder ssbFat = new SpannableStringBuilder("Fat  " + foodItem.getFat() + "g");
                        ssbFat.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        fatTextView.setText(ssbFat);
                        SpannableStringBuilder ssbServing = new SpannableStringBuilder("Serving  " + foodItem.getServiceUnit());
                        ssbServing.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        servingTextView.setText(ssbServing);

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        String wordEncoding = URLEncoder.encode(searchString, "utf-8").replaceAll("\\+", "%20");
                        ImageSearch.activity = getActivity();
                        new ImageSearch().execute(wordEncoding);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    img.setImageResource(R.mipmap.food_image_default);
                    foodDetailsTextView.setVisibility(View.GONE);
                    fatTextView.setVisibility(View.GONE);
                    servingTextView.setVisibility(View.GONE);
                    addToDailyButton.setVisibility(View.GONE);
                    addFoodButton.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNewFoodActivity.class);
                startActivity(intent);
            }
        });


        return vMyDailyDiet;
    }
}
