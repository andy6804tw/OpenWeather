package com.openweather.openweather.WeatherNowActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.openweather.openweather.R;

import java.util.ArrayList;

public class EditLocationActivity extends AppCompatActivity {

    private static final String TAG = "EditLocationActivity";
    private ListView listView;
    private ArrayList<String> groups;

    class ViewHolder {
        TextView tv;
    }

    private TextView tvCity;

    private String cityName = "";
    private String default_cityName = "Taipei";
    String strCity = "";
    private String prefName = "prefSet";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);

        tvCity = (TextView) findViewById(R.id.tv_city);
        listView = (ListView) findViewById(R.id.lvGroup);
        Log.d(TAG, "pre :"+strCity);
        tvCity.setText("選擇城市 : " + strCity);



        initDataList();




        GroupAdapter adapter = new GroupAdapter(this, groups);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                String text = viewHolder.tv.getText().toString();
                CheckBox item_cb=(CheckBox)findViewById(R.id.item_cb);
                item_cb.setChecked(true);
                //queryEnCityName(text);

                tvCity.setText("選擇城市 : " + text);


            }
        });

    }

    private void initDataList() {
        groups = new ArrayList<String>();
        groups.add(getResources().getString(R.string.Keelung));
        groups.add(getResources().getString(R.string.Taipei));
        groups.add(getResources().getString(R.string.Taoyuan));
        groups.add(getResources().getString(R.string.Hsinchu));
        groups.add(getResources().getString(R.string.Miaoli));
        groups.add(getResources().getString(R.string.Taichung));
        groups.add(getResources().getString(R.string.Changhua));
        groups.add(getResources().getString(R.string.Yunlin));
        groups.add(getResources().getString(R.string.Chiayi));
        groups.add(getResources().getString(R.string.Tainan));
        groups.add(getResources().getString(R.string.Kaohsiung));
        groups.add(getResources().getString(R.string.Pingtung));
        groups.add(getResources().getString(R.string.Taitung));
        groups.add(getResources().getString(R.string.Hualien));
        groups.add(getResources().getString(R.string.Yilan));
        groups.add(getResources().getString(R.string.Penghu));
        groups.add(getResources().getString(R.string.Kinmen));
        groups.add(getResources().getString(R.string.Matsu));
    }


    public String queryEnCityName(String city) {

        switch (city) {

            case "基隆":
                city = "Keelung";
                break;
            case "台北":
                city = "Taipei";
                break;
            case "桃園":
                city = "Taoyuan";
                break;
            case "新竹":
                city = "Hsinchu";
                break;
            case "苗栗":
                city = "Miaoli";
                break;
            case "台中":
                city = "Taichung";
                break;
            case "彰化":
                city = "Changhua";
                break;
            case "雲林":
                city = "Yunlin";
                break;
            case "嘉義":
                city = "Chiayi";
                break;
            case "高雄":
                city = "Taipei";
                break;
            case "屏東":
                city = "Taipei";
                break;
            case "台東":
                city = "Taipei";
                break;
            case "花蓮":
                city = "Hualien";
                break;
            case "宜蘭":
                city = "Yilan";
                break;
            case "澎湖":
                city = "Penghu";
                break;
            case "金門":
                city = "Kinmen";
                break;
            case "馬祖":
                city = "Matsu";
                break;

        }

        return city;
    }




    public class GroupAdapter extends BaseAdapter {
        private ArrayList<String> list;
        private LayoutInflater inflater = null;//導入布局

        public GroupAdapter(Activity context, ArrayList<String> list) {
            this.list = list;
            inflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            Log.d(TAG, String.valueOf(i));

            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {//當第一次加載ListView控件時  convertView为空
                convertView = inflater.inflate(R.layout.weather_location_listi_tem, null);//所以當ListView控件沒有滑動時都會執行這條語句
                holder = new ViewHolder();
                holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
                convertView.setTag(holder);//为view設置標簽
            } else {//取出holder
                holder = (ViewHolder) convertView.getTag();//the Object stored in this view as a tag
            }
            //設置list的textview顯示
            holder.tv.setTextColor(Color.WHITE);
            holder.tv.setText(list.get(position));
            holder.tv.setTextSize(24f);
            return convertView;
        }


    }


}

