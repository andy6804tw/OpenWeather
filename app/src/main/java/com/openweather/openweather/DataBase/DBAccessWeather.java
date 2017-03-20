package com.openweather.openweather.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.openweather.openweather.R;

/**
 * Created by andy6804tw on 2017/2/26.
 */

public class DBAccessWeather extends SQLiteOpenHelper {

    static  Context mContext;

    public DBAccessWeather(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "create table Location("
                +"loc_id int(10) not null primary key,"
                +"country string(10),"
                +"city char(10),"
                +"village char(10),"
                +"district varchar(10),"
                +"latitude float(50),"
                +"longitude float(50)"
                +")";
        Log.e("SQLDB",sql1);
        db.execSQL(sql1);


        String sql2 = "create table Wind("
                +"loc_id integer not null primary key,"
                +"chill int(10),"
                +"direction int(10),"
                +"speed int(10),"
                +"foreign key (loc_id) references Location(loc_id)"
                +")";
        Log.e("SQLDB",sql2);
        db.execSQL(sql2);

        String sql21 = "create table Speed("
                +"speed integer not null primary key,"
                +"series varchar(10),"
                +"series_name varchar(10),"
                +"foreign key (speed) references Wind(speed)"
                +")";
        Log.e("SQLDB",sql21);
        db.execSQL(sql21);

        String sql3 = "create table Atmosphere("
                +"loc_id int(10) not null primary key,"
                +"humidity float(5),"
                +"pressure int(5),"
                +"visibility int(5),"
                +"rising int(1)"
                +")";
        Log.e("SQLDB",sql3);
        db.execSQL(sql3);


        String sql5 = "create table Astronomy("
                +"loc_id int(10) not null primary key,"
                +"sunrise string(10),"
                +"sunset string(10)"
                +")";
        Log.e("SQLDB",sql5);
        db.execSQL(sql5);

        String sql6 = "create table Condition("
                +"loc_id int(10) not null primary key,"
                +"date string(20),"
                +"day string(5),"
                +"high int(5),"
                +"low int(5),"
                +"temp int(5),"
                +"code int(4),"
                +"publish_time string(20)"
                +")";
        Log.e("SQLDB",sql6);
        db.execSQL(sql6);

        String sql7 = "create table Code("
                +"code_id integer not null primary key,"
                +"description string(255),"
                +"Wearing_Suggest string(255),"
                +"foreign key (code_id) references Condition(code) on delete cascade"
                +")";
        Log.e("SQLDB",sql7);
        db.execSQL(sql7);

        String sql8 = "create table Forecast("
                + "forecast_id integer not null primary key,"
                + "date varchar(15),"
                + "day varchar(5),"
                + "high int(5),"
                + "low int(5),"
                + "text varchar(20)"
                + ")";
        Log.e("SQLDB",sql8);
        db.execSQL(sql8);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Location");
        db.execSQL("DROP TABLE IF EXISTS Wind");
        db.execSQL("DROP TABLE IF EXISTS Direction");
        db.execSQL("DROP TABLE IF EXISTS Speed");
        db.execSQL("DROP TABLE IF EXISTS Atmosphere");
        db.execSQL("DROP TABLE IF EXISTS Astronomy");
        db.execSQL("DROP TABLE IF EXISTS Condition");
        db.execSQL("DROP TABLE IF EXISTS Code");
        db.execSQL("DROP TABLE IF EXISTS Forecast");
        onCreate(db);
    }

