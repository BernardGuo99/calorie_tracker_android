package com.example.calorietrackerapp.controller.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.controller.asynctask.UsdaFoodListSearchAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class AddNewFoodActivity extends AppCompatActivity {

    EditText searchFoodEditText;
    Button searchButton;
    TextView tv;


    List<HashMap<String, String>> unitListArray;
    SimpleAdapter myListAdapter;
    ListView unitList;
    String[] colHEAD = new String[]{"FOODITEM"};
    int[] dataCell = new int[]{R.id.food_item};
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food);
        searchFoodEditText = findViewById(R.id.search_food);
        searchButton = findViewById(R.id.b_search_food);
        tv = findViewById(R.id.cnm);


        unitList = this.findViewById(R.id.food_list);
        unitListArray = new ArrayList<HashMap<String, String>>();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unitListArray.clear();
                String searchString = searchFoodEditText.getText().toString();
                try {
                    Map<String, String> cnm = new UsdaFoodListSearchAsyncTask().execute(searchString).get();
                    String hehe = "";
                    Iterator iter = cnm.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String key = entry.getKey().toString();
                        String val = entry.getValue().toString();

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("FOODITEM", key);
                        unitListArray.add(map);
                        hehe += key + ": " + val + "\n";
                    }

                    myListAdapter = new SimpleAdapter(AddNewFoodActivity.this, unitListArray, R.layout.food_list_view, colHEAD, dataCell);
                    unitList.setAdapter(myListAdapter);

                    //tv.setText(hehe);
                    //System.out.println(cnm);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
                unitListArray.clear();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // do your actual work here

                        if (searchFoodEditText.getText().toString().trim().length() != 0) {


                            String searchString = searchFoodEditText.getText().toString();
                            try {
                                Map<String, String> cnm = new UsdaFoodListSearchAsyncTask().execute(searchString).get();
                                String hehe = "";
                                Iterator iter = cnm.entrySet().iterator();
                                while (iter.hasNext()) {
                                    Map.Entry entry = (Map.Entry) iter.next();
                                    String key = entry.getKey().toString();
                                    String val = entry.getValue().toString();

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("FOODITEM", key);
                                    unitListArray.add(map);
                                    hehe += key + ": " + val + "\n";
                                }

                                myListAdapter = new SimpleAdapter(AddNewFoodActivity.this, unitListArray, R.layout.food_list_view, colHEAD, dataCell);
                                unitList.setAdapter(myListAdapter);

                                //tv.setText(hehe);
                                //System.out.println(cnm);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            unitList.setAdapter(null);
                        }
                    }
                }, 600);


            }
        });

    }
}
