package com.openweather.openweather.Settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.openweather.openweather.R;
import com.xiaoyongit.settingview.SettingView;
import com.xiaoyongit.settingview.entity.SettingModel;
import com.xiaoyongit.settingview.entity.SettingViewItem;
import com.xiaoyongit.settingview.item.BasicItemViewH;
import com.xiaoyongit.settingview.item.CheckItemViewH;
import com.xiaoyongit.settingview.item.SwitchItemView;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private SettingView mSettingView1 = null;
    private SettingView mSettingView2 = null;

    private SettingModel mItemData = null;
    private SettingViewItem mItemViewData = null;
    private List<SettingViewItem> mListData = new ArrayList<SettingViewItem>();
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;
        mSettingView1 = (SettingView) findViewById(R.id.ios_style_setting_view_01);
        mSettingView2 = (SettingView) findViewById(R.id.ios_style_setting_view_02);

        mSettingView1.setOnSettingViewItemClickListener(new SettingView.onSettingViewItemClickListener() {

            @Override
            public void onItemClick(int index) {

                Toast.makeText(mContext, "第" + index + "項被點擊", Toast.LENGTH_SHORT).show();
                if (index == 4) {
                    mSettingView1.setItemSubTitle("中華電信", 5);

                }
                else if(index==1){
                    mSettingView1.setItemSubTitle("°F",2);
                }
                else if (index == 3) {
                    mSettingView1.setItemSubTitle("移動",4);
                }
                else if (index == 2) {
                    mSettingView1.setItemSubTitle("關閉",3);
                }
            }
        });

        mSettingView1.setOnSettingViewItemSwitchListener(new SettingView.onSettingViewItemSwitchListener() {

            @Override
            public void onSwitchChanged(int index, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(mContext, "第" + index + "項打開", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "第" + index + "項關閉", Toast.LENGTH_SHORT).show();
                }
            }
        });

        initView(mContext);
    }

    private void initView(Context context) {
		/* ==========================SettingView1========================== */
        mItemViewData = new SettingViewItem();
        mItemData = new SettingModel();
        mItemData.setTitle("24小時制");
        mItemData.setDrawable(getResources().getDrawable(R.mipmap.icon07));
        mItemData.setChecked(true);

        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new SwitchItemView(context));
        mListData.add(mItemViewData);

        mItemViewData = new SettingViewItem();
        mItemData = new SettingModel();
        mItemData.setTitle("溫度");
        mItemData.setSubTitle("°C");
        mItemData.setDrawable(getResources().getDrawable(R.mipmap.icon02));

        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new BasicItemViewH(context));
        mListData.add(mItemViewData);

        mItemViewData = new SettingViewItem();
        mItemData = new SettingModel();
        mItemData.setTitle("GPS");
        mItemData.setSubTitle("開啟");

        mItemData.setDrawable(getResources().getDrawable(R.mipmap.icon02));

        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new BasicItemViewH(context));
        mListData.add(mItemViewData);

        mItemViewData = new SettingViewItem();
        mItemData = new SettingModel();
        mItemData.setTitle("天氣");
        mItemData.setSubTitle(" ");
        mItemData.setDrawable(getResources().getDrawable(R.mipmap.icon05));

        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new BasicItemViewH(context));
        mListData.add(mItemViewData);

        mItemViewData = new SettingViewItem();
        mItemData = new SettingModel();
        mItemData.setTitle("營運商");
        mItemData.setSubTitle("遠傳電信");
        mItemData.setDrawable(getResources().getDrawable(R.mipmap.icon03));

        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new BasicItemViewH(context));
        mListData.add(mItemViewData);

        mSettingView1.setAdapter(mListData);
		/* ==========================SettingView1========================== */

		/* ==========================SettingView2========================== */
        mListData.clear();
        mItemViewData = new SettingViewItem();
        mItemData = new SettingModel();
        mItemData.setTitle("通知中心");
        mItemData.setDrawable(getResources().getDrawable(R.mipmap.icon10));

        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new CheckItemViewH(mContext));
        mListData.add(mItemViewData);

        mItemViewData = new SettingViewItem();
        mItemData = new SettingModel();
        mItemData.setTitle("控制中心");
        mItemData.setDrawable(getResources().getDrawable(R.mipmap.icon10));

        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new CheckItemViewH(context));
        mListData.add(mItemViewData);

        mItemViewData = new SettingViewItem();
        mItemData = new SettingModel();
        mItemData.setTitle("勿擾模式");
        //ContextCompat.getDrawable(context,R.mipmap.icon09)
        mItemData.setDrawable(getResources().getDrawable(R.mipmap.icon09));

        mItemViewData.setData(mItemData);
        mItemViewData.setItemView(new BasicItemViewH(context));
        mListData.add(mItemViewData);

        mSettingView2.setAdapter(mListData);
		/* ==========================SettingView2========================== */
    }
}