    public long add(){
        SQLiteDatabase db = getWritableDatabase();

        //Wind Speed
        ContentValues values2 = new ContentValues();
        values2.put("speed", 0);
        values2.put("series","0");
        values2.put("series_name",mContext.getResources().getString(R.string.speed0));
        db.insert("Speed", null, values2);


        values2.put("speed", 1);
        values2.put("series","1");
        values2.put("series_name",mContext.getResources().getString(R.string.speed1));
        db.insert("Speed", null, values2);


        values2.put("speed", 2);
        values2.put("series","2");
        values2.put("series_name",mContext.getResources().getString(R.string.speed2));
        db.insert("Speed", null, values2);


        values2.put("speed", 3);
        values2.put("series","3");
        values2.put("series_name",mContext.getResources().getString(R.string.speed3));
        db.insert("Speed", null, values2);

        values2.put("speed", 4);
        values2.put("series","4");
        values2.put("series_name",mContext.getResources().getString(R.string.speed4));
        db.insert("Speed", null, values2);

        values2.put("speed", 5);
        values2.put("series","5");
        values2.put("series_name",mContext.getResources().getString(R.string.speed5));
        db.insert("Speed", null, values2);

        values2.put("speed", 6);
        values2.put("series","6");
        values2.put("series_name",mContext.getResources().getString(R.string.speed6));
        db.insert("Speed", null, values2);

        values2.put("speed", 7);
        values2.put("series","7");
        values2.put("series_name",mContext.getResources().getString(R.string.speed7));
        db.insert("Speed", null, values2);

        values2.put("speed", 8);
        values2.put("series","8");
        values2.put("series_name",mContext.getResources().getString(R.string.speed8));
        db.insert("Speed", null, values2);

        values2.put("speed", 9);
        values2.put("series","9");
        values2.put("series_name",mContext.getResources().getString(R.string.speed9));
        db.insert("Speed", null, values2);

        values2.put("speed", 10);
        values2.put("series","10");
        values2.put("series_name",mContext.getResources().getString(R.string.speed10));
        db.insert("Speed", null, values2);

        values2.put("speed", 11);
        values2.put("series","11");
        values2.put("series_name",mContext.getResources().getString(R.string.speed11));
        db.insert("Speed", null, values2);

        values2.put("speed", 12);
        values2.put("series","12");
        values2.put("series_name",mContext.getResources().getString(R.string.speed12));
        db.insert("Speed", null, values2);



        //Code0
        ContentValues va700 = new ContentValues();
        va700.put("code_id",0);
        va700.put("description",mContext.getResources().getString(R.string.code00));
        va700.put("Wearing_Suggest","00");
        db.insert("Code", null, va700);

        //Code1
        ContentValues va701 = new ContentValues();
        va701.put("code_id",1);
        va701.put("description",mContext.getResources().getString(R.string.code01));
        va701.put("Wearing_Suggest","01");
        db.insert("Code", null, va701);

        //Code2
        ContentValues va702 = new ContentValues();
        va702.put("code_id",2);
        va702.put("description",mContext.getResources().getString(R.string.code02));
        va702.put("Wearing_Suggest","02");
        db.insert("Code", null, va702);

        //Code3
        ContentValues va703 = new ContentValues();
        va703.put("code_id",3);
        va703.put("description",mContext.getResources().getString(R.string.code03));
        va703.put("Wearing_Suggest","03");
        db.insert("Code", null, va703);

        //Code4
        ContentValues va704 = new ContentValues();
        va704.put("code_id",4);
        va704.put("description",mContext.getResources().getString(R.string.code04));
        va704.put("Wearing_Suggest","04");
        db.insert("Code", null, va704);

        //Code5
        ContentValues va705 = new ContentValues();
        va705.put("code_id",5);
        va705.put("description",mContext.getResources().getString(R.string.code05));
        va705.put("Wearing_Suggest","05");
        db.insert("Code", null, va705);

        //Code6
        ContentValues va706 = new ContentValues();
        va706.put("code_id",6);
        va706.put("description",mContext.getResources().getString(R.string.code06));
        va706.put("Wearing_Suggest","06");
        db.insert("Code", null, va706);

        //Code7
        ContentValues va707 = new ContentValues();
        va707.put("code_id",7);
        va707.put("description",mContext.getResources().getString(R.string.code07));
        va707.put("Wearing_Suggest","07");
        db.insert("Code", null, va707);

        //Code8
        ContentValues va708 = new ContentValues();
        va708.put("code_id",8);
        va708.put("description",mContext.getResources().getString(R.string.code08));
        va708.put("Wearing_Suggest","08");
        db.insert("Code", null, va708);

        //Code9
        ContentValues va709 = new ContentValues();
        va709.put("code_id",9);
        va709.put("description",mContext.getResources().getString(R.string.code09));
        va709.put("Wearing_Suggest","09");
        db.insert("Code", null, va709);

        //Code10
        ContentValues va710 = new ContentValues();
        va710.put("code_id",10);
        va710.put("description",mContext.getResources().getString(R.string.code10));
        va710.put("Wearing_Suggest","10");
        db.insert("Code", null, va710);

        //Code11
        ContentValues va711 = new ContentValues();
        va711.put("code_id",11);
        va711.put("description",mContext.getResources().getString(R.string.code11));
        va711.put("Wearing_Suggest","11");
        db.insert("Code", null, va711);

        //Code12
        ContentValues va712 = new ContentValues();
        va712.put("code_id",12);
        va712.put("description",mContext.getResources().getString(R.string.code12));
        va712.put("Wearing_Suggest","12");
        db.insert("Code", null, va712);

        //Code13
        ContentValues va713 = new ContentValues();
        va713.put("code_id",13);
        va713.put("description",mContext.getResources().getString(R.string.code13));
        va713.put("Wearing_Suggest","13");
        db.insert("Code", null, va713);

        //Code14
        ContentValues va714 = new ContentValues();
        va714.put("code_id",14);
        va714.put("description",mContext.getResources().getString(R.string.code14));
        va714.put("Wearing_Suggest","14");
        db.insert("Code", null, va714);

        //Code15
        ContentValues va715 = new ContentValues();
        va715.put("code_id",15);
        va715.put("description",mContext.getResources().getString(R.string.code15));
        va715.put("Wearing_Suggest","15");
        db.insert("Code", null, va715);

        //Code16
        ContentValues va716 = new ContentValues();
        va716.put("code_id",16);
        va716.put("description",mContext.getResources().getString(R.string.code16));
        va716.put("Wearing_Suggest","16");
        db.insert("Code", null, va716);

        //Code17
        ContentValues va717 = new ContentValues();
        va717.put("code_id",17);
        va717.put("description",mContext.getResources().getString(R.string.code17));
        va717.put("Wearing_Suggest","17");
        db.insert("Code", null, va717);

        //Code18
        ContentValues va718 = new ContentValues();
        va718.put("code_id",18);
        va718.put("description",mContext.getResources().getString(R.string.code18));
        va718.put("Wearing_Suggest","18");
        db.insert("Code", null, va718);

        //Code19
        ContentValues va719 = new ContentValues();
        va719.put("code_id",19);
        va719.put("description",mContext.getResources().getString(R.string.code19));
        va719.put("Wearing_Suggest","19");
        db.insert("Code", null, va719);

        //Code20
        ContentValues va720 = new ContentValues();
        va720.put("code_id",20);
        va720.put("description",mContext.getResources().getString(R.string.code20));
        va720.put("Wearing_Suggest","20");
        db.insert("Code", null, va720);

        //Code21
        ContentValues va721 = new ContentValues();
        va721.put("code_id",21);
        va721.put("description",mContext.getResources().getString(R.string.code21));
        va721.put("Wearing_Suggest","21");
        db.insert("Code", null, va721);

        //Code22
        ContentValues va722 = new ContentValues();
        va722.put("code_id",22);
        va722.put("description",mContext.getResources().getString(R.string.code22));
        va722.put("Wearing_Suggest","22");
        db.insert("Code", null, va722);

        //Code23
        ContentValues va723 = new ContentValues();
        va723.put("code_id",23);
        va723.put("description",mContext.getResources().getString(R.string.code23));
        va723.put("Wearing_Suggest","23");
        db.insert("Code", null, va723);

        //Code24
        ContentValues va724 = new ContentValues();
        va724.put("code_id",24);
        va724.put("description",mContext.getResources().getString(R.string.code24));
        va724.put("Wearing_Suggest","24");
        db.insert("Code", null, va724);

        //Code25
        ContentValues va725 = new ContentValues();
        va725.put("code_id",25);
        va725.put("description",mContext.getResources().getString(R.string.code25));
        va725.put("Wearing_Suggest","25");
        db.insert("Code", null, va725);

        //Code26
        ContentValues va726 = new ContentValues();
        va726.put("code_id",26);
        va726.put("description",mContext.getResources().getString(R.string.code26));
        va726.put("Wearing_Suggest","26");
        db.insert("Code", null, va726);

        //Code27
        ContentValues va727 = new ContentValues();
        va727.put("code_id",27);
        va727.put("description",mContext.getResources().getString(R.string.code27));
        va727.put("Wearing_Suggest","27");
        db.insert("Code", null, va727);

        //Code28
        ContentValues va728 = new ContentValues();
        va728.put("code_id",28);
        va728.put("description",mContext.getResources().getString(R.string.code28));
        va728.put("Wearing_Suggest","28");
        db.insert("Code", null, va728);

        //Code29
        ContentValues va729 = new ContentValues();
        va729.put("code_id",29);
        va729.put("description",mContext.getResources().getString(R.string.code29));
        va729.put("Wearing_Suggest","29");
        db.insert("Code", null, va729);

        //Code30
        ContentValues va730 = new ContentValues();
        va730.put("code_id",30);
        va730.put("description",mContext.getResources().getString(R.string.code30));
        va730.put("Wearing_Suggest","30");
        db.insert("Code", null, va730);

        //Code31
        ContentValues va731 = new ContentValues();
        va731.put("code_id",31);
        va731.put("description",mContext.getResources().getString(R.string.code31));
        va731.put("Wearing_Suggest","31");
        db.insert("Code", null, va731);

        //Code32
        ContentValues va732 = new ContentValues();
        va732.put("code_id",32);
        va732.put("description",mContext.getResources().getString(R.string.code32));
        va732.put("Wearing_Suggest","32");
        db.insert("Code", null, va732);

        //Code33
        ContentValues va733 = new ContentValues();
        va733.put("code_id",33);
        va733.put("description",mContext.getResources().getString(R.string.code33));
        va733.put("Wearing_Suggest","33");
        db.insert("Code", null, va733);

        //Code34
        ContentValues va734 = new ContentValues();
        va734.put("code_id",34);
        va734.put("description",mContext.getResources().getString(R.string.code34));
        va734.put("Wearing_Suggest","34");
        db.insert("Code", null, va734);

        //Code35
        ContentValues va735 = new ContentValues();
        va735.put("code_id",35);
        va735.put("description",mContext.getResources().getString(R.string.code35));
        va735.put("Wearing_Suggest","35");
        db.insert("Code", null, va735);

        //Code36
        ContentValues va736 = new ContentValues();
        va736.put("code_id",36);
        va736.put("description",mContext.getResources().getString(R.string.code36));
        va736.put("Wearing_Suggest","36");
        db.insert("Code", null, va736);

        //Code37
        ContentValues va737 = new ContentValues();
        va737.put("code_id",37);
        va737.put("description",mContext.getResources().getString(R.string.code37));
        va737.put("Wearing_Suggest","37");
        db.insert("Code", null, va737);

        //Code38
        ContentValues va738 = new ContentValues();
        va738.put("code_id",38);
        va738.put("description",mContext.getResources().getString(R.string.code38));
        va738.put("Wearing_Suggest","38");
        db.insert("Code", null, va738);

        //Code39
        ContentValues va739 = new ContentValues();
        va739.put("code_id",39);
        va739.put("description",mContext.getResources().getString(R.string.code39));
        va739.put("Wearing_Suggest","39");
        db.insert("Code", null, va739);

        //Code40
        ContentValues va740 = new ContentValues();
        va740.put("code_id",40);
        va740.put("description",mContext.getResources().getString(R.string.code40));
        va740.put("Wearing_Suggest","40");
        db.insert("Code", null, va740);

        //Code41
        ContentValues va741 = new ContentValues();
        va741.put("code_id",41);
        va741.put("description",mContext.getResources().getString(R.string.code41));
        va741.put("Wearing_Suggest","41");
        db.insert("Code", null, va741);

        //Code42
        ContentValues va742 = new ContentValues();
        va742.put("code_id",42);
        va742.put("description",mContext.getResources().getString(R.string.code42));
        va742.put("Wearing_Suggest","42");
        db.insert("Code", null, va742);

        //Code43
        ContentValues va743 = new ContentValues();
        va743.put("code_id",43);
        va743.put("description",mContext.getResources().getString(R.string.code43));
        va743.put("Wearing_Suggest","43");
        db.insert("Code", null, va743);

        //Code44
        ContentValues va744 = new ContentValues();
        va744.put("code_id",44);
        va744.put("description",mContext.getResources().getString(R.string.code44));
        va744.put("Wearing_Suggest","44");
        db.insert("Code", null, va744);

        //Code45
        ContentValues va745 = new ContentValues();
        va745.put("code_id",45);
        va745.put("description",mContext.getResources().getString(R.string.code45));
        va745.put("Wearing_Suggest","45");
        db.insert("Code", null, va745);

        //Code46
        ContentValues va746 = new ContentValues();
        va746.put("code_id",46);
        va746.put("description",mContext.getResources().getString(R.string.code46));
        va746.put("Wearing_Suggest","46");
        db.insert("Code", null, va746);

        //Code47
        ContentValues va747 = new ContentValues();
        va747.put("code_id",47);
        va747.put("description",mContext.getResources().getString(R.string.code47));
        va747.put("Wearing_Suggest","47");
        db.insert("Code", null, va747);

        //Code48
        ContentValues va748 = new ContentValues();
        va748.put("code_id",3200);
        va748.put("description",mContext.getResources().getString(R.string.code3200));
        va748.put("Wearing_Suggest","3200");
        db.insert("Code", null, va748);

        db.close();
        return 1;
    }

