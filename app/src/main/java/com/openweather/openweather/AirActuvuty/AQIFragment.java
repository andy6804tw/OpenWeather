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

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.openweather.openweather.DataBase.DBAccessEnvironment;
import com.openweather.openweather.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AQIFragment extends Fragment {

    private DBAccessEnvironment mAccess2;
    private ArcProgress arc_progress;
    private int mIndex=0;
    private RelativeLayout AQIrelativeLayout;
    private TextView tvStr,tvDes,tvNormalsuggest,tvSiteName,tvPublishtime;

    public AQIFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_aqi, container, false);
        mAccess2= new DBAccessEnvironment(getContext(), "Environment", null, 1);
        arc_progress=(ArcProgress)view.findViewById(R.id.arc_progress);
        AQIrelativeLayout=(RelativeLayout)view.findViewById(R.id.AQIrelativeLayout);
        tvStr=(TextView)view.findViewById(R.id.tvStr);
        tvDes=(TextView)view.findViewById(R.id.tvHumidity);
        tvNormalsuggest=(TextView)view.findViewById(R.id.tvNormalsuggest);
        tvSiteName=(TextView)view.findViewById(R.id.tvSiteName);
        tvPublishtime=(TextView)view.findViewById(R.id.tvPublishtime);
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

    public void init(){
        Cursor cl2 = mAccess2.getData("AIR", null, null);
        cl2.moveToFirst();
        Cursor cl3 = mAccess2.getData("AQI", null, null);
        cl3.moveToFirst();

        if(cl2.getShort(2)>=0&&cl2.getShort(2)<=50){
            mIndex=1;
            AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air1);
        }
        else if(cl2.getShort(2)>=51&&cl2.getShort(2)<=100){
            mIndex=2;
            AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air2);
        }
        else if(cl2.getShort(2)>=101&&cl2.getShort(2)<=150){
            mIndex=3;
            AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air3);
        }
        else if(cl2.getShort(2)>=151&&cl2.getShort(2)<=200){
            mIndex=4;
            AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air4);
        }
        else if(cl2.getShort(2)>=201&&cl2.getShort(2)<=300){
            mIndex=5;
            AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air5);
        }
        else if(cl2.getShort(2)>=301&&cl2.getShort(2)<=500){
            mIndex=6;
            AQIrelativeLayout.setBackgroundResource(R.drawable.round_box_air6);
        }
        arc_progress.setProgress(cl2.getShort(3));
        cl3.moveToPosition(mIndex-1);
        tvStr.setText(cl3.getString(1));
        tvDes.setText(cl3.getString(3));
        tvNormalsuggest.setText(cl3.getString(2));
        tvSiteName.setText("測站: "+cl2.getString(2));
        tvPublishtime.setText("最後更新時間: "+cl2.getString(1));
    }
}
