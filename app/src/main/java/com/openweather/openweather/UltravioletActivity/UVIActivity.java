package com.openweather.openweather.UltravioletActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import com.kaopiz.kprogresshud.KProgressHUD;
import com.openweather.openweather.DataBase.DBAccessEnvironment;
import com.openweather.openweather.LoadingSplash.GPSTracker;
import com.openweather.openweather.R;

import org.json.JSONException;
import org.json.JSONObject;

import me.drakeet.BreathingViewHelper;

public class UVIActivity extends AppCompatActivity {

    DBAccessEnvironment mAccess2;
    private TextView tvUv,tvCity,tvLocation,tvPublishTime,tvPublishAgency,tvSiteName,tvStr,tvSuggest;
    double mLatitude,mLongitude;
    GPSTracker mGps;
    private KProgressHUD hud;
    private long temptime = 0;//計算退出秒數

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvi);
        //ExitApplication.getInstance().addActivity(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }
    public void init_View(){
        Cursor c = mAccess2.getData("Ultraviolet", null, null);
        c.moveToFirst();
        //Toast.makeText(UVIActivity.this,c.getString(1)+" "+c.getString(2)+" "+c.getString(3)+" "+c.getString(4),Toast.LENGTH_SHORT).show();
        Cursor cl1 = mAccess2.getData("Location", null, null);
        cl1.moveToFirst();
        tvUv=(TextView)findViewById(R.id.tvUv);
        tvCity=(TextView)findViewById(R.id.tvCity);
        tvLocation=(TextView)findViewById(R.id.tvLocation);
        tvPublishTime=(TextView)findViewById(R.id.tvPublishTime);
        tvPublishAgency=(TextView)findViewById(R.id.tvPublishAgency);
        tvSiteName=(TextView)findViewById(R.id.tvSiteName);
        tvStr=(TextView)findViewById(R.id.tvStr);
        tvSuggest=(TextView)findViewById(R.id.tvSuggest);
        tvUv.setText(c.getString(1));
        tvPublishTime.setText("發布時間: "+c.getString(3));
        tvPublishAgency.setText("發布機關: "+c.getString(2));
        tvSiteName.setText("測站: "+c.getString(4));
        tvLocation.setText("您目前的位置為: "+cl1.getString(3));
        tvCity.setText(cl1.getString(2));
        if(c.getShort(1)==0||c.getShort(1)==1||c.getShort(1)==2){
            tvStr.setText("低量級");
            tvSuggest.setText(getResources().getString(R.string.uv_suggest1));
            // start
            BreathingViewHelper.setBreathingBackgroundColor(
                    findViewById(R.id.relativelatout),
                    getResources().getColor(R.color.alermUV1)
            );
        }
        else if(c.getShort(1)==3||c.getShort(1)==4||c.getShort(1)==5){
            tvStr.setText("中量級");
            tvSuggest.setText(getResources().getString(R.string.uv_suggest2));
            // start
            BreathingViewHelper.setBreathingBackgroundColor(
                    findViewById(R.id.relativelatout),
                    getResources().getColor(R.color.alermUV2)
            );
        }
        else if(c.getShort(1)==6||c.getShort(1)==7){
            tvStr.setText("高量級");
            tvSuggest.setText(getResources().getString(R.string.uv_suggest3));
            // start
            BreathingViewHelper.setBreathingBackgroundColor(
                    findViewById(R.id.relativelatout),
                    getResources().getColor(R.color.alermUV3)
            );
        }
        else if(c.getShort(1)==8||c.getShort(1)==9||c.getShort(1)==10){
            tvStr.setText("過量級");
            tvSuggest.setText(getResources().getString(R.string.uv_suggest4));
            // start
            BreathingViewHelper.setBreathingBackgroundColor(
                    findViewById(R.id.relativelatout),
                    getResources().getColor(R.color.alermUV4)
            );
        }
        else if(c.getShort(1)>=11){
            tvStr.setText("危險級");
            tvSuggest.setText(getResources().getString(R.string.uv_suggest5));
            // start
            BreathingViewHelper.setBreathingBackgroundColor(
                    findViewById(R.id.relativelatout),
                    getResources().getColor(R.color.alermUV5)
            );
        }


        // stop
        //BreathingViewHelper.stopBreathingBackgroundColor(findViewById(R.id.relativelatout));
    }
    @Override
    protected void onResume() {
        super.onResume();
        init_UV();
        mAccess2= new DBAccessEnvironment(this, "Environment", null, 1);
        Cursor c = mAccess2.getData("Ultraviolet", null, null);
        c.moveToFirst();
        if(c.getCount()!=0)
            init_View();
        //stop
        BreathingViewHelper.stopBreathingBackgroundColor(findViewById(R.id.relativelatout));
        hud = KProgressHUD.create(UVIActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("請稍後...")
                .setDimAmount(0.5f).setCancellable(false);
        scheduleDismiss();
        hud.show();
    }
    private void scheduleDismiss() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
                Cursor cl5 = mAccess2.getData("Ultraviolet", null, null);
                cl5.moveToFirst();
                if(cl5.getCount()==0) {
                    Toast.makeText(UVIActivity.this,"Wait!",Toast.LENGTH_SHORT).show();
                    onResume();
                }
                else
                    init_View();
            }
        }, 3000);
    }

    public void init_UV() {
        mGps = new GPSTracker(this);
        if (mGps.canGetLocation && mGps.getLatitude() != (0.0) && mGps.getLongtitude() != (0.0)) {
            mLatitude = mGps.getLatitude();
            mLongitude = mGps.getLongtitude();
        }
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
                            Cursor cl5 = mAccess2.getData("Ultraviolet", null, null);
                            cl5.moveToFirst();
                            if(cl5.getCount()==0) {
                                mAccess2.add("1",(int)Double.parseDouble(UVI),PublishAgency,PublishTime,SiteName,mLatitude,mLongitude);
                            }else{
                                mAccess2.update("1",(int)Double.parseDouble(UVI),PublishAgency,PublishTime,SiteName,mLatitude,mLongitude,null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "無法連接網路!", Toast.LENGTH_SHORT).show();
                Cursor cl5 = mAccess2.getData("Ultraviolet", null, null);
                cl5.moveToFirst();
                if(cl5.getCount()==0) {
                    mAccess2.add("1", 0, "null", "null", "null", 0.0, 0.0);
                }
            }

        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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

    public void onBack(View view) {
        //Toast.makeText(UVIActivity.this,"123",Toast.LENGTH_LONG).show();
        finish();
    }
}