    public Cursor getData(String NAME, String whereStr, String orderbyStr){
        SQLiteDatabase db=getReadableDatabase();
        switch (NAME) {
            case "Location": {
                return db.query(NAME, new String[]{"loc_id", "country", "city", "district"
                                , "village", "latitude", "longitude"}
                        , whereStr, null, null, null, orderbyStr);
            }
            case "Wind": {
                return db.query(NAME, new String[]{"loc_id", "chill", "direction", "speed"}
                        , whereStr, null, null, null, orderbyStr);
            }
            case "Speed":{
                return db.query(NAME, new String[]{"speed","series","series_name"}
                        ,whereStr,null,null,null,orderbyStr);
            }
            case "Atmosphere": {
                return db.query(NAME, new String[]{"loc_id", "humidity", "pressure"
                                ,"visibility", "rising"}
                        , whereStr, null, null, null, orderbyStr);
            }
            case "Astronomy":{
                return db.query(NAME, new String[]{"loc_id", "sunrise", "sunset"}
                        , whereStr, null, null, null, orderbyStr);
            }
            case "Condition":{
                return db.query(NAME, new String[]{"loc_id", "date", "day", "high"
                                , "low","temp","code","publish_time"}
                        , whereStr, null, null, null, orderbyStr);
            }
            case "Code":{
                return db.query(NAME, new String[]{"code_id", "description","Wearing_Suggest"}
                        , whereStr, null, null, null, orderbyStr);
            }
            case "Forecast":{
                return db.query(NAME, new String[]{"forecast_id","date","day","high","low","text"}
                        , whereStr, null, null, null, orderbyStr);
            }
            default: {
                return null;
            }
        }
    }



