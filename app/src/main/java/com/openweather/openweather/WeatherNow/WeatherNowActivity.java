package com.openweather.openweather.WeatherNow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.goka.blurredgridmenu.GridMenu;
import com.goka.blurredgridmenu.GridMenuFragment;
import com.openweather.openweather.DataBase.DBAccessWeather;
import com.openweather.openweather.ExitApplication;
import com.openweather.openweather.LoadingSplash.GPSTracker;
import com.openweather.openweather.Main2Activity;
import com.openweather.openweather.MainActivity;
import com.openweather.openweather.Pm25Activity;
import com.openweather.openweather.R;
import com.openweather.openweather.Settings.SettingsActivity;
import com.openweather.openweather.View.SunBabyLoadingView;
import com.qiushui.blurredview.BlurredView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherNowActivity extends AppCompatActivity {

    private PullRefreshLayout layout;
    private GridMenuFragment mGridMenuFragment;
    /**
     * blurredview
     */
    private BlurredView mBlurredView;

    /**
     * RecyclerView
     */
    private RecyclerView mRecyclerView;

    private int mScrollerY=0;

    private int mAlpha=0;

    private long temptime = 0;//計算退出秒數

    private TextView tvTime,tvCity;
    GPSTracker mGps;
    DBAccessWeather mAccess;
    double latitude,longtitude;

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
                init_GPS();
                init_Weather();
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setRefreshing(false);
                        Toast.makeText(WeatherNowActivity.this,"Yes",Toast.LENGTH_LONG).show();
                        Cursor cl1 = mAccess.getData("Location", null, null);
                        cl1.moveToFirst();
                        Cursor cl6 = mAccess.getData("Condition", null, null);
                        cl6.moveToFirst();
                        //取得系統時間 Fri, 10 Mar 2017 03:23 PM CST
                        String str[]=cl6.getString(7).split(" "),time[]=str[4].split(":");
                        String hour=time[0],minute=time[1];
                        /*if(str[5].equals("PM")){
                            hour=Integer.parseInt(time[0])+12+"";
                        }
                        tvTime.setText(hour+":"+minute+" "+str[6]);*/
                        tvTime.setText(str[4]+" "+str[5]+" "+str[6]);
                        tvCity.setText(cl1.getString(2));
                        mRecyclerView.setAdapter(new WeatherNowRVA(WeatherNowActivity.this));
                        mAlpha=0;
                        mScrollerY=0;
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
        mGridMenuFragment = GridMenuFragment.newInstance(R.mipmap.tainan2);

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
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                if(position==3) {
                    Intent intent = new Intent(WeatherNowActivity.this, Pm25Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(position==7) {
                    Intent intent = new Intent(WeatherNowActivity.this, SettingsActivity.class);
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
    private void init_Weather() {
        ///**撈取資料END**///
        ///**撈取天氣資料START**///
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(SELECT%20woeid%20FROM%20geo.places%20WHERE%20text%3D\"("+latitude+","+longtitude+")\")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            //風 wind
                            String chill = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getString("chill");
                            String direction = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getString("direction");
                            String speed = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getString("speed");
                            //大氣 Atmosphere
                            String humidity = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("atmosphere").getString("humidity");
                            String pressure = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("atmosphere").getString("pressure");
                            String rising = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("atmosphere").getString("rising");
                            String visibility = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("atmosphere").getString("visibility");
                            //天文 Astronomy
                            String sunrise = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("astronomy").getString("sunrise");
                            String sunset = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("astronomy").getString("sunset");
                            //狀態 Condition
                            String date = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("date");
                            String day = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("day");
                            String high = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("high");
                            String low = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("low");
                            String temp = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("temp");
                            String code = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("code");
                            String pushTime = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getString("pubDate");
                            String publish_time = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getString("lastBuildDate");
                            Cursor c = mAccess.getData("Condition", null, null);
                            c.moveToFirst();

                                Toast.makeText(WeatherNowActivity.this,pushTime,Toast.LENGTH_LONG).show();
                                //寫入 Wind資料表
                                mAccess.update("1", Double.parseDouble(chill), direction, speed,null);
                                //寫入 Atmosphere資料表
                                mAccess.update("1", humidity, pressure,visibility ,rising,null);
                                //寫入 Astronomy資料表
                                mAccess.update("1", sunrise, sunset,null);
                                //寫入 Condition資料表
                                mAccess.update("1", date, day, Double.parseDouble(high), Double.parseDouble(low), Double.parseDouble(temp), Integer.parseInt(code),publish_time,null);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WeatherNowActivity.this, "無法連接網路!", Toast.LENGTH_SHORT).show();
                SunBabyLoadingView.str = "正載入歷史資料...";
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void init_GPS() {
        mGps = new GPSTracker(this);
        if (mGps.canGetLocation && mGps.getLatitude() != (0.0) && mGps.getLongtitude() != (0.0)) {
            latitude = mGps.getLatitude();
            longtitude = mGps.getLongtitude();
            //Toast.makeText(getApplicationContext(), "Your Location is->\nLat: " + latitude + "\nLong: " + longtitude, Toast.LENGTH_LONG).show();
            ///**撈取時間資料START**///
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longtitude + "&language=zh-TW&sensor=true&key=AIzaSyDHA4UDKuJ_hZafj8Xn6m3mMzOsQnbTZ_w&lafhdfhfdhfdhrh";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //mTextView.setText("Response is: "+ response.substring(0,500));
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                int count = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").length();
                                String country = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 2).getString("long_name");
                                String city = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 3).getString("short_name");
                                String district = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 4).getString("short_name");
                                String village = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 5).getString("short_name");
                                String str5 = jsonObject.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                                //寫入Location資料表
                                Cursor c = mAccess.getData("Location", null, null);
                                c.moveToFirst();
                               if(c.getDouble(5)!=latitude||c.getDouble(6)!=longtitude){
                                    Toast.makeText(WeatherNowActivity.this,"更新位置"+" "+latitude+" "+longtitude,Toast.LENGTH_SHORT).show();
                                    mAccess.update("1",country,city,district,village,Double.toString(latitude),Double.toString(longtitude),null);
                                }
                                Toast.makeText(WeatherNowActivity.this,"位置->\nLat: " + latitude + "\nLong: " + longtitude,Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }else{
            final LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
                alertDialog.setTitle("Gps is settings");
                alertDialog.setMessage("Gps is not enabled.");
                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        }
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
                + cl6.getString(4) + " " + cl6.getString(5)+ " " + cl6.getString(6)+ " " + cl6.getString(7));

        Cursor cl7 = mAccess.getData("Code", null, null);
        cl7.moveToFirst();
        cl7.moveToNext();
        cl7.moveToNext();
        Log.e("Data Code",cl7.getString(0) + " " +cl7.getString(1)+" "+cl7.getString(2));



        //取得系統時間 Fri, 10 Mar 2017 03:23 PM CST
        String str[]=cl6.getString(7).split(" "),time[]=str[4].split(":");
        String hour=time[0],minute=time[1];
        if(str[5].equals("PM")){
            hour=Integer.parseInt(time[0])+12+"";
        }

        //tvTime.setText(hour+":"+minute+" "+str[6]);
        tvTime.setText(str[4]+" "+str[5]+" "+str[6]);
        tvCity.setText(cl1.getString(2));
    }
}