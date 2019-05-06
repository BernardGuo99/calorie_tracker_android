package com.example.calorietrackerapp.controller.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.calorietrackerapp.R;

public class DisplayHomeFragment extends Fragment {

    private View vHomepage;
    //private TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vHomepage = inflater.inflate(R.layout.fragment_main, container, false);
        TextView tv = vHomepage.findViewById(R.id.tv);
        tv.setText("cnm");
        return vHomepage;
    }
}