    //Location
    public long add(String loc_id,String country,String city,String district
            ,String village,String latitude,String longitude){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("country",country);
        values.put("city",city);
        values.put("district",district);
        values.put("village",village);
        values.put("latitude",Double.parseDouble(latitude));
        values.put("longitude",Double.parseDouble(longitude));
        return db.insert("Location",null,values);
    }
    public long update(String loc_id,String country,String city,String district
            ,String village,String latitude,String longitude,String whereClause){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("country",country);
        values.put("city",city);
        values.put("district",district);
        values.put("village",village);
        values.put("latitude",latitude);
        values.put("longitude",longitude);
        long result=db.update("Location", values, whereClause, null);
        db.close();
        return result;//回傳更新資料筆數
    }

    //Wind
    public long add(String loc_id,Double chill,String direction,String speed){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("chill",chill);
        values.put("direction",direction);
        values.put("speed",speed);
        return db.insert("Wind", null,values);
    }
    public long update(String loc_id,Double chill,String direction,String speed,String whereClause){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("chill",chill);
        values.put("direction",direction);
        values.put("speed",speed);
        long result=db.update("Wind", values, whereClause, null);
        db.close();
        return result;//回傳更新資料筆數
    }

    //Atmosphere
    public long add(String loc_id,String humidity,String pressure,String visibility,String rising){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("humidity",humidity);
        values.put("pressure",pressure);
        values.put("visibility",visibility);
        values.put("rising",rising);
        return db.insert("Atmosphere", null,values);
    }
    public long update(String loc_id,String humidity,String pressure,String visibility,String rising,String whereClause){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("humidity",humidity);
        values.put("pressure",pressure);
        values.put("visibility",visibility);
        values.put("rising",rising);
        long result=db.update("Atmosphere", values, whereClause, null);
        db.close();
        return result;//回傳更新資料筆數
    }

