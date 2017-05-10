package com.openweather.openweather;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.openweather.openweather.DataBase.DBAccessWeather;

public class TemperatureActivity extends AppCompatActivity {

    private WebView mWebView = null;
    private Toolbar toolbar;
    private DBAccessWeather mAccess;
    private TextView tvHumidity;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        ExitApplication.getInstance().addActivity(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        settings=getApplicationContext().getSharedPreferences("Data",MODE_PRIVATE);

        toolbar=(Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle("全台溫度分布圖");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAccess=new DBAccessWeather(this, "weather", null, 1);

        tvHumidity=(TextView)findViewById(R.id.tvHumidity);
        mWebView = (WebView)findViewById(R.id.webView);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setInitialScale(1);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://opendata.cwb.gov.tw/opendata/DIV2/O-A0038-001.jpg");
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

    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };
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

}
