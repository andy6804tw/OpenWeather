package com.openweather.openweather.WeatherNow;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.goka.blurredgridmenu.GridMenu;
import com.goka.blurredgridmenu.GridMenuFragment;
import com.openweather.openweather.DataBase.DBAccessWeather;
import com.openweather.openweather.ExitApplication;
import com.openweather.openweather.Main2Activity;
import com.openweather.openweather.MainActivity;
import com.openweather.openweather.R;
import com.qiushui.blurredview.BlurredView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeatherNowActivity extends AppCompatActivity {

    private PullRefreshLayout layout;
    DBAccessWeather mAccess;
    private GridMenuFragment mGridMenuFragment;
    /**
     * blurredview
     */
    private BlurredView mBlurredView;

    /**
     * RecyclerView
     */
    private RecyclerView mRecyclerView;

    private int mScrollerY;

    private int mAlpha;

    private long temptime = 0;//計算退出秒數

    private TextView tvTime,tvCity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_now);
        ExitApplication.getInstance().addActivity(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tvTime=(TextView)findViewById(R.id.tvTime);
        tvCity=(TextView)findViewById(R.id.tvCity);


        mAccess = new DBAccessWeather(this, "weather", null, 1);
        menu_init();//menu初始化
        blurred_init();//背景初始化
        reflash();

    }

    private void reflash() {
        layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setRefreshing(false);
                        Calendar c = Calendar.getInstance();
                        //取得系統時間
                        String hour = c.get(Calendar.HOUR_OF_DAY)+"";
                        String minute = c.get(Calendar.MINUTE)+"";
                        if(hour.length()==1)
                            hour="0"+hour;
                        if(minute.length()==1)
                            minute="0"+minute;
                        tvTime.setText(hour+":"+minute+" CST");
                        mRecyclerView.setAdapter(new WeatherNowRVA(WeatherNowActivity.this));
                    }
                }, 3000);
            }
        });
    }

    /**背景霧化Blurred**/
    private void blurred_init(){
        mBlurredView = (BlurredView) findViewById(R.id.yahooweather_blurredview);
        mRecyclerView = (RecyclerView) findViewById(R.id.yahooweather_recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new WeatherNowRVA(this));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollerY += dy;
                if (Math.abs(mScrollerY) > 1000) {
                    mBlurredView.setBlurredTop(100);
                    mAlpha = 100;
                } else {
                    mBlurredView.setBlurredTop(mScrollerY / 10);
                    mAlpha = Math.abs(mScrollerY) / 10;
                }
                mBlurredView.setBlurredLevel(mAlpha);
            }
        });
    }

    /**選單Menu**/
    //menu初始化
    private void menu_init(){
        mGridMenuFragment = GridMenuFragment.newInstance(R.mipmap.bg_tainan2);

        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.main_frame, mGridMenuFragment);
                tx.addToBackStack(null);
                tx.commit();
            }
        });

        setupGridMenu();

        mGridMenuFragment.setOnClickMenuListener(new GridMenuFragment.OnClickMenuListener() {
            @Override
            public void onClickMenu(GridMenu gridMenu, int position) {
                Toast.makeText(WeatherNowActivity.this, "Title:" + gridMenu.getTitle() + ", Position:" + position,
                        Toast.LENGTH_SHORT).show();
                if(position==1) {
                    Intent intent = new Intent(WeatherNowActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(position==2) {
                    Intent intent = new Intent(WeatherNowActivity.this, Main2Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }
    private void setupGridMenu() {
        List<GridMenu> menus = new ArrayList<>();
        menus.add(new GridMenu("Home", R.drawable.home));
        menus.add(new GridMenu("Calendar", R.drawable.calendar));
        menus.add(new GridMenu("Overview", R.drawable.overview));
        menus.add(new GridMenu("Groups", R.drawable.groups));
        menus.add(new GridMenu("Lists", R.drawable.lists));
        menus.add(new GridMenu("Profile", R.drawable.profile));
        menus.add(new GridMenu("Timeline", R.drawable.timeline));
        menus.add(new GridMenu("Setting", R.drawable.settings));

        mGridMenuFragment.setupMenu(menus);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//手機按鈕事件
    {
        // TODO Auto-generated method stub
       if (1 == getSupportFragmentManager().getBackStackEntryCount()) {
            getSupportFragmentManager().popBackStack();
            return true;
        }else{
            if((keyCode == KeyEvent.KEYCODE_BACK)&&(event.getAction() == KeyEvent.ACTION_DOWN))
            {
                if(System.currentTimeMillis() - temptime >2000) // 2s內再次選擇back有效
                {
                    System.out.println(Toast.LENGTH_LONG);
                    Toast.makeText(this, "再按一次離開", Toast.LENGTH_LONG).show();
                    temptime = System.currentTimeMillis();
                }
                else {
                    ExitApplication.getInstance().exit();
                }

                return true;

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cl1 = mAccess.getData("Location", null, null);
        cl1.moveToFirst();
        Log.e("Data Location", cl1.getString(0) + " " + cl1.getString(1) + " "
                + cl1.getString(2) + " " + cl1.getString(3) + " "
                + cl1.getString(4) + " " + cl1.getDouble(5)+" "+cl1.getDouble(6));
        Cursor cl2 = mAccess.getData("Wind", null, null);
        cl2.moveToFirst();
        Log.e("Data Wind", cl2.getString(0) + " " + cl2.getString(1) + " "
                + cl2.getString(2) + " " + cl2.getString(3));
        Cursor cl3 = mAccess.getData("Atmosphere", null, null);
        cl3.moveToFirst();
        Log.e("Data Atmosphere",cl3.getString(0) + " " +  cl3.getString(1) + " "
                + cl3.getString(2) + " " + cl3.getString(3) + " " + cl3.getString(4) );

        Cursor cl5 = mAccess.getData("Astronomy", null, null);
        cl5.moveToFirst();
        Log.e("Data Astronomy", cl5.getString(1) + " " + cl5.getString(2));

        Cursor cl6 = mAccess.getData("Condition", null, null);
        cl6.moveToFirst();
        Log.e("Data Condition",cl6.getString(0) + " " +  cl6.getString(1) + " "
                + cl6.getString(2) + " " + cl6.getString(3) + " "
                + cl6.getString(4) + " " + cl6.getString(5)+ " " + cl6.getString(5));

        Cursor cl7 = mAccess.getData("Code", null, null);
        cl7.moveToFirst();
        cl7.moveToNext();
        cl7.moveToNext();
        Log.e("Data Code",cl7.getString(0) + " " +cl7.getString(1)+" "+cl7.getString(2));


        Calendar c = Calendar.getInstance();
        //取得系統時間
        String hour = c.get(Calendar.HOUR_OF_DAY)+"";
        String minute = c.get(Calendar.MINUTE)+"";
        if(hour.length()==1)
            hour="0"+hour;
        if(minute.length()==1)
            minute="0"+minute;
        tvTime.setText(hour+":"+minute+" CST");
        tvCity.setText(cl1.getString(2));
    }
}