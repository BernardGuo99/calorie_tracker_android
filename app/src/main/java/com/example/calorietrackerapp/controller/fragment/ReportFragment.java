package com.example.calorietrackerapp.controller.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.controller.activity.MainActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportFragment extends Fragment {

    private View vReport;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextInputLayout singleDateLayout;
    private EditText singleDateEditText;


    float data[] = {1f, 2f, 3f};
    String name[] = {"one", "two", "three"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vReport = inflater.inflate(R.layout.fragment_report, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("My Report");
        singleDateLayout = vReport.findViewById(R.id.inputLayout_single_date);
        singleDateEditText = vReport.findViewById(R.id.single_date);


        singleDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                singleDateEditText.setText(sdf.format(cal.getTime()));
            }
        };


        setPieChart();

        return vReport;
    }

    private void setPieChart() {

        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            pieEntries.add(new PieEntry(data[i], name[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "hehehe");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(dataSet);
        PieChart chart = vReport.findViewById(R.id.piechart);
        chart.setData(pieData);
        chart.animateY(1000);
        chart.invalidate();

    }
}
