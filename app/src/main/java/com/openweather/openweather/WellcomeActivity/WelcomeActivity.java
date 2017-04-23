package com.openweather.openweather.WellcomeActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.openweather.openweather.DataBase.DBAccessEnvironment;
import com.openweather.openweather.DataBase.DBAccessWeather;
import com.openweather.openweather.ExitApplication;
import com.openweather.openweather.LoadingSplash.GPSTracker;
import com.openweather.openweather.LoadingSplash.SplashActivity;
import com.openweather.openweather.R;
import com.openweather.openweather.View.SunBabyLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Akshay Raj on 7/28/2016.
 * Snow Corporation Inc.
 * www.snowcorp.org
 */
public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private Session session;
    public static int seconds=10000;
    //Database
    GPSTracker mGps;
    DBAccessWeather mAccess;
    double mLatitude,mLongitude;
    String mLanguage="en",mCity,mCountry,mDistrict,mVillage;
    private Context mContext;
    DBAccessEnvironment mAccess2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        //Database
        mContext=getApplicationContext();
        mAccess = new DBAccessWeather(this, "weather", null,1);
        mAccess2= new DBAccessEnvironment(this, "Environment", null, 1);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Checking for first time launch - before calling setContentView()
        session = new Session(this);
        if (!session.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }else{
            GPSPremessionCheck();
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        session.setFirstTimeLaunch(false);
        seconds=3000;
        startActivity(new Intent(WelcomeActivity.this, SplashActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
    private void GPSPremessionCheck() {
        /**偵測權限**/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //如果沒有授權使用定位就會跳出來這個
            // TODO: Consider calling
            //Log.e("Data6", "進入!");
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // 如果裝置版本是6.0（包含）以上
            if (Build.VERSION.SDK_INT >= M) {
                // 取得授權狀態，參數是請求授權的名稱
                int hasPermission = checkSelfPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION);
                // 如果未授權
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    // 請求授權
                    //     第一個參數是請求授權的名稱
                    //     第二個參數是請求代碼
                    //Log.e("Data3", "失敗!");
                    requestPermissions(
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            10);
                }
                //Log.e("Data4", "成功!");
            }
        }else{
            init_GPS();
            init_Weather();
            //initAirLoc();
            //init_UV();
            //initRain();
            Cursor cl5 = mAccess2.getData("Ultraviolet", null, null);
            cl5.moveToFirst();
            if(cl5.getCount()==0) {
                mAccess2.add();
            }
        }
        /***/
    }
    private void init_Weather() {
        ///**撈取資料END**///
        ///**撈取天氣資料START**///
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(SELECT%20woeid%20FROM%20geo.places%20WHERE%20text%3D\"("+mLatitude+","+mLongitude+")\")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

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
                            if(!(mLatitude>=20&&mLatitude<=27)&&!(mLongitude>=118&&mLongitude<=124)){
                                mCountry = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("location").getString("country");
                                mCity = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("location").getString("city");
                                //寫入 Location 資料表
                                mAccess.update("1",mCountry,mCity,mDistrict,mVillage,Double.toString(mLatitude),Double.toString(mLongitude),null);
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
                                    String forecast_code = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("code");
                                    mAccess.add(i+"", forecast_date, forecast_day,Double.parseDouble(forecast_high),Double.parseDouble(forecast_low),forecast_code);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WelcomeActivity.this, "無法連接網路!", Toast.LENGTH_SHORT).show();
                SunBabyLoadingView.str = "正載入歷史資料...";
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void init_GPS() {
        mGps = new GPSTracker(this);
        if (mGps.canGetLocation && mGps.getLatitude() != (0.0) && mGps.getLongtitude() != (0.0)) {
            mLatitude = mGps.getLatitude();
            mLongitude =mGps.getLongtitude();
            if((mLatitude>=20&&mLatitude<=27)&&(mLongitude>=118&&mLongitude<=124))
                mLanguage="zh-TW";

            //Toast.makeText(getApplicationContext(), "Your Location is->\nLat: " + latitude + "\nLong: " + longtitude, Toast.LENGTH_LONG).show();
            ///**撈取時間資料START**///
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + mLatitude + "," + mLongitude + "&language="+mLanguage+"&sensor=true&key=AIzaSyDHA4UDKuJ_hZafj8Xn6m3mMzOsQnbTZ_w&lafhdfhfdhfdhrh";

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
                                    //Weather的位置
                                    mAccess.add("1",mCountry,mCity,mDistrict,mVillage,mLatitude+"",mLongitude+"");
                                    //Enviroment的位置
                                    mAccess2.add("1",mCountry,mCity,mDistrict,mVillage,mLatitude+"",mLongitude+"");
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
    //計算兩點距離
    public  double calLocation(double wd1, double jd1, double wd2, double jd2) {
        double x, y, out;
        double PI = 3.14159265;
        double R = 6.371229 * 1e6;

        x = (jd2 - jd1) * PI * R * Math.cos(((wd1 + wd2) / 2) * PI / 180) / 180;
        y = (wd2 - wd1) * PI * R / 180;
        out = Math.hypot(x, y);
        return out / 1000;
    }
    public double transWGS84(String WGS84){
        String arr[]=WGS84.split(",");
        return Double.parseDouble(arr[0])+Double.parseDouble(arr[1])/60+Double.parseDouble(arr[2])/3600;
    }
    public void initAirLoc() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://opendata.epa.gov.tw/webapi/api/rest/datastore/355000000I-000005?sort=SiteName&offset=0&limit=1000";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            //Log.e("jsonObject",jsonObject.toString());
                            //String SiteName=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(0).getString("SiteName");
                            Double min=Double.MAX_VALUE;
                            String siteName="";
                            int index=0;
                            for(int i=0;i<jsonObject.getJSONObject("result").getJSONArray("records").length();i++){
                                String SiteName=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(i).getString("SiteName");
                                String latitude=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(i).getString("TWD97Lat");
                                String longitude=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(i).getString("TWD97Lon");
                                Double loc=calLocation(mLatitude,mLongitude,Double.parseDouble(latitude),Double.parseDouble(longitude));
                                if(min>loc) {
                                    index=i;
                                    min = loc;
                                    siteName=SiteName;
                                }
                                //Log.e("Data"+i,"測站:"+SiteName+"    經度:"+latitude+"    緯度:"+longitude+"   "+loc+"km");
                            }
                            //Log.e("Informatin","Your Location is: "+mLatitude+","+mLongitude);
                            Log.e("Air Result","The AirSite closest to you is "+siteName+"測站  distance->"+min+" km"+" "+index);
                            initAir(index);//找出距離最近測站擷取空氣品質OpenData

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "無法連接網路!", Toast.LENGTH_SHORT).show();
            }

        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public void initAir(int Airindex) {
        final int index=Airindex;
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://opendata.epa.gov.tw/webapi/api/rest/datastore/355000000I-001805?sort=SiteName&offset=0&limit=1000";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            String SiteName=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("SiteName");
                            String PublishTime=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("PublishTime");
                            String AQI=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("AQI");
                            String SO2=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("SO2");
                            String CO=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("CO");
                            String O3=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("O3");
                            String PM10=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("PM10");
                            String PM25=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("PM2.5");
                            String NO2=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("NO2");
                            String NOx=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("NOx");
                            String NO1=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("NO");
                            Log.e("Air info","SiteName:"+SiteName+"   PublishTime:"+PublishTime+"   AQI:"+AQI+"   SO2:"+SO2+"   CO:"+CO+"   O3:"+O3+"  PM10:"+PM10+"   PM25:"
                                    +PM25+"   NO2:"+NO2+"   NOX:"+NOx+"  NO:"+NO1);
                            if(PM25.equals(""))
                                PM25="0";
                            Cursor cl2 = mAccess2.getData("AIR", null, null);
                            cl2.moveToFirst();
                            if(cl2.getCount()==0){
                                mAccess2.add("1", PublishTime, SiteName, AQI, SO2, CO, O3, PM10, PM25, NO2, NOx, NO1);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "無法連接網路!", Toast.LENGTH_SHORT).show();
                mAccess2.add("1", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null");
            }

        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public void init_UV() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://opendata.epa.gov.tw/webapi/api/rest/datastore/355000000I-000004?sort=PublishTime&offset=0&limit=1000";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            Double min=Double.MAX_VALUE;
                            int index=0;
                            for(int i=0;i<jsonObject.getJSONObject("result").getJSONArray("records").length();i++){
                                String latitude=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(i).getString("WGS84Lat");
                                String longitude=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(i).getString("WGS84Lon");
                                Double loc=calLocation(mLatitude,mLongitude,transWGS84(latitude),transWGS84(longitude));
                                if(min>loc) {
                                    index=i;
                                    min = loc;
                                }
                                //Log.e("Data"+i,"測站:"+SiteName+"    經度:"+latitude+"    緯度:"+longitude+"   "+loc+"km");
                            }
                            String SiteName=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("SiteName");
                            String UVI=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("UVI");
                            String PublishAgency=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("PublishAgency");
                            String PublishTime=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("PublishTime");
                            Log.e("UVI Result","The UVISite closest to you is "+SiteName+"測站  distance->"+min+" km"+" "+index);
                            Log.e("UVI info","SiteName:"+SiteName+" UVI:"+UVI+" PublishAgency:"+PublishAgency+" PublishTime:"+PublishTime);
                            Cursor cl5 = mAccess2.getData("Ultraviolet", null, null);
                            cl5.moveToFirst();
                            if(cl5.getCount()==0) {
                                mAccess2.add();
                                mAccess2.add("1",(int)Double.parseDouble(UVI),PublishAgency,PublishTime,SiteName,mLatitude,mLongitude);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "無法連接網路!", Toast.LENGTH_SHORT).show();
                mAccess2.add("1", 0, "null", "null", "null",0.0,0.0);
            }

        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public void initRain() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://opendata.epa.gov.tw/webapi/api/rest/datastore/315070000H-000001?sort=PublishTime&offset=0&limit=1000";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            Double min=Double.MAX_VALUE;
                            int index=0;
                            for(int i=0;i<jsonObject.getJSONObject("result").getJSONArray("records").length();i++){
                                String latitude=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(i).getString("TWD67Lat");
                                String longitude=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(i).getString("TWD67Lon");
                                Double loc=calLocation(mLatitude,mLongitude,Double.parseDouble(latitude),Double.parseDouble(longitude));
                                if(min>loc) {
                                    index=i;
                                    min = loc;
                                }
                                //Log.e("Data"+i,"測站:"+SiteName+"    經度:"+latitude+"    緯度:"+longitude+"   "+loc+"km");
                            }
                            String SiteName=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("SiteName");
                            String Now=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("Now");
                            String PublishAgency=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("Unit");
                            String Rainfall10min=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("Rainfall10min");
                            String Rainfall1hr=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("Rainfall1hr");
                            String Rainfall3hr=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("Rainfall3hr");
                            String Rainfall12hr=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("Rainfall12hr");
                            String PublishTime=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(index).getString("PublishTime");
                            Log.e("Rain Result","The RainSite closest to you is "+SiteName+"測站  distance->"+min+" km"+" "+index);
                            Log.e("Rain info","SiteName:"+SiteName+"   Now:"+Now+"   PublishAgency:"+PublishAgency+"   Rainfall10min:"+Rainfall10min
                                    +"   Rainfall1hr:"+Rainfall1hr+"   Rainfall3hr:"+Rainfall3hr+"   Rainfall12hr:"+Rainfall12hr+"   PublishTime:"+PublishTime);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "無法連接網路!", Toast.LENGTH_SHORT).show();
            }

        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}