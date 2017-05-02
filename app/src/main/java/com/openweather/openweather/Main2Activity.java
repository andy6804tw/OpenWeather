package com.openweather.openweather;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.goka.blurredgridmenu.GridMenu;
import com.goka.blurredgridmenu.GridMenuFragment;
import com.openweather.openweather.Settings.SettingsActivity;
import com.openweather.openweather.UVIActivity.UVIActivity;
import com.openweather.openweather.WeatherNowActivity.WeatherNowActivity;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private GridMenuFragment mGridMenuFragment;
    private long temptime = 0;//計算退出秒數
    private WebView mWebView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ExitApplication.getInstance().addActivity(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mGridMenuFragment = GridMenuFragment.newInstance(R.drawable.bg_fragment_5);

        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.main_frame, mGridMenuFragment);
                tx.addToBackStack(null);
                tx.commit();
            }
        });

        menu_init();


        mWebView = (WebView)findViewById(R.id.webView);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setInitialScale(1);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://opendata.cwb.gov.tw/opendata/DIV2/O-A0038-001.jpg");
    }

    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };

    /**選單Menu**/
    //menu初始化
    private void menu_init(){

        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.main_frame, mGridMenuFragment);
                tx.addToBackStack(null);
                tx.commit();
               // Toast.makeText(Main2Activity.this,"Coming soon",Toast.LENGTH_SHORT).show();
            }
        });

        setupGridMenu();

        mGridMenuFragment.setOnClickMenuListener(new GridMenuFragment.OnClickMenuListener() {
            @Override
            public void onClickMenu(GridMenu gridMenu, int position) {

                if(position==0) {
                    Intent intent = new Intent(Main2Activity.this, WeatherNowActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(position==2) {
                    Intent intent = new Intent(Main2Activity.this, UVIActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                if(position==3) {
                    Intent intent = new Intent(Main2Activity.this, Pm25Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(position==4){
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setComponent(new ComponentName("com.f74372017.twreservoir","com.f74372017.twreservoir.SplashActivity")); //包裹名稱，要開啟的頁面
                    //Intent intent = getApplication().getPackageManager()
                    //       .getLaunchIntentForPackage("com.f74372017.twreservoir.SplashActivity");
                    //intent.putExtra("value", "test"); //要傳送的值
                    try{
                        startActivity(intent);
                    }
                    catch(Exception e){
                        Toast.makeText(Main2Activity.this,"無安裝此應用!",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.f74372017.twreservoir")));
                    }
                }
                if(position==7) {
                    Intent intent = new Intent(Main2Activity.this, SettingsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }
    private void setupGridMenu() {
        List<GridMenu> menus = new ArrayList<>();
        menus.add(new GridMenu("即時天氣", R.mipmap.weather_now));
        menus.add(new GridMenu("氣溫圖", R.mipmap.temperature));
        menus.add(new GridMenu("紫外線", R.mipmap.uvi));
        menus.add(new GridMenu("全台PM2.5", R.mipmap.mask));
        menus.add(new GridMenu("即時水庫", R.mipmap.water));
        //menus.add(new GridMenu("Profile", R.drawable.profile));
        //menus.add(new GridMenu("Timeline", R.drawable.timeline));
        //menus.add(new GridMenu("Setting", R.mipmap.settings));

        mGridMenuFragment.setupMenu(menus);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//手機按鈕事件
    {
        // TODO Auto-generated method stub
        if (1 == getSupportFragmentManager().getBackStackEntryCount()) {
            getSupportFragmentManager().popBackStack();
            return true;
        }else{
            if((keyCode == KeyEvent.KEYCODE_BACK)&&(event.getAction() == KeyEvent.ACTION_DOWN))
            {
                if(System.currentTimeMillis() - temptime >2000) // 2s內再次選擇back有效
                {
                    System.out.println(Toast.LENGTH_LONG);
                    Toast.makeText(this, "再按一次離開", Toast.LENGTH_LONG).show();
                    temptime = System.currentTimeMillis();
                }
                else {
                    ExitApplication.getInstance().exit();
                }

                return true;

            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
