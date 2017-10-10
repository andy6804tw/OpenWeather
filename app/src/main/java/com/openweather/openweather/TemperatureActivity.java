package com.openweather.openweather;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.openweather.openweather.DataBase.DBAccessWeather;

import java.io.InputStream;
import java.util.Calendar;

import io.fabric.sdk.android.Fabric;

public class TemperatureActivity extends AppCompatActivity {

    private ImageView imageView;
    private Toolbar toolbar;
    private DBAccessWeather mAccess;
    private TextView tvHumidity;
    private SharedPreferences settings;
    private String mDate="";
    private Boolean mCheck=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        ExitApplication.getInstance().addActivity(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Fabric.with(this, new Crashlytics());
        settings=getApplicationContext().getSharedPreferences("Data",MODE_PRIVATE);

        toolbar=(Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle("全台溫度分布圖");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAccess=new DBAccessWeather(this, "weather", null, 1);

        tvHumidity=(TextView)findViewById(R.id.tvHumidity);
        imageView=(ImageView)findViewById(R.id.imageView);
        initTime();
        //mWebView.loadUrl("http://opendata.cwb.gov.tw/opendata/DIV2/O-A0038-001.jpg");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor c = mAccess.getData("Atmosphere", null, null);
        c.moveToFirst();
        Cursor c3 = mAccess.getData("Wind", null, null);
        c3.moveToFirst();
        String des="",qul="",chill;
        if(c.getShort(1)>=20&&c.getShort(1)<40) {
            qul="生活品質: 乾燥\n\n";
            des = "皮膚乾燥，嘴唇特別容易乾燥脫皮，呼吸道黏膜上皮細胞損傷，發燒、喉咽痛、流鼻水、鼻塞、頭痛等症狀，咳嗽、咳痰、發燒、胸痛。";
        }
        else if(c.getShort(1)>=40&&c.getShort(1)<70) {
            qul="生活品質: 舒適濕度\n\n";
            des = "人體在室內感覺舒適的狀態，就廎前不妨泡泡腳促使足部和下肢的血管擴張，使大腦得以休息，有助于進入睡眠。";
        }
        else if(c.getShort(1)>=70&&c.getShort(1)<100) {
            qul="生活品質: 潮濕\n\n";
            des = "太過潮濕容易引發過敏性鼻炎、氣喘、久咳、浮腫、便秘、腹瀉等不適， 容易引發呼吸道、骨關節、腸胃道、心血管及精神方面等五類症狀及疾病。";
        }
        else{
            qul="生活品質: 極度乾燥\n\n";
            des = "乾燥的空氣使人表皮細胞脫水、皮脂腺分泌減少，導致皮膚粗糙起皺甚至開裂。因此，過敏性皮炎、皮膚瘙癢不適等過敏性疾病也都和空氣乾燥有關。空氣乾燥對人體免疫力也有不利影響。";
        }
        //體感溫度
        if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals("")) {
            chill=getApplicationContext().getString(R.string.tvChill)+" "+Math.round((c3.getShort(1)-32)*5/9.)+"°C\n\n";
        }else{
            chill=getApplicationContext().getString(R.string.tvChill)+" "+c3.getString(1)+"°F\n\n";
        }
        tvHumidity.setText(chill+"相對濕度: "+c.getString(1)+" %RH\n\n"+qul+des);

    }

    //返回
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //照片下載
    private void initTime() {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        //清空
        mDate="";
        //年
        mDate=year+"-";
        //月份
        if(Integer.toString((month+1)).length()==1)
            mDate+="0"+(month+1);
        else
            mDate+=(month+1);
        //日期(先判斷小時是否00)
        if(hour==0){
            hour=24;
            day-=1;
        }
        if(Integer.toString(day).length()==1)
            mDate+="-0"+day;
        else
            mDate+="-"+day;
        //小時並判斷錯誤偵測
        if(mCheck){
            if(Integer.toString((hour)).length()==1)
                mDate+="_0"+(hour)+"00";
            else
                mDate+="_"+(hour)+"00";
        }else{
            if(Integer.toString((hour-1)).length()==1)
                mDate+="_0"+(hour-1)+"00";
            else
                mDate+="_"+(hour-1)+"00";
        }
        Log.e("time",mDate);
        //載入圖片
        DownloadImageTask downloadImageTask=new DownloadImageTask(imageView);
        downloadImageTask.execute("http://www.cwb.gov.tw/V7/observe/temperature/Data/"+mDate+".GTP.jpg");


    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                mCheck=false;
                initTime();
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
