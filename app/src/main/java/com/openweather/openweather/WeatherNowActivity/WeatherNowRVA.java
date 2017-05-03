package com.openweather.openweather.WeatherNowActivity;

/**
 * Created by andy6804tw on 2017/2/24.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.ahmadnemati.wind.WindView;
import com.github.ahmadnemati.wind.enums.TrendType;
import com.github.pwittchen.weathericonview.WeatherIconView;
import com.openweather.openweather.DataBase.DBAccessWeather;
import com.openweather.openweather.R;
import com.openweather.openweather.View.TemperatureView;
import com.openweather.openweather.View.WeatherChartView;
import com.openweather.sunviewlibrary.SunView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by andy6804tw on 2017/1/25.
 */

public class WeatherNowRVA extends RecyclerView.Adapter<WeatherNowRVA.ViewHolder> {

    private final Context mContext;
    private static final int ITEM_COUNT = 6;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    int mPosition=0;
    private DBAccessWeather mAccess;
    SharedPreferences settings;

    public WeatherNowRVA(Context context) {
        this.mContext = context;
    }




    class ViewHolder extends RecyclerView.ViewHolder{
        //第零個
        private TextView tvTemp,tvHigh,tvLow,tvWeather;
        private WeatherIconView weatherIconView;
        //第一個condition目前狀況
        private TextView tvLocation,tv_temp,tv_low,tv_high,tvChill;
        private TemperatureView temperatureView;
        //第三個wind風速與風向
        private WindView windView;
        //第四個atmosphere大氣與氣壓
        private TextView tvPressure,tvHumidity,tvVisiblity;
        //第五個 Astronomy 天文
        private SunView sunView;
        //第六個
        private TableRow tableAdd;
        private WeatherChartView chartView;
        private WeatherIconView weatherIconView1,weatherIconView2,weatherIconView3,weatherIconView4,weatherIconView5;
        private TextView tvDay1,tvDay2,tvDay3,tvDay4,tvDay5;
        private TextView tvTemp1,tvTemp2,tvTemp3,tvTemp4,tvTemp5;
        private TextView tvFive,tvTen;


