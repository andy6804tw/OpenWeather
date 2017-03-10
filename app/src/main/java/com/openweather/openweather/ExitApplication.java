package com.openweather.openweather;

/**
 * Created by andy6804tw on 2017/2/25.
 */

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;



/**

 * 用於處理退出程序時可以退出所有的activity，而編寫的通用類

 * 每個Activity在oncreate的時候都需調用MyApplication.getInstance().addActivity(this);

 * 以便將當前Activity加入到Activity集合中

 * @author duanyr

 *

 */

public class ExitApplication extends Application {



    private List<Activity> activityList = new LinkedList<Activity>();

    private static ExitApplication instance;



    private ExitApplication() {

    }



// 單例模式中獲取唯一的MyApplication實例

    public static ExitApplication getInstance() {

        if (null == instance) {

            instance = new ExitApplication();

        }

        return instance;

    }



// 添加Activity到容器中

    public void addActivity(Activity activity) {

        activityList.add(activity);

    }



// 遍曆所有Activity並finish

    public void exit() {

        for (Activity activity : activityList) {

            activity.finish();

        }

        System.exit(0);
    }
    public void clean(){
        for(int i=0;i<activityList.size()-1;i++){
            activityList.get(i).finish();
        }
    }

}




