package com.example.calorietrackerapp.controller.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.controller.activity.MainActivity;
import com.example.calorietrackerapp.model.service.ReportService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportFragment extends Fragment {

    private View vReport;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListenerStart;
    private DatePickerDialog.OnDateSetListener mDateSetListenerEnd;

    private TextView singleDateTextView;
    private TextView noPieTextView;
    private ViewFlipper flipper;
    private Button changeChartButton;
    private TextView startDateTextView;
    private TextView endDateTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vReport = inflater.inflate(R.layout.fragment_report, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("My Report");
        noPieTextView = vReport.findViewById(R.id.tv_no_pie);
        singleDateTextView = vReport.findViewById(R.id.tv_single_date);
        singleDateTextView.setText("Today");

        flipper = vReport.findViewById(R.id.view_flipper_chart);
        changeChartButton = vReport.findViewById(R.id.b_change_graph);
        startDateTextView = vReport.findViewById(R.id.tv_start_date);
        endDateTextView = vReport.findViewById(R.id.tv_end_date);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");


        SetPieChartAsyncTask setPieChartAsyncTask = new SetPieChartAsyncTask();
        setPieChartAsyncTask.execute(new Date());

        SetBarChartAsyncTask setBarChartAsyncTask = new SetBarChartAsyncTask();
        setBarChartAsyncTask.execute();


        singleDateTextView.setOnClickListener(new View.OnClickListener() {
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
                dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
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
                if (cal.equals(Calendar.getInstance())) {
                    singleDateTextView.setText("Today");
                } else {
                    singleDateTextView.setText(sdf.format(cal.getTime()));
                }
                SetPieChartAsyncTask setPieChartAsyncTask = new SetPieChartAsyncTask();
                setPieChartAsyncTask.execute(cal.getTime());
            }
        };

        endDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerEnd,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                dialog.show();
            }
        });
        mDateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDateTextView.setText("To: " + sdf.format(cal.getTime()));

                SetPieChartAsyncTask setPieChartAsyncTask = new SetPieChartAsyncTask();
                setPieChartAsyncTask.execute(cal.getTime());
            }
        };

        startDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
                try {
                    String endDate = endDateTextView.getText().toString();

                    Date startDate = sdf.parse(endDate.substring(endDate.indexOf(":") + 2));
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(startDate);
                    cal.add(Calendar.DATE, -1);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dialog = new DatePickerDialog(
                            getActivity(),
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mDateSetListenerStart,
                            year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cal.add(Calendar.DATE, 1);
                    dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                    dialog.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        mDateSetListenerStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDateTextView.setText("From: " + sdf.format(cal.getTime()));

                SetPieChartAsyncTask setPieChartAsyncTask = new SetPieChartAsyncTask();
                setPieChartAsyncTask.execute(cal.getTime());
            }
        };


        changeChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flipper.getDisplayedChild() == 0) {
                    flipper.showNext();
                    changeChartButton.setText("CHANGE TO DAILY REPORT");
                } else {
                    flipper.showPrevious();
                    changeChartButton.setText("CHANGE TO PERIODIC REPORT");
                }
            }
        });


        return vReport;
    }


    private class SetPieChartAsyncTask extends AsyncTask<Date, Void, float[]> {

        ReportService reportService = new ReportService();

        @Override
        protected float[] doInBackground(Date... dates) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("user_auth", Context.MODE_PRIVATE);
            String userId = sharedPref.getString("user_id", null);



            try {
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                List<String[]> hehehe = reportService.getReportsByDateRange(userId, sdf1.parse("2019-05-12"), sdf1.parse("2019-05-17"));
            System.out.println("UUUUUUUU%%%%%%%%%%%%%%%^&&&&&&&&&&&&&&&&&&(");
            for (String[] s : hehehe){
                Log.i("cnm",Arrays.toString(s));
               System.out.println("1"+Arrays.toString(s));
            }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return reportService.findCaloriesByUserAndDate(userId, dates[0]);
        }

        @Override
        protected void onPostExecute(float[] floats) {
            PieChart chart = vReport.findViewById(R.id.piechart);
            chart.getDescription().setEnabled(false);
            chart.setExtraOffsets(30, 10, 30, 10);
            Legend l = chart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setEnabled(true);
            l.setYOffset(20f);
            l.setTextSize(16f);

            chart.setTransparentCircleColor(Color.WHITE);
            chart.setTransparentCircleAlpha(110);


            if (floats != null) {
                chart.setVisibility(View.VISIBLE);
                noPieTextView.setVisibility(View.GONE);
                String[] name = new String[3];
                float[] data = new float[3];
                if (floats[2] >= 0) {
                    name = new String[]{"Consumed", "Burned", "Remaining"};

                    data = new float[]{floats[0], floats[1], floats[2]};
                } else {
                    name = new String[]{"Consumed", "Burned", "Deficit"};
                    data = new float[]{floats[0], floats[1], -floats[2]};
                }


                List<PieEntry> pieEntries = new ArrayList<>();
                for (int i = 0; i < data.length; i++) {
                    pieEntries.add(new PieEntry(data[i], name[i]));
                }

                PieDataSet dataSet = new PieDataSet(pieEntries, "");
                dataSet.setValueTextColor(Color.rgb(127, 78, 69));
                chart.setEntryLabelColor(Color.rgb(0, 0, 0));
                chart.setEntryLabelTextSize(14f);


                ArrayList<Integer> colors = new ArrayList<>();

                colors.add(Color.rgb(254, 247, 120));
                colors.add(Color.rgb(254, 156, 138));
                colors.add(Color.rgb(138, 187, 255));


                colors.add(ColorTemplate.getHoloBlue());

                dataSet.setColors(colors);


//                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                dataSet.setSliceSpace(3f);
                dataSet.setValueLinePart1OffsetPercentage(80.f);
                dataSet.setValueLinePart1Length(0.4f);
                dataSet.setValueLinePart2Length(0.4f);
                //dataSet.setUsingSliceColorAsValueLineColor(true);

                //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                dataSet.setValueTextSize(12f);


                PieData pieData = new PieData(dataSet);


                chart.setData(pieData);
                chart.animateY(1000);
                chart.invalidate();
            } else {
                chart.invalidate();
                chart.setVisibility(View.GONE);
                noPieTextView.setVisibility(View.VISIBLE);
            }
        }


    }


    private class SetBarChartAsyncTask extends AsyncTask<Date, Void, float[]> {

        @Override
        protected float[] doInBackground(Date... dates) {


            return new float[0];
        }


        @Override
        protected void onPostExecute(float[] floats) {
            BarChart chart = vReport.findViewById(R.id.barchart);
            chart.getDescription().setEnabled(false);

            chart.setPinchZoom(false);

            chart.setDrawBarShadow(false);

            chart.setDrawGridBackground(false);

            Legend l = chart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(true);
            l.setYOffset(0f);
            l.setXOffset(10f);
            l.setYEntrySpace(0f);
            l.setTextSize(8f);

            XAxis xAxis = chart.getXAxis();
            //xAxis.setTypeface(tfLight);
            xAxis.setGranularity(1f);
            xAxis.setCenterAxisLabels(true);


            YAxis leftAxis = chart.getAxisLeft();
            //leftAxis.setTypeface(tfLight);
            leftAxis.setValueFormatter(new LargeValueFormatter());
            leftAxis.setDrawGridLines(false);
            leftAxis.setSpaceTop(35f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            chart.getAxisRight().setEnabled(false);

            float groupSpace = 0.08f;
            float barSpace = 0.03f; // x4 DataSet
            float barWidth = 0.2f; // x4 DataSet
            // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

            // int groupCount = seekBarX.getProgress() + 1;
            int startYear = 1980;
            //int endYear = startYear + groupCount;

            //tvX.setText(String.format(Locale.ENGLISH, "%d-%d", startYear, endYear));
            // tvY.setText(String.valueOf(seekBarY.getProgress()));

            ArrayList<BarEntry> values1 = new ArrayList<>();
            ArrayList<BarEntry> values2 = new ArrayList<>();
            ArrayList<BarEntry> values3 = new ArrayList<>();
            ArrayList<BarEntry> values4 = new ArrayList<>();

            //float randomMultiplier = seekBarY.getProgress() * 100000f;

            for (int i = startYear; i < startYear + 10; i++) {
                values1.add(new BarEntry(i, (float) (Math.random() * 100)));
                values2.add(new BarEntry(i, (float) (Math.random() * 100)));
                values3.add(new BarEntry(i, (float) (Math.random() * 100)));
                values4.add(new BarEntry(i, (float) (Math.random() * 100)));
            }


            BarDataSet set1, set2, set3, set4;

            if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

                set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
                set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
                set3 = (BarDataSet) chart.getData().getDataSetByIndex(2);
                set4 = (BarDataSet) chart.getData().getDataSetByIndex(3);
                set1.setValues(values1);
                set2.setValues(values2);
                set3.setValues(values3);
                set4.setValues(values4);
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();

            } else {
                // create 4 DataSets
                set1 = new BarDataSet(values1, "Company A");
                set1.setColor(Color.rgb(104, 241, 175));
                set2 = new BarDataSet(values2, "Company B");
                set2.setColor(Color.rgb(164, 228, 251));
                set3 = new BarDataSet(values3, "Company C");
                set3.setColor(Color.rgb(242, 247, 158));
                set4 = new BarDataSet(values4, "Company D");
                set4.setColor(Color.rgb(255, 102, 0));

                BarData data = new BarData(set1, set2, set3, set4);
                data.setValueFormatter(new LargeValueFormatter());
                //data.setValueTypeface(tfLight);

                chart.setData(data);
            }

            // specify the width each bar should have
            chart.getBarData().setBarWidth(barWidth);

            // restrict the x-axis range
            chart.getXAxis().setAxisMinimum(startYear);

            // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
            //chart.getXAxis().setAxisMaximum(startYear + chart.getBarData().getGroupWidth(groupSpace, barSpace) * 10);
//            final String[] quarters = new String[]{"Q1", "Q2", "Q3", "Q4", "Q1", "Q2", "Q3", "Q4", "Q1", "Q2", "Q3","Q4", "Q1", "Q2", "Q3"};
//            IAxisValueFormatter formatter = new IAxisValueFormatter() {
//
//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
//                    return quarters[(int) value];
//                }
//
//
//            };
//
//
//            xAxis.setValueFormatter(formatter);
            chart.groupBars(startYear, groupSpace, barSpace);
            chart.invalidate();

        }
    }
}