        public ViewHolder(View itemView,int viewType) {
            super(itemView);
            if(viewType==0){
                tvTemp=(TextView)itemView.findViewById(R.id.tvTemp);
                tvLow=(TextView)itemView.findViewById(R.id.tvLow);
                tvHigh=(TextView)itemView.findViewById(R.id.tvHigh);
                tvWeather=(TextView)itemView.findViewById(R.id.tvWeather);
                weatherIconView = (WeatherIconView) itemView.findViewById(R.id.my_weather_icon);
            }
            if(viewType==1){
                /*tvDay = (TextView) itemView.findViewById(R.id.tv_condition);
                tvDate = (TextView) itemView.findViewById(R.id.tv_date);
                tvLocation = (TextView) itemView.findViewById(R.id.tv_location);*/
                tv_low = (TextView) itemView.findViewById(R.id.tv_low);
                tv_high = (TextView) itemView.findViewById(R.id.tv_high);
                tv_temp = (TextView) itemView.findViewById(R.id.tv_temp);
                tvChill = (TextView) itemView.findViewById(R.id.tvChill);
                //imageView = (ImageView) itemView.findViewById(R.id.imageView);
                temperatureView = (TemperatureView) itemView.findViewById(R.id.temperatureView);
                tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);
            }
            else if(viewType==2){
                windView=(WindView)itemView.findViewById(R.id.windView);
            }
            else if(viewType==3){
                tvPressure = (TextView) itemView.findViewById(R.id.tvPressure);
                tvHumidity = (TextView) itemView.findViewById(R.id.tvHumidity);
                tvVisiblity = (TextView) itemView.findViewById(R.id.tvVisiblity);
            }
            else if(viewType==4){
                sunView=(SunView)itemView.findViewById(R.id.sunView);
            }
            else if(viewType==5){
                tableAdd=(TableRow)itemView.findViewById(R.id.tableAdd);
                chartView = (WeatherChartView)itemView.findViewById(R.id.weatherChartView);
                weatherIconView1 = (WeatherIconView) itemView.findViewById(R.id.weatherIconView1);
                weatherIconView2 = (WeatherIconView) itemView.findViewById(R.id.weatherIconView2);
                weatherIconView3 = (WeatherIconView) itemView.findViewById(R.id.weatherIconView3);
                weatherIconView4 = (WeatherIconView) itemView.findViewById(R.id.weatherIconView4);
                weatherIconView5 = (WeatherIconView) itemView.findViewById(R.id.weatherIconView5);
                tvDay1=(TextView)itemView.findViewById(R.id.tvDay1);
                tvDay2=(TextView)itemView.findViewById(R.id.tvDay2);
                tvDay3=(TextView)itemView.findViewById(R.id.tvDay3);
                tvDay4=(TextView)itemView.findViewById(R.id.tvDay4);
                tvDay5=(TextView)itemView.findViewById(R.id.tvDay5);
                tvTemp1=(TextView)itemView.findViewById(R.id.tvTemp1);
                tvTemp2=(TextView)itemView.findViewById(R.id.tvTemp2);
                tvTemp3=(TextView)itemView.findViewById(R.id.tvTemp3);
                tvTemp4=(TextView)itemView.findViewById(R.id.tvTemp4);
                tvTemp5=(TextView)itemView.findViewById(R.id.tvTemp5);
                tvFive=(TextView)itemView.findViewById(R.id.tvFive);
                tvTen=(TextView)itemView.findViewById(R.id.tvTen);
            }
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder{

        public HeaderHolder(View itemView) {
            super(itemView);

        }
    }
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Toast.makeText(context,mPosition+" "+viewType+" ",Toast.LENGTH_SHORT).show();
//        if (viewType == TYPE_HEADER) {
//            return new HeaderHolder(LayoutInflater.from(context).inflate(R.layout.rv_header_weather_now, parent, false));
//        }
//         else if(viewType==1)
//            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item1, parent, false),viewType);
//         else if(viewType==2)
//            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item2, parent, false),viewType);
//        else if(viewType==9)
//            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item10, parent, false),viewType);
//        else
//            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_weather_now, parent, false),viewType);
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Toast.makeText(mContext,mPosition+" "+viewType+" ",Toast.LENGTH_SHORT).show();
        if (viewType == TYPE_HEADER) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_header_weather_now, parent, false),viewType);
        }
        else if(viewType==1)
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item1, parent, false),viewType);
        else if(viewType==2)
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_wind, parent, false),viewType);
        else if(viewType==3)
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_atmosphere, parent, false),viewType);
        else if(viewType==4)
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_astronomy, parent, false),viewType);
        else if(viewType==5) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.rv_forecast, parent, false);
            final ViewHolder viewHolder = new ViewHolder(v,viewType);


            //對每一個cell註冊點擊事件
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index;
                    LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.linearLayout);
                    View subView = LayoutInflater.from(v.getContext()).inflate(R.layout.rv_forecast_add_layout, (ViewGroup)v, false);

                    WeatherChartView chartView;
                    WeatherIconView weatherIconView1,weatherIconView2,weatherIconView3,weatherIconView4,weatherIconView5;
                    TextView tvDay1,tvDay2,tvDay3,tvDay4,tvDay5;
                    TextView tvTemp1,tvTemp2,tvTemp3,tvTemp4,tvTemp5;
                    chartView = (WeatherChartView)subView.findViewById(R.id.weatherChartView);
                    weatherIconView1 = (WeatherIconView) subView.findViewById(R.id.weatherIconView1);
                    weatherIconView2 = (WeatherIconView) subView.findViewById(R.id.weatherIconView2);
                    weatherIconView3 = (WeatherIconView) subView.findViewById(R.id.weatherIconView3);
                    weatherIconView4 = (WeatherIconView) subView.findViewById(R.id.weatherIconView4);
                    weatherIconView5 = (WeatherIconView) subView.findViewById(R.id.weatherIconView5);
                    tvDay1=(TextView)subView.findViewById(R.id.tvDay1);
                    tvDay2=(TextView)subView.findViewById(R.id.tvDay2);
                    tvDay3=(TextView)subView.findViewById(R.id.tvDay3);
                    tvDay4=(TextView)subView.findViewById(R.id.tvDay4);
                    tvDay5=(TextView)subView.findViewById(R.id.tvDay5);
                    tvTemp1=(TextView)subView.findViewById(R.id.tvTemp1);
                    tvTemp2=(TextView)subView.findViewById(R.id.tvTemp2);
                    tvTemp3=(TextView)subView.findViewById(R.id.tvTemp3);
                    tvTemp4=(TextView)subView.findViewById(R.id.tvTemp4);
                    tvTemp5=(TextView)subView.findViewById(R.id.tvTemp5);
                    Cursor cl8 = mAccess.getData("Forecast",null,null);
                    cl8.moveToFirst();
                    int arr_high[]=new int [5],arr_low[]=new int [5],code[]=new int[5];
                    for(int i=0;i<5;i++){
                        cl8.moveToPosition(i+5);
                        arr_high[i]=cl8.getShort(3);
                        arr_low[i]=cl8.getShort(4);
                    }
                    if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals("")) {
                        // set day
                        chartView.setTempDay(new int[]{(int)Math.round((arr_high[0]-32)*5/9.), (int)Math.round((arr_high[1]-32)*5/9.), (int)Math.round((arr_high[2]-32)*5/9.), (int)Math.round((arr_high[3]-32)*5/9.), (int)Math.round((arr_high[4]-32)*5/9.)});
                        // set night
                        chartView.setTempNight(new int[]{(int)Math.round((arr_low[0]-32)*5/9.), (int)Math.round((arr_low[1]-32)*5/9.),(int)Math.round((arr_low[2]-32)*5/9.), (int)Math.round((arr_low[3]-32)*5/9.), (int)Math.round((arr_low[4]-32)*5/9.)});
                        chartView.invalidate();
                    }else{
                        // set day
                        chartView.setTempDay(new int[]{arr_high[0], arr_high[1], arr_high[2], arr_high[3], arr_high[4]});
                        // set night
                        chartView.setTempNight(new int[]{arr_low[0], arr_low[1], arr_low[2], arr_low[3], arr_low[4]});
                        chartView.invalidate();
                    }
                    for(int i=0;i<5;i++){
                        cl8.moveToPosition(i+5);
                        //Log.e("Code"+(i+5),cl8.getShort(5)+" "+cl8.getString(1));
                        if(i==0) {
                            //set Day
                            tvDay1.setText(day(cl8.getString(2)));
                            //set temperature
                            if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals(""))
                                tvTemp1.setText((int)Math.round((cl8.getShort(4)-32)*5/9.)+"°/"+(int)Math.round((cl8.getShort(3)-32)*5/9.)+"°");
                            else
                                tvTemp1.setText(cl8.getShort(4)+"°/"+cl8.getShort(3)+"°");
                            //set icon
                            weatherIconView1.setIconSize(25);
                            weatherIconView1.setIconColor(Color.WHITE);
                            weatherIconView1.setIconResource(weatherIcon(cl8.getShort(5)));
                        }
                        if(i==1) {
                            //set Day
                            tvDay2.setText(day(cl8.getString(2)));
                            //set temperature
                            if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals(""))
                                tvTemp2.setText((int)Math.round((cl8.getShort(4)-32)*5/9.)+"°/"+(int)Math.round((cl8.getShort(3)-32)*5/9.)+"°");
                            else
                                tvTemp2.setText(cl8.getShort(4)+"°/"+cl8.getShort(3)+"°");
                            //set icon
                            weatherIconView2.setIconSize(25);
                            weatherIconView2.setIconColor(Color.WHITE);
                            weatherIconView2.setIconResource(weatherIcon(cl8.getShort(5)));
                        }
                        if(i==2) {
                            //set Day
                            tvDay3.setText(day(cl8.getString(2)));
                            //set Temperature
                            if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals(""))
                                tvTemp3.setText((int)Math.round((cl8.getShort(4)-32)*5/9.)+"°/"+(int)Math.round((cl8.getShort(3)-32)*5/9.)+"°");
                            else
                                tvTemp3.setText(cl8.getShort(4)+"°/"+cl8.getShort(3)+"°");
                            //set icon
                            weatherIconView3.setIconSize(25);
                            weatherIconView3.setIconColor(Color.WHITE);
                            weatherIconView3.setIconResource(weatherIcon(cl8.getShort(5)));
                        }
                        if(i==3) {
                            //set Day
                            tvDay4.setText(day(cl8.getString(2)));
                            //set temperature
                            if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals(""))
                                tvTemp4.setText((int)Math.round((cl8.getShort(4)-32)*5/9.)+"°/"+(int)Math.round((cl8.getShort(3)-32)*5/9.)+"°");
                            else
                                tvTemp4.setText(cl8.getShort(4)+"°/"+cl8.getShort(3)+"°");
                            //set icon
                            weatherIconView4.setIconSize(25);
                            weatherIconView4.setIconColor(Color.WHITE);
                            weatherIconView4.setIconResource(weatherIcon(cl8.getShort(5)));
                        }
                        if(i==4) {
                            //set Day
                            tvDay5.setText(day(cl8.getString(2)));
                            //set temperature
                            if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals(""))
                                tvTemp5.setText((int)Math.round((cl8.getShort(4)-32)*5/9.)+"°/"+(int)Math.round((cl8.getShort(3)-32)*5/9.)+"°");
                            else
                                tvTemp5.setText(cl8.getShort(4)+"°/"+cl8.getShort(3)+"°");
                            //set icon
                            weatherIconView5.setIconSize(25);
                            weatherIconView5.setIconColor(Color.WHITE);
                            weatherIconView5.setIconResource(weatherIcon(cl8.getShort(5)));
                        }
                    }



                    //利用單元控制的標記值就標記為單元格的單元格，而不是單元格的單元格。標記值也就不存在了。
                    //如果不取消重用，那麼將會出現未曾點擊就已經添加子視圖的效果，再點擊的時間會繼續添加而不是收回。
                    if (v.findViewById(R.id.linearLayout).getTag() == null) {
                        index = 1;
                    } else {
                        index = (int)v.findViewById(R.id.linearLayout).getTag();
                    }

                    int oldScrollOpen=WeatherNowActivity.mScrollerY;
                    int oldScrollCloss=WeatherNowActivity.mScrollerY;
                    // close狀態: 增加內容
                    if (index == 1) {
                        oldScrollOpen=WeatherNowActivity.mScrollerY;
                        if(oldScrollCloss!=WeatherNowActivity.mScrollerY)
                            WeatherNowActivity.mScrollerY+=702;//修正背景虛化收起模糊Bug
                        linearLayout.addView(subView);
                        subView.setTag(1000);
                        v.findViewById(R.id.linearLayout).setTag(2);
                        //5日10日文字顏色高亮
                        viewHolder.tvTen.setTextColor(mContext.getResources().getColor(R.color.tvForecast1));
                        viewHolder.tvFive.setTextColor(mContext.getResources().getColor(R.color.tvForecast2));
                    } else {
                        oldScrollCloss=WeatherNowActivity.mScrollerY;
                        // open狀態： 移除增加內容
                        linearLayout.removeView(v.findViewWithTag(1000));
                        v.findViewById(R.id.linearLayout).setTag(1);
                        //5日10日文字顏色高亮
                        viewHolder.tvTen.setTextColor(mContext.getResources().getColor(R.color.tvForecast2));
                        viewHolder.tvFive.setTextColor(mContext.getResources().getColor(R.color.tvForecast1));
                        //Log.d("Scro@@@",WeatherNowActivity.mScrollerY+"");
                        if(WeatherNowActivity.mScrollerY!=oldScrollOpen||WeatherNowActivity.mScrollerY==4718)
                            WeatherNowActivity.mScrollerY-=702;//修正背景虛化收起模糊Bug

                    }
                }
            });
            // 取消viewHolder的重用機制（滑出View自動收回成預設狀態index=0 close）
            viewHolder.setIsRecyclable(false);

            return viewHolder;
        }
        else
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item_weather_now, parent, false),viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        mPosition=position;
        mAccess = new DBAccessWeather(mContext, "weather", null, 1);
        settings=mContext.getSharedPreferences("Data",MODE_PRIVATE);
        //Toast.makeText(mContext,settings.getString("Temperature",""),Toast.LENGTH_LONG).show();
        if(mPosition==0){
            Cursor c = mAccess.getData("Condition", null, null);
            c.moveToFirst();
            Cursor c2 = mAccess.getData("Code", null, null);
            c2.moveToPosition(c.getShort(6));
            if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals("")) {
                viewHolder.tvHigh.setText(Math.round((c.getShort(3)-32)*5/9.)+"°");
                viewHolder.tvLow.setText(Math.round((c.getShort(4)-32)*5/9.)+"°");
                viewHolder.tvTemp.setText(Math.round((c.getShort(5)-32)*5/9.)+"°");
           }else{
                viewHolder.tvHigh.setText(c.getString(3)+"°");
                viewHolder.tvLow.setText(c.getString(4)+"°");
                viewHolder.tvTemp.setText(c.getString(5)+"°");
            }
            viewHolder.tvWeather.setText(c2.getString(1));

            viewHolder.weatherIconView.setIconSize(85);
            viewHolder.weatherIconView.setIconColor(Color.WHITE);
            //天氣圖示
            viewHolder.weatherIconView.setIconResource(weatherIcon(c.getShort(6)));


        }
        if(mPosition==1){
            Cursor c = mAccess.getData("Location", null, null);
            c.moveToFirst();
            final Cursor c2 = mAccess.getData("Condition", null, null);
            c2.moveToFirst();
            Cursor c3 = mAccess.getData("Wind", null, null);
            c3.moveToFirst();
            long temp=Math.round((c2.getShort(5)-32)*5/9.);
            if(temp<-9)
                viewHolder.temperatureView.start((temp-16));
            else if(temp<5)
                viewHolder.temperatureView.start(temp-12);
            else if(temp<11)
                viewHolder.temperatureView.start((temp-7));
            else if(temp<16)
                viewHolder.temperatureView.start((temp-4));
            else if(temp<20)
                viewHolder.temperatureView.start(temp-2);
            else if(temp<30&&temp>=25)
                viewHolder.temperatureView.start(temp+2);
            else if(temp>=35)
                viewHolder.temperatureView.start(temp+6);
            else if(temp>=30)
                viewHolder.temperatureView.start(temp+4);
            else
                viewHolder.temperatureView.start(temp);
            viewHolder.tvLocation.setText(c.getString(2));
            if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals("")) {
                viewHolder.tvChill.setText(mContext.getString(R.string.tvChill)+" "+Math.round((c3.getShort(1)-32)*5/9.)+"°C");
            }else{
                viewHolder.tvChill.setText(mContext.getString(R.string.tvChill)+" "+c3.getString(1)+"°F");
            }
            if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals("")) {
                viewHolder.tv_high.setText(Math.round((c2.getShort(3)-32)*5/9.)+"°");
                viewHolder.tv_low.setText(Math.round((c2.getShort(4)-32)*5/9.)+"°");
                viewHolder.tv_temp.setText(Math.round((c2.getShort(5)-32)*5/9.)+"°");
            }else{
                viewHolder.tv_high.setText(c2.getString(3)+"°");
                viewHolder.tv_low.setText(c2.getString(4)+"°");
                viewHolder.tv_temp.setText(c2.getString(5)+"°");
            }
            viewHolder.temperatureView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long temp=Math.round((c2.getShort(5)-32)*5/9.);
                    if(temp<-9)
                        viewHolder.temperatureView.start((temp-16));
                    else if(temp<5)
                        viewHolder.temperatureView.start(temp-12);
                    else if(temp<11)
                        viewHolder.temperatureView.start((temp-7));
                    else if(temp<16)
                        viewHolder.temperatureView.start((temp-4));
                    else if(temp<20)
                        viewHolder.temperatureView.start(temp-2);
                    else if(temp<30&&temp>=25)
                        viewHolder.temperatureView.start(temp+2);
                    else if(temp>=35)
                        viewHolder.temperatureView.start(temp+6);
                    else if(temp>=30)
                        viewHolder.temperatureView.start(temp+4);
                    else
                        viewHolder.temperatureView.start(temp);
                }
            });
        }
        else if(mPosition==2){
            Cursor c2 = mAccess.getData("Wind", null, null);
            c2.moveToFirst();
            Cursor c21 = mAccess.getData("Speed", null, null);
            c21.moveToFirst();
            //Speed
            int wind_speed=c2.getShort(3),speed_id=0;
            if(wind_speed<1) {
                speed_id=0;
            }
            if(wind_speed>=1&&wind_speed<=3) {
                speed_id=1;
            }
            if(wind_speed>=4&&wind_speed<=7) {
                speed_id=2;
            }
            if(wind_speed>=8&&wind_speed<=12) {
                speed_id=3;
            }
            if(wind_speed>=13&&wind_speed<=18) {
                speed_id=4;
            }
            if(wind_speed>=19&&wind_speed<=24) {
                speed_id=5;
            }
            if(wind_speed>=25&&wind_speed<=31) {
                speed_id=6;
            }
            if(wind_speed>=32&&wind_speed<=38) {
                speed_id=7;
            }
            if(wind_speed>=39&&wind_speed<=46) {
                speed_id=8;
            }
            if(wind_speed>=47&&wind_speed<=54) {
                speed_id=9;
            }
            if(wind_speed>=55&&wind_speed<=63) {
                speed_id=10;
            }
            if(wind_speed>=64&&wind_speed<=72) {
                speed_id=11;
            }
            if(wind_speed>=73) {
                speed_id=12;
            }
            c21.moveToPosition(speed_id);


            viewHolder.windView.setWindSpeed(c2.getShort(3));
            viewHolder.windView.setWindText(c2.getString(2));
            viewHolder. windView.setPressure(c21.getShort(1));
            viewHolder.windView.setPressureUnit(" "+mContext.getResources().getString(R.string.wind_class));
            viewHolder.windView.setBarometerText(c21.getString(2));
            viewHolder.windView.setWindSpeedUnit(" mph");
                viewHolder.windView.setTrendType(TrendType.UP);
            viewHolder.windView.start();
            viewHolder.windView.animateBaroMeter();
            viewHolder.windView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.windView.animateBaroMeter();
                }
            });
        }
        else if(mPosition==3){
            Cursor c = mAccess.getData("Atmosphere", null, null);
            c.moveToFirst();
            viewHolder.tvPressure.setText(c.getString(2)+" millibars");
            viewHolder.tvHumidity.setText(c.getString(1)+" %");
            viewHolder.tvVisiblity.setText(c.getString(3)+" km");
        }
        else if(mPosition==4){
            Cursor cl6 = mAccess.getData("Condition", null, null);
            cl6.moveToFirst();
            //取得系統時間 Fri, 10 Mar 2017 03:23 PM CST
            String str[]=cl6.getString(7).split(" "),time[]=str[4].split(":");
                String hour=time[0],minute=time[1];
                if(str[5].equals("PM")&&Integer.parseInt(time[0])!=12){
                    hour=Integer.parseInt(time[0])+12+"";
                }
                if(str[5].equals("AM")&&Integer.parseInt(time[0])==12)
                    hour="00";
            Cursor cl5 = mAccess.getData("Astronomy", null, null);
            cl5.moveToFirst();
            String str_start[]=cl5.getString(1).split(":");
            String str_end[]=cl5.getString(2).split(":");
            str_start[1]=str_start[1].split(" ")[0]; //把am去掉
            str_end[1]=str_end[1].split(" ")[0];//把pm去掉
            if(str_start[1].length()==1)//開始分補零
                str_start[1]="0"+str_start[1];
            //結束時間+12小時
            str_end[0]=Integer.parseInt(str_end[0])+12+"";
            if(str_end[1].length()==1)//結束分補零
                str_end[1]="0"+str_end[1];
            viewHolder.sunView.setStartTime("0"+str_start[0]+":"+str_start[1]);
            viewHolder.sunView.setEndTime(str_end[0]+":"+str_end[1]);
            viewHolder.sunView.setCurrentTime(hour+":"+minute);
            viewHolder.sunView.setArcSolidColor(mContext.getResources().getColor(R.color.ArcSolidColor));//拱型內部顏色
            viewHolder.sunView.setSunColor(mContext.getResources().getColor(R.color.SunColor));//拱型內部顏色
            viewHolder.sunView.setTimeTextColor(mContext.getResources().getColor(R.color.TimeTextColor));//字體顏色
        }
        else if(mPosition==5){
            Cursor cl8 = mAccess.getData("Forecast",null,null);
            cl8.moveToFirst();
            int arr_high[]=new int [5],arr_low[]=new int [5],code[]=new int[5];
            for(int i=0;i<5;i++){
                cl8.moveToPosition(i);
                arr_high[i]=cl8.getShort(3);
                arr_low[i]=cl8.getShort(4);
            }
            if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals("")) {
                // set day
                viewHolder.chartView.setTempDay(new int[]{(int)Math.round((arr_high[0]-32)*5/9.), (int)Math.round((arr_high[1]-32)*5/9.), (int)Math.round((arr_high[2]-32)*5/9.), (int)Math.round((arr_high[3]-32)*5/9.), (int)Math.round((arr_high[4]-32)*5/9.)});
                // set night
                viewHolder.chartView.setTempNight(new int[]{(int)Math.round((arr_low[0]-32)*5/9.), (int)Math.round((arr_low[1]-32)*5/9.),(int)Math.round((arr_low[2]-32)*5/9.), (int)Math.round((arr_low[3]-32)*5/9.), (int)Math.round((arr_low[4]-32)*5/9.)});
                viewHolder.chartView.invalidate();
            }else{
                // set day
                viewHolder.chartView.setTempDay(new int[]{arr_high[0], arr_high[1], arr_high[2], arr_high[3], arr_high[4]});
                // set night
                viewHolder.chartView.setTempNight(new int[]{arr_low[0], arr_low[1], arr_low[2], arr_low[3], arr_low[4]});
                viewHolder.chartView.invalidate();
            }
            for(int i=1;i<=5;i++){
                cl8.moveToPosition(i-1);
                //Log.e("Code"+(i-1),cl8.getShort(5)+" "+cl8.getString(1));
                if(i==1) {
                    //set Day
                    viewHolder.tvDay1.setText(mContext.getString(R.string.Today));
                    //set temperature
                    if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals(""))
                        viewHolder.tvTemp1.setText((int)Math.round((cl8.getShort(4)-32)*5/9.)+"°/"+(int)Math.round((cl8.getShort(3)-32)*5/9.)+"°");
                    else
                        viewHolder.tvTemp1.setText(cl8.getShort(4)+"°/"+cl8.getShort(3)+"°");
                    //set icon
                    viewHolder.weatherIconView1.setIconSize(25);
                    viewHolder.weatherIconView1.setIconColor(Color.WHITE);
                    viewHolder.weatherIconView1.setIconResource(weatherIcon(cl8.getShort(5)));
                }
                if(i==2) {
                    //set Day
                    viewHolder.tvDay2.setText(mContext.getString(R.string.Tomorrow));
                    //set temperature
                    if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals(""))
                        viewHolder.tvTemp2.setText((int)Math.round((cl8.getShort(4)-32)*5/9.)+"°/"+(int)Math.round((cl8.getShort(3)-32)*5/9.)+"°");
                    else
                        viewHolder.tvTemp2.setText(cl8.getShort(4)+"°/"+cl8.getShort(3)+"°");
                    //set icon
                    viewHolder.weatherIconView2.setIconSize(25);
                    viewHolder.weatherIconView2.setIconColor(Color.WHITE);
                    viewHolder.weatherIconView2.setIconResource(weatherIcon(cl8.getShort(5)));
                }
                if(i==3) {
                    //set Day
                    viewHolder.tvDay3.setText(day(cl8.getString(2)));
                    //set Temperature
                    if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals(""))
                        viewHolder.tvTemp3.setText((int)Math.round((cl8.getShort(4)-32)*5/9.)+"°/"+(int)Math.round((cl8.getShort(3)-32)*5/9.)+"°");
                    else
                        viewHolder.tvTemp3.setText(cl8.getShort(4)+"°/"+cl8.getShort(3)+"°");
                    //set icon
                    viewHolder.weatherIconView3.setIconSize(25);
                    viewHolder.weatherIconView3.setIconColor(Color.WHITE);
                    viewHolder.weatherIconView3.setIconResource(weatherIcon(cl8.getShort(5)));
                }
                if(i==4) {
                    //set Day
                    viewHolder.tvDay4.setText(day(cl8.getString(2)));
                    //set temperature
                    if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals(""))
                        viewHolder.tvTemp4.setText((int)Math.round((cl8.getShort(4)-32)*5/9.)+"°/"+(int)Math.round((cl8.getShort(3)-32)*5/9.)+"°");
                    else
                        viewHolder.tvTemp4.setText(cl8.getShort(4)+"°/"+cl8.getShort(3)+"°");
                    //set icon
                    viewHolder.weatherIconView4.setIconSize(25);
                    viewHolder.weatherIconView4.setIconColor(Color.WHITE);
                    viewHolder.weatherIconView4.setIconResource(weatherIcon(cl8.getShort(5)));
                }
                if(i==5) {
                    //set Day
                    viewHolder.tvDay5.setText(day(cl8.getString(2)));
                    //set temperature
                    if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals(""))
                    viewHolder.tvTemp5.setText((int)Math.round((cl8.getShort(4)-32)*5/9.)+"°/"+(int)Math.round((cl8.getShort(3)-32)*5/9.)+"°");
                    else
                    viewHolder.tvTemp5.setText(cl8.getShort(4)+"°/"+cl8.getShort(3)+"°");
                    //set icon
                    viewHolder.weatherIconView5.setIconSize(25);
                    viewHolder.weatherIconView5.setIconColor(Color.WHITE);
                    viewHolder.weatherIconView5.setIconResource(weatherIcon(cl8.getShort(5)));
                }
            }

        }
    }
    public String weatherIcon(int code){
        //天氣圖示
        if(code==0)
            return mContext.getString(R.string.wi_yahoo_0);
        else if(code==1)
            return mContext.getString(R.string.wi_yahoo_1);
        else if(code==2)
            return mContext.getString(R.string.wi_yahoo_2);
        else if(code==3)
            return mContext.getString(R.string.wi_yahoo_3);
        else if(code==4)
            return mContext.getString(R.string.wi_yahoo_4);
        else if(code==5)
            return mContext.getString(R.string.wi_yahoo_5);
        else if(code==6)
            return mContext.getString(R.string.wi_yahoo_6);
        else if(code==7)
            return mContext.getString(R.string.wi_yahoo_7);
        else if(code==8)
            return mContext.getString(R.string.wi_yahoo_8);
        else if(code==9)
            return mContext.getString(R.string.wi_yahoo_9);
        else if(code==10)
            return mContext.getString(R.string.wi_yahoo_10);
        else if(code==11)
            return mContext.getString(R.string.wi_yahoo_11);
        else if(code==12)
            return mContext.getString(R.string.wi_yahoo_12);
        else if(code==13)
            return mContext.getString(R.string.wi_yahoo_13);
        else if(code==14)
            return mContext.getString(R.string.wi_yahoo_14);
        else if(code==15)
            return mContext.getString(R.string.wi_yahoo_15);
        else if(code==16)
            return mContext.getString(R.string.wi_yahoo_16);
        else if(code==17)
            return mContext.getString(R.string.wi_yahoo_17);
        else if(code==18)
            return mContext.getString(R.string.wi_yahoo_18);
        else if(code==19)
            return mContext.getString(R.string.wi_yahoo_19);
        else if(code==20)
            return mContext.getString(R.string.wi_yahoo_20);
        else if(code==21)
            return mContext.getString(R.string.wi_yahoo_21);
        else if(code==22)
            return mContext.getString(R.string.wi_yahoo_22);
        else if(code==23)
            return mContext.getString(R.string.wi_yahoo_23);
        else if(code==24)
            return mContext.getString(R.string.wi_yahoo_24);
        else if(code==25)
            return mContext.getString(R.string.wi_yahoo_25);
        else if(code==26)
            return mContext.getString(R.string.wi_yahoo_26);
        else if(code==27)
            return mContext.getString(R.string.wi_yahoo_27);
        else if(code==28)
            return mContext.getString(R.string.wi_yahoo_28);
        else if(code==29)
            return mContext.getString(R.string.wi_yahoo_29);
        else if(code==30)
            return mContext.getString(R.string.wi_yahoo_30);
        else if(code==31)
            return mContext.getString(R.string.wi_yahoo_31);
        else if(code==32)
            return mContext.getString(R.string.wi_yahoo_32);
        else if(code==33)
            return mContext.getString(R.string.wi_yahoo_33);
        else if(code==34)
            return mContext.getString(R.string.wi_yahoo_34);
        else if(code==35)
            return mContext.getString(R.string.wi_yahoo_35);
        else if(code==36)
            return mContext.getString(R.string.wi_yahoo_36);
        else if(code==37)
            return mContext.getString(R.string.wi_yahoo_37);
        else if(code==38)
            return mContext.getString(R.string.wi_yahoo_38);
        else if(code==39)
            return mContext.getString(R.string.wi_yahoo_39);
        else if(code==40)
            return mContext.getString(R.string.wi_yahoo_40);
        else if(code==41)
            return mContext.getString(R.string.wi_yahoo_41);
        else if(code==42)
            return mContext.getString(R.string.wi_yahoo_42);
        else if(code==43)
            return mContext.getString(R.string.wi_yahoo_43);
        else if(code==44)
            return mContext.getString(R.string.wi_yahoo_44);
        else if(code==45)
            return mContext.getString(R.string.wi_yahoo_45);
        else if(code==46)
            return mContext.getString(R.string.wi_yahoo_46);
        else if(code==47)
            return mContext.getString(R.string.wi_yahoo_47);
        else
            return mContext.getString(R.string.wi_yahoo_3200);
    }
    public String day(String day){
        if(day.equals("Sun")){
            return mContext.getResources().getString(R.string.Sun);
        }
        else if(day.equals("Mon")){
            return mContext.getResources().getString(R.string.Mon);
        }
        else if(day.equals("Tue")){
            return mContext.getResources().getString(R.string.Tue);
        }
        else if(day.equals("Wed")){
            return mContext.getResources().getString(R.string.Wed);
        }
        else if(day.equals("Thu")){
            return mContext.getResources().getString(R.string.Thu);
        }
        else if(day.equals("Fri")){
            return mContext.getResources().getString(R.string.Fri);
        }
        else{
            return mContext.getResources().getString(R.string.Sat);
        }

    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//        mPosition=position;
//        if(mPosition==1){
//            viewHolder.
//            tvDate.setText(myWeather.forecastdate0);
//            tvLocation.setText(myWeather.city);
//            tvTemp.setText(myWeather.conditiontemp+" °C");
//            tvLowtemp.setText(myWeather.forecastlow0 +" °C");
//            tvHighttemp.setText(myWeather.forecasthigh0+" °C");
//            Toast.makeText(MainActivity.this,myWeather.forecasttext0+" ",Toast.LENGTH_SHORT).show();
//            if(myWeather.forecastdate0==null)
//                Toast.makeText(MainActivity.this,"連接網路好不",Toast.LENGTH_SHORT).show();
//            else if(myWeather.forecasttext0.toString().equals("Partly Cloudy"))
//            //imageView.setImageResource(R.drawable.c);
//        }
//    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return position;
        }

    }
}