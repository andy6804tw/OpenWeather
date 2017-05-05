package com.openweather.openweather.WeatherNowActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.openweather.openweather.DataBase.DBAccessWeather;
import com.openweather.openweather.ExitApplication;
import com.openweather.openweather.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditLocationActivity extends AppCompatActivity {

    private static final String TAG = "EditLocationActivity";
    private ListView listView;
    private ArrayList<String> groups;
    DBAccessWeather mAccess;
    double latitude,longtitude;
    String mLanguage="en",mCity,mCountry,mDistrict,mVillage;
    private Context mContext;
    String mPlace="Taipei";
    public static Boolean myPlace=false;

    class ViewHolder {
        TextView tv;
    }

    private TextView tvCity;

    private String cityName = "";
    private String default_cityName = "Taipei";
    String strCity = "";
    private String prefName = "prefSet";
    private KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        ExitApplication.getInstance().addActivity(this);

        tvCity = (TextView) findViewById(R.id.tv_city);
        listView = (ListView) findViewById(R.id.lvGroup);
        tvCity.setText(getResources().getString(R.string.SelectCity)+": " + strCity);
        mAccess = new DBAccessWeather(this, "weather", null, 1);
        mContext=getApplicationContext();

        initDataList();

        GroupAdapter adapter = new GroupAdapter(this, groups);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                mPlace = viewHolder.tv.getText().toString();
                //queryEnCityName(text);
                tvCity.setText(getResources().getString(R.string.SelectCity)+": " + mPlace);
                mAccess.update("1",mCountry,mPlace,mDistrict,mVillage,Double.toString(latitude),Double.toString(longtitude),null);
                init_PlaceWeather();
                EditLocationActivity.myPlace=true;
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity( new Intent(EditLocationActivity.this, WeatherNowActivity.class));
                    }
                }, 3000);*/
                hud = KProgressHUD.create(EditLocationActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("請稍後...")
                        .setDimAmount(0.5f)
                        .setCancellable(false);
                scheduleDismiss();
                hud.show();
            }
        });

    }
    private void scheduleDismiss() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
                startActivity( new Intent(EditLocationActivity.this, WeatherNowActivity.class));
            }
        }, 3000);
    }
    private void initDataList() {
        groups = new ArrayList<String>();
        groups.add(getResources().getString(R.string.Keelung));
        groups.add(getResources().getString(R.string.NewTaipei));
        groups.add(getResources().getString(R.string.Taipei));
        groups.add(getResources().getString(R.string.Taoyuan));
        groups.add(getResources().getString(R.string.Hsinchu));
        groups.add(getResources().getString(R.string.HsinchuCountry));
        groups.add(getResources().getString(R.string.Miaoli));
        groups.add(getResources().getString(R.string.Taichung));
        groups.add(getResources().getString(R.string.Changhua));
        groups.add(getResources().getString(R.string.Nantou));
        groups.add(getResources().getString(R.string.Yunlin));
        groups.add(getResources().getString(R.string.Chiayi));
        groups.add(getResources().getString(R.string.ChiayiCountry));
        groups.add(getResources().getString(R.string.Tainan));
        groups.add(getResources().getString(R.string.Kaohsiung));
        groups.add(getResources().getString(R.string.Pingtung));
        groups.add(getResources().getString(R.string.Taitung));
        groups.add(getResources().getString(R.string.Hualien));
        groups.add(getResources().getString(R.string.Yilan));
        groups.add(getResources().getString(R.string.Penghu));
        groups.add(getResources().getString(R.string.Kinmen));
        groups.add(getResources().getString(R.string.Lienchiang));
    }




    public class GroupAdapter extends BaseAdapter {
        private ArrayList<String> list;
        private LayoutInflater inflater = null;//導入布局

        public GroupAdapter(Activity context, ArrayList<String> list) {
            this.list = list;
            inflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            Log.d(TAG, String.valueOf(i));

            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {//當第一次加載ListView控件時  convertView为空
                convertView = inflater.inflate(R.layout.weather_location_listi_tem, null);//所以當ListView控件沒有滑動時都會執行這條語句
                holder = new ViewHolder();
                holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
                convertView.setTag(holder);//为view設置標簽
            } else {//取出holder
                holder = (ViewHolder) convertView.getTag();//the Object stored in this view as a tag
            }
            //設置list的textview顯示
            holder.tv.setTextColor(Color.WHITE);
            holder.tv.setText(list.get(position));
            holder.tv.setTextSize(24f);
            return convertView;
        }


    }
    private void init_PlaceWeather() {
        ///**撈取資料END**///
        ///**撈取天氣資料START**///
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        //處理英文空白字串增加%20
        String tempPlace=mPlace;
        mPlace=enCity(mPlace);
        if(mPlace.split(" ").length==2)
            mPlace=mPlace.split(" ")[0]+"%20"+mPlace.split(" ")[1];
        if(mPlace.split(" ").length==3)
            mPlace=mPlace.split(" ")[0]+"%20"+mPlace.split(" ")[1]+"%20"+mPlace.split(" ")[2];
        String url= "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D\""+mPlace+"\")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        //把空白%20變回
        mPlace=tempPlace;
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
                            mCountry = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("location").getString("country");
                            mCity = mPlace;
                            //寫入 Location 資料表
                            mAccess.update("1",mCountry,mCity,mDistrict,mVillage,Double.toString(latitude),Double.toString(longtitude),null);
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
                Toast.makeText(EditLocationActivity.this, "無法連接網路!", Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public String enCity(String city){
        switch (city){
            case "基隆市":
                return "Keelung City";
            case "新北市":
                return "Taipei City";
            case "臺北市":
                return "New Taipei City";
            case "新竹市":
                return "Hsinchu City";
            case "新竹縣":
                return "Hsinchu Country";
            case "臺中市":
                return "Taichung City";
            case "台南市":
                return "Tainan City";
            case "高雄市":
                return "Kaohsiung City";
            case "宜蘭縣":
                return "Yilan Country";
            case "桃園市":
                return "Taoyuan City";
            case "苗栗縣":
                return "Miaoli Country";
            case "彰化縣":
                return "Changhua Country";
            case "南投縣":
                return "Nantou Country";
            case "雲林縣":
                return "Yunlin Country";
            case "嘉義市":
                return "Chiayi City";
            case "嘉義縣":
                return "Chiayi Country";
            case "屏東縣":
                return "Pingtung Country";
            case "臺東縣":
                return "Taitung";
            case "花蓮縣":
                return "Hualien Country";
            case "澎湖縣":
                return "Penghu Country";
            case "金門縣":
                return "Kinmen Country";
            default:
                return "Lienchiang Country";

        }
    }
}