    //Astronomy
    public long add(String loc_id,String sunrise,String sunset){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("sunrise",sunrise);
        values.put("sunset",sunset);
        return db.insert("Astronomy", null,values);
    }
    public long update(String loc_id,String sunrise,String sunset,String whereClause){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("sunrise",sunrise);
        values.put("sunset",sunset);
        long result=db.update("Astronomy", values, whereClause, null);
        db.close();
        return result;//回傳更新資料筆數
    }

    //Condition
    public long add(String loc_id,String date,String day,Double high,Double low,Double temp,int code,String publish_time){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("date",date);
        values.put("day",day);
        values.put("high",high);
        values.put("low",low);
        values.put("temp",temp);
        values.put("code",code+"");
        values.put("publish_time",publish_time);
        return db.insert("Condition", null,values);
    }
    public long update(String loc_id,String date,String day,Double high,Double low,Double temp,int code,String publish_time,String whereClause){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("loc_id",loc_id);
        values.put("date",date);
        values.put("day",day);
        values.put("high",high);
        values.put("low",low);
        values.put("temp",temp);
        values.put("code",code);
        values.put("publish_time",publish_time);
        long result=db.update("Condition", values, whereClause, null);
        db.close();
        return result;//回傳更新資料筆數
    }
    //Forecast
    public long add(String forecast_id,String date,String day,Double high,Double low,String text){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("forecast_id",forecast_id);
        values.put("date",date);
        values.put("day",day);
        values.put("high",high);
        values.put("low",low);
        values.put("text",text);
        return db.insert("Forecast",null,values);
    }
    public long update(String forecast_id,String date,String day,Double high,Double low,String text,String whereClause){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        //values.put("forecast_id",forecast_id);
        values.put("date",date);
        values.put("day",day);
        values.put("high",high);
        values.put("low",low);
        values.put("text",text);
        long result=db.update("Forecast", values, whereClause, null);
        db.close();
        return result;//回傳更新資料筆數
    }

}