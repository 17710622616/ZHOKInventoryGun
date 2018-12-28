package com.cocacola.john_li.zhokinventorygun.ZHOKIGUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

/**
 * Created by John_Li on 17/12/2018.
 */

public class ZHOKIGSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "ZHOKInventory.db";
    public static final String DB_PATH = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ZHOKDB").getAbsolutePath() + DB_NAME;
    private Context mContext;

    public ZHOKIGSQLiteOpenHelper(Context context) {
        super(context, DB_PATH, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        // 产品表
        db.execSQL("create table tb_product (_id integer primary key, bar_code varchar(50), cat_id varchar(50), dept_id varchar(50), prod_id varchar(50), prod_name varchar(50), subcat_id varchar(50))");
        // 正常仓盘点表
        db.execSQL("create table tb_stock_take (_id integer primary key, prod_id varchar(50), qty varchar(50), oper_name varchar(50), status integer)");
        // 退货仓盘点表
        db.execSQL("create table tb_bad_stock_take (_id integer primary key, prod_id varchar(50), qty varchar(50), oper_name varchar(50), status integer)");
    }

    /**获取数据库路径**/
    public String getDBPath(){
        return mContext.getDatabasePath(DB_PATH).getPath();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
