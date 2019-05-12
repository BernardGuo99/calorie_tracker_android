package com.example.calorietrackerapp.controller.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.controller.activity.MainActivity;
import com.example.calorietrackerapp.controller.activity.PopAddNewFoodActivity;
import com.example.calorietrackerapp.controller.activity.PopFoodAmountActivity;
import com.example.calorietrackerapp.controller.asynctask.QueryFoodByCategoryAsyncTask;
import com.example.calorietrackerapp.controller.asynctask.QueryFoodByNameAsynctask;
import com.example.calorietrackerapp.controller.asynctask.TextSearchAsyncTask;
import com.example.calorietrackerapp.controller.asynctask.UsdaFoodListSearchAsyncTask;
import com.example.calorietrackerapp.controller.asynctask.UsdaFoodNutritionAsyncTask;
import com.example.calorietrackerapp.model.entity.Food;
import com.example.calorietrackerapp.model.google_custom_search.ImageSearch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class DailyDietFragment extends Fragment {
    private View vMyDailyDiet;
    private Spinner foodCategorySpinner;
    private Spinner itemsSpinner;
    public ImageView img;
    private TextView imageTextView;
    private TextView fatTextView;
    private TextView servingTextView;
    private TextView foodDetailsTextView;
    private Button addToDailyButton;
    private Button addFoodButton;
    private ViewFlipper viewFlipper;
    private ImageView newFoodImg;
    private TextView backTv1;
    private TextView backTv2;
    private TextView newFoodNameTextView;
    private TextView newFatTextView;
    private TextView newCalTextView;
    private TextView newProteinTextView;
    private TextView newServingTextView;
    private TextView orTextView;
    private Button addNewFoodButton;


    EditText searchFoodEditText;


    List<HashMap<String, String>> foodListArray;
    SimpleAdapter myListAdapter;
    ListView foodList;
    String[] colHEAD = new String[]{"FOODITEM"};
    int[] dataCell = new int[]{R.id.food_item};

    private Timer timer;


    private String newFoodName;
    private double newFoodFat;
    private double newFoodCalorie;
    private String newFoodServing;

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
        categoryList.add("Others");

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
        viewFlipper = vMyDailyDiet.findViewById(R.id.view_flipper_add_food);
        newFoodImg = vMyDailyDiet.findViewById(R.id.imageNewFood);
        backTv1 = vMyDailyDiet.findViewById(R.id.back_tv1);
        backTv2 = vMyDailyDiet.findViewById(R.id.back_tv2);
        newFoodNameTextView = vMyDailyDiet.findViewById(R.id.newFoodName);
        newCalTextView = vMyDailyDiet.findViewById(R.id.tv_new_cal);
        newFatTextView = vMyDailyDiet.findViewById(R.id.tv_new_fat);
        newProteinTextView = vMyDailyDiet.findViewById(R.id.tv_new_protein);
        newServingTextView = vMyDailyDiet.findViewById(R.id.tv_new_serving);
        orTextView = vMyDailyDiet.findViewById(R.id.tv_or);
        addNewFoodButton = vMyDailyDiet.findViewById(R.id.b_add_ate_new);


        foodListArray = new ArrayList<>();


        addToDailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopFoodAmountActivity.class);
                intent.putExtra("Food", itemsSpinner.getSelectedItem().toString());
                startActivity(intent);

            }
        });

        addNewFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopAddNewFoodActivity.class);
                intent.putExtra("Category", foodCategorySpinner.getSelectedItemPosition());
                intent.putExtra("Name", newFoodName);

                intent.putExtra("Calorie", newFoodCalorie);
                intent.putExtra("Fat", newFoodFat);
                intent.putExtra("Serving", newFoodServing);
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
                    addToDailyButton.setVisibility(View.GONE);
                    imageTextView.setText("Select Your Food Now");
                    foodDetailsTextView.setVisibility(View.GONE);
                    fatTextView.setVisibility(View.GONE);
                    servingTextView.setVisibility(View.GONE);
                    orTextView.setVisibility(View.GONE);
                    addFoodButton.setVisibility(View.GONE);

                } else {
                    itemsSpinner.setVisibility(View.VISIBLE);
                    List<String> spinnerFoodList = new ArrayList<>();
                    try {
                        spinnerFoodList = new QueryFoodByCategoryAsyncTask().execute(categorySelected).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final ArrayAdapter<String> foodSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerFoodList);
                    foodSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemsSpinner.setAdapter(foodSpinnerAdapter);

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
                    orTextView.setVisibility(View.GONE);

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
                        new ImageSearch(img).execute(wordEncoding);
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
                    orTextView.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();

            }
        });


        searchFoodEditText = vMyDailyDiet.findViewById(R.id.search_food);


        foodList = vMyDailyDiet.findViewById(R.id.food_list);
        viewFlipper = viewFlipper.findViewById(R.id.view_flipper_add_food);


        foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> foodItem = (HashMap<String, String>) foodList.getItemAtPosition(position);
                String foodNameString = foodItem.get("FOODITEM");
                if (!"NO RESULT".equals(foodNameString)) {
                    try {
                        String wordEncoding = URLEncoder.encode(foodItem.get("FOODITEM"), "utf-8").replaceAll("\\+", "%20");
                        ImageSearch.activity = getActivity();
                        new ImageSearch(newFoodImg).execute(wordEncoding);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    try {
                        Map<String, String> nutritionMap = new UsdaFoodNutritionAsyncTask().execute(foodItem.get("id")).get();


                        newFoodName = foodNameString;
                        newFoodCalorie = Double.parseDouble(nutritionMap.get("Energy"));
                        newFoodFat = Double.parseDouble(nutritionMap.get("Fat"));
                        newFoodServing = nutritionMap.get("Serving");

                        SpannableStringBuilder ssbNewCal = new SpannableStringBuilder("Energy  " + newFoodCalorie + "Cal");
                        ssbNewCal.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        newCalTextView.setText(ssbNewCal);
                        SpannableStringBuilder ssbNewServing = new SpannableStringBuilder("Serving  " + nutritionMap.get("Serving"));
                        ssbNewServing.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        newServingTextView.setText(ssbNewServing);
                        SpannableStringBuilder ssbNewFat = new SpannableStringBuilder("Fat  " + nutritionMap.get("Fat") + "g");
                        ssbNewFat.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        newFatTextView.setText(ssbNewFat);
                        SpannableStringBuilder ssbNewProtein = new SpannableStringBuilder("Protein  " + nutritionMap.get("Protein") + "g");
                        ssbNewProtein.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        newProteinTextView.setText(ssbNewProtein);


                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    newFoodNameTextView.setText(foodNameString);

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(vMyDailyDiet.getWindowToken(), 0);
                    viewFlipper.showNext();
                }
            }
        });


        searchFoodEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                foodListArray.clear();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // Stuff that updates the UI
                                if (searchFoodEditText.getText().toString().trim().length() != 0) {
                                    foodList.setVisibility(View.VISIBLE);

                                    String searchString = searchFoodEditText.getText().toString();
                                    try {
                                        Map<String, String> foodItems = new UsdaFoodListSearchAsyncTask().execute(searchString).get();
                                        Iterator iter = foodItems.entrySet().iterator();
                                        while (iter.hasNext()) {
                                            Map.Entry entry = (Map.Entry) iter.next();
                                            String key = entry.getKey().toString();
                                            String val = entry.getValue().toString();

                                            HashMap<String, String> map = new HashMap<String, String>();
                                            map.put("FOODITEM", key);
                                            map.put("id", val);
                                            foodListArray.add(map);
                                        }
                                        if (foodListArray.size() == 0) {
                                            HashMap<String, String> map = new HashMap<String, String>();
                                            map.put("FOODITEM", "NO RESULT");
                                            foodListArray.add(map);
                                        }

                                        myListAdapter = new SimpleAdapter(getActivity(), foodListArray, R.layout.food_list_view, colHEAD, dataCell);
                                        foodList.setAdapter(myListAdapter);

                                        int count = foodList.getAdapter().getCount();
                                        if (count == 0) {
                                            foodList.setVisibility(View.GONE);
                                        }
                                        for (int i = 0; i < count; i++) {
                                            TextView foodName = (TextView) foodList.getAdapter().getView(i, null, foodList).findViewById(R.id.food_item);
                                            //foodName.setSelected(true);

                                            foodName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                                            foodName.setHorizontallyScrolling(true);
                                            foodName.setMarqueeRepeatLimit(-1);
                                            foodName.setFocusable(true);
                                            //foodName.setFocusableInTouchMode(true);
                                            //System.out.println(foodName.getText());
                                        }


                                        //tv.setText(hehe);
                                        //System.out.println(cnm);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    foodList.setAdapter(null);
                                    foodList.setVisibility(View.GONE);
                                }
                            }
                        });

                    }
                }, 800);

            }
        });

        backTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showPrevious();
            }
        });

        backTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showPrevious();
            }
        });


        return vMyDailyDiet;
    }


}
