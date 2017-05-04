package com.openweather.openweather.AirActuvuty;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.openweather.openweather.DataBase.DBAccessEnvironment;
import com.openweather.openweather.DataBase.DBAccessWeather;
import com.openweather.openweather.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class AirMainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private DBAccessEnvironment mAccess2;
    private DBAccessWeather mAccess;
    double mLatitude,mLongitude;
    private TextView tvCity,tvTime;
    private SharedPreferences settings;
    private KProgressHUD hud;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airmain);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAccess2= new DBAccessEnvironment(this, "Environment", null, 1);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new AQIFragment(),"AQI");
        viewPagerAdapter.addFragments(new PM25Fragment(),"PM25");
        viewPagerAdapter.addFragments(new BlankFragment(),"AIR");
        viewPager.setAdapter(viewPagerAdapter);
        initUI();

        mAccess = new DBAccessWeather(this, "weather", null, 1);
        mAccess2= new DBAccessEnvironment(this, "Environment", null, 1);
        settings=getSharedPreferences("Data",MODE_PRIVATE);


    }

    @Override
    protected void onResume() {
        super.onResume();
        initAirLoc();
        Cursor cl1 = mAccess2.getData("Location", null, null);
        cl1.moveToFirst();
        Cursor cl6 = mAccess.getData("Condition", null, null);
        cl6.moveToFirst();
        mLatitude=Double.parseDouble(cl1.getString(5));mLongitude=Double.parseDouble(cl1.getString(6));
        tvCity=(TextView)findViewById(R.id.tvCity);
        tvTime=(TextView)findViewById(R.id.tvTime);

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
            tvCity.setText(cl1.getString(2)+"/"+cl1.getString(3));
        }
        else{
            if(str[5].equals("PM")&&Integer.parseInt(time[0])!=12)
                hour+=12;
            tvTime.setText(str[4]+" "+str[5]+" "+str[6]);
            tvCity.setText(cl1.getString(2)+"/"+cl1.getString(3));
        }

        hud = KProgressHUD.create(AirMainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("請稍後...")
                .setDimAmount(0.5f);
        scheduleDismiss();
        hud.show();
    }
    private void scheduleDismiss() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
                Cursor cl2 = mAccess2.getData("AIR", null, null);
                cl2.moveToFirst();
                if(cl2.getCount()==0)
                    onResume();
            }
        }, 3000);
    }

    private void initUI() {
//        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
//        viewPager.setAdapter(new PagerAdapter() {
//            @Override
//            public int getCount() {
//                return 5;
//            }
//
//            @Override
//            public boolean isViewFromObject(final View view, final Object object) {
//                return view.equals(object);
//            }
//
//            @Override
//            public void destroyItem(final View container, final int position, final Object object) {
//                ((ViewPager) container).removeView((View) object);
//            }
//
//            @Override
//            public Object instantiateItem(final ViewGroup container, final int position) {
//                final View view = LayoutInflater.from(
//                        getBaseContext()).inflate(R.layout.item_vp, null, false);
//
//                final TextView txtPage = (TextView) view.findViewById(R.id.txt_vp_item_page);
//                txtPage.setText(String.format("Page #%d", position));
//
//                container.addView(view);
//                return view;
//            }
//        });

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.aqi),
                        Color.parseColor(colors[0]))
                        //.selectedIcon(getResources().getDrawable(R.mipmap.aqi2))
                        .title("AQI")
                        //.badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.pm25),
                        Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("PM2.5")
                        //.badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.air1),
                        Color.parseColor(colors[2]))
                        .selectedIcon(getResources().getDrawable(R.mipmap.air))
                        .title("AIR")
                        //.badgeTitle("state")
                        .build()
        );
       /* models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fourth),
                        Color.parseColor(colors[3]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Flag")
                        .badgeTitle("icon")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fifth),
                        Color.parseColor(colors[4]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Medal")
                        .badgeTitle("777")
                        .build()
        );*/

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
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
                            }else{
                                mAccess2.update("1", PublishTime, SiteName, AQI, SO2, CO, O3, PM10, PM25, NO2, NOx, NO1,null);
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

}