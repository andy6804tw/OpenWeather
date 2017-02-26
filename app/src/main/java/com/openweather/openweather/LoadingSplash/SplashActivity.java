package com.openweather.openweather.LoadingSplash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.openweather.openweather.ExitApplication;
import com.openweather.openweather.R;
import com.openweather.openweather.View.SunBabyLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ExitApplication.getInstance().addActivity(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ///**撈取資料END**///
        ///**撈取天氣資料START**///
        // Instantiate the RequestQueue.
        RequestQueue queue2 = Volley.newRequestQueue(this);
        String url2 ="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(SELECT%20woeid%20FROM%20geo.places%20WHERE%20text%3D\"(22.9097,120.275002)\")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

        // Request a string response from the provided URL.
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            //風 wind
                            String chill=jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getString("chill");
                            String direction=jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getString("direction");
                            String speed=jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getString("speed");
                            //大氣 Atmosphere
                            String humidity=jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("atmosphere").getString("humidity");
                            String pressure=jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("atmosphere").getString("pressure");
                            String rising=jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("atmosphere").getString("rising");
                            String visibility=jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("atmosphere").getString("visibility");
                            //天文 Astronomy
                            String sunrise=jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("astronomy").getString("sunrise");
                            String sunset=jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("astronomy").getString("sunset");
                            //狀態 Condition
                            String date=jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("date");
                            String day=jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("day");
                            String high=jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("high");
                            String low=jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("low");
                            String code=jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("code");
                            Log.e("wind",chill+" | "+direction+" | "+speed);
                            Log.e("Atmosphere",humidity+" | "+pressure+" | "+" | "+rising+" | "+visibility);
                            Log.e("Astronomy",sunrise+" | "+sunset);
                            Log.e("Condition",date+" | "+day+" | "+" | "+high+" | "+low);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SplashActivity.this,"無法連接網路!",Toast.LENGTH_SHORT).show();
                SunBabyLoadingView.str="正載入歷史資料...";
            }
        });
        // Add the request to the RequestQueue.
        queue2.add(stringRequest2);

        ///**撈取資料END**///
    }
}
