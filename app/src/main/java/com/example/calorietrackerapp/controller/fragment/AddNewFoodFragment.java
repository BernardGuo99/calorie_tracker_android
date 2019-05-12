package com.example.calorietrackerapp.controller.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.controller.asynctask.UsdaFoodListSearchAsyncTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class AddNewFoodFragment extends Fragment {
    private View vAddNewFood;
    EditText searchFoodEditText;
    private ViewFlipper viewFlipper;


    List<HashMap<String, String>> foodListArray;
    SimpleAdapter myListAdapter;
    ListView foodList;
    String[] colHEAD = new String[]{"FOODITEM"};
    int[] dataCell = new int[]{R.id.food_item};

    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vAddNewFood = inflater.inflate(R.layout.fragment_add_new_food, container, false);

        searchFoodEditText = vAddNewFood.findViewById(R.id.search_food);


        foodList = vAddNewFood.findViewById(R.id.food_list);
        viewFlipper = viewFlipper.findViewById(R.id.view_flipper_add_food);


        foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> foodItem = (HashMap<String, String>) foodList.getItemAtPosition(position);
                foodItem.get("FOODITEM");
                viewFlipper.showNext();

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
                                        Map<String, String> cnm = new UsdaFoodListSearchAsyncTask().execute(searchString).get();
                                        String hehe = "";
                                        Iterator iter = cnm.entrySet().iterator();
                                        while (iter.hasNext()) {
                                            Map.Entry entry = (Map.Entry) iter.next();
                                            String key = entry.getKey().toString();
                                            String val = entry.getValue().toString();

                                            HashMap<String, String> map = new HashMap<String, String>();
                                            map.put("FOODITEM", key);
                                            map.put("id", val);
                                            foodListArray.add(map);
                                            hehe += key + ": " + val + "\n";
                                        }

                                        myListAdapter = new SimpleAdapter(getActivity(), foodListArray, R.layout.food_list_view, colHEAD, dataCell);
                                        foodList.setAdapter(myListAdapter);

                                        int count = foodList.getAdapter().getCount();
                                        for (int i = 0; i < count; i++) {
                                            TextView foodName = (TextView) foodList.getAdapter().getView(i, null, foodList).findViewById(R.id.food_item);
                                            foodName.setSelected(true);
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
                }, 600);

            }
        });


        return vAddNewFood;
    }
}
