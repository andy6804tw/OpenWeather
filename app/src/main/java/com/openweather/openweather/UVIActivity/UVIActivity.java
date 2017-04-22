package com.openweather.openweather.UVIActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.goka.blurredgridmenu.GridMenu;
import com.goka.blurredgridmenu.GridMenuFragment;
import com.openweather.openweather.DataBase.DBAccessEnvironment;
import com.openweather.openweather.ExitApplication;
import com.openweather.openweather.Main2Activity;
import com.openweather.openweather.Pm25Activity;
import com.openweather.openweather.R;
import com.openweather.openweather.Settings.SettingsActivity;
import com.openweather.openweather.WeatherNowActivity.WeatherNowActivity;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.BreathingViewHelper;

public class UVIActivity extends AppCompatActivity {

    private GridMenuFragment mGridMenuFragment;
    private long temptime = 0;//計算退出秒數
    DBAccessEnvironment mAccess2;
    private TextView tvUv,tvCity,tvLocation,tvPublishTime,tvPublishAgency,tvSiteName,tvStr,tvSuggest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvi);
        ExitApplication.getInstance().addActivity(this);
        mAccess2= new DBAccessEnvironment(this, "Environment", null, 1);
        Cursor c = mAccess2.getData("Ultraviolet", null, null);
        c.moveToFirst();
        Toast.makeText(UVIActivity.this,c.getString(1)+" "+c.getString(2)+" "+c.getString(3)+" "+c.getString(4),Toast.LENGTH_SHORT).show();
        Cursor cl1 = mAccess2.getData("Location", null, null);
        cl1.moveToFirst();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        mGridMenuFragment = GridMenuFragment.newInstance(R.mipmap.bguvi2);
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
    }
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
                Toast.makeText(UVIActivity.this,"Coming soon",Toast.LENGTH_SHORT).show();
            }
        });

        setupGridMenu();

        mGridMenuFragment.setOnClickMenuListener(new GridMenuFragment.OnClickMenuListener() {
            @Override
            public void onClickMenu(GridMenu gridMenu, int position) {

                if(position==0) {
                    Intent intent = new Intent(UVIActivity.this, WeatherNowActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(position==1) {
                    Intent intent = new Intent(UVIActivity.this, Main2Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                if(position==3) {
                    Intent intent = new Intent(UVIActivity.this, Pm25Activity.class);
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
                        Toast.makeText(UVIActivity.this,"無安裝此應用!",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.f74372017.twreservoir")));
                    }
                }
                if(position==7) {
                    Intent intent = new Intent(UVIActivity.this, SettingsActivity.class);
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
