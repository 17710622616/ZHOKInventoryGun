package com.cocacola.john_li.zhokinventorygun.ZHOKIGDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cocacola.john_li.zhokinventorygun.ZHOKIGModel.DCStockTake;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGModel.ProductModel;
import com.cocacola.john_li.zhokinventorygun.ZHOKIGUtils.ZHOKIGSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John_Li on 17/12/2018.
 */

public class NormalDCSockTakeDAO {
    private ZHOKIGSQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private Context mContext;

    public NormalDCSockTakeDAO(Context c) {
        helper = new ZHOKIGSQLiteOpenHelper(c);
        mContext = c;
    }

    public void add(DCStockTake model) {
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("insert into tb_stock_take (prod_id,qty,oper_name,status) values(?,?,?,?)", new Object[] { model.getProd_id() ,model.getQty(), model.getOper_name(), model.getStatus()});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 清空表
     **/
    public void clearTable() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from tb_stock_take");
    }

    /**
     * 获取总记录数
     *
     * @return
     */
    public long getCount() {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        Cursor cursor = db.rawQuery("select count(_id) from tb_stock_take", null);
        if (cursor.moveToNext())// 判断Cursor中是否有数据
        {
            return cursor.getLong(0);// 返回总记录数
        }
        return 0;// 如果没有数据，则返回0
    }


    /**
     * 根据条件查询
     */
    public List<DCStockTake> search() {
        db = helper.getWritableDatabase();
        db.beginTransaction();
        List<DCStockTake> list = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery("select * from tb_stock_take", null);
            int count = cursor.getCount();
            while (cursor.moveToNext()) {
                DCStockTake model = new DCStockTake();
                model.setProd_id(cursor.getString(cursor.getColumnIndex("prod_id")));
                model.setQty(cursor.getInt(cursor.getColumnIndex("qty")));
                model.setOper_name(cursor.getString(cursor.getColumnIndex("oper_name")));
                model.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                list.add(model);
            }

            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return list;
    }

    /**
     * 修改盘点记录状态, 0未提交/提交中， 1提交成功，2提交失败
     **/
    public void updataStatus(String prod_id, int status) {
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            // update Orders set OrderPrice = 800 where Id = 6
            ContentValues cv = new ContentValues();
            cv.put("status", status);
            db.update("tb_stock_take", cv, "prod_id = ?", new String[]{prod_id});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 删除某条
     **/
    public void deleteByProdId(String prod_id) {
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("tb_stock_take", "prod_id = ?", new String[]{prod_id});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
