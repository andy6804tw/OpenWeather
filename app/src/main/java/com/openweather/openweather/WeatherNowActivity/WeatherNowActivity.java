package com.openweather.openweather.WeatherNowActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
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
import com.crashlytics.android.Crashlytics;
import com.goka.blurredgridmenu.GridMenu;
import com.goka.blurredgridmenu.GridMenuFragment;
import com.openweather.openweather.DataBase.DBAccessEnvironment;
import com.openweather.openweather.DataBase.DBAccessWeather;
import com.openweather.openweather.ExitApplication;
import com.openweather.openweather.LoadingSplash.GPSTracker;
import com.openweather.openweather.R;
import com.openweather.openweather.View.SunBabyLoadingView;
import com.qiushui.blurredview.BlurredView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class WeatherNowActivity extends AppCompatActivity {

    String mLanguage="en",mCity,mCountry,mDistrict,mVillage;
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

    public static int mScrollerY=0;

    public static int mAlpha=0;

    private long temptime = 0;//計算退出秒數
    private TextView tvTime,tvCity;
    GPSTracker mGps;
    DBAccessWeather mAccess;
    double latitude,longtitude;
    public static String str5;
    SharedPreferences settings;
    private Context mContext;
    private int mImgDay[]={R.mipmap.day01,R.mipmap.day02,R.mipmap.day03,R.mipmap.day04,R.mipmap.day05,R.mipmap.day06};
    private int mImgAfternoon[]={R.mipmap.afternoon01,R.mipmap.afternoon02,R.mipmap.afternoon03,R.mipmap.afternoon04,R.mipmap.afternoon05};
    private int mImgnight[]={R.mipmap.night01,R.mipmap.night02,R.mipmap.night03,R.mipmap.night04,R.mipmap.night05,R.mipmap.night06,R.mipmap.night07};
    private int mImgMidnight[]={R.mipmap.midnight01};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_now);
        Fabric.with(this, new Crashlytics());
        ExitApplication.getInstance().addActivity(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBlurredView = (BlurredView) findViewById(R.id.blurredview);
        tvTime=(TextView)findViewById(R.id.tvTime);
        tvCity=(TextView)findViewById(R.id.tvCity);
        settings=getSharedPreferences("Data",MODE_PRIVATE);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setAdapter(new WeatherNowRVA(WeatherNowActivity.this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAlpha=0;
        mScrollerY=0;
        mContext=getApplicationContext();
        mAccess = new DBAccessWeather(this, "weather", null, 1);

        blurred_init();//背景初始化

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
                        initInfo();
                    }
                }, 3000);
            }
        });
    }

    /**背景霧化Blurred**/
    private void blurred_init(){

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
                //Log.d("Scroll",mScrollerY+" "+mAlpha);
            }
        });

    }

    /**選單Menu**/
    //menu初始化
    private void menu_init(){

        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.main_frame, mGridMenuFragment);
                tx.addToBackStack(null);
                tx.commit();*/
                Toast.makeText(WeatherNowActivity.this,"Coming soon",Toast.LENGTH_SHORT).show();
            }
        });

        /*setupGridMenu();

        mGridMenuFragment.setOnClickMenuListener(new GridMenuFragment.OnClickMenuListener() {
            @Override
            public void onClickMenu(GridMenu gridMenu, int position) {

                if(position==1) {
                    Intent intent = new Intent(WeatherNowActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(position==2) {
                    Intent intent = new Intent(WeatherNowActivity.this, UVIActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                if(position==3) {
                    Intent intent = new Intent(WeatherNowActivity.this, Pm25Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(position==4){
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setComponent(new ComponentName("com.f74372017.twreservoir","com.f74372017.twreservoir.SplashActivity")); //包裹名稱，要開啟的頁面
                    //Intent intent = getApplication().getPackageManager()
                     //       .getLaunchIntentForPackage("com.f74372017.twreservoir.SplashActivity");
                    //intent.putExtra("value", "test"); //要傳送的值
                    try{
                        startActivity(intent);
                    }
                    catch(Exception e){
                        Toast.makeText(WeatherNowActivity.this,"無安裝此應用!",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.f74372017.twreservoir")));
                    }
                }
                if(position==7) {
                    Intent intent = new Intent(WeatherNowActivity.this, SettingsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });*/
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
                            //位置 Location
                            if(!(latitude>=20&&latitude<=27)&&!(longtitude>=118&&longtitude<=124)){
                                mCountry = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("location").getString("country");
                                mCity = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("location").getString("city");
                                //寫入 Location 資料表
                                mAccess.update("0",mCountry,mCity,mDistrict,mVillage,Double.toString(latitude),Double.toString(longtitude),null);
                            }
                            //風 wind
                            String chill = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getString("chill");
                            double direction = Double.parseDouble(jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getString("direction"));
                            int speed = Integer.parseInt(jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getString("speed"));
                            //direction外來鍵
                            String str_direction="";
                            if((direction>=0&&direction<=11.25) || (direction>=348.76&&direction<=360)) {
                                str_direction=mContext.getResources().getString(R.string.N);
                            }
                            if(direction>=11.26&&direction<=33.75){
                                str_direction=mContext.getResources().getString(R.string.NNE);
                            }
                            if(direction>=33.76&&direction<=56.25){
                                str_direction=mContext.getResources().getString(R.string.NE);
                            }
                            if(direction>=56.26&&direction<=78.75){
                                str_direction=mContext.getResources().getString(R.string.ENE);
                            }
                            if(direction>=78.76&&direction<=101.25){
                                str_direction=mContext.getResources().getString(R.string.E);
                            }
                            if(direction>=101.26&&direction<=123.75){
                                str_direction=mContext.getResources().getString(R.string.ESE);
                            }
                            if(direction>=123.76&&direction<=146.25){
                                str_direction=mContext.getResources().getString(R.string.SE);
                            }
                            if(direction>=146.26&&direction<=168.75){
                                str_direction=mContext.getResources().getString(R.string.SSE);
                            }
                            if(direction>=168.76&&direction<=191.25){
                                str_direction=mContext.getResources().getString(R.string.S);
                            }
                            if(direction>=191.26&&direction<=213.75){
                                str_direction=mContext.getResources().getString(R.string.SSW);
                            }
                            if(direction>=213.76&&direction<=236.25){
                                str_direction=mContext.getResources().getString(R.string.SW);
                            }
                            if(direction>=236.26&&direction<=258.75){
                                str_direction=mContext.getResources().getString(R.string.WSW);
                            }
                            if(direction>=258.76&&direction<=281.25){
                                str_direction=mContext.getResources().getString(R.string.W);
                            }
                            if(direction>=281.26&&direction<=303.75){
                                str_direction=mContext.getResources().getString(R.string.WNW);
                            }
                            if(direction>=303.76&&direction<=326.25){
                                str_direction=mContext.getResources().getString(R.string.NW);
                            }
                            if(direction>=326.26&&direction<=348.75){
                                str_direction=mContext.getResources().getString(R.string.NNW);
                            }

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
                            if(c.getCount()==0) {
                                mAccess.add();
                                //寫入 Location 資料表
                                //寫入 Wind資料表
                                mAccess.add("1", Double.parseDouble(chill), str_direction, speed+"");
                                //寫入 Atmosphere資料表
                                mAccess.add("1", humidity, pressure, rising, visibility);
                                //寫入 Astronomy資料表
                                mAccess.add("1", sunrise, sunset);
                                //寫入 Condition資料表
                                mAccess.add("1", date, day, Double.parseDouble(high), Double.parseDouble(low), Double.parseDouble(temp), Integer.parseInt(code),publish_time);
                                //寫入 Forecast
                                for(int i=0;i<10;i++){
                                    //預報Forecast
                                    String forecast_date = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("date");
                                    String forecast_day = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("day");
                                    String forecast_high = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("high");
                                    String forecast_low = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("low");
                                    String forecast_text = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("code");
                                    mAccess.add(i+"", forecast_date, forecast_day,Double.parseDouble(forecast_high),Double.parseDouble(forecast_low),forecast_text);
                                }

                            }else{
                                //寫入 Wind資料表
                                mAccess.update("1", Double.parseDouble(chill), str_direction, speed+"",null);
                                //寫入 Atmosphere資料表
                                mAccess.update("1", humidity, pressure,visibility ,rising,null);
                                //寫入 Astronomy資料表
                                mAccess.update("1", sunrise, sunset,null);
                                //寫入 Condition資料表
                                mAccess.update("1", date, day, Double.parseDouble(high), Double.parseDouble(low), Double.parseDouble(temp), Integer.parseInt(code),publish_time,null);
                                //寫入 Forecast
                                for(int i=0;i<10;i++){
                                    //預報Forecast
                                    String forecast_date = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("date");
                                    String forecast_day = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("day");
                                    String forecast_high = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("high");
                                    String forecast_low = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("low");
                                    String forecast_text = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("code");
                                    mAccess.update(i+"", forecast_date, forecast_day,Double.parseDouble(forecast_high),Double.parseDouble(forecast_low),forecast_text,"forecast_id ="+i);
                                }
                            }

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
            longtitude =mGps.getLongtitude();
            if((latitude>=20&&latitude<=27)&&(longtitude>=118&&longtitude<=124))
                mLanguage="zh-TW";

            //Toast.makeText(getApplicationContext(), "Your Location is->\nLat: " + latitude + "\nLong: " + longtitude, Toast.LENGTH_LONG).show();
            ///**撈取時間資料START**///
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longtitude + "&language="+mLanguage+"&sensor=true&key=AIzaSyDHA4UDKuJ_hZafj8Xn6m3mMzOsQnbTZ_w&lafhdfhfdhfdhrh";

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
                                mCountry = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 2).getString("long_name");
                                mCity = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 3).getString("long_name");
                                mDistrict = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 4).getString("short_name");
                                mVillage = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 5).getString("short_name");
                                String str5 = jsonObject.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                                //寫入Location資料表
                                Cursor c = mAccess.getData("Location", null, null);
                                c.moveToFirst();
                                if(c.getCount()==0){
                                    mAccess.add("1",mCountry,mCity,mDistrict,mVillage,latitude+"",longtitude+"");
                                }else if(c.getDouble(5)!=latitude||c.getDouble(6)!=longtitude){
                                    //Toast.makeText(WeatherNowActivity.this,"更新位置->\nLat: " + latitude + "\nLong: " + longtitude,Toast.LENGTH_SHORT).show();
                                    mAccess.update("1",mCountry,mCity,mDistrict,mVillage,Double.toString(latitude),Double.toString(longtitude),null);
                                }
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
        /*Cursor cl1 = mAccess.getData("Location", null, null);
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

        Cursor cl8 = mAccess.getData("Forecast",null,null);
        cl8.moveToPosition(9);
        Log.e("Data Forecast",cl8.getString(0) + " " +  cl8.getString(1) + " "
                + cl8.getString(2) + " " + cl8.getString(3) + " "
                + cl8.getString(4) + " " + cl8.getString(5));*/




        initInfo();
        menu_init();//menu初始化

        reflash();

        //callDB2();

    }
    public void initInfo(){
        Cursor cl1 = mAccess.getData("Location", null, null);
        cl1.moveToFirst();
        Cursor cl6 = mAccess.getData("Condition", null, null);
        cl6.moveToFirst();
        //取得系統時間 Fri, 10 Mar 2017 03:23 PM CST
        String str[]=cl6.getString(7).split(" "),time[]=str[4].split(":");
        int hour=Integer.parseInt(time[0]);
        if(settings.getString("Clock","").equals("24hr")||settings.getString("Clock","").equals("")){
            if(str[5].equals("AM")&&Integer.parseInt(time[0])==12)
                hour=00;
            if(str[5].equals("PM")&&Integer.parseInt(time[0])!=12) {
                hour+=12;
                tvTime.setText(Integer.parseInt(time[0]) + 12 + ":" + time[1] + " " + str[6]);
            }
            else
                tvTime.setText(Integer.parseInt(time[0])+":"+time[1]+" "+str[6]);
            tvCity.setText(cl1.getString(2));
        }
        else{
            if(str[5].equals("PM")&&Integer.parseInt(time[0])!=12)
                hour+=12;
            tvTime.setText(str[4]+" "+str[5]+" "+str[6]);
            tvCity.setText(cl1.getString(2));
        }
        mRecyclerView.setAdapter(new WeatherNowRVA(WeatherNowActivity.this));
        mAlpha=0;
        mScrollerY=0;
        //以時間判斷背景
        if(hour>=7&&hour<=16) {
            int round=(int) (Math.random() * mImgDay.length);
            mBlurredView.setBlurredImg(mContext.getResources().getDrawable(mImgDay[round]));//主畫面背景
            mGridMenuFragment = GridMenuFragment.newInstance(mImgDay[round]);//選單背景
        }
        else if(hour>16&&hour<=18) {
            int round=(int) (Math.random() * mImgAfternoon.length);
            mBlurredView.setBlurredImg(mContext.getResources().getDrawable(mImgAfternoon[round]));//主畫面背景
            mGridMenuFragment = GridMenuFragment.newInstance(mImgAfternoon[round]);//選單背景
        }
        else if(hour>18&&hour<=23) {
            int round=(int) (Math.random() * mImgnight.length);
            mBlurredView.setBlurredImg(mContext.getResources().getDrawable(mImgnight[round]));//主畫面背景
            mGridMenuFragment = GridMenuFragment.newInstance(mImgnight[round]);//選單背景
        }
        else {
            int round=(int) (Math.random() * mImgMidnight.length);
            mBlurredView.setBlurredImg(mContext.getResources().getDrawable(mImgMidnight[round]));//主畫面背景
            mGridMenuFragment = GridMenuFragment.newInstance(mImgMidnight[round]);//選單背景
        }
    }
    void callDB2(){
        DBAccessEnvironment access2;
        access2= new DBAccessEnvironment(this, "Environment", null, 1);
        Cursor cl1 = access2.getData("Location", null, null);
        cl1.moveToFirst();
        Log.e("Data Location", cl1.getString(0) + " " + cl1.getString(1) + " "
                + cl1.getString(2) + " " + cl1.getString(3) + " "
                + cl1.getString(4) + " " + cl1.getDouble(5) + " "
                + cl1.getDouble(6));

        Cursor cl2 = access2.getData("AIR", null, null);
        cl2.moveToFirst();
        Log.e("Data Air", cl2.getString(0) + " " + cl2.getString(1) + " "
                + cl2.getString(2) + " " + cl2.getString(3) + " "
                + cl2.getString(4) + " " + cl2.getString(5) + " "
                + cl2.getString(6) + " " + cl2.getString(7) + " "
                + cl2.getString(8) + " " + cl2.getString(9) + " "
                + cl2.getString(10));

        Cursor cl22 = access2.getData("AQI", null, null);
        cl22.moveToFirst();
        Log.e("Data AQI", cl22.getString(0) + " " + cl22.getString(1) + " "
                + cl22.getString(2) + " " + cl22.getString(3));

        Cursor cl3 = access2.getData("PM25", null, null);
        cl3.moveToFirst();
        Log.e("Data PM25", cl3.getString(0) + " " + cl3.getString(1) + " "
                + cl3.getString(2) + " " + cl3.getString(3) + " " + cl3.getString(4));

        Cursor cl5 = access2.getData("Ultraviolet", null, null);
        cl5.moveToFirst();
        Log.e("Data Ultraviolet", cl5.getString(0) + " "
                + cl5.getString(1) + " " + cl5.getString(2) + " "
                + cl5.getString(3) + " " + cl5.getString(4) + " "
                + cl5.getString(5) + " " + cl5.getString(6));

        Cursor cl6 = access2.getData("Rain", null, null);
        cl6.moveToFirst();
        Log.e("Data Rain", cl6.getString(0) + " " + cl6.getString(1) + " "
                + cl6.getString(2) + " " + cl6.getString(3) + " "
                + cl6.getString(4) + " " + cl6.getString(5) + " "
                + cl6.getString(6) + " " + cl6.getString(7) + " "
                + cl6.getString(8));
    }
}