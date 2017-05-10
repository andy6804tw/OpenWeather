package com.openweather.openweather.AirActuvuty;


import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private TextView tvPublishtime,tvStr,tvNormalsuggest,tvAllergysuggest,tvSiteName;
    private RelativeLayout PMrelativeLayout;

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
        tvStr=(TextView)view.findViewById(R.id.tvStr);
        tvNormalsuggest=(TextView)view.findViewById(R.id.tvHumidity);
        tvAllergysuggest=(TextView)view.findViewById(R.id.tvNormalsuggest);
        tvSiteName=(TextView)view.findViewById(R.id.tvSiteName);
        PMrelativeLayout=(RelativeLayout)view.findViewById(R.id.PMrelativeLayout);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final Cursor cl2 = mAccess2.getData("AIR", null, null);
        cl2.moveToFirst();
        if(cl2.getCount()!=0)
            init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(cl2.getCount()!=0) {
                    init();
                }else
                    onResume();
            }
        }, 3000);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相當於Fragment的onResume
            Cursor cl2 = mAccess2.getData("AIR", null, null);
            cl2.moveToFirst();
            if(cl2.getCount()!=0)
                init();
        }
    }
    public void init(){
        Cursor cl2 = mAccess2.getData("AIR", null, null);
        cl2.moveToFirst();
        Cursor cl3 = mAccess2.getData("PM25", null, null);
        cl3.moveToFirst();
        if(cl2.getShort(8)>=0&&cl2.getShort(8)<=11){
            mIndex=1;
            PMrelativeLayout.setBackgroundResource(R.drawable.round_box_pm1);
        }
        else if(cl2.getShort(8)>=12&&cl2.getShort(8)<=23){
            mIndex=2;
            PMrelativeLayout.setBackgroundResource(R.drawable.round_box_pm2);
        }
        else if(cl2.getShort(8)>=24&&cl2.getShort(8)<=35){
            mIndex=3;
            PMrelativeLayout.setBackgroundResource(R.drawable.round_box_pm3);
        }
        else if(cl2.getShort(8)>=36&&cl2.getShort(8)<=41){
            mIndex=4;
            PMrelativeLayout.setBackgroundResource(R.drawable.round_box_pm4);
        }
        else if(cl2.getShort(8)>=42&&cl2.getShort(8)<=47){
            mIndex=5;
            PMrelativeLayout.setBackgroundResource(R.drawable.round_box_pm5);
        }
        else if(cl2.getShort(8)>=48&&cl2.getShort(8)<=53){
            mIndex=6;
            PMrelativeLayout.setBackgroundResource(R.drawable.round_box_pm6);
        }
        else if(cl2.getShort(8)>=54&&cl2.getShort(8)<=58){
            mIndex=7;
            PMrelativeLayout.setBackgroundResource(R.drawable.round_box_pm7);
        }
        else if(cl2.getShort(8)>=59&&cl2.getShort(8)<=64){
            mIndex=8;
            PMrelativeLayout.setBackgroundResource(R.drawable.round_box_pm8);
        }
        else if(cl2.getShort(8)>=65&&cl2.getShort(8)<=70){
            mIndex=9;
            PMrelativeLayout.setBackgroundResource(R.drawable.round_box_pm9);
        }
        else if(cl2.getShort(8)>=71){
            mIndex=10;
            PMrelativeLayout.setBackgroundResource(R.drawable.round_box_pm10);
        }

        dashboardView.setPercent(mIndex*10);
        cl3.moveToPosition(mIndex-1);
        dashboardView.setUnit("指標等級");
        dashboardView.setText(cl2.getShort(8)+"μg/m3");
        tvSiteName.setText("測站: "+cl2.getString(2));
        tvPublishtime.setText("最後更新時間: "+cl2.getString(1));
        tvStr.setText(cl3.getString(1));
        tvNormalsuggest.setText(cl3.getString(3));
        tvAllergysuggest.setText(cl3.getString(4));
        //Toast.makeText(getContext(),cl3.getString(1)+" "+mIndex+" "+cl2.getShort(8),Toast.LENGTH_LONG).show();
    }
}
