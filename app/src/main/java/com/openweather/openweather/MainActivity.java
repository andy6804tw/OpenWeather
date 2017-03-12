package com.openweather.openweather;

import android.content.Intent;
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
import com.openweather.openweather.WeatherNow.WeatherNowActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridMenuFragment mGridMenuFragment;
    private long temptime = 0;//計算退出秒數

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExitApplication.getInstance().addActivity(this);

        TextView textView=(TextView) findViewById(R.id.textView);
        textView.setText(WeatherNowActivity.str5);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mGridMenuFragment = GridMenuFragment.newInstance(R.drawable.back);

        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.main_frame, mGridMenuFragment);
                tx.addToBackStack(null);
                tx.commit();
            }
        });

        setupGridMenu();

        mGridMenuFragment.setOnClickMenuListener(new GridMenuFragment.OnClickMenuListener() {
            @Override
            public void onClickMenu(GridMenu gridMenu, int position) {
                Toast.makeText(MainActivity.this, "Title:" + gridMenu.getTitle() + ", Position:" + position,
                        Toast.LENGTH_SHORT).show();
                if(position==0) {
                    Intent intent = new Intent(MainActivity.this, WeatherNowActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(position==2) {
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }

    private void setupGridMenu() {
        List<GridMenu> menus = new ArrayList<>();
        menus.add(new GridMenu("Home", R.drawable.home));
        menus.add(new GridMenu("Calendar", R.drawable.calendar));
        menus.add(new GridMenu("Overview", R.drawable.overview));
        menus.add(new GridMenu("Groups", R.drawable.groups));
        menus.add(new GridMenu("Lists", R.drawable.lists));
        menus.add(new GridMenu("Profile", R.drawable.profile));
        menus.add(new GridMenu("Timeline", R.drawable.timeline));
        menus.add(new GridMenu("Setting", R.drawable.settings));

        mGridMenuFragment.setupMenu(menus);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//手機按鈕事件
    {
        // TODO Auto-generated method stub
       if (1 == getSupportFragmentManager().getBackStackEntryCount()) {
            super.onBackPressed();
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
