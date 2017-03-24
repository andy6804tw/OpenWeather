package com.openweather.openweather.UVIActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.openweather.openweather.R;

import me.drakeet.BreathingViewHelper;

public class UVIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvi);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    // start
        BreathingViewHelper.setBreathingBackgroundColor(
                findViewById(R.id.relativelatout),
                getResources().getColor(R.color.alermUVI)
        );
    // stop
        //BreathingViewHelper.stopBreathingBackgroundColor(findViewById(R.id.relativelatout));
    }
}
