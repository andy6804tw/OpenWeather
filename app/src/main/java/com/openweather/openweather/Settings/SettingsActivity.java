package com.openweather.openweather.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.openweather.openweather.R;
import com.xiaoyongit.settingview.SettingView;
import com.xiaoyongit.settingview.entity.SettingModel;
import com.xiaoyongit.settingview.entity.SettingViewItem;
import com.xiaoyongit.settingview.item.BasicItemViewH;
import com.xiaoyongit.settingview.item.SwitchItemView;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences settings;
    int mCurrentapiVersion = android.os.Build.VERSION.SDK_INT;
    private SettingView mSettingView1 = null;
    private SettingView mSettingView2 = null;

    private SettingModel mItemData = null;
    private SettingViewItem mItemViewData = null;
    private List<SettingViewItem> mListData = new ArrayList<SettingViewItem>();
    private Context mContext;
    private ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Fabric.with(this, new Crashlytics());

        settings=getSharedPreferences("Data",MODE_PRIVATE);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mContext = this;
        mSettingView1 = (SettingView) findViewById(R.id.ios_style_setting_view_01);
        mSettingView2 = (SettingView) findViewById(R.id.ios_style_setting_view_02);



        if(mCurrentapiVersion==16){
            mSettingView1.setOnSettingViewItemClickListener(new SettingView.onSettingViewItemClickListener() {

                @Override
                public void onItemClick(int index) {

                    //Toast.makeText(mContext, "第" + index + "項被點擊", Toast.LENGTH_SHORT).show();
                    if (index == 4) {
                        //mSettingView1.setItemSubTitle("中華電信", 5);
                        Toast.makeText(mContext,"Coming soon !",Toast.LENGTH_LONG).show();
                    }
                    else if(index==1){
                        if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals("")) {
                            mSettingView1.setItemSubTitle("°F", 2);
                            settings.edit().putString("Temperature", "°F").commit();
                        }
                        else {
                            mSettingView1.setItemSubTitle("°C",2);
                            settings.edit().putString("Temperature","°C").commit();
                        }
                    }
                    else if (index == 3) {
                        //mSettingView1.setItemSubTitle("移動",4);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSfj8hA229yeSQ_eRLpw1tjeUD0OqzXSWH1kBxuDZ55Lh_gS1A/viewform?usp=sf_link")));
                    }
                    else if (index == 2) {
                        //mSettingView1.setItemSubTitle("關閉",3);
                        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                    else if (index == 0) {
                            if(settings.getString("Clock","").equals("12hr")||settings.getString("Temperature","").equals("")) {
                                mSettingView1.setItemSubTitle("24hr", 1);
                                settings.edit().putString("Clock", "24hr").commit();
                            }
                            else {
                                mSettingView1.setItemSubTitle("12hr",1);
                                settings.edit().putString("Clock","12hr").commit();
                            }
                    }
                }
            });

        }else{
            mSettingView1.setOnSettingViewItemClickListener(new SettingView.onSettingViewItemClickListener() {

                @Override
                public void onItemClick(int index) {

                    //Toast.makeText(mContext, "第" + index + "項被點擊", Toast.LENGTH_SHORT).show();
                    if (index == 4) {
                        //mSettingView1.setItemSubTitle("中華電信", 5);
                        Toast.makeText(mContext,"Coming soon !",Toast.LENGTH_LONG).show();
                    }
                    else if(index==1){
                        if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals("")) {
                            mSettingView1.setItemSubTitle("°F", 2);
                            settings.edit().putString("Temperature", "°F").commit();
                        }
                        else {
                            mSettingView1.setItemSubTitle("°C",2);
                            settings.edit().putString("Temperature","°C").commit();
                        }
                    }
                    else if (index == 3) {
                        //mSettingView1.setItemSubTitle("移動",4);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSfj8hA229yeSQ_eRLpw1tjeUD0OqzXSWH1kBxuDZ55Lh_gS1A/viewform?usp=sf_link")));
                    }
                    else if (index == 2) {
                        //mSettingView1.setItemSubTitle("關閉",3);
                        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                }
            });
            mSettingView1.setOnSettingViewItemSwitchListener(new SettingView.onSettingViewItemSwitchListener() {

                @Override
                public void onSwitchChanged(int index, boolean isChecked) {
                    if (isChecked) {
                        settings.edit().putString("Clock","24hr").commit();
                    } else {
                        settings.edit().putString("Clock","12hr").commit();
                    }
                }
            });
        }
        /* ==========================SettingView2========================== */
        mSettingView2.setOnSettingViewItemClickListener(new SettingView.onSettingViewItemClickListener() {
            @Override
            public void onItemClick(int i) {
                Toast.makeText(mContext,"Coming soon !",Toast.LENGTH_LONG).show();
            }
        });


        initView(mContext);
    }

    private void initView(Context context) {
		/* ==========================SettingView1========================== */
        if (mCurrentapiVersion == 16){
            mItemViewData = new SettingViewItem();
            mItemData = new SettingModel();
            mItemData.setTitle("24小時制");
            mItemData.setDrawable(getResources().getDrawable(R.mipmap.setting_clock));
            if(settings.getString("Clock","").equals("24hr")||settings.getString("Clock","").equals(""))
                mItemData.setSubTitle("24hr");
            else
                mItemData.setSubTitle("12hr");
            mItemViewData.setData(mItemData);
            mItemViewData.setItemView(new BasicItemViewH(context));
            mListData.add(mItemViewData);
        }else{
            mItemViewData = new SettingViewItem();
            mItemData = new SettingModel();
            mItemData.setTitle("24小時制");
            mItemData.setDrawable(getResources().getDrawable(R.mipmap.setting_clock));
            if(settings.getString("Clock","").equals("24hr")||settings.getString("Clock","").equals(""))
                mItemData.setChecked(true);
            else
                mItemData.setChecked(false);

            mItemViewData.setData(mItemData);
            mItemViewData.setItemView(new SwitchItemView(context));
            mListData.add(mItemViewData);
        }

        mItemViewData = new SettingViewItem();
        mItemData = new SettingModel();
        mItemData.setTitle("溫度");
        mItemData.setDrawable(getResources().getDrawable(R.mipmap.setting_temp));
        if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals(""))
            mItemData.setSubTitle("°C");
        else
            mItemData.setSubTitle("°F");

        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new BasicItemViewH(context));
        mListData.add(mItemViewData);

        mItemViewData = new SettingViewItem();
        mItemData = new SettingModel();
        mItemData.setTitle("GPS");
        //mItemData.setSubTitle("開啟");

        mItemData.setDrawable(getResources().getDrawable(R.mipmap.setting_gps));

        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new BasicItemViewH(context));
        mListData.add(mItemViewData);

        mItemViewData = new SettingViewItem();
        mItemData = new SettingModel();
        mItemData.setTitle("建議與回饋");
        mItemData.setSubTitle(" ");
        mItemData.setDrawable(getResources().getDrawable(R.mipmap.setting_write));

        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new BasicItemViewH(context));
        mListData.add(mItemViewData);

        mItemViewData = new SettingViewItem();
        mItemData = new SettingModel();
        mItemData.setTitle("更換背景");
        //mItemData.setSubTitle("遠傳電信");
        mItemData.setDrawable(getResources().getDrawable(R.mipmap.setting_img));

        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new BasicItemViewH(context));
        mListData.add(mItemViewData);

        mSettingView1.setAdapter(mListData);
		/* ==========================SettingView1========================== */

		/* ==========================SettingView2========================== */
        mListData.clear();
        mItemViewData = new SettingViewItem();
        mItemData = new SettingModel();
        mItemData.setTitle("通知中心");
        mItemData.setDrawable(getResources().getDrawable(R.mipmap.icon10));

        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new BasicItemViewH(context));
        mListData.add(mItemViewData);

        mItemViewData = new SettingViewItem();
        mItemData = new SettingModel();
        mItemData.setTitle("控制中心");
        mItemData.setDrawable(getResources().getDrawable(R.mipmap.icon10));

        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new BasicItemViewH(context));
        mListData.add(mItemViewData);

        mItemViewData = new SettingViewItem();
        mItemData = new SettingModel();
        mItemData.setTitle("勿擾模式");
        //ContextCompat.getDrawable(context,R.mipmap.icon09)
        mItemData.setDrawable(getResources().getDrawable(R.mipmap.icon09));

        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new BasicItemViewH(context));
        mListData.add(mItemViewData);

        mSettingView2.setAdapter(mListData);
		/* ==========================SettingView2========================== */
    }
}