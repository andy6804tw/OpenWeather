package com.openweather.openweather.WeatherNow;

/**
 * Created by andy6804tw on 2017/2/24.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    public WeatherNowRVA(Context context) {
        this.mContext = context;
    }




    class ViewHolder extends RecyclerView.ViewHolder{
        //第零個
        public TextView tv_temp;
        //第一個
        private TextView tvDay;
        private TextView tvDate;
        private TextView tvLocation;
        private TextView tvTemp;
        private TextView tvLowtemp;
        private TextView tvHighttemp;
        private ImageView imageView;
        private TemperatureView temperatureView;

        public ViewHolder(View itemView,int viewType) {
            super(itemView);
            if(viewType==0){
                tv_temp=(TextView)itemView.findViewById(R.id.tv_temp);
            }
            if(viewType==1){
                /*tvDay = (TextView) itemView.findViewById(R.id.tv_condition);
                tvDate = (TextView) itemView.findViewById(R.id.tv_date);
                tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
                tvTemp = (TextView) itemView.findViewById(R.id.tv_temp);
                tvLowtemp = (TextView) itemView.findViewById(R.id.tv_lowtemp);
                tvHighttemp = (TextView) itemView.findViewById(R.id.tv_highttemp);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);*/
                temperatureView = (TemperatureView) itemView.findViewById(R.id.temperatureView);
                tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
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
        Toast.makeText(mContext,mPosition+" "+viewType+" ",Toast.LENGTH_SHORT).show();
        if (viewType == TYPE_HEADER) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_header_weather_now, parent, false),viewType);
        }
        else if(viewType==1)
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item1, parent, false),viewType);
        else if(viewType==2)
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item2, parent, false),viewType);
        else if(viewType==9)
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item10, parent, false),viewType);
        else
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item_weather_now, parent, false),viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        mPosition=position;
        if(mPosition==0){
            //viewHolder.tv_temp.setText("15");
            }
        if(mPosition==1){
            /*viewHolder.tvDay.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/NotoSans-Regular_0.ttf"));
            viewHolder.tvDate.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/NotoSans-Regular_0.ttf"));
            viewHolder.tvLocation.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/NotoSans-Regular_0.ttf"));
            viewHolder.tvTemp.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/NotoSans-Regular_0.ttf"));
            viewHolder.tvLowtemp.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/NotoSans-Regular_0.ttf"));
            viewHolder.tvHighttemp.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/NotoSans-Regular_0.ttf"));
            viewHolder.tvDate.setText(MainActivity.mDate);
            viewHolder.tvDay.setText(MainActivity.mDay);
            viewHolder.tvLocation.setText(MainActivity.mLocation);
            viewHolder.tvTemp.setText(MainActivity.mTemp);
            viewHolder.tvLowtemp.setText(MainActivity.mLowTemp);
            viewHolder.tvHighttemp.setText(MainActivity.mHightTemp);
            if(MainActivity.mWeather==null)
                Toast.makeText(mContext,"連接網路好不",Toast.LENGTH_SHORT).show();
            else if(MainActivity.mWeather.equals("Partly Cloudy"))
                viewHolder.imageView.setImageResource(R.drawable.c);
            else if(MainActivity.mWeather.equals("Mostly Cloudy"))
                viewHolder.imageView.setImageResource(R.drawable.b);*/
            viewHolder.temperatureView.start(23);
            viewHolder.tvLocation.setText("123");
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