package com.openweather.openweather.WeatherNow;

/**
 * Created by andy6804tw on 2017/2/24.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ahmadnemati.wind.WindView;
import com.github.ahmadnemati.wind.enums.TrendType;
import com.openweather.openweather.DataBase.DBAccessWeather;
import com.openweather.openweather.R;
import com.openweather.openweather.View.TemperatureView;
import com.openweather.sunviewlibrary.SunView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by andy6804tw on 2017/1/25.
 */

public class WeatherNowRVA extends RecyclerView.Adapter<WeatherNowRVA.ViewHolder> {

    private final Context mContext;
    private static final int ITEM_COUNT = 5;

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
        public TextView tvTemp,tvHigh,tvLow,tvWeather;
        //第一個condition目前狀況
        private TextView tvLocation,tv_temp,tv_low,tv_high,tvChill;
        private TemperatureView temperatureView;
        //第三個wind風速與風向
        private WindView windView;
        //第四個atmosphere大氣與氣壓
        private TextView tvPressure,tvHumidity,tvVisiblity;
        //第五個 Astronomy 天文
        private SunView sunView;

        public ViewHolder(View itemView,int viewType) {
            super(itemView);
            if(viewType==0){
                tvTemp=(TextView)itemView.findViewById(R.id.tvTemp);
                tvLow=(TextView)itemView.findViewById(R.id.tvLow);
                tvHigh=(TextView)itemView.findViewById(R.id.tvHigh);
                tvWeather=(TextView)itemView.findViewById(R.id.tvWeather);
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
        else if(viewType==5)
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item2, parent, false),viewType);
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
                viewHolder.tvChill.setText(viewHolder.tvChill.getText()+" "+Math.round((c3.getShort(1)-32)*5/9.)+"°C");
            }else{
                viewHolder.tvChill.setText(viewHolder.tvChill.getText()+" "+c3.getString(1)+"°F");
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
            Cursor c21 = mAccess.getData("Direction", null, null);
            c21.moveToPosition(c2.getShort(2));
            viewHolder.windView.setWindSpeed(c2.getShort(3));
            viewHolder.windView.setWindText(c21.getString(1));
            viewHolder. windView.setPressure(3);
            viewHolder.windView.setPressureUnit(" 級");
            viewHolder.windView.setBarometerText("微風");
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
            viewHolder.sunView.setCurrentTime(hour+":"+minute);
            viewHolder.sunView.setStartTime("0"+str_start[0]+":"+str_start[1]);
            viewHolder.sunView.setEndTime(str_end[0]+":"+str_end[1]);
            viewHolder.sunView.setArcSolidColor(mContext.getResources().getColor(R.color.ArcSolidColor));//拱型內部顏色
            viewHolder.sunView.setSunColor(mContext.getResources().getColor(R.color.SunColor));//拱型內部顏色
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