package com.example.calorietrackerapp.controller.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.controller.asynctask.QueryFoodByCategoryAsyncTask;
import com.example.calorietrackerapp.restclient.entity.Food;
import com.example.calorietrackerapp.restclient.google_image_search.CustomSearch;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMyDailyDiet = inflater.inflate(R.layout.daily_diet_fragment, container, false);

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

        foodCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categorySelected = foodCategorySpinner.getSelectedItem().toString();
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
                String searchFood = itemsSpinner.getSelectedItem().toString();
                if (!searchFood.equals("Select Your Food")){
                    try {
                        String wordEncoding = URLEncoder.encode(searchFood, "UTF-8");
                        CustomSearch.activity = getActivity();
                        new CustomSearch().execute(wordEncoding);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return vMyDailyDiet;
    }
}
