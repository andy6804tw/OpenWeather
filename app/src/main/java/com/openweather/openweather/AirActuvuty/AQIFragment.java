package com.openweather.openweather.AirActuvuty;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anderson.dashboardview.view.DashboardView;
import com.openweather.openweather.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AQIFragment extends Fragment {

    DashboardView dashboardView;

    public AQIFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_aqi, container, false);
        dashboardView = (DashboardView) view.findViewById(R.id.dashboardView);
        dashboardView.setStartColor(getResources().getColor(R.color.GREEN));
        dashboardView.setEndColor(getResources().getColor(R.color.RED));
        dashboardView.setPercent(80);
        dashboardView.setUnit(" 普通");


        return view;
    }

}
