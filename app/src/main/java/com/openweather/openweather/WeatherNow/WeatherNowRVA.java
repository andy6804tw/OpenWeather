package com.openweather.openweather.WeatherNow;

/**
 * Created by andy6804tw on 2017/2/24.
 */

import android.content.Context;
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

/**
 * Created by andy6804tw on 2017/1/25.
 */

public class WeatherNowRVA extends RecyclerView.Adapter<WeatherNowRVA.ViewHolder> {

    private final Context mContext;
    private static final int ITEM_COUNT = 10;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    int mPosition=0;
    private DBAccessWeather mAccess;

    public WeatherNowRVA(Context context) {
        this.mContext = context;
    }




    class ViewHolder extends RecyclerView.ViewHolder{
        //第零個
        public TextView tvTemp,tvHigh,tvLow;
        //第一個condition目前狀況
        private TextView tvLocation,tv_temp,tv_low,tv_high,tvChill;
        private TemperatureView temperatureView;
        //第三個wind風速與風向
        private WindView windView;
        //第四個atmosphere大氣與氣壓
        private TextView tvPressure,tvHumidity,tvVisiblity;

        public ViewHolder(View itemView,int viewType) {
            super(itemView);
            if(viewType==0){
                tvTemp=(TextView)itemView.findViewById(R.id.tvTemp);
                tvLow=(TextView)itemView.findViewById(R.id.tvLow);
                tvHigh=(TextView)itemView.findViewById(R.id.tvHigh);
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
        else
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item_weather_now, parent, false),viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        mPosition=position;
        mAccess = new DBAccessWeather(mContext, "weather", null, 1);
        if(mPosition==0){
            Cursor c = mAccess.getData("Condition", null, null);
            c.moveToFirst();
            viewHolder.tvHigh.setText(c.getString(3)+"°");
            viewHolder.tvLow.setText(c.getString(4)+"°");
            viewHolder.tvTemp.setText(c.getString(5)+"°");
            }
        if(mPosition==1){
            Cursor c = mAccess.getData("Location", null, null);
            c.moveToFirst();
            final Cursor c2 = mAccess.getData("Condition", null, null);
            c2.moveToFirst();
            Cursor c3 = mAccess.getData("Wind", null, null);
            c3.moveToFirst();
            if(c2.getShort(5)<-9)
                viewHolder.temperatureView.start((c2.getShort(5)-16));
            else if(c2.getShort(5)<5)
                viewHolder.temperatureView.start(c2.getShort(5)-12);
            else if(c2.getShort(5)<11)
                viewHolder.temperatureView.start((c2.getShort(5)-7));
            else if(c2.getShort(5)<16)
                viewHolder.temperatureView.start((c2.getShort(5)-4));
            else if(c2.getShort(5)<20)
                viewHolder.temperatureView.start(c2.getShort(5)-2);
            else if(c2.getShort(5)>=30)
                viewHolder.temperatureView.start(c2.getShort(5)+4);
            else if(c2.getShort(5)>=35)
                viewHolder.temperatureView.start(c2.getShort(5)+6);
            else if(c2.getShort(5)<30&&c2.getShort(5)>=25)
                viewHolder.temperatureView.start(c2.getShort(5)+2);
            else
                viewHolder.temperatureView.start(c2.getShort(5));
            viewHolder.tv_high.setText(c2.getString(3)+"°C");
            viewHolder.tv_low.setText(c2.getString(4)+"°C");
            viewHolder.tv_temp.setText(c2.getString(5)+"°C");
            viewHolder.tvLocation.setText(c.getString(2));
            viewHolder.tvChill.setText("體感溫度:"+c3.getString(1)+"°C");
            viewHolder.temperatureView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(c2.getShort(5)<-9)
                        viewHolder.temperatureView.start((c2.getShort(5)-16));
                    else if(c2.getShort(5)<5)
                        viewHolder.temperatureView.start(c2.getShort(5)-12);
                    else if(c2.getShort(5)<11)
                        viewHolder.temperatureView.start((c2.getShort(5)-7));
                    else if(c2.getShort(5)<16)
                        viewHolder.temperatureView.start((c2.getShort(5)-4));
                    else if(c2.getShort(5)<20)
                        viewHolder.temperatureView.start(c2.getShort(5)-2);
                    else if(c2.getShort(5)<30&&c2.getShort(5)>=25)
                        viewHolder.temperatureView.start(c2.getShort(5)+2);
                    else if(c2.getShort(5)>=35)
                        viewHolder.temperatureView.start(c2.getShort(5)+6);
                    else if(c2.getShort(5)>=30)
                        viewHolder.temperatureView.start(c2.getShort(5)+4);
                    else
                        viewHolder.temperatureView.start(c2.getShort(5));
                }
            });
        }
        else if(mPosition==2){
            Cursor c = mAccess.getData("Wind", null, null);
            c.moveToFirst();
            viewHolder.windView.setWindSpeed(c.getShort(3));
            viewHolder.windView.setWindText("東北");
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