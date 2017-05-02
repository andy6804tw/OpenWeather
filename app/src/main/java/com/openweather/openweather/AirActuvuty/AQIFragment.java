package com.openweather.openweather.AirActuvuty;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anderson.dashboardview.view.DashboardView;
import com.openweather.openweather.DataBase.DBAccessEnvironment;
import com.openweather.openweather.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AQIFragment extends Fragment {

    DashboardView dashboardView;
    DBAccessEnvironment mAccess2;

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
        dashboardView.setPercent(20);
        dashboardView.setUnit(" 普通");
        mAccess2= new DBAccessEnvironment(getContext(), "Environment", null, 1);
        Cursor cl2 = mAccess2.getData("AIR", null, null);
        cl2.moveToFirst();
        /*Log.e("Data Air", cl2.getString(0) + " " + cl2.getString(1) + " "
                + cl2.getString(2) + " " + cl2.getString(3) + " "
                + cl2.getString(4) + " " + cl2.getString(5) + " "
                + cl2.getString(6) + " " + cl2.getString(7) + " "
                + cl2.getString(8) + " " + cl2.getString(9) + " "
                + cl2.getString(10) + " "+ cl2.getString(11));*/

        return view;
    }

}
