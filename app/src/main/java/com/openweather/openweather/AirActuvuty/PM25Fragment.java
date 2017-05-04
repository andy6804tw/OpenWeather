package com.openweather.openweather.AirActuvuty;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anderson.dashboardview.view.DashboardView;
import com.openweather.openweather.DataBase.DBAccessEnvironment;
import com.openweather.openweather.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PM25Fragment extends Fragment {

    private DashboardView dashboardView;
    private DBAccessEnvironment mAccess2;
    private int mIndex=0;
    private TextView tvPublishtime;

    public PM25Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pm25, container, false);
        dashboardView = (DashboardView) view.findViewById(R.id.dashboardView);
        dashboardView.setStartColor(getResources().getColor(R.color.GREEN));
        dashboardView.setEndColor(getResources().getColor(R.color.RED));
        mAccess2= new DBAccessEnvironment(getContext(), "Environment", null, 1);

        tvPublishtime=(TextView)view.findViewById(R.id.tvPublishtime);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Cursor cl2 = mAccess2.getData("AIR", null, null);
        cl2.moveToFirst();
        Cursor cl3 = mAccess2.getData("PM25", null, null);
        cl3.moveToFirst();
        if(cl2.getShort(8)>=0&&cl2.getShort(8)<=11)
            mIndex=1;
        else if(cl2.getShort(8)>=12&&cl2.getShort(8)<=23)
            mIndex=2;
        else if(cl2.getShort(8)>=24&&cl2.getShort(8)<=35)
            mIndex=3;
        else if(cl2.getShort(8)>=36&&cl2.getShort(8)<=41)
            mIndex=4;
        else if(cl2.getShort(8)>=42&&cl2.getShort(8)<=47)
            mIndex=5;
        else if(cl2.getShort(8)>=48&&cl2.getShort(8)<=53)
            mIndex=6;
        else if(cl2.getShort(8)>=54&&cl2.getShort(8)<=58)
            mIndex=7;
        else if(cl2.getShort(8)>=59&&cl2.getShort(8)<=64)
            mIndex=8;
        else if(cl2.getShort(8)>=65&&cl2.getShort(8)<=70)
            mIndex=9;
        else if(cl2.getShort(8)>=71)
            mIndex=10;
        dashboardView.setPercent(mIndex*10);
        cl3.moveToPosition(mIndex);

        dashboardView.setUnit(cl3.getString(1));
        tvPublishtime.setText(cl2.getString(1));
        Toast.makeText(getContext(),cl3.getString(1)+" "+mIndex+" "+cl2.getShort(8),Toast.LENGTH_LONG).show();
    }
}